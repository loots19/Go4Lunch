package com.e.go4lunch.workmates;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListFragment extends Fragment {

    private WorkmateViewModel mListWorkmateFragmentViewModel;
    private WorkmatesAdapter mWorkmatesAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this,view);
        mListWorkmateFragmentViewModel = ViewModelProviders.of(this).get(WorkmateViewModel.class);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);


        mRecyclerView.setAdapter(mWorkmatesAdapter);
        return view;

    }

}
