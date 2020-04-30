package com.e.go4lunch.workmates;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.go4lunch.R;
import com.e.go4lunch.restaurant.DetailsRestaurantActivity;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListFragment extends Fragment implements WorkmatesAdapter.OnNoteListener {


    private WorkmatesAdapter mWorkmatesAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this,view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);


        mRecyclerView.setAdapter(mWorkmatesAdapter);
        return view;

    }

    @Override
    public void onNoteClick(int position) {
    }
}
