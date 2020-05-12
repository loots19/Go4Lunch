package com.e.go4lunch.restaurant;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.e.go4lunch.R;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.myPlace.Result;
import com.e.go4lunch.util.Constants;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

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
    @BindView(R.id.iv_RatingBar_item)
    RatingBar mRatingBar;
    @BindView(R.id.iv_photo_restaurant_item)
    ImageView mImageRestaurant;


    RestaurantAdapter.OnNoteListener OnNoteListener;
    Context mContext;

    private LatLng currentPlace;
    private static final int REQUEST_CALL = 10;
    private Location lastLocation;
    private double latitude, longitude;
    private Intent mIntent;


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


    public void update(Restaurant restaurant) {
        //---------- Opening ----------
        displayOpeningHours(restaurant);

        //---------- Name ----------
        this.mTvName.setText(restaurant.getName());

        //---------- Address ----------
        this.mTvAdress.setText(restaurant.getAddress());

        //---------- Photo ----------
        displayPhotoOfRestaurant(restaurant);

        //---------- Rating ----------
        displayRating(restaurant);

        //---------- Distance ----------
        displayDistance(restaurant);

    }
    // set rating of the place
    private void displayRating(Restaurant restaurant) {
        if (restaurant.getRating() != 0) {
            double googleRating = restaurant.getRating();
            double rating = googleRating / 5 * 3;
            this.mRatingBar.setRating((float) rating);
            this.mRatingBar.setVisibility(View.VISIBLE);
        } else {
            this.mRatingBar.setVisibility(View.GONE);
        }
    }
    // set distance of the place
    public void displayDistance(Restaurant restaurant) {

        Location currentLocation = new Location("locationA");
        currentLocation.setLatitude(49.044238);
        currentLocation.setLongitude(2.304685);
        Location destination = new Location("locationB");
        destination.setLatitude(restaurant.getLocation().getLat());
        destination.setLongitude(restaurant.getLocation().getLng());
        double distance = currentLocation.distanceTo(destination);
        double distanceF = distance / 1000;
        String rounded = String.format("%.0f", distanceF);
        mTvMetters.setText(rounded + " KMS");
    }
    // set opening hours of the place
    public void displayOpeningHours (Restaurant restaurant){
        if (restaurant.getOpenNow()!= null) {
            if (restaurant.getOpenNow()) {
                if (restaurant.getOpenNow())
                    mTvTime.setText(R.string.Open);
                mTvTime.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.quantum_lightblue));
            } else {
                mTvTime.setText(R.string.Close);
                mTvTime.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorAccent));
            }

        } else {
            mTvTime.setText(itemView.getContext().getString(R.string.opening_hour_not_available));
            mTvTime.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.quantum_black_100));
        }
    }
    //set photo of the place
    public void displayPhotoOfRestaurant(Restaurant restaurant){
        if (restaurant.getUrlPhoto() != null) {
            Glide.with(itemView)
                    .load(Constants.BASE_URL_PHOTO
                            + restaurant.getUrlPhoto()
                            + "&key=" + Constants.API_KEY)
                    .apply(RequestOptions.circleCropTransform())
                    .into(mImageRestaurant);
        }
    }

}










