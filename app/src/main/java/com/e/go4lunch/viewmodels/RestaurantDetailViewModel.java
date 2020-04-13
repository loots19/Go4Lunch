package com.e.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.e.go4lunch.models.placeDetail.PlaceDetail;
import com.e.go4lunch.repositories.RestaurantRepository;

import java.util.List;

public class RestaurantDetailViewModel extends ViewModel {
    private MutableLiveData<PlaceDetail> mMutableLiveData;
    private RestaurantRepository mRestaurantRepository;

    public void initd() {
        if (mMutableLiveData != null) {
            return;
        }
        mRestaurantRepository = RestaurantRepository.getInstance();
        mMutableLiveData = mRestaurantRepository.getRestaurantDetail("73a2066f1dc63f56d20e72a95b216142d2f93fad");
    }

    public LiveData<PlaceDetail> getRestaurantRepository() {
        return mMutableLiveData;
    }
}
