package com.e.go4lunch.restaurant;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.e.go4lunch.models.placeDetail.PlaceDetail;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.util.AbsentLiveData;

public class RestaurantDetailViewModel extends ViewModel {

    private RestaurantRepository mRestaurantRepository;
    private MutableLiveData<String> place_id = new MutableLiveData<>();
    private LiveData<PlaceDetail> placeDetail;



    public RestaurantDetailViewModel(RestaurantRepository repository) {
        this.mRestaurantRepository = repository;
        placeDetail = Transformations.switchMap(place_id,input -> {
            if(input.isEmpty()){
                return AbsentLiveData.create();
            }
            return mRestaurantRepository.getRestaurantDetail(input);
        });


    }
    public void setInput(String input){
        place_id.setValue(input);
    }

    public LiveData<PlaceDetail>getPlaceDetail(){
        return placeDetail;
    }

}











