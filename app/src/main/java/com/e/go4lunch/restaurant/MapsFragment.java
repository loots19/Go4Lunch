package com.e.go4lunch.restaurant;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.e.go4lunch.R;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.repositories.injection.App;
import com.e.go4lunch.repositories.injection.Injection;
import com.e.go4lunch.repositories.injection.ViewModelFactory;
import com.e.go4lunch.util.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
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
import static com.facebook.FacebookSdk.getApplicationContext;

public class MapsFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    // ----------------- FOR DESIGN -----------------

    @BindView(R.id.fab)
    FloatingActionButton mFab;


    // ----------------- FOR DATA // -----------------

    private static final int REQUEST_USER_LOCATION_CODE = 99;
    public static final String MY_PREF = "MY_PREF";
    private static final String LAT = "LAT";
    private static final String LNG = "LNG";
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleApiClient mGoogleApiClient;
    private RestaurantViewModel mRestaurantViewModel;
    private List<Restaurant> mRestaurantList = new ArrayList<>();
    private Double lat, lng, rating;
    private List<Workmates> mWorkmatesList = new ArrayList<>();
    private String placeId, name, address, urlPhoto;
    private com.e.go4lunch.models.placeDetail.Location location;


    // ----------------- Required empty public constructor // -----------------
    public MapsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        ButterKnife.bind(this, v);
        configureViewModel();


        //  //Request Runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();

        }
        mapFragment.getMapAsync(this);
        return v;


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_USER_LOCATION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (mGoogleApiClient == null) {
                        buildGoogleApiClient();
                    }
                    mMap.setMyLocationEnabled(false);
                    locationRequest();
                }
            } else {
                Toast.makeText(getActivity(), "Permission denied...", Toast.LENGTH_LONG).show();
            }
        }
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(requireActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    private void locationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest();
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
        // Place current location marker
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        // move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        // get lat and lng and put in sharedPreferences
        if (isAdded()) {
            saveLatLng(latitude, longitude);
            String lat = App.getInstance().getLat();
            String lng = App.getInstance().getLng();
            mRestaurantViewModel.setPlace(Constants.TYPE, lat + " " + lng, Constants.RADIUS);
            getRestaurantList();
        }
    }

    private void saveLatLng(double lat, double lng) {

        SharedPreferences prefs = requireActivity().getSharedPreferences("MY_PREF", Activity.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();
        mEditor.putString(LAT, String.valueOf(lat));
        mEditor.putString(LNG, String.valueOf(lng));
        mEditor.apply();

        App mApp = (App) getApplicationContext();
        mApp.setLat(String.valueOf(lat));
        mApp.setLng(String.valueOf(lng));
        mRestaurantViewModel.setPlace(Constants.TYPE, lat + " " + lng, Constants.RADIUS);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(false);

        mFab.setOnClickListener(v -> getRestaurantList());

        // make event click on marker
        mMap.setOnMarkerClickListener(marker -> {
            lunchDetailActivity(marker);
            return false;
        });

    }

    private void checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_USER_LOCATION_CODE);
        }

    }

    // ---------------------
    // Configuring ViewModel
    // ---------------------
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mRestaurantViewModel = new ViewModelProvider(this, mViewModelFactory).get(RestaurantViewModel.class);
        String lat = App.getInstance().getLat();
        String lng = App.getInstance().getLng();
    }

    // ---------------------
    // Configuring Observers
    // ---------------------

    private void getRestaurantList() {
        mRestaurantViewModel.getRestaurantList().observe(getViewLifecycleOwner(), restaurants -> {
            mRestaurantList = restaurants;
            Log.e("map",mRestaurantList.size()+",");
            int size = restaurants.size();
            for (int i = 0; i < size; i++) {
                Restaurant restaurant = restaurants.get(i);
                int in = mRestaurantList.indexOf(restaurant);
                mRestaurantList.get(in).setWorkmatesList(restaurant.getWorkmatesList());
                lat = mRestaurantList.get(in).getLocation().getLat();
                lng = mRestaurantList.get(in).getLocation().getLng();
                LatLng latLng = new LatLng(lat, lng);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                if (restaurant.getWorkmatesList().size() > 0 && restaurant.getWorkmatesList() != null) {
                    if (mRestaurantList.contains(restaurant)) {
                        markerOptions.icon(MapsFragment.this.bitmapDescriptorFromVector1(MapsFragment.this.getContext()));
                    } else {
                        markerOptions.icon(MapsFragment.this.bitmapDescriptorFromVector(MapsFragment.this.getContext()));
                    }

                } else {
                    markerOptions.icon(MapsFragment.this.bitmapDescriptorFromVector(MapsFragment.this.getContext()));
                }

                Marker marker = mMap.addMarker(markerOptions);
                Gson gson = new Gson();
                String jsonSelectedRestaurant = gson.toJson(mRestaurantList.get(i));
                marker.setTag(jsonSelectedRestaurant);

            }
        });
    }


    // -----------
    // Draw marker
    // -----------
    private BitmapDescriptor bitmapDescriptorFromVector(Context context) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_location);
        Objects.requireNonNull(background).setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_restaurant_black_24dp);
        Objects.requireNonNull(vectorDrawable).setBounds(20, 20, vectorDrawable.getIntrinsicWidth() + 20, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private BitmapDescriptor bitmapDescriptorFromVector1(Context context) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_location1);
        Objects.requireNonNull(background).setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_restaurant_black_24dp);
        Objects.requireNonNull(vectorDrawable).setBounds(20, 20, vectorDrawable.getIntrinsicWidth() + 20, vectorDrawable.getIntrinsicHeight() + 20);
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





























