package com.e.go4lunch.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.e.go4lunch.R;
import com.e.go4lunch.models.myPlace.Result;
import com.e.go4lunch.models.placeDetail.Location;
import com.e.go4lunch.models.placeDetail.PlaceDetail;
import com.e.go4lunch.models.placeDetail.ResultDetail;
import com.e.go4lunch.util.Constants;
import com.e.go4lunch.util.Geometry;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.e.go4lunch.util.Constants.API_KEY;
import static com.e.go4lunch.util.Constants.BASE_URL_PHOTO;
import static com.e.go4lunch.util.Constants.MAX_HEIGHT;
import static com.e.go4lunch.util.Constants.MAX_WIDTH;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

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
    public void update(Result result){

        //---------- Opening ----------
        if(result.getOpeningHours() != null){
            if (result.getOpeningHours().getOpenNow()){
                if(result.getOpeningHours().getOpenNow())
                mTvTime.setText(R.string.Open);
                mTvTime.setTextColor(ContextCompat.getColor(itemView.getContext(),R.color.quantum_lightblue));
            } else {
                mTvTime.setText(R.string.Close);
                mTvTime.setTextColor(ContextCompat.getColor(itemView.getContext(),R.color.colorAccent));
            }

        } else {
            mTvTime.setText(itemView.getContext().getString(R.string.opening_hour_not_available));
            mTvTime.setTextColor(ContextCompat.getColor(itemView.getContext(),R.color.quantum_black_100));
        }
        //---------- Name ----------
        this.mTvName.setText(result.getName());
        //---------- Address ----------
        this.mTvAdress.setText(result.getVicinity());

        //---------- Photo ----------
        if(result.getPhotos() != null){
            Glide.with(itemView)
                    .load( Constants.BASE_URL_PHOTO
                            +result.getPhotos().get(0).getPhotoReference()
                            +"&key="+ Constants.API_KEY)
                    .apply(RequestOptions.circleCropTransform())
                    .into(mImageRestaurant);
        }
        //---------- Distance ----------

    }

    }









