package com.e.go4lunch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.e.go4lunch.models.PlaceDetail;
import com.e.go4lunch.remote.IGoogleAPIService;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.iv_photo_restaurant_detail_activity)
    ImageView mImageViewPhoto;
    @BindView(R.id.tv_name_restaurant_detail_activity)
    TextView mTextViewNameRestaurant;
    @BindView(R.id.tv_adress_restaurant_detail_activity)
    TextView mTextViewAdress;

   IGoogleAPIService mService;

   PlaceDetail mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        mService = Common.getGoogleAPIService();

        if (Common.currentResult.getPhotos() != null && Common.currentResult.getPhotos().length > 0) {
            Glide.with(this)
                    .load(getPhotoOfPlace(Common.currentResult.getPhotos()[0].getPhoto_reference(), 1000))
                    .into(mImageViewPhoto);
        }
        mService.getDetailRestaurant(getPlaceDetailUrl(Common.currentResult.getPlace_id()))
                .enqueue(new Callback<PlaceDetail>() {
                    @Override
                    public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                       mPlace = response.body();

                       mTextViewNameRestaurant.setText(mPlace.getResult().getName());
                       mTextViewAdress.setText(mPlace.getResult().getFormatted_address());
                    }

                    @Override
                    public void onFailure(Call<PlaceDetail> call, Throwable t) {

                    }
                });

    }

    private String getPlaceDetailUrl(String place_id) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        url.append("?place_id=" + place_id);
        url.append("&key="+getResources().getString(R.string.browser_key));
        return url.toString();

    }

    private String getPhotoOfPlace(String photo_reference, int maxWidth) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        url.append("?maxwidth=" + maxWidth);
        url.append("&photoreference=" + photo_reference);
        url.append("&key=" + getResources().getString(R.string.browser_key));
        return url.toString();
    }
}
