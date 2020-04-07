package com.e.go4lunch.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.go4lunch.R;
import com.e.go4lunch.adapter.WorkmatesAdapter;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.viewmodels.ListWorkmateViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListFragment extends Fragment {

    private ListWorkmateViewModel mListWorkmateFragmentViewModel;
    private WorkmatesAdapter mWorkmatesAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this,view);
        mListWorkmateFragmentViewModel = ViewModelProviders.of(this).get(ListWorkmateViewModel.class);

        mListWorkmateFragmentViewModel.init();
        mListWorkmateFragmentViewModel.getWorkmates().observe(this, new Observer<List<Workmates>>() {
            @Override
            public void onChanged(List<Workmates> workmates) {
                mWorkmatesAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mWorkmatesAdapter = new WorkmatesAdapter(this.getActivity(),mListWorkmateFragmentViewModel.getWorkmates().getValue());
        mRecyclerView.setAdapter(mWorkmatesAdapter);
        return view;

    }

}
