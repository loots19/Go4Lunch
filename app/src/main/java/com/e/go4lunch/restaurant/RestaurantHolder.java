package com.e.go4lunch.restaurant;

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
import com.e.go4lunch.injection.App;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;


public class RestaurantHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // ----------------- FOR DESIGN -----------------
    @BindView(R.id.tv_name_restaurant_item)
    TextView mTvName;
    @BindView(R.id.tv_address_restaurant_item)
    TextView mTvAddress;
    @BindView(R.id.tv_time_restaurant_item)
    TextView mTvTime;
    @BindView(R.id.tv_meters_restaurant_item)
    TextView mTvMeters;
    @BindView(R.id.tv_numbers_restaurant_item)
    TextView mTvNumbers;
    @BindView(R.id.iv_RatingBar_item)
    RatingBar mRatingBar;
    @BindView(R.id.iv_photo_restaurant_item)
    ImageView mImageRestaurant;
    @BindView(R.id.imageView2)
    ImageView mImageViewWorkmates;


    private RestaurantAdapter.OnNoteListener OnNoteListener;



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
        this.mTvAddress.setText(restaurant.getAddress());

        //---------- Photo ----------
        displayPhotoOfRestaurant(restaurant);

        //---------- Rating ----------
        displayRating(restaurant);

        //---------- Distance ----------
        displayDistance(restaurant);

        //---------- NumbersWorkmates ----------
        displayWorkmatesNumbers(restaurant);



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
    private void displayDistance(Restaurant restaurant) {
        App globals = (App) getApplicationContext();
        String lat = globals.getLat();
        String lng = globals.getLng();
        Location currentLocation = new Location("locationA");
        currentLocation.setLatitude(Double.parseDouble(lat));
        currentLocation.setLongitude(Double.parseDouble(lng));
        Location destination = new Location("locationB");
        destination.setLatitude(restaurant.getLocation().getLat());
        destination.setLongitude(restaurant.getLocation().getLng());
        double distance = currentLocation.distanceTo(destination);
        double distanceF = distance / 1000;
        String rounded = String.format("%.0f", distanceF);
        mTvMeters.setText(rounded + " KMS");

    }

    // set opening hours of the place
    private void displayOpeningHours(Restaurant restaurant) {
        if (restaurant.getOpenNow() != null) {
            if (restaurant.getOpenNow()) {
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
    private void displayPhotoOfRestaurant(Restaurant restaurant) {
        if (restaurant.getUrlPhoto() != null) {
            Glide.with(itemView)
                    .load(Constants.BASE_URL_PHOTO
                            + restaurant.getUrlPhoto()
                            + "&key=" + Constants.API_KEY)
                    .apply(RequestOptions.circleCropTransform())
                    .into(mImageRestaurant);
        }
    }

    private void displayWorkmatesNumbers(Restaurant restaurant) {
        if (restaurant.getWorkmatesList() != null && restaurant.getWorkmatesList().size()>0) {
            int numberWorkmates = restaurant.getWorkmatesList().size();
            mTvNumbers.setText(String.valueOf(numberWorkmates));
            mImageViewWorkmates.setVisibility(View.VISIBLE);
        }else{
            mTvNumbers.setText("");
            mImageViewWorkmates.setVisibility(View.INVISIBLE);

        }

    }

}













