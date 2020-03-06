package com.e.go4lunch.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.go4lunch.R;
import com.e.go4lunch.adapter.RestaurantAdapter;
import com.e.go4lunch.adapter.WorkmatesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RestaurantsFragment extends Fragment {
    @BindView(R.id.recycler_view_restaurant)
    RecyclerView mRecyclerViewRestaurant;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurants, container, false);
        ButterKnife.bind(this,view);
        mRecyclerViewRestaurant.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerViewRestaurant.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerViewRestaurant.addItemDecoration(dividerItemDecoration);

        RestaurantAdapter adapter = new RestaurantAdapter(this.getActivity());
        mRecyclerViewRestaurant.setAdapter(adapter);
        return view;
    }

}
