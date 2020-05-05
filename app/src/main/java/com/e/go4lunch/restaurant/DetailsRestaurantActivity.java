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
import android.content.ActivityNotFoundException;
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
import com.e.go4lunch.injection.Injection;
import com.e.go4lunch.injection.ViewModelFactory;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.myPlace.Result;
import com.e.go4lunch.models.placeDetail.PlaceDetail;
import com.e.go4lunch.models.placeDetail.ResultDetail;
import com.e.go4lunch.util.Constants;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
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
    private RestaurantDetailViewModel mRestaurantDetailViewModel;
    private ResultDetail mResultDetail;
    private String placeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        userActionClick();
        getIncomingIntent();
        configureViewModel();
        setupObservers();


    }

    //get intent from recyclerView and map and show details
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
            placeId = result.getPlaceId();


        }

    }

    // show phone number of the place
    public void callRestaurant() {
        if (mResultDetail.getInternationalPhoneNumber() != null) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mResultDetail.getInternationalPhoneNumber(), null));
            startActivity(callIntent);
        } else {
            Toast.makeText(this, ("nono"), Toast.LENGTH_SHORT).show();
        }
    }

    // show webSite of the place
    public void openWebsitePage() {
        if (mResultDetail.getWebsite() != null) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mResultDetail.getWebsite()));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error text", Toast.LENGTH_SHORT).show();
        }
    }

    // Action of user
    public void userActionClick() {
        mButtonCall.setOnClickListener(v -> {
            callRestaurant();

        });
        mButtonWeb.setOnClickListener(v ->
                openWebsitePage()
        );
        mfab.setOnClickListener(v -> {


        });

    }


    // Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mRestaurantDetailViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RestaurantDetailViewModel.class);
        mRestaurantDetailViewModel.setInput(placeId);
    }

    private void setupObservers() {
        mRestaurantDetailViewModel.getPlaceDetail().observe(this, new Observer<PlaceDetail>() {
            @Override
            public void onChanged(PlaceDetail placeDetail) {
                mResultDetail = placeDetail.getResult();

            }
        });


    }


}
