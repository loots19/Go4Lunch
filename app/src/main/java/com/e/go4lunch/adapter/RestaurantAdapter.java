package com.e.go4lunch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.go4lunch.R;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantHolder> {

    public RestaurantAdapter(Context context) {
        mContext = context;
    }

    Context mContext;

    @NonNull
    @Override
    public RestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_restaurant,parent,false);
        return new RestaurantHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
