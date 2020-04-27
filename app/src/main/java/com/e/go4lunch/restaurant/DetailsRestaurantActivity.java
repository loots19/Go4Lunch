package com.e.go4lunch.restaurant;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.e.go4lunch.R;
import com.e.go4lunch.Retrofit.RetrofitRequest;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.myPlace.Result;
import com.e.go4lunch.models.placeDetail.PlaceDetail;
import com.e.go4lunch.models.placeDetail.ResultDetail;
import com.e.go4lunch.util.Constants;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;

public class DetailsRestaurantActivity extends AppCompatActivity {

    @BindView(R.id.tv_name_restaurant_detail_activity)
    TextView mTvNameRestaurant;
    @BindView(R.id.tv_adress_restaurant_detail_activity)
    TextView mTvAddressRestaurant;
    @BindView(R.id.iv_photo_restaurant_detail_activity)
    ImageView mIvPhotoRestaurant;
    @BindView(R.id.buttonCall)
    Button mButtonCall;
    @BindView(R.id.buttonLike)
    Button mButtonLike;
    @BindView(R.id.buttonWeb)
    Button mButtonWeb;
    @BindView(R.id.fab_detail_activity)
    FloatingActionButton mfab;

    public static final String EXTRA_RESTAURANT = "restaurant";
    public static final String EXTRA_MARKER = "Marker";
    private static final int REQUEST_CALL = 10;
    private static String TAG = "test";
    private RestaurantDetailViewModel mRestaurantDetailViewModel;
    PlacesClient placesClient;
    private Restaurant mRestaurant;

    private ResultDetail mResultDetail;
    private String placeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        placeId = getIntent().getStringExtra(EXTRA_MARKER);

        userActionClick();

        getIncomingIntent();


        mRestaurantDetailViewModel = ViewModelProviders.of(this).get(RestaurantDetailViewModel.class);
        mRestaurantDetailViewModel.init();

        subscribeObservers();


    }


    private void getIncomingIntent() {
        if (getIntent().hasExtra(EXTRA_RESTAURANT)) {
            String jsonResult = getIntent().getStringExtra(EXTRA_RESTAURANT);
            Gson gson = new Gson();
            Result result = gson.fromJson(jsonResult, Result.class);
            Log.e(EXTRA_RESTAURANT, gson.toJson(result));
            mTvAddressRestaurant.setText(result.getVicinity());
            mTvNameRestaurant.setText(result.getName());
            Glide.with(this)
                    .load(Constants.BASE_URL_PHOTO
                            + result.getPhotos().get(0).getPhotoReference()
                            + "&key=" + Constants.API_KEY)
                    .centerCrop()
                    .into(mIvPhotoRestaurant);



        }

    }

    public void CallRestaurant() {
        if (!Places.isInitialized()) {
            String gApiKey = Constants.API_KEY;
            Places.initialize(this, gApiKey);
        }
        placesClient = Places.createClient(this);
        List<Place.Field> placeFields = Arrays.asList(Place.Field.PHONE_NUMBER);
        // Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
        placesClient = Places.createClient(this);
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            if (place.getPhoneNumber() != null) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", place.getPhoneNumber(), null));
                startActivity(callIntent);
            } else {
                Toast.makeText(this, ("nono"), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openWebsitePage(String url) {

        if (!Places.isInitialized()) {
            String gApiKey = Constants.API_KEY;
            Places.initialize(this, gApiKey);
        }
        placesClient = Places.createClient(this);
        List<Place.Field> placeFields = Arrays.asList(Place.Field.WEBSITE_URI);
        // Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
        placesClient = Places.createClient(this);
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            Uri uri = Uri.parse(String.valueOf(place.getWebsiteUri()));
            Log.e(TAG, String.valueOf(uri));
            if (url != null) {
                Uri webPage = Uri.parse(String.valueOf(uri));
                Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            } else {
                Toast.makeText(DetailsRestaurantActivity.this, "no", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void userActionClick() {
        mButtonCall.setOnClickListener(v -> {
            CallRestaurant();


        });
        mButtonWeb.setOnClickListener(v ->
                openWebsitePage("")
        );
        mfab.setOnClickListener(v -> {


        });

    }

    private void subscribeObservers() {

        mRestaurantDetailViewModel.getRestaurantRepository().observe(this, new Observer<PlaceDetail>() {
            @Override
            public void onChanged(PlaceDetail placeDetail) {
                mResultDetail = placeDetail.getResult();
                String name = mResultDetail.getName();
                Log.e("from viewModel", String.valueOf(name));
            }
        });

    }
}
