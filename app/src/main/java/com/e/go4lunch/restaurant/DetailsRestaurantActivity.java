package com.e.go4lunch.restaurant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.e.go4lunch.R;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.repositories.injection.App;
import com.e.go4lunch.repositories.injection.Injection;
import com.e.go4lunch.repositories.injection.ViewModelFactory;
import com.e.go4lunch.util.Constants;
import com.e.go4lunch.workmates.WorkmateViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsRestaurantActivity extends AppCompatActivity {

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
    private RestaurantViewModel mRestaurantViewModel;
    private WorkmateViewModel mWorkmateViewModel;
    private String placeId, phone, webSite;
    private Restaurant mRestaurant;
    private Workmates currentWorkmate;
    private List<Restaurant> mRestaurantListFav, mRestaurantList;
    private List<Workmates> mWorkmatesList = new ArrayList<>();
    private RestaurantDetailAdapter mRestaurantDetailAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);


        configureViewModelRestaurant();
        configureViewModelWorkmate();
        getIncomingIntent();
        configureRecyclerView();
        getRestaurantList();
        getRestaurant();
        getCurrentWorkmate();
        userActionClick();


    }


    // ----------------------------------------------------------------------------
    // ----- get intent from recyclerView, map, mainActivity and show details -----
    // ----------------------------------------------------------------------------
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
            phone = restaurant.getPhoneNumber();
            webSite = restaurant.getWebSite();
            mRestaurant = restaurant;

            configureRecyclerView();


        }
    }

    // ------------------------------------------
    // ----- call phone number of the place -----
    // ------------------------------------------
    public void callRestaurant() {
        if (phone != null) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse(String.format("tel:%s", phone)));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL);
            } else {
                startActivity(callIntent);
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_phone), Toast.LENGTH_LONG).show();
        }
    }

    // -------------------------------------
    // ----- show webSite of the place -----
    // -------------------------------------
    public void openWebsite() {
        if (webSite != null) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(String.valueOf(webSite)));
            startActivity(intent);
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_web_site), Toast.LENGTH_SHORT).show();
        }
    }


    // --------------------------
    // ----- Action of user -----
    // --------------------------
    public void userActionClick() {
        mButtonCall.setOnClickListener(v -> callRestaurant());

        mButtonWeb.setOnClickListener(v -> openWebsite());

        mfab.setOnClickListener(v -> actionOnFab());

        mButtonLike.setOnClickListener(v -> actionOnLikeButton());

    }

    // ---------------------------------
    // ----- Configuring ViewModel -----
    // ---------------------------------
    private void configureViewModelWorkmate() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.mWorkmateViewModel = new ViewModelProvider(this, viewModelFactory).get(WorkmateViewModel.class);

    }

    private void configureViewModelRestaurant() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.mRestaurantViewModel = new ViewModelProvider(this, viewModelFactory).get(RestaurantViewModel.class);
        String lat = App.getInstance().getLat();
        String lng = App.getInstance().getLng();
        mRestaurantViewModel.setPlace(Constants.TYPE, lat + " " + lng, Constants.RADIUS);


    }

    // ---------------------------------
    // ----- Configuring Observers -----
    // ---------------------------------
    private void getCurrentWorkmate() {
        mWorkmateViewModel.getCurrentWorkmate().observe(this, event -> {
            Workmates workmates = event.getContentIfNotHandled();
            if (workmates != null) {
                currentWorkmate = workmates;
                mRestaurantListFav = workmates.getListRestaurantFavorite();
                updateRestaurant(mRestaurant);
            }

        });

    }


    private void getRestaurant() {
        mRestaurantViewModel.getRestaurant(placeId).observe(this, event -> {
            Restaurant restaurant = event.getContentIfNotHandled();
            if (restaurant != null) {
                mWorkmatesList = restaurant.getWorkmatesList();
                mRestaurant.setWorkmatesList(mWorkmatesList);
                mRestaurantDetailAdapter.setWorkmates(mWorkmatesList);

            }


        });
    }

    private void getRestaurantList() {
        mRestaurantViewModel.getRestaurantList().observe(this, restaurants -> {
            if (restaurants != null) {
                mRestaurantList = restaurants;


            }

        });
    }


    // ----------------------------------
    // ----- Configuring LikeButton -----
    // ----------------------------------
    private void actionOnLikeButton() {
        if (currentWorkmate.getListRestaurantFavorite() == null) {
            mRestaurantListFav = new ArrayList<>();
        } else {
            mRestaurantListFav = currentWorkmate.getListRestaurantFavorite();
        }
        if (!currentWorkmate.getListRestaurantFavorite().contains(mRestaurant)) {
            mRestaurantListFav.add(mRestaurant);
        } else {
            mRestaurantListFav.remove(mRestaurant);
        }
        this.mWorkmateViewModel.updateIsRestaurantFavorite(mRestaurantListFav);
        this.updateIVLike();

    }

    // ------------------------------
    // ----- updating like Star -----
    // ------------------------------
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

    // ----------------------------------------
    // ----- Configuring ChoiceButton FAB -----
    // ----------------------------------------
    private void actionOnFab() {
        Workmates workmatesChoice = new Workmates(currentWorkmate.getWorkmateEmail(), currentWorkmate.getWorkmateName(), currentWorkmate.getUrlPicture());
        if (currentWorkmate.getRestaurantChosen() == null) {
            this.mfab.setImageResource(R.drawable.ic_check_circle_black_24dp);
            this.mWorkmatesList.add(workmatesChoice);
            this.currentWorkmate.setRestaurantChosen(mRestaurant);
            this.mWorkmateViewModel.updateRestaurantChosen(currentWorkmate.getRestaurantChosen());
            this.mRestaurantViewModel.updateRestaurantWorkmateList(mRestaurant.getPlaceId(), mWorkmatesList);
            this.mRestaurantDetailAdapter.notifyDataSetChanged();

        } else {
            updateSelectedRestaurant(currentWorkmate.getRestaurantChosen());
            this.mfab.setImageResource(R.drawable.ic_check_black_24dp);
            this.mWorkmatesList.remove(workmatesChoice);
            this.currentWorkmate.setRestaurantChosen(null);
            this.mWorkmateViewModel.updateRestaurantChosen(currentWorkmate.getRestaurantChosen());
            this.mRestaurantViewModel.updateRestaurantWorkmateList(mRestaurant.getPlaceId(), mWorkmatesList);
            this.mRestaurantDetailAdapter.notifyDataSetChanged();
            Toast.makeText(this, R.string.you_changed_your_choice, Toast.LENGTH_SHORT).show();

        }
    }
    // ------------------------
    // ----- updating Fab -----
    // ------------------------
    private void updateFab() {
        if (this.currentWorkmate.getRestaurantChosen() != null) {
            if (currentWorkmate.getRestaurantChosen().equals(mRestaurant))
                this.mfab.setImageResource(R.drawable.ic_check_circle_black_24dp);
        } else {
            this.mfab.setImageResource(R.drawable.ic_check_black_24dp);
        }

    }
    // ----------------------------------
    // ----- updating workmatesList -----
    // ----------------------------------
    private void updateSelectedRestaurant(Restaurant restaurant) {
        if (mRestaurantList.contains(restaurant)) {
            Workmates workmatesChoice = new Workmates(currentWorkmate.getWorkmateEmail(), currentWorkmate.getWorkmateName(), currentWorkmate.getUrlPicture());
            int in = mRestaurantList.indexOf(restaurant);
            List<Workmates> workmatesList = mRestaurantList.get(in).getWorkmatesList();
            workmatesList.remove(workmatesChoice);
            mRestaurantViewModel.updateRestaurantWorkmateList(restaurant.getPlaceId(), workmatesList);
        }
    }

    // ------------------------------------
    // ----- Configuring RecyclerView -----
    // ------------------------------------
    private void configureRecyclerView() {
        mRecyclerViewDetail.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerViewDetail.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerViewDetail.addItemDecoration(dividerItemDecoration);
        mRestaurantDetailAdapter = new RestaurantDetailAdapter(this);
        mRecyclerViewDetail.setAdapter(mRestaurantDetailAdapter);
        mRestaurantDetailAdapter.notifyDataSetChanged();

    }

    // -----------------------------------------------
    // ----- Update View with data from FireBase -----
    // -----------------------------------------------
    private void updateRestaurant(Restaurant restaurant) {
        String name = restaurant.getName();
        String address = restaurant.getAddress();
        String photo = String.valueOf(restaurant.getUrlPhoto());
        this.mTvNameRestaurant.setText(name);
        this.mTvAddressRestaurant.setText(address);
        Glide.with(this)
                .load(Constants.BASE_URL_PHOTO
                        + photo
                        + "&key=" + Constants.API_KEY)
                .centerCrop()
                .into(mIvPhotoRestaurant);
        updateIVLike();
        updateFab();

    }


}