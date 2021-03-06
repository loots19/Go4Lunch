package com.e.go4lunch.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.go4lunch.R;
import com.e.go4lunch.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantHolder> {

    private Context mContext;
    private OnItemListener mOnItemListener;
    private List<Restaurant> mRestaurants = new ArrayList<>();


    RestaurantAdapter(Context context, OnItemListener onItemListener) {
        this.mContext = context;
        this.mOnItemListener = onItemListener;

    }


    @NonNull
    @Override
    public RestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_restaurant, parent, false);

        return new RestaurantHolder(view, mOnItemListener, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantHolder holder, int position) {
        holder.update(this.mRestaurants.get(position));

    }

    @Override
    public int getItemCount() {
        if (mRestaurants != null) {
            return mRestaurants.size();
        }
        return 0;
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }

    void setRestaurants(List<Restaurant> restaurant) {
        mRestaurants = restaurant;
        notifyDataSetChanged();
    }

    Restaurant getSelectedRestaurant(int position) {
        if (mRestaurants != null) {
            if (mRestaurants.size() > 0) {
                return mRestaurants.get(position);
            }
        }
        return null;
    }


}
