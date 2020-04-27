package com.e.go4lunch.restaurant;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.e.go4lunch.R;
import com.e.go4lunch.models.myPlace.Location;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.myPlace.Result;
import com.e.go4lunch.restaurant.RestaurantAdapter;
import com.e.go4lunch.util.Constants;
import com.e.go4lunch.util.DistanceCalcul;
import com.e.go4lunch.util.Geometry;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.android.SphericalUtil;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.e.go4lunch.restaurant.DetailsRestaurantActivity.EXTRA_MARKER;

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
    private List<Result> mResults;
    private Result mResult;
    private LatLng currentPlace;
    private static final int REQUEST_CALL = 10;
    PlacesClient placesClient;




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

    public void update(Result result) {

        //---------- Opening ----------
        if (result.getOpeningHours() != null) {
            if (result.getOpeningHours().getOpenNow()) {
                if (result.getOpeningHours().getOpenNow())
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
        //---------- Name ----------
        this.mTvName.setText(result.getName());
        //---------- Address ----------
        this.mTvAdress.setText(result.getVicinity());

        //---------- Photo ----------
        if (result.getPhotos() != null) {
            Glide.with(itemView)
                    .load(Constants.BASE_URL_PHOTO
                            + result.getPhotos().get(0).getPhotoReference()
                            + "&key=" + Constants.API_KEY)
                    .apply(RequestOptions.circleCropTransform())
                    .into(mImageRestaurant);
        }
        //---------- Distance ----------
        this.calculateDistance(result);
        //this.display_distance_to_restaurant(result);

    }



    private void display_distance_to_restaurant(Result result) {

        if (currentPlace != null &&  result.getGeometry() != null) {

            if (result.getGeometry().getLocation() != null) {

                DistanceCalcul tool_distance_calcul = new DistanceCalcul();

                String text = tool_distance_calcul.calulate_distance(currentPlace.latitude, currentPlace.longitude,
                        result.getGeometry().getLocation().getLat(),
                        result.getGeometry().getLocation().getLng());

                this.mTvMetters.setText(text);
            }
        }
    }

    // Method that calculate the distance between user location and restaurant location
    private void calculateDistance(Result result){
        LatLng destinationLocation = new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());
        double doubleDistanceToRestaurant = SphericalUtil.computeDistanceBetween(Geometry.getInstance().getLocation(), destinationLocation);
        int intDistanceToRestaurant = (int)Math.round(doubleDistanceToRestaurant)/1000;
        String stringDistanceToRestaurant = "" + intDistanceToRestaurant + "m";
        this.mTvMetters.setText(stringDistanceToRestaurant);
    }



    }












