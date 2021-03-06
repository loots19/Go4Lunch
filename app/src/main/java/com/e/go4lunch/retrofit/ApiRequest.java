package com.e.go4lunch.retrofit;

import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.placeDetail.PlaceDetail;
import com.e.go4lunch.util.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRequest {


    @GET("api/place/nearbysearch/json?key=" + Constants.API_KEY)
    Call<MyPlace> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

    @GET("api/place/details/json?key=" + Constants.API_KEY)
    Call<PlaceDetail> getDetailsRestaurant(@Query("place_id") String placeId);


}
