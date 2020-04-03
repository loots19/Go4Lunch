package com.e.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.myPlace.Result;
import com.e.go4lunch.repositories.RestaurantRepository;

import java.util.List;

public class RestaurantViewModel extends ViewModel {

    private RestaurantRepository mRestaurantRepository;

    public RestaurantViewModel() {
        mRestaurantRepository = RestaurantRepository.getInstance();

    }

    public LiveData<List<Result>> getResults(){
        return mRestaurantRepository.getResults();
    }
    public void searchRestaurantApi(String type,String location,int radius){
        mRestaurantRepository.searchRestaurantApi(type,location,radius);
    }
}



