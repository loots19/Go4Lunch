package com.e.go4lunch.restaurant;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.placeDetail.PlaceDetail;
import com.e.go4lunch.repositories.RestaurantRepository;



import static android.content.Intent.getIntent;
import static com.e.go4lunch.restaurant.DetailsRestaurantActivity.EXTRA_MARKER;

public class RestaurantDetailViewModel extends ViewModel {

    private RestaurantRepository mRestaurantRepository;
    private MutableLiveData<String> nameRestaurant = new MutableLiveData<>();
    private MutableLiveData<String> adressRestaurant = new MutableLiveData<>();
    private MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private MutableLiveData<String> webSite = new MutableLiveData<>();
    private MutableLiveData<String> urlPhoto = new MutableLiveData<>();
    private MutableLiveData<PlaceDetail> mMutableLiveData;
    private MutableLiveData<String> place_id;


    public void init() {
        if (mMutableLiveData != null) {
            return;
        }
        mRestaurantRepository = RestaurantRepository.getInstance();
        mMutableLiveData = mRestaurantRepository.getRestaurantDetail("ChIJxwCVpMlC5kcRYDWLaMOCCwQ");
    }

    public LiveData<PlaceDetail> getRestaurantRepository() {
        return mMutableLiveData;
    }
}






