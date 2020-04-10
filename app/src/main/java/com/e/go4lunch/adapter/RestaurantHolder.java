package com.e.go4lunch.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.e.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.e.go4lunch.util.Constants.API_KEY;
import static com.e.go4lunch.util.Constants.BASE_URL_PHOTO;
import static com.e.go4lunch.util.Constants.MAX_HEIGHT;
import static com.e.go4lunch.util.Constants.MAX_WIDTH;

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
    @BindView(R.id.iv_photo_restaurant_item)
    ImageView mImageRestaurant;

    RestaurantAdapter.OnNoteListener OnNoteListener;
    Context mContext;


    public RestaurantHolder(@NonNull View itemView, RestaurantAdapter.OnNoteListener onNoteListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.OnNoteListener = onNoteListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        OnNoteListener.onNoteClick(getAdapterPosition());

    }
    public void updateData(ResultsDetail resultsDetail){
        if (!(resultsDetail.getPhotos() == null)) {
            if (!(resultsDetail.getPhotos().isEmpty())) {
                Glide.with(mContext)
                        .load(BASE_URL_PHOTO + "?maxwidth=" + MAX_WIDTH + "&maxheight=" + MAX_HEIGHT + "&photoreference=" + resultsDetail
                                .getPhotos().get(0).getPhotoReference() + "&key=" + API_KEY).apply(new RequestOptions())
                        .into(mImageRestaurant);

            }
        }
    }



        }

