package com.e.go4lunch.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.go4lunch.R;
import com.e.go4lunch.models.Restaurant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.tv_name_restaurant_item)
    TextView mTvName;
    @BindView(R.id.tv_adress_restaurant_item)
    TextView mTvAdress;
    @BindView(R.id.tv_time_restaurant_item)
    TextView mTvTime;
    @BindView(R.id.tv_metters_restaurant_item)
    TextView mTvMetters;
    @BindView(R.id.tv_numbers_restaurant_item)
    TextView mTvNumbers;
    @BindView(R.id.iv_stars_restaurant_item)
    ImageView mIvStars;

    RestaurantAdapter.OnNoteListener OnNoteListener;


    public RestaurantHolder(@NonNull View itemView, RestaurantAdapter.OnNoteListener onNoteListener) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.OnNoteListener = onNoteListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        OnNoteListener.onNoteClick(getAdapterPosition());

    }
}
