package com.e.go4lunch.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.e.go4lunch.Retrofit.RestaurantApiClient;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.myPlace.Result;

import java.util.List;

public class RestaurantRepository {

    private static  RestaurantRepository instance;
    private RestaurantApiClient mRestaurantsApiClient;

    public static RestaurantRepository getInstance(){
        if (instance == null){
            instance = new RestaurantRepository();
        }
        return instance;
    }
    private RestaurantRepository(){
        mRestaurantsApiClient = RestaurantApiClient.getInstance();

    }
    public LiveData<List<Result>>getResults(){
        return mRestaurantsApiClient.getResults();

    }
    public void searchRestaurantApi(String type,String location,int radius){
        mRestaurantsApiClient.searchRestaurantApi(type, location, radius);
    }





}