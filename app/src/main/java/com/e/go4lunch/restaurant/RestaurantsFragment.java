package com.e.go4lunch.restaurant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.go4lunch.R;
import com.e.go4lunch.injection.App;
import com.e.go4lunch.injection.Injection;
import com.e.go4lunch.injection.ViewModelFactory;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.myPlace.Location;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.myPlace.Result;
import com.e.go4lunch.util.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RestaurantsFragment extends Fragment implements RestaurantAdapter.OnNoteListener {

    // ----------------- FOR DESIGN -----------------
    @BindView(R.id.recycler_view_restaurant)
    RecyclerView mRecyclerViewRestaurant;

    // ----------------- FOR DATA -----------------
    private RestaurantViewModel mRestaurantViewModel;
    private RestaurantAdapter mAdapter;
    private List<Restaurant> mRestaurants;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurants, container, false);
        ButterKnife.bind(this, view);

        configureViewModel();
        getListFromPlace();
        initialization();

        return view;

    }

    // ----------------- Configuring Observers -----------------
    private void getListFromPlace() {
        mRestaurantViewModel.getMyPlace().observe(this, new Observer<MyPlace>() {
            @Override
            public void onChanged(MyPlace myPlace) {

                mRestaurants = new ArrayList<>();
                List<Result> results = myPlace.getResults();
                int size = results.size();
                for (int i = 0; i < size; i++) {
                    String placeId = results.get(i).getPlaceId();
                    String name = results.get(i).getName();
                    String address = results.get(i).getVicinity();
                    String urlPhoto = results.get(i).getPhotos().get(0).getPhotoReference();
                    double rating = results.get(i).getRating();
                    Boolean openNow = (results.get(i).getOpeningHours() != null ? results.get(i).getOpeningHours().getOpenNow() : false);
                    if (results.get(i).getGeometry().getLocation() != null) {
                        Location location = results.get(i).getGeometry().getLocation();
                        Restaurant restaurant = new Restaurant(placeId, name, address, urlPhoto, openNow, location, rating);
                        mRestaurants.add(restaurant);
                        mAdapter.setRestaurants(mRestaurants);
                        getListWithWorkmate();


                    }


                }
            }

        });


    }

    private void getListWithWorkmate() {
        mRestaurantViewModel.getRestaurantList().observe(this, new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> restaurants) {
                int size = restaurants.size();
                for (int i = 0; i < size; i++) {
                    Restaurant restaurant = restaurants.get(i);
                    if (restaurant.getWorkmatesList().size() > 0) {
                        if (mRestaurants.contains(restaurant)) {
                            int in = mRestaurants.indexOf(restaurant);
                            mRestaurants.get(in).setWorkmatesList(restaurant.getWorkmatesList());
                            mAdapter.setRestaurants(mRestaurants);

                        }


                    }


                }
            }

        });
    }


    private void initialization() {
        mRecyclerViewRestaurant.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerViewRestaurant.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerViewRestaurant.addItemDecoration(dividerItemDecoration);

        mAdapter = new RestaurantAdapter(this.getActivity(), this);
        mRecyclerViewRestaurant.setAdapter(mAdapter);

    }

    // ----------------- Configuring ViewModel -----------------
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mRestaurantViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RestaurantViewModel.class);
        String lat = App.getInstance().getLat();
        String lng = App.getInstance().getLng();
        mRestaurantViewModel.setPlace(Constants.TYPE, lat + " " + lng, Constants.RADIUS);


    }

    @Override
    public void onNoteClick(int position) {
        Context context = getActivity();
        Intent intent = new Intent(context, DetailsRestaurantActivity.class);
        Gson gson = new Gson();
        String jsonSelectedRestaurant = gson.toJson(mAdapter.getSelectedRestaurant(position));
        intent.putExtra(DetailsRestaurantActivity.EXTRA_RESTAURANT, jsonSelectedRestaurant);
        startActivity((intent));


    }


}
