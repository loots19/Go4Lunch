package com.e.go4lunch.restaurant;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.util.AbsentLiveData;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantViewModel extends ViewModel {
    private RestaurantRepository mRestaurantRepository;
    private MutableLiveData<GetPlace> getPlace = new MutableLiveData<>();
    private LiveData<MyPlace> myPlace;
    private MutableLiveData<List<Restaurant>> mRestaurantList = new MutableLiveData<>();
    private MutableLiveData<Restaurant>mRestaurantMutableLiveData = new MutableLiveData<>();


    public RestaurantViewModel(RestaurantRepository restaurantRepository) {
        this.mRestaurantRepository = restaurantRepository;
        myPlace = Transformations.switchMap(getPlace, input -> {
            if (input == null) {
                return AbsentLiveData.create();
            }
            return mRestaurantRepository.getNearbyPlace(input.type, input.location, input.radius);
        });

    }

    public LiveData<MyPlace> getMyPlace() {
        return myPlace;
    }

    public void setPlace(String type, String location, int radius) {
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

    public void createRestaurant(String placeId,String name,String address,List<Workmates> workmatesList){
        this.mRestaurantRepository.createRestaurant(placeId, name, address, workmatesList);
    }

    public void updateRestaurantListFavorite (String uid, List<Restaurant> restaurantList){
        this.mRestaurantRepository.updateRestaurantListFavorites(uid, restaurantList);
    }






    }










