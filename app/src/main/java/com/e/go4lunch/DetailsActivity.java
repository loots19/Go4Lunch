package com.e.go4lunch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.icu.text.Transliterator;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.myPlace.Result;
import com.e.go4lunch.util.Constants;
import com.e.go4lunch.viewmodels.RestaurantViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.tv_name_restaurant_detail_activity)
    TextView mTvNameRestaurant;
    @BindView(R.id.tv_adress_restaurant_detail_activity)
    TextView mTvAddressRestaurant;
    @BindView(R.id.iv_photo_restaurant_detail_activity)
    ImageView mIvPhotoRestaurant;
    public static final String EXTRA_RESTAURANT = "restaurant";
    private RestaurantViewModel mRestaurantViewModel;
    private ArrayList<Result> mResultArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        getIncomingIntent();
       // mRestaurantViewModel = ViewModelProviders.of(this).get(RestaurantViewModel.class);
        //mRestaurantViewModel.init();
        //subscribeObservers();

    }

    private void subscribeObservers() {
        mRestaurantViewModel.getRestaurantRepository().observe(this, new Observer<MyPlace>() {
            @Override
            public void onChanged(MyPlace myPlace) {
                List<Result> results = myPlace.getResults();
                for (int i = 0; i < results.size(); i++) {
                    mTvAddressRestaurant.setText(results.get(i).getVicinity());
                    mTvNameRestaurant.setText(results.get(i).getName());

                }
            }

        });
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra(EXTRA_RESTAURANT)) {
            String jsonResult = getIntent().getStringExtra(EXTRA_RESTAURANT);
            Gson gson = new Gson();
            Result result = gson.fromJson(jsonResult, Result.class);
            Log.d(EXTRA_RESTAURANT,gson.toJson(result));
            mTvAddressRestaurant.setText(result.getVicinity());
            mTvNameRestaurant.setText(result.getName());
                    Glide.with(this)
                            .load( Constants.BASE_URL_PHOTO
                                    +result.getPhotos().get(0).getPhotoReference()
                                    +"&key="+ Constants.API_KEY)
                            .centerCrop()
                            .into(mIvPhotoRestaurant);

            }
        }
    }
