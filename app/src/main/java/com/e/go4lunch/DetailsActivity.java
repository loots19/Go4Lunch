package com.e.go4lunch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.provider.Contacts;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.e.go4lunch.models.myPlace.Result;
import com.e.go4lunch.models.placedetail.Photo;
import com.e.go4lunch.util.Constants;
import com.e.go4lunch.viewmodels.RestaurantViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.tv_name_restaurant_detail_activity)
    TextView mTvNameRestaurant;
    @BindView(R.id.tv_adress_restaurant_detail_activity)
    TextView mTvAdressResaurant;

    private RestaurantViewModel mRestaurantViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        mRestaurantViewModel = ViewModelProviders.of(this).get(RestaurantViewModel.class);
        




    }


}