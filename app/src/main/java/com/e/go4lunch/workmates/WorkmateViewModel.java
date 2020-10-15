package com.e.go4lunch.workmates;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.e.go4lunch.util.Event;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class WorkmateViewModel extends ViewModel {
    private WorkmatesRepository mWorkmatesRepository;
    private MutableLiveData<FirebaseUser> userLiveData;


    public WorkmateViewModel(WorkmatesRepository workmatesRepository) {
        this.mWorkmatesRepository = workmatesRepository;
        userLiveData = mWorkmatesRepository.getUserLiveData();
    }


    // ---------------------------------------
    // ----- Get workmates from fireBase -----
    // ---------------------------------------
    public LiveData<Event<Workmates>> getCurrentWorkmate() {
        return mWorkmatesRepository.getCurrentWorkmate();
    }

    // --------------------------------------------
    // ----- Get workmatesNames from fireBase -----
    // --------------------------------------------
    public LiveData<Workmates> getWorkmateNames() {
        return mWorkmatesRepository.getWorkmateName();
    }

    // -------------------------------------------------
    // ----- Get a list of Workmates from fireBase -----
    // -------------------------------------------------
    public LiveData<List<Workmates>> getAllWorkmates() {
        return mWorkmatesRepository.getWorkmateList();
    }

    // -----------------------------------------------------------
    // ----- Create a workmate in fireBase register activity -----
    // -----------------------------------------------------------
    public void createWorkmate(String email, String name, String urlPicture) {
        mWorkmatesRepository.createWorkmates(email, name, urlPicture);
    }

    // ---------------------------------------------------------------------
    // ----- Update in fireBase if workmate have a favorite restaurant -----
    // ---------------------------------------------------------------------
    public void updateIsRestaurantFavorite(List<Restaurant> listRestaurantFavorite) {
        mWorkmatesRepository.updateRestaurantFavorite(listRestaurantFavorite);
    }

    // ------------------------------------------------------------------------
    // ----- Update in fireBase if workmate choose a restaurant for lunch -----
    // ------------------------------------------------------------------------
    public void updateRestaurantChosen(Restaurant restaurantChosen) {
        mWorkmatesRepository.updateRestaurantChosen(restaurantChosen);
    }

    public void register(String email, String password) {
        mWorkmatesRepository.register(email, password);
    }

    public void logIn(String email, String password) {
        mWorkmatesRepository.LogIn(email, password);
    }




    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }


}







