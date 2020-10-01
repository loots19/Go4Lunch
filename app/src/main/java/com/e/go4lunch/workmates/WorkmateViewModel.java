package com.e.go4lunch.workmates;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.e.go4lunch.util.Event;

import java.util.List;

public class WorkmateViewModel extends ViewModel {
    private WorkmatesRepository mWorkmatesRepository;
    private RestaurantRepository mRestaurantRepository;

    public WorkmateViewModel(RestaurantRepository restaurantRepository, WorkmatesRepository workmatesRepository) {
        this.mRestaurantRepository = restaurantRepository;
        this.mWorkmatesRepository = workmatesRepository;
    }


    // ---------------------------
    // Get workmates from fireBase
    // ---------------------------
    public LiveData<Event<Workmates>> getCurrentWorkmate() {
        return mWorkmatesRepository.getCurrentWorkmate();
    }


    // --------------------------------
    // Get workmatesNames from fireBase
    // --------------------------------
    public LiveData<Workmates> getWorkmateNames() {
        return mWorkmatesRepository.getWorkmateName();
    }


    // -------------------------------------
    // Get a list of Workmates from fireBase
    // -------------------------------------
    public LiveData<List<Workmates>> getAllWorkmates() {
        return mWorkmatesRepository.getWorkmateList();
    }

    // -----------------------------
    // Create a workmate in fireBase
    // -----------------------------
    public void CreateWorkmate(int requestCode, int resultCode, @Nullable Intent data) {
        mWorkmatesRepository.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // -----------------------------------------------
    // Create a workmate in fireBase register activity
    // -----------------------------------------------
    public void createWorkmate( String email, String name, String urlPicture) {
        mWorkmatesRepository.createWorkmates( email, name, urlPicture);
    }

    // ---------------------------------------------------------
    // Update in fireBase if workmate have a favorite restaurant
    // ---------------------------------------------------------
    public void updateIsRestaurantFavorite( List<Restaurant> listRestaurantFavorite) {
        mWorkmatesRepository.updateRestaurantFavorite( listRestaurantFavorite);
    }


    // ------------------------------------------------------------
    // Update in fireBase if workmate choose a restaurant for lunch
    // ------------------------------------------------------------
    public void updateRestaurantChosen( Restaurant restaurantChosen) {
        mWorkmatesRepository.updateRestaurantChosen( restaurantChosen);
    }





}







