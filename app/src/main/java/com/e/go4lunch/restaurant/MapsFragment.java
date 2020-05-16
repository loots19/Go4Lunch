package com.e.go4lunch.restaurant;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.e.go4lunch.R;
import com.e.go4lunch.injection.Injection;
import com.e.go4lunch.injection.ViewModelFactory;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.myPlace.Result;
import com.e.go4lunch.util.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.e.go4lunch.restaurant.DetailsRestaurantActivity.EXTRA_RESTAURANT;

public class MapsFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private static final int REQUEST_USER_LOCATION_CODE = 99;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location lastLocation;
    private double latitude, longitude;
    private Marker currentUserLocationMarker;
    private RestaurantViewModel mRestaurantViewModel;
    private Context mContext;
    private List<Restaurant> restaurants = new ArrayList<>();
    private LatLng mLatLng;




    public MapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        ButterKnife.bind(this, v);

        configureViewModel();



        //Request Runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        return v;


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_USER_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                            mMap.setMyLocationEnabled(true);


                        }

                    }
                } else {
                    Toast.makeText(getActivity(), "Permission denied...", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(getActivity()))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        lastLocation = location;
        if (currentUserLocationMarker != null) {
            currentUserLocationMarker.remove();
        }
        // Place current location marker
        latitude = lastLocation.getLatitude();
        longitude = lastLocation.getLongitude();
        Log.e("location", String.valueOf(latitude));
        mLatLng = new LatLng(latitude, longitude);
        Log.e("location", String.valueOf(mLatLng));
        // move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));


        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();
            subscribeObservers();
            mMap.setMyLocationEnabled(false);

            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subscribeObservers();
                }
            });




        }
        // make event click on marker
        mMap.setOnMarkerClickListener(marker -> {

            lunchDetailActivity(marker);

            return false;
        });


    }


    public boolean checkUserLocationPermission() {

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_USER_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_USER_LOCATION_CODE);
                subscribeObservers();
            }
            return false;
        } else {
            return true;
        }

    }

    // Configuring ViewModel
    private void configureViewModel() {
                    ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(mContext);
                    this.mRestaurantViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RestaurantViewModel.class);

                    mRestaurantViewModel.setPlace(Constants.TYPE, String.valueOf(mLatLng), Constants.RADIUS);
                    Log.e("wiew", String.valueOf(mLatLng));

    }
    private void subscribeObservers() {
                    mRestaurantViewModel.getMyPlace().observe(this, new Observer<MyPlace>() {
                        @Override
                        public void onChanged(MyPlace myPlace) {
                            restaurants = new ArrayList<>();
                            List<Result> results = myPlace.getResults();
                            if (mMap != null) {
                                // This loop will go through all the results and add marker on each location.
                                for (int i = 0; i < results.size(); i++) {

                                    Double lat = results.get(i).getGeometry().getLocation().getLat();
                                    Double lng = results.get(i).getGeometry().getLocation().getLng();
                                    String placeName = results.get(i).getName();
                                    String vicinity = results.get(i).getVicinity();

                                    MarkerOptions markerOptions = new MarkerOptions();
                                    LatLng latLng = new LatLng(lat, lng);
                                    //Position of Marker on Map
                                    markerOptions.position(latLng);
                                    // Adding Title to the Marker
                                    markerOptions.title(placeName + " : " + vicinity);
                                    // Adding Marker to the Camera.
                                    // Adding colour to the marker
                                    markerOptions.icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_restaurant_black_24dp));
                                    // move map camera
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                                    String placeId = results.get(i).getPlaceId();
                                    String name = results.get(i).getName();
                                    String address = results.get(i).getVicinity();
                                    String urlPhoto = results.get(i).getPhotos().get(0).getPhotoReference();
                                    double rating = results.get(i).getRating();
                                    Boolean openNow = (results.get(i).getOpeningHours() != null ? results.get(i).getOpeningHours().getOpenNow() : false);

                                    if (results.get(i).getGeometry().getLocation() != null) {
                                        com.e.go4lunch.models.myPlace.Location location = results.get(i).getGeometry().getLocation();
                                        Restaurant restaurant = new Restaurant(placeId, name, address, urlPhoto, openNow, location, rating);

                                        restaurants.add(restaurant);

                                        Marker marker = mMap.addMarker(markerOptions);
                                        Gson gson = new Gson();
                                        String jsonSelectedRestaurant = gson.toJson(restaurants.get(i));
                                        marker.setTag(jsonSelectedRestaurant);
                                        Log.e("marker", String.valueOf(restaurants.get(i).getPlaceId()));


                                    }

                                }
                            }

                        }


                    });

                }



    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_location);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(20, 20, vectorDrawable.getIntrinsicWidth() + 20, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void lunchDetailActivity(Marker marker) {
        String placeId = (String) marker.getTag();
        Intent intent = new Intent(getContext(), DetailsRestaurantActivity.class);
        intent.putExtra(EXTRA_RESTAURANT, placeId);
        startActivity(intent);
    }





}



















