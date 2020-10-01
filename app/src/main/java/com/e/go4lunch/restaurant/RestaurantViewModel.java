package com.e.go4lunch.restaurant;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.placeDetail.Location;
import com.e.go4lunch.models.placeDetail.OpeningHours;
import com.e.go4lunch.models.placeDetail.Period;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.e.go4lunch.util.AbsentLiveData;
import com.e.go4lunch.util.Event;

import java.util.List;
import java.util.Objects;

public class RestaurantViewModel extends ViewModel {
    private RestaurantRepository mRestaurantRepository;
    private MutableLiveData<GetPlace> getPlace = new MutableLiveData<>();
    private LiveData<List<Restaurant>> restaurant;
    private final MutableLiveData<Event<Object>> openDetailRestaurant = new MutableLiveData<>();

    // -----------------------------
    // --- GET PLACE FROM NEARBY ---
    // -----------------------------
    public RestaurantViewModel(RestaurantRepository restaurantRepository) {
        this.mRestaurantRepository = restaurantRepository;
        restaurant = Transformations.switchMap(getPlace, input -> {
            if (input == null) {
                return AbsentLiveData.create();
            }
            return mRestaurantRepository.getRestaurantList(input.type, input.location, input.radius);
        });

    }


    public void setPlace(String type, String location, int radius) {
        Log.e("testLocation", location + radius);
        GetPlace update = new GetPlace(type, location, radius);
        if (Objects.equals(getPlace.getValue(), update)) {
            return;
        }
        getPlace.setValue(update);
    }

    static class GetPlace {
        final String type, location;
        final int radius;

        GetPlace(String type, String location, int radius) {
            this.type = type == null ? null : type.trim();
            this.location = location == null ? null : location.trim();
            this.radius = radius;
        }


    }

    public void showUserRestaurant(String placeId) {
        mRestaurantRepository.setRestaurantSelected(placeId);
        openDetailRestaurant.setValue(new Event<>(new Object()));

    }

    // -----------
    // --- GET ---
    // -----------
    public LiveData<Restaurant> getRestaurantAuto(String placeId){
        return mRestaurantRepository.getRestaurantAuto(placeId);
    }

    public LiveData<Event<Restaurant>> getRestaurant(String placeId) {
        return mRestaurantRepository.getRestaurant(placeId);
    }

    public LiveData<List<Restaurant>> getRestaurantList() {
        return restaurant;

    }
    public LiveData<Event<Object>> getOpenDetailRestaurant() {
        return openDetailRestaurant;
    }

    // --------------
    // --- UPDATE ---
    // --------------
    public void updateRestaurantWorkmateList(String uid, List<Workmates> workmatesList) {
        this.mRestaurantRepository.updateRestaurantWorkmateList(uid, workmatesList);
    }


}











