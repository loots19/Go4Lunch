package com.e.go4lunch.remote;

import com.e.go4lunch.models.MyPlace;
import com.e.go4lunch.models.PlaceDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleAPIService {
   @GET
    Call<MyPlace> getNearbyByRestaurant(@Url String url);

   @GET
    Call<PlaceDetail> getDetailRestaurant(@Url String url);
}

