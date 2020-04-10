package com.e.go4lunch.Retrofit;

import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.placeDetail.PlaceDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRequest {


    @GET("api/place/nearbysearch/json?location=49.044238,2.304685&radius=10000&types=restaurant&key=AIzaSyCbD-Ektsu_fCIS7YIU0G5BWic30ZXpDiA")
    Call<MyPlace>getNearbyPlaces(@Query("restaurant") String type, @Query("location") String location, @Query("radius") int radius);

    @GET("maps/api/place/details/json?key=AIzaSyCbD-Ektsu_fCIS7YIU0G5BWic30ZXpDiA")
    Call<PlaceDetail> getDetailsRestaurant(@Query("placeid") String placeId);




}
