package com.e.go4lunch.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.go4lunch.R;
import com.e.go4lunch.models.myPlace.Location;
import com.e.go4lunch.models.myPlace.Result;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantHolder> {

    private Context mContext;
    private OnNoteListener mOnNoteListener;
    private List<Result> mResults;


    public RestaurantAdapter(Context context, OnNoteListener onNoteListener) {
        this.mContext = context;
        this.mOnNoteListener = onNoteListener;


    }

    @NonNull
    @Override
    public RestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_restaurant, parent, false);
        return new RestaurantHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantHolder holder, int position) {
        holder.update(this.mResults.get(position));

    }

    @Override
    public int getItemCount() {
        if (mResults != null) {
            return mResults.size();
        }
        return 0;
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

    public void setRestaurants(List<Result> results) {
        mResults = results;
        notifyDataSetChanged();
    }

    public Result getSelectedRestaurant(int position) {
        if (mResults != null) {
            if (mResults.size() > 0) {
                return mResults.get(position);
            }
        }
        return null;
    }


}
