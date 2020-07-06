package com.e.go4lunch.restaurant;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.e.go4lunch.R;
import com.e.go4lunch.injection.Injection;
import com.e.go4lunch.injection.ViewModelFactory;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.models.placeDetail.ResultDetail;
import com.e.go4lunch.ui.BaseActivity;
import com.e.go4lunch.util.Constants;
import com.e.go4lunch.workmates.WorkmateViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsRestaurantActivity extends BaseActivity {

    // ----------------- FOR DESIGN -----------------
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
    @BindView(R.id.recycler_view_detail)
    RecyclerView mRecyclerViewDetail;


    // ----------------- FOR DATA -----------------
    private final static int REQUEST_CODE_CALL = 3;
    public static final String EXTRA_RESTAURANT = "restaurant";
    private RestaurantDetailViewModel mRestaurantDetailViewModel;
    private RestaurantViewModel mRestaurantViewModel;
    private WorkmateViewModel mWorkmateViewModel;
    private ResultDetail mResultDetail;
    private String placeId;
    private String workmateUid;
    private Restaurant mRestaurant;
    private Workmates currentWorkmate;
    private List<Restaurant> mRestaurantListFavFromFirebase;
    private List<Restaurant> mRestaurantListFromFirebase;
    private List<Workmates> mWorkmatesList = new ArrayList<>();
    private RestaurantDetailAdapter mRestaurantDetailAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);


        getIncomingIntent();
        configureRecyclerView();
        configureViewModelRestaurant();
        configureViewModelWorkmate();
        configureViewModelDetail();
        getCurrentWorkmate();
        getDetailOfPlace();
        userActionClick();
        getRestaurant();
        getRestaurantList();


    }


    //----------------- get intent from recyclerView and map and show details -----------------
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
                            + "&key=" + getString(R.string.google_api_key))
                    .centerCrop()
                    .into(mIvPhotoRestaurant);
            placeId = restaurant.getPlaceId();
            mRestaurant = restaurant;
            configureRecyclerView();


        }
    }

    // ----------------- call phone number of the place -----------------
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

    // ----------------- show webSite of the place -----------------

    public void openWebsitePage() {
        if (mResultDetail.getWebsite() != null) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mResultDetail.getWebsite()));
            startActivity(intent);
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_web_site), Toast.LENGTH_LONG).show();
        }
    }

    // ----------------- Action of user -----------------

    public void userActionClick() {
        mButtonCall.setOnClickListener(v -> callRestaurant());

        mButtonWeb.setOnClickListener(v -> openWebsitePage());

        mfab.setOnClickListener(v -> DetailsRestaurantActivity.this.actionOnFab());

        mButtonLike.setOnClickListener(v -> actionOnLikeButton());


    }


    // ----------------- Configuring ViewModel -----------------

    private void configureViewModelDetail() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mRestaurantDetailViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RestaurantDetailViewModel.class);
        mRestaurantDetailViewModel.setInput(placeId);


    }

    private void configureViewModelWorkmate() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.mWorkmateViewModel = ViewModelProviders.of(this, viewModelFactory).get(WorkmateViewModel.class);


    }

    private void configureViewModelRestaurant() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.mRestaurantViewModel = ViewModelProviders.of(this, viewModelFactory).get(RestaurantViewModel.class);


    }
    // ----------------- Configuring Observers -----------------

    private void getDetailOfPlace() {
        mRestaurantDetailViewModel.getPlaceDetail().observe(this, placeDetail -> mResultDetail = placeDetail.getResult());


    }

    private void getCurrentWorkmate() {
        workmateUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        mWorkmateViewModel.getWorkmate(workmateUid).observe(this, workmates -> {
            currentWorkmate = workmates;
            mRestaurantListFavFromFirebase = workmates.getListRestaurantFavorite();
            updateRestaurant(mRestaurant);


        });

    }

    private void getRestaurantList() {
        mRestaurantViewModel.getRestaurantList().observe(this, restaurants -> {
            mRestaurantListFromFirebase = restaurants;
            if (!mRestaurantListFromFirebase.contains(mRestaurant)) {
                mWorkmatesList = new ArrayList<>();
                mRestaurantViewModel.createRestaurant(mRestaurant.getPlaceId(), mRestaurant.getName(), mRestaurant.getAddress(), mRestaurant.getUrlPhoto(),mRestaurant.getOpenNow(),mRestaurant.getLocation(),mRestaurant.getRating(), mWorkmatesList);
            }
            getCurrentWorkmate();


        });
    }

    private void getRestaurant() {
        mRestaurantViewModel.getRestaurant(placeId).observe(this, restaurant -> {
            mWorkmatesList = restaurant.getWorkmatesList();
            mRestaurant.setWorkmatesList(mWorkmatesList);
            mRestaurantDetailAdapter.setWorkmates(mWorkmatesList);


        });
    }

    // Configuring LikeButton
    private void actionOnLikeButton() {

        if (currentWorkmate.getListRestaurantFavorite() == null) {

            mRestaurantListFavFromFirebase = new ArrayList<>();

        } else {
            mRestaurantListFavFromFirebase = currentWorkmate.getListRestaurantFavorite();
        }
        if (!currentWorkmate.getListRestaurantFavorite().contains(mRestaurant)) {
            mRestaurantListFavFromFirebase.add(mRestaurant);


        } else {
            mRestaurantListFavFromFirebase.remove(mRestaurant);
        }
        this.mWorkmateViewModel.updateIsRestaurantFavorite(workmateUid, mRestaurantListFavFromFirebase);
        this.updateIVLike();


    }

    // updating like Star
    private void updateIVLike() {

        boolean fav = false;
        if (currentWorkmate.getListRestaurantFavorite() != null) {
            if (currentWorkmate.getListRestaurantFavorite().contains(mRestaurant)) {
                fav = true;

            }
        }

        if (fav) {
            this.mImageViewStar.setImageResource(R.drawable.ic_star_black1_24dp);
        } else {
            this.mImageViewStar.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
    }

    // Configuring ChoiceButton FAB
    private void actionOnFab() {

        Workmates workmatesChoice = new Workmates(currentWorkmate.getWorkmateEmail(), currentWorkmate.getWorkmateName(), currentWorkmate.getUrlPicture());

        if (currentWorkmate.getRestaurantChosen() == null) {
            this.mfab.setImageResource(R.drawable.ic_check_circle_black_24dp);
            this.mRestaurantViewModel.createRestaurant(placeId, mRestaurant.getName(), mRestaurant.getAddress(), mRestaurant.getUrlPhoto(),mRestaurant.getOpenNow(),mRestaurant.getLocation(),mRestaurant.getRating(), mWorkmatesList);
            this.mWorkmatesList.add(workmatesChoice);
            this.currentWorkmate.setRestaurantChosen(mRestaurant);
            this.mWorkmateViewModel.updateRestaurantChosen(workmateUid, currentWorkmate.getRestaurantChosen());
            this.mRestaurantViewModel.updateRestaurantWorkmateList(mRestaurant.getPlaceId(), mWorkmatesList);
            this.mRestaurantDetailAdapter.notifyDataSetChanged();

        } else {
            updateSelectedRestaurant(currentWorkmate.getRestaurantChosen());
            this.mfab.setImageResource(R.drawable.ic_check_black_24dp);
            this.mWorkmatesList.remove(workmatesChoice);
            this.mRestaurantViewModel.updateRestaurantWorkmateList(mRestaurant.getPlaceId(), mWorkmatesList);
            this.currentWorkmate.setRestaurantChosen(null);
            this.mWorkmateViewModel.updateRestaurantChosen(workmateUid, currentWorkmate.getRestaurantChosen());
            this.mRestaurantDetailAdapter.notifyDataSetChanged();
            Toast.makeText(this, R.string.you_changed_your_choice, Toast.LENGTH_SHORT).show();

        }


    }


    private void updateFab() {
        if (this.currentWorkmate.getRestaurantChosen() != null) {
            if (currentWorkmate.getRestaurantChosen().equals(mRestaurant))
                this.mfab.setImageResource(R.drawable.ic_check_circle_black_24dp);

        } else {
            this.mfab.setImageResource(R.drawable.ic_check_black_24dp);


        }

    }

    private void updateSelectedRestaurant(Restaurant restaurant) {
        if (mRestaurantListFromFirebase.contains(restaurant)) {
            Workmates workmatesChoice = new Workmates(currentWorkmate.getWorkmateEmail(), currentWorkmate.getWorkmateName(), currentWorkmate.getUrlPicture());
            int in = mRestaurantListFromFirebase.indexOf(restaurant);
            List<Workmates> workmatesList = mRestaurantListFromFirebase.get(in).getWorkmatesList();
            workmatesList.remove(workmatesChoice);
            mRestaurantViewModel.updateRestaurantWorkmateList(restaurant.getPlaceId(), workmatesList);
        }
    }


    // Configuring RecyclerView
    private void configureRecyclerView() {
        mRecyclerViewDetail.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerViewDetail.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerViewDetail.addItemDecoration(dividerItemDecoration);
        mRestaurantDetailAdapter = new RestaurantDetailAdapter(this);
        mRecyclerViewDetail.setAdapter(mRestaurantDetailAdapter);
        mRestaurantDetailAdapter.notifyDataSetChanged();


    }

    private void updateRestaurant(Restaurant restaurant) {
        String name = String.valueOf(restaurant.getName());
        String address = String.valueOf(restaurant.getAddress());
        String photo = String.valueOf(restaurant.getUrlPhoto());
        this.mTvNameRestaurant.setText(name);
        this.mTvAddressRestaurant.setText(address);
        Glide.with(this)
                .load(Constants.BASE_URL_PHOTO
                        + photo
                        + "&key=" + getString(R.string.google_api_key))
                .centerCrop()
                .into(mIvPhotoRestaurant);
        updateIVLike();
        updateFab();


    }


}