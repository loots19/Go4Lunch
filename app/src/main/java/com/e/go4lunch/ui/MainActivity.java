package com.e.go4lunch.ui;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.e.go4lunch.R;
import com.e.go4lunch.auth.AuthActivity;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.repositories.injection.App;
import com.e.go4lunch.repositories.injection.Injection;
import com.e.go4lunch.repositories.injection.ViewModelFactory;
import com.e.go4lunch.restaurant.DetailsRestaurantActivity;
import com.e.go4lunch.restaurant.MapsFragment;
import com.e.go4lunch.restaurant.RestaurantViewModel;
import com.e.go4lunch.restaurant.RestaurantsFragment;
import com.e.go4lunch.service.ControllerWorkerManager;
import com.e.go4lunch.util.Constants;
import com.e.go4lunch.workmates.WorkmateFragment;
import com.e.go4lunch.workmates.WorkmateViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    // ----------------- FOR DESIGN -----------------
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    // ----------------- FOR DATA -----------------
    private ImageView mImageViewProfile;
    private TextView mTextViewName;
    private TextView mTextViewEmail;
    private WorkmateViewModel mWorkmateViewModel;
    private RestaurantViewModel mRestaurantViewModel;
    private Workmates currentWorkmate;
    private Workmates currentWorkmateName;
    private Restaurant mRestaurant;
    private String placeId;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final int SIGN_OUT_TASK = 10;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 11;
    private Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        mTextViewName = navigationView.getHeaderView(0).findViewById(R.id.tv_name_header);
        mTextViewEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_mail_header);
        mImageViewProfile = navigationView.getHeaderView(0).findViewById(R.id.imageProfile_header);


        configureNavigationView();
        configureBottomNavigationView();
        configureToolbar();
        configureViewModel();
        configureRestaurantViewModel();
        configureDrawerLayout();
        setupOpenDetailRestaurant();
        updateUIWhenCreating();


        // --------------------------------------------------------------------
        // set time for notification and clear everyday the selected restaurant
        // --------------------------------------------------------------------
        // ControllerWorkerManager.scheduleWork(10, 38);
        // ControllerWorkerManager.deleteWork(10, 42);


        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MapsFragment()).commit();
        }
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), Constants.API_KEY);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(getComponentName()));
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.search:
                onSearchCalled();
                return true;
            default:
                return true;
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.action_map:
                        selectedFragment = new MapsFragment();
                        this.mToolbar.getMenu().findItem(R.id.search).setVisible(true);
                        break;

                    case R.id.action_list:
                        selectedFragment = new RestaurantsFragment();
                        this.mToolbar.getMenu().findItem(R.id.search).setVisible(true);
                        break;

                    case R.id.action_workmates:
                        selectedFragment = new WorkmateFragment();
                        this.mToolbar.getMenu().findItem(R.id.search).setVisible(false);
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        Objects.requireNonNull(selectedFragment)).commit();

                return true;
            };


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
        setContentView(R.layout.fragment_maps);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.lunch_drawer:
                mRestaurantViewModel.showUserRestaurant(placeId);
                break;

            case R.id.setting_drawer:
                ControllerWorkerManager.goToNotificationSettings(null, getApplicationContext());
                break;

            case R.id.logout_drawer:
                alertLogOut();
                break;

            default:
                break;

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // -----------------------
    // Configure Drawer Layout
    // -----------------------
    private void configureDrawerLayout() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    // -------------------------
    // Configure NavigationView
    // -------------------------
    private void configureNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    // ------------------
    // Configure Toolbar
    // ------------------
    private void configureToolbar() {
        setSupportActionBar(mToolbar);

    }

    // ------------------------------
    // Configure BottomNavigationView
    // ------------------------------
    private void configureBottomNavigationView() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }

    // ---------------------------------------------------
    // logout from FireBase in drawerMenu create a request
    // ---------------------------------------------------
    private void logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRequestCompleted(SIGN_OUT_TASK));
    }

    private OnSuccessListener<Void> updateUIAfterRequestCompleted(final int origin) {
        return aVoid -> {
            if (origin == SIGN_OUT_TASK) {
                Intent intent = new Intent(this, AuthActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        };
    }


    // ---------------------------------------------
    // Alert Dialog when workmates want disconnected
    // ---------------------------------------------
    public void alertLogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.titlle_alert);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            logout();
            FirebaseAuth.getInstance().signOut();
        });
        builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel());
        builder.show();
    }


    public void onSearchCalled() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.ID, Place.Field.LAT_LNG);
        // Create a RectangularBounds object.
        double lat = Double.parseDouble(App.getInstance().getLat());
        double lng = Double.parseDouble(App.getInstance().getLng());
        double lat1 = lat + 0.1;
        double lng1 = lng + 0.1;

        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(lat, lng), //dummy lat/lng
                new LatLng(lat1, lng1));
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setLocationBias(bounds)
                .build(this);

        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(Objects.requireNonNull(data));
                placeId = place.getId();
                this.getRestaurantAuto();

            }
        }
    }

    // -----------------------------------------------------
    // Arranging method that updating UI with FireStore data
    // -----------------------------------------------------
    private void updateUIWhenCreating() {
        getWorkmateName();
        if (this.getCurrentUser() != null) {
            if (this.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mImageViewProfile);
            } else {
                Glide.with(this)
                        .load(R.drawable.ic_hot_food_in_a_bowl2)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mImageViewProfile);
            }
        }
        if (Objects.requireNonNull(this.getCurrentUser()).getDisplayName() != null) {
            String name = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(Integer.parseInt("info_no_email_found")) : this.getCurrentUser().getDisplayName();
            this.mTextViewName.setText(name);
        }
        String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(Integer.parseInt("info_no_email_found")) : this.getCurrentUser().getEmail();
        this.mTextViewEmail.setText(email);

    }

    // ---------------------
    // Configuring ViewModel
    // ---------------------
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mWorkmateViewModel = new ViewModelProvider(this, mViewModelFactory).get(WorkmateViewModel.class);

    }

    private void configureRestaurantViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.mRestaurantViewModel = new ViewModelProvider(this, viewModelFactory).get(RestaurantViewModel.class);

    }

    // ---------------------
    // Configuring Observers
    // ---------------------
    private void getCurrentWorkmate(boolean showLunch) {
        mWorkmateViewModel.getCurrentWorkmate().observe(this, event -> {
            Workmates workmates = event.getContentIfNotHandled();
            if (workmates != null) {
                currentWorkmate = workmates;
                if (currentWorkmate.getRestaurantChosen() != null) {
                    placeId = currentWorkmate.getRestaurantChosen().getPlaceId();
                    this.getRestaurant(showLunch);
                } else if (showLunch) {
                    this.showLunch();

                }
            }
        });
    }

    private void getWorkmateName() {
        mWorkmateViewModel.getWorkmateNames().observe(this, workmates -> {
            currentWorkmateName = workmates;
            String name = currentWorkmateName.getWorkmateName();
            mTextViewName.setText(name);

        });
    }

    private void getRestaurant(boolean showLunch) {
        mRestaurantViewModel.getRestaurant(placeId).observe(this, event -> {
            Restaurant restaurant = event.getContentIfNotHandled();
            if (restaurant != null) {
                mRestaurant = restaurant;
                if (showLunch) {
                    showLunch();
                }
            }
        });
    }

    private void getRestaurantAuto() {
        mRestaurantViewModel.getRestaurantAuto(placeId).observe(this, restaurant -> {
            mRestaurant = restaurant;
            if (restaurant != null) {
                showRestaurant(restaurant);
            } else {
                alertAutocompleteResult();
            }
        });
    }


    private void setupOpenDetailRestaurant() {
        mRestaurantViewModel.getOpenDetailRestaurant().observe(this, objectEvent -> {
            if (objectEvent.getContentIfNotHandled() != null) {
                getCurrentWorkmate(true);

            }
        });
    }

    // -------------------------------------------------
    // open detail activity after result of autocomplete
    // -------------------------------------------------
    private void showRestaurant(Restaurant restaurant) {
        Intent intent = new Intent(this, DetailsRestaurantActivity.class);
        Gson gson = new Gson();
        String jsonSelectedRestaurant = gson.toJson(restaurant);
        intent.putExtra(DetailsRestaurantActivity.EXTRA_RESTAURANT, jsonSelectedRestaurant);
        startActivity(intent);

    }

    // -------------------------------------------------------------
    // open detail activity when user click on menuDrawer "My lunch"
    // -------------------------------------------------------------
    private void showLunch() {
        if (this.currentWorkmate.getRestaurantChosen() == null) {
            alertDisplayChoice();
        } else {
            Intent intent = new Intent(this, DetailsRestaurantActivity.class);
            Gson gson = new Gson();
            String jsonSelectedRestaurant = gson.toJson(mRestaurant);
            intent.putExtra(DetailsRestaurantActivity.EXTRA_RESTAURANT, jsonSelectedRestaurant);
            startActivity(intent);
        }

    }

    // ------------
    // Alert Dialog
    // ------------
    public void alertDisplayChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.tittle_alert_Lunch);
        builder.setPositiveButton(R.string.app_name, (dialog, which) -> {

        });

        builder.show();
    }

    public void alertAutocompleteResult() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.tittle_autocomplete_result);
        builder.setPositiveButton(R.string.app_name, (dialog, which) -> {

        });

        builder.show();
    }
}







