package com.e.go4lunch.workmates;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.go4lunch.R;
import com.e.go4lunch.injection.Injection;
import com.e.go4lunch.injection.ViewModelFactory;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.restaurant.DetailsRestaurantActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListFragment extends Fragment implements WorkmatesAdapter.OnNoteListener {

    private WorkmateViewModel mWorkmateViewModel;
    private WorkmatesAdapter mWorkmatesAdapter;
    private Context mContext;
    private static List<Workmates> mWorkmatesList = new ArrayList<>();
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this,view);

        configureRecyclerView();
        configureViewModel();
        subscribeObservers();

        return view;

    }
    // Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(mContext);
        this.mWorkmateViewModel = ViewModelProviders.of(this,mViewModelFactory).get(WorkmateViewModel.class);
        subscribeObservers();
    }
    // Configuring RecyclerView
    private void configureRecyclerView(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mWorkmatesAdapter);

    }
    private void subscribeObservers(){
        mWorkmateViewModel.getWorkmatesList().observe(this, new Observer<List<Workmates>>() {
            @Override
            public void onChanged(List<Workmates> workmates) {
                mWorkmatesList = new ArrayList<>();
                int size = mWorkmatesList.size();
                for (int i = 0; i < size; i++){
                    String name = mWorkmatesList.get(i).getWorkmateName();
                    String email = mWorkmatesList.get(i).getWorkmateEmail();
                    String url = mWorkmatesList.get(i).getUrlPicture();
                    Workmates workmates1 = new Workmates(name,email,url);
                    Log.e("newnew", name);

                    mWorkmatesList.add(workmates1);
                    mWorkmatesAdapter.setWorkmates(mWorkmatesList);
                }


            }
        });
    }

    @Override
    public void onNoteClick(int position) {
    }
}
