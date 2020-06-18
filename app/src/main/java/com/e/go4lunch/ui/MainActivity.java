package com.e.go4lunch.ui;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.e.go4lunch.R;
import com.e.go4lunch.auth.AuthActivity;
import com.e.go4lunch.injection.App;
import com.e.go4lunch.injection.Injection;
import com.e.go4lunch.injection.ViewModelFactory;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.notifications.MyWorker;
import com.e.go4lunch.restaurant.DetailsRestaurantActivity;
import com.e.go4lunch.restaurant.MapsFragment;
import com.e.go4lunch.restaurant.RestaurantViewModel;
import com.e.go4lunch.restaurant.RestaurantsFragment;
import com.e.go4lunch.workmates.ListFragment;
import com.e.go4lunch.workmates.WorkmateViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    //FOR DESIGN
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.toolbar_editText)
    EditText mEditTextToolbar;

    //FOR DATA
    private ImageView mImageViewProfil;
    private TextView mTextViewName;
    private TextView mTextViewEmail;
    private WorkmateViewModel mWorkmateViewModel;
    private RestaurantViewModel mRestaurantViewModel;
    private Workmates currentWorkmate;
    private Restaurant mRestaurant;
    private String placeId;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final String WORK_TAG = "WORK_REQUEST_TAG_Go4Lunch";
    private static final int SIGN_OUT_TASK = 10;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 11;
    public static final String EXTRA_AUTOCOMPLETE = "restaurantId";
    private static final String TAG = "FROM_AUTOCOMPLETE";
    // Creating identifier to identify REST REQUEST (Update username)
    private static final int UPDATE_USERNAME = 30;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        mTextViewName = navigationView.getHeaderView(0).findViewById(R.id.tv_name_header);
        mTextViewEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_mail_header);
        mImageViewProfil = navigationView.getHeaderView(0).findViewById(R.id.imageProfile_header);


        configureDrawerLayout();
        configureNavigationView();
        configureBottomNavigationView();
        configureToolbar();
        configureViewModel();
        configureRestaurantViewModel();
        updateUIWhenCreating();
        getCurrentWorkmate();

        scheduleWork(17, 20);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_api_key));
        }

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MapsFragment()).commit();
        }


        //      this.mEditTextToolbar.addTextChangedListener(new TextWatcher() {
        //          @Override
        //          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        //          @Override
        //          public void onTextChanged(CharSequence s, int start, int before, int count) {}
        //          @Override
        //          public void afterTextChanged(Editable s)
        //          {
        //              String input = s.toString();
        //              mMapsFragment = new MapsFragment();
        //             mMapsFragment.autocompleteSearch(input);
        //          }
        //      });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Search restaurants");
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
                return false;
        }

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.action_map:
                        selectedFragment = new MapsFragment();
                        this.mEditTextToolbar.setVisibility(View.VISIBLE);
                        break;

                    case R.id.action_list:
                        selectedFragment = new RestaurantsFragment();
                        this.mEditTextToolbar.setVisibility(View.VISIBLE);
                        break;

                    case R.id.action_workmates:
                        selectedFragment = new ListFragment();
                        this.mEditTextToolbar.setVisibility(View.INVISIBLE);
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            };


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.lunch_drawer:
               // getCurrentWorkmate();
                showLunch();
                break;

            case R.id.setting_drawer:
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

    //  Configure Drawer Layout
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


    //  Configure NavigationView
    private void configureNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    //  Configure Toolbar
    private void configureToolbar() {
        setSupportActionBar(mToolbar);

    }

    //  Configure BottomNavigationView
    private void configureBottomNavigationView() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }

    // logout from Firebase in drawerMenu create a request
    private void logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRequestCompleted(SIGN_OUT_TASK));
    }

    private OnSuccessListener<Void> updateUIAfterRequestCompleted(final int origin) {
        return aVoid -> {
            if (origin == SIGN_OUT_TASK) {
                Intent intent = new Intent(this, AuthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        };
    }


    public void alertLogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are your sure you want to logout.");
        builder.setPositiveButton("YES", (dialog, which) -> {
            logout();
            FirebaseAuth.getInstance().signOut();
        });
        builder.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
        builder.show();
    }


    public void onSearchCalled() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ID);

        // Create a RectangularBounds object.
        double lat = Double.parseDouble(App.getInstance().getLat());
        double lng = Double.parseDouble(App.getInstance().getLng());

        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(lat, lng), //dummy lat/lng
                new LatLng(49.092363, 2.229925));

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .setCountry("FR")
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


                Place place = Autocomplete.getPlaceFromIntent(data);
                String placeId = place.getId();
                Log.e(TAG, "Place: " + placeId);
                Toast.makeText(MainActivity.this, "Name: " + place.getName(), Toast.LENGTH_LONG).show();


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

                Status status = Autocomplete.getStatusFromIntent(data);

                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
    // --------------------
    // UI
    // --------------------

    //  Arranging method that updating UI with Firestore data
    private void updateUIWhenCreating() {
        if (this.getCurrentUser() != null) {

            if (this.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mImageViewProfil);
            } else {
                Glide.with(this)
                        .load(R.drawable.ic_hot_food_in_a_bowl2)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mImageViewProfil);

            }
            if (this.getCurrentUser().getDisplayName() == null) {
                String name = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ? getString(Integer.parseInt("info_no_username_found")) : this.getCurrentUser().getDisplayName();
                this.mTextViewName.setText(name);
            } else {
                getCurrentWorkmate();
            }
            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(Integer.parseInt("info_no_email_found")) : this.getCurrentUser().getEmail();

            this.mTextViewEmail.setText(email);


        }

    }

    // Get additional data from Firestore (Username)
    // Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mWorkmateViewModel = ViewModelProviders.of(this, mViewModelFactory).get(WorkmateViewModel.class);
    }

    private void configureRestaurantViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.mRestaurantViewModel = ViewModelProviders.of(this, viewModelFactory).get(RestaurantViewModel.class);
    }

    private void getCurrentWorkmate() {
        String workmateUid = FirebaseAuth.getInstance().getUid();
        mWorkmateViewModel.getWorkmate(workmateUid).observe(this, workmates -> {
            String name = workmates.getWorkmateName();
            mTextViewName.setText(name);
            currentWorkmate = workmates;
            if (currentWorkmate.getRestaurantChoosen() != null) {
                placeId = currentWorkmate.getRestaurantChoosen().getPlaceId();
                getRestaurant();

            }

        });

    }

    private void getRestaurant() {
        mRestaurantViewModel.getRestaurant(placeId).observe(this, restaurant -> {
            mRestaurant = restaurant;


        });
    }

    private void showLunch() {
        getCurrentWorkmate();
        if (currentWorkmate.getRestaurantChoosen() != null) {
            Intent intent = new Intent(this, DetailsRestaurantActivity.class);
            Gson gson = new Gson();
            String jsonSelectedRestaurant = gson.toJson(mRestaurant);
            intent.putExtra(DetailsRestaurantActivity.EXTRA_RESTAURANT, jsonSelectedRestaurant);
            startActivity((intent));
        } else {
            alertDisplayChoice();
        }
    }

    public void alertDisplayChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You haven't made your choice yet ");
        builder.setPositiveButton("Go4Lunch", (dialog, which) -> {

        });

        builder.show();
    }

    public static void scheduleWork(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        long nowMillis = calendar.getTimeInMillis();

        if (calendar.get(Calendar.HOUR_OF_DAY) > hour ||
                (calendar.get(Calendar.HOUR_OF_DAY) == hour && calendar.get(Calendar.MINUTE) + 1 >= minute)) {
            calendar.add(Calendar.DAY_OF_MONTH, 0);
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long diff = calendar.getTimeInMillis() - nowMillis;

        WorkManager mWorkManager = WorkManager.getInstance();
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        mWorkManager.cancelAllWorkByTag(WORK_TAG);
        OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setConstraints(constraints)
                .setInitialDelay(diff, TimeUnit.MILLISECONDS)
                .addTag(WORK_TAG)
                .build();
        mWorkManager.enqueue(mRequest);

    }


}
