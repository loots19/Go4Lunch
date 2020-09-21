package com.e.go4lunch.workmates;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.go4lunch.R;
import com.e.go4lunch.repositories.injection.Injection;
import com.e.go4lunch.repositories.injection.ViewModelFactory;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.restaurant.DetailsRestaurantActivity;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WorkmateFragment extends Fragment implements WorkmatesAdapter.OnNoteListener {


    // ----------------- FOR DESIGN -----------------
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    // ----------------- FOR DATA -----------------
    private WorkmateViewModel mWorkmateViewModel;
    private WorkmatesAdapter mWorkmatesAdapter;
    private List<Workmates> mWorkmatesList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        configureRecyclerView();
        configureViewModel();
        getWorkmateList();

        return view;

    }

    // ---------------------
    // Configuring ViewModel
    // ---------------------
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mWorkmateViewModel = new ViewModelProvider(this, mViewModelFactory).get(WorkmateViewModel.class);

    }


    // ---------------------
    // Configuring Observers
    // ---------------------
    private void getWorkmateList() {
        mWorkmateViewModel.getAllWorkmates().observe(getViewLifecycleOwner(), workmates -> {
            mWorkmatesList = workmates;
            mWorkmatesAdapter.setWorkmates(workmates);
        });
    }
    // ------------------------
    // Configuring RecyclerView
    // ------------------------
    private void configureRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mWorkmatesAdapter = new WorkmatesAdapter( this);
        mRecyclerView.setAdapter(mWorkmatesAdapter);

    }
    // -----------------------------------------------------
    // OpenDetail Activity when workmate click on item of RV
    // -----------------------------------------------------
    @Override
    public void onNoteClick(int position) {
        if (mWorkmatesList.get(position).getRestaurantChosen() != null) {
            Intent intent = new Intent(getContext(), DetailsRestaurantActivity.class);
            Gson gson = new Gson();
            String jsonSelectedRestaurant = gson.toJson(mWorkmatesList.get(position).getRestaurantChosen());
            intent.putExtra(DetailsRestaurantActivity.EXTRA_RESTAURANT, jsonSelectedRestaurant);
            startActivity((intent));
        }
    }
}
