package com.e.go4lunch.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.go4lunch.R;
import com.e.go4lunch.models.Workmates;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailAdapter extends RecyclerView.Adapter<RestaurantDetailHolder> {

    private Context mContext;
    private List<Workmates> mWorkmates;

    public RestaurantDetailAdapter( Context context) {
        mContext = context;
        this.mWorkmates = new ArrayList<>();
    }

    @NonNull
    @Override
    public RestaurantDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmates_fragment, parent, false);
        return new RestaurantDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantDetailHolder holder, int position) {
        holder.update(this.mWorkmates.get(position));
    }
    public void setWorkmates(List<Workmates> workmatesList) {
        mWorkmates = workmatesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mWorkmates != null) {
            return mWorkmates.size();
        }
        return 0;
    }
}
