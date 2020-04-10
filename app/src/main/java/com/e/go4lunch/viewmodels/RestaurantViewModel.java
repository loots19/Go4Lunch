package com.e.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.myPlace.Result;
import com.e.go4lunch.repositories.RestaurantRepository;

import java.util.List;

public class RestaurantViewModel extends ViewModel {

    private MutableLiveData<MyPlace> mMutableLiveData;
    private RestaurantRepository mRestaurantRepository;

    public void init() {
        if (mMutableLiveData != null) {
            return;
        }
        mRestaurantRepository = RestaurantRepository.getInstance();
        mMutableLiveData = mRestaurantRepository.getNearbyPlace("restaurant", "49.044238,2.304685", 10000);
    }

    public LiveData<MyPlace> getRestaurantRepository() {
        return mMutableLiveData;
    }

}









