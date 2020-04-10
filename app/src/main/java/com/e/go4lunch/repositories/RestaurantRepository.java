package com.e.go4lunch.repositories;

import androidx.lifecycle.MutableLiveData;

import com.e.go4lunch.Retrofit.ApiRequest;
import com.e.go4lunch.Retrofit.RetrofitRequest;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.placeDetail.PlaceDetail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {

    private static RestaurantRepository instance;


    public static RestaurantRepository getInstance() {
        if (instance == null) {
            instance = new RestaurantRepository();
        }
        return instance;
    }

    private ApiRequest mApiRequest;

    public RestaurantRepository() {
        mApiRequest = RetrofitRequest.getApiRequest();
    }

    public MutableLiveData<MyPlace> getNearbyPlace(String type, String location, int radius) {
        MutableLiveData<MyPlace> newData = new MutableLiveData<>();
        mApiRequest.getNearbyPlaces(type, location, radius).enqueue(new Callback<MyPlace>() {
            @Override
            public void onResponse(Call<MyPlace> call, Response<MyPlace> response) {
                if (response.isSuccessful()) {
                    newData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MyPlace> call, Throwable t) {

                newData.setValue(null);

            }
        });
        return newData;
    }
    public MutableLiveData<PlaceDetail> getRestaurantDetail(String placeId){
        MutableLiveData<PlaceDetail> newData = new MutableLiveData<>();
        mApiRequest.getDetailsRestaurant(placeId).enqueue(new Callback<PlaceDetail>() {
            @Override
            public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                if (response.isSuccessful()) {
                    newData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<PlaceDetail> call, Throwable t) {

                newData.setValue(null);

            }
        });
        return newData;
    }


}







