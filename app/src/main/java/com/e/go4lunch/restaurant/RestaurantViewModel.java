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
    private WorkmatesRepository mWorkmatesRepository;
    private MutableLiveData<GetPlace> getPlace = new MutableLiveData<>();
    private LiveData<MyPlace> myPlace;
    private final MutableLiveData<Event<Object>> openDetailRestaurant = new MutableLiveData<>();

    // -----------------------------
    // --- GET PLACE FROM NEARBY ---
    // -----------------------------
    public RestaurantViewModel(RestaurantRepository restaurantRepository, WorkmatesRepository workmatesRepository) {
        this.mRestaurantRepository = restaurantRepository;
        this.mWorkmatesRepository = workmatesRepository;
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

    public LiveData<Event<Object>> getOpenDetailRestaurant() {
        return openDetailRestaurant;
    }


    public void createRestaurant(String placeId, String name, String address, String urlPhoto, List<String> openHours,boolean openNow, Location location, double rating, String webSite, String phoneNumbers, List<Workmates> workmatesList) {
        this.mRestaurantRepository.createRestaurant(placeId, name, address, urlPhoto, openHours,openNow, location, rating,webSite,phoneNumbers, workmatesList);
    }



    // -----------
    // --- GET ---
    // -----------
    public LiveData<Event<Restaurant>> getRestaurant(String placeId) {
        return mRestaurantRepository.getRestaurant(placeId);
    }

    public LiveData<List<Restaurant>> getRestaurantList() {
        return mRestaurantRepository.getRestaurantList();

    }

    // --------------
    // --- UPDATE ---
    // --------------
    public void updateRestaurantWorkmateList(String uid, List<Workmates> workmatesList) {
        this.mRestaurantRepository.updateRestaurantWorkmateList(uid, workmatesList);
    }


}











