package com.e.go4lunch.restaurant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.go4lunch.R;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.repositories.injection.App;
import com.e.go4lunch.repositories.injection.Injection;
import com.e.go4lunch.repositories.injection.ViewModelFactory;
import com.e.go4lunch.util.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RestaurantsFragment extends Fragment implements RestaurantAdapter.OnItemListener {

    // ----------------- FOR DESIGN -----------------
    @BindView(R.id.recycler_view_restaurant)
    RecyclerView mRecyclerViewRestaurant;

    // ----------------- FOR DATA -----------------
    private RestaurantViewModel mRestaurantViewModel;
    private RestaurantAdapter mAdapter;
    private List<Restaurant> mRestaurantList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurants, container, false);
        ButterKnife.bind(this, view);

        initialization();
        configureViewModel();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        getRestaurantList();
    }

    // ---------------------
    // Configuring Observers
    // ---------------------
    private void getRestaurantList() {
        mRestaurantViewModel.getRestaurantList().observe(getViewLifecycleOwner(), restaurants -> {
            if (restaurants != null) {
                mRestaurantList = restaurants;
                mAdapter.setRestaurants(mRestaurantList);
                mAdapter.notifyDataSetChanged();
                
            }

        });
    }


    // ------------------------------------
    // ----- Configuring RecyclerView -----
    // ------------------------------------

    private void initialization() {
        mRecyclerViewRestaurant.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerViewRestaurant.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerViewRestaurant.addItemDecoration(dividerItemDecoration);

        mAdapter = new RestaurantAdapter(this.getActivity(), this);
        mRecyclerViewRestaurant.setAdapter(mAdapter);

    }


    // ---------------------------------
    // ----- Configuring ViewModel -----
    // ---------------------------------
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mRestaurantViewModel = new ViewModelProvider(this, mViewModelFactory).get(RestaurantViewModel.class);
        String lat = App.getInstance().getLat();
        String lng = App.getInstance().getLng();
        mRestaurantViewModel.setPlace(Constants.TYPE, lat + " " + lng, Constants.RADIUS);
    }

    // ---------------------------------------------------------------
    // ----- display detail activity when the user click on item -----
    // ---------------------------------------------------------------
    @Override
    public void onItemClick(int position) {
        Context context = getActivity();
        Intent intent = new Intent(context, DetailsRestaurantActivity.class);
        Gson gson = new Gson();
        String jsonSelectedRestaurant = gson.toJson(mAdapter.getSelectedRestaurant(position));
        intent.putExtra(DetailsRestaurantActivity.EXTRA_RESTAURANT, jsonSelectedRestaurant);
        startActivity((intent));

    }


}