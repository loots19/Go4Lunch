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
import com.e.go4lunch.injection.Globals;
import com.e.go4lunch.injection.Injection;
import com.e.go4lunch.injection.ViewModelFactory;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.myPlace.Result;
import com.e.go4lunch.models.placeDetail.PlaceDetail;
import com.e.go4lunch.models.placeDetail.ResultDetail;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.e.go4lunch.ui.BaseActivity;
import com.e.go4lunch.util.Constants;
import com.e.go4lunch.workmates.WorkmateViewModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;

import static com.e.go4lunch.ui.MainActivity.EXTRA_AUTOCOMPLETE;
import static java.security.AccessController.getContext;

public class DetailsRestaurantActivity extends BaseActivity {

    @BindView(R.id.tv_name_restaurant_detail_activity)
    TextView mTvNameRestaurant;
    @BindView(R.id.tv_address_restaurant_detail_activity)
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
    @BindView(R.id.Im_Star_detail)
    ImageView mImageViewStar;

    private final static int REQUEST_CODE_CALL = 3;
    public static final String EXTRA_RESTAURANT = "restaurant";
    public static final String EXTRA_AUTOCOMPLETE = "restaurantId";
    private RestaurantDetailViewModel mRestaurantDetailViewModel;
    private RestaurantViewModel mRestaurantViewModel;
    private WorkmateViewModel mWorkmateViewModel;
    private ResultDetail mResultDetail;
    private String placeId;
    private String workmateId;
    private Workmates currentWorkmate;
    private Restaurant mRestaurant;
    private static List<Restaurant> restaurants = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);


        userActionClick();
        getIncomingIntent();
        configureViewModelDetail();
        configureViewModelWorkmate();
        configureViewModelRestaurant();
        setupObservers();
        getCurrentWorkmate();


    }

    //get intent from recyclerView and map and show details
    private void getIncomingIntent() {
        if (getIntent().hasExtra(EXTRA_RESTAURANT)) {
            String jsonResult = getIntent().getStringExtra(EXTRA_RESTAURANT);
            Gson gson = new Gson();
            Restaurant restaurant = gson.fromJson(jsonResult, Restaurant.class);
            Log.e(EXTRA_RESTAURANT, gson.toJson(restaurant));
            mTvAddressRestaurant.setText(restaurant.getAddress());
            mTvNameRestaurant.setText(restaurant.getName());
            Glide.with(this)
                    .load(Constants.BASE_URL_PHOTO
                            + restaurant.getUrlPhoto()
                            + "&key=" + Constants.API_KEY)
                    .centerCrop()
                    .into(mIvPhotoRestaurant);
            placeId = restaurant.getPlaceId();


        }
    }

    // call phone number of the place
    public void callRestaurant() {
        if (mResultDetail.getInternationalPhoneNumber() != null) {
            String phone = mResultDetail.getInternationalPhoneNumber();
            Uri uri = Uri.parse("tel:" + phone);
            Intent callIntent = new Intent(Intent.ACTION_CALL, uri);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL);
            } else {
                startActivity(callIntent);
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_phone), Toast.LENGTH_LONG).show();
        }
    }

    // show webSite of the place
    public void openWebsitePage() {
        if (mResultDetail.getWebsite() != null) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mResultDetail.getWebsite()));
            startActivity(intent);
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_web_site), Toast.LENGTH_LONG).show();
        }
    }

    // Action of user
    public void userActionClick() {
        mButtonCall.setOnClickListener(v -> callRestaurant());

        mButtonWeb.setOnClickListener(v -> openWebsitePage());

        mfab.setOnClickListener(v -> {
        });

        mButtonLike.setOnClickListener(v -> clickOnLikeButton());


    }


    // Configuring ViewModel
    private void configureViewModelDetail() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mRestaurantDetailViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RestaurantDetailViewModel.class);
        mRestaurantDetailViewModel.setInput(placeId);
    }

    private void configureViewModelWorkmate() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.mWorkmateViewModel = ViewModelProviders.of(this, viewModelFactory).get(WorkmateViewModel.class);
        this.getCurrentWorkmate();
    }

    private void configureViewModelRestaurant() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.mRestaurantViewModel = ViewModelProviders.of(this, viewModelFactory).get(RestaurantViewModel.class);


    }

    private void setupObservers() {
        mRestaurantDetailViewModel.getPlaceDetail().observe(this, placeDetail -> mResultDetail = placeDetail.getResult());


    }

    private void getCurrentWorkmate() {
        workmateId = FirebaseAuth.getInstance().getUid();
        mWorkmateViewModel.getWorkmatesMutableLiveData(workmateId).observe(this, new Observer<Workmates>() {
            @Override
            public void onChanged(Workmates workmates) {
                currentWorkmate = workmates;
            }
        });
    }


    private void clickOnLikeButton() {

        List<Restaurant> restaurantList;
        if (currentWorkmate.getRestaurantListFav() == null) {
            restaurantList = new ArrayList<>();

        } else {
            restaurantList = currentWorkmate.getRestaurantListFav();
        }
        if (!currentWorkmate.getRestaurantListFav().contains(mRestaurant)) {
            restaurantList.add(mRestaurant);
        } else {
            restaurantList.remove(mRestaurant);
        }
        this.mRestaurantViewModel.updateRestaurantListFavorite(workmateId, restaurantList);
        Log.e("testUpdate", String.valueOf(restaurantList));
        this.updateLike();

    }

    private void updateLike() {
        boolean fav = false;

        if (currentWorkmate.getRestaurantListFav() != null) {

            if (currentWorkmate.getRestaurantListFav().contains(mRestaurant)) {
                fav = true;
            }
        }
        if (fav) {
            this.mImageViewStar.setImageResource(R.drawable.ic_star_black_24dp);

            Toast.makeText(this, getResources().getString(R.string.add_to_favorites), Toast.LENGTH_LONG).show();

        } else {
            this.mImageViewStar.setImageResource(R.drawable.ic_star_border_black_24dp);
            Toast.makeText(this, getResources().getString(R.string.remove_to_favorites), Toast.LENGTH_LONG).show();

        }


    }

}

