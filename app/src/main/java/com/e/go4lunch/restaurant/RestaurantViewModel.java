package com.e.go4lunch.restaurant;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.e.go4lunch.util.AbsentLiveData;

public class RestaurantViewModel extends ViewModel {

    private MutableLiveData<MyPlace> mMutableLiveData;
    private RestaurantRepository mRestaurantRepository;
    private LiveData<String> place_id;


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






