package com.e.go4lunch.workmates;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.go4lunch.R;
import com.e.go4lunch.injection.Injection;
import com.e.go4lunch.injection.ViewModelFactory;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.restaurant.DetailsRestaurantActivity;
import com.e.go4lunch.restaurant.RestaurantViewModel;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListFragment extends Fragment implements WorkmatesAdapter.OnNoteListener {

    private WorkmateViewModel mWorkmateViewModel;
    private WorkmatesAdapter mWorkmatesAdapter;
    private List<Workmates> mWorkmatesList;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        configureRecyclerView();
        configureViewModel();
        getWorkmateList();

        return view;

    }

    // ----------------- Configuring ViewModel -----------------

    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mWorkmateViewModel = ViewModelProviders.of(this, mViewModelFactory).get(WorkmateViewModel.class);

    }


    // ----------------- Configuring Observers -----------------

    private void getWorkmateList() {
        mWorkmateViewModel.getWorkmatesList().observe(this, workmates -> {
            mWorkmatesList = workmates;
            mWorkmatesAdapter.setWorkmates(workmates);
        });
    }

    // ----------------- Configuring RecyclerView -----------------

    private void configureRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mWorkmatesAdapter = new WorkmatesAdapter(getContext(), this);
        mRecyclerView.setAdapter(mWorkmatesAdapter);

    }


    @Override
    public void onNoteClick(int position) {
        if (mWorkmatesList.get(position).getRestaurantChoosen() != null) {
            Intent intent = new Intent(getContext(), DetailsRestaurantActivity.class);
            Gson gson = new Gson();
            String jsonSelectedRestaurant = gson.toJson(mWorkmatesList.get(position).getRestaurantChoosen());
            intent.putExtra(DetailsRestaurantActivity.EXTRA_RESTAURANT, jsonSelectedRestaurant);
            startActivity((intent));
        }
    }
}
