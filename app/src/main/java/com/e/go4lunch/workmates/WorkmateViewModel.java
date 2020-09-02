package com.e.go4lunch.workmates;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.e.go4lunch.util.Event;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class WorkmateViewModel extends ViewModel {
    private WorkmatesRepository mWorkmatesRepository;
    private RestaurantRepository mRestaurantRepository;


    //---- LIVE DATA ---
    private MutableLiveData<List<Workmates>> mWorkmatesList = new MutableLiveData<>();
    private MutableLiveData<Event<Workmates>> mWorkmatesMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Workmates> mWorkmatesNameMutableLiveData = new MutableLiveData<>();

    public WorkmateViewModel(RestaurantRepository restaurantRepository, WorkmatesRepository workmatesRepository) {
        this.mRestaurantRepository = restaurantRepository;
        this.mWorkmatesRepository = workmatesRepository;
    }

    // ---------------------------
    // Get workmates from fireBase
    // ---------------------------
    public MutableLiveData<Event<Workmates>> getWorkmate(String uid) {
        if (this.mWorkmatesMutableLiveData != null) {
            this.setWorkmatesMutableLiveData(uid);
        }
        return this.mWorkmatesMutableLiveData;
    }

    private void setWorkmatesMutableLiveData(String uid) {
        this.mWorkmatesRepository.getWorkmate(uid).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Workmates workmates = documentSnapshot.toObject(Workmates.class);
                mWorkmatesMutableLiveData.setValue(new Event<>(workmates));
            }
        });
    }

    // ---------------------------
    // Get workmatesNames from fireBase
    // ---------------------------
    public MutableLiveData<Workmates> getWorkmateNames(String uid) {
        if (this.mWorkmatesNameMutableLiveData != null) {
            this.setWorkmatesNameMutableLiveData(uid);
        }
        return this.mWorkmatesNameMutableLiveData;
    }

    private void setWorkmatesNameMutableLiveData(String uid) {
        this.mWorkmatesRepository.getWorkmate(uid).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Workmates workmates = documentSnapshot.toObject(Workmates.class);
                mWorkmatesNameMutableLiveData.setValue(workmates);
            }
        });
    }

    // -------------------------------------
    // Get a list of Workmates from fireBase
    // -------------------------------------
    public MutableLiveData<List<Workmates>> getWorkmatesList() {
        if (mWorkmatesList != null) {

            loadWorkmatesList();
        }
        return mWorkmatesList;
    }

    private void loadWorkmatesList() {
        mWorkmatesRepository.getAllWorkmates().addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (queryDocumentSnapshots != null) {
                List<DocumentSnapshot> workmateList = queryDocumentSnapshots.getDocuments();
                List<Workmates> workmate = new ArrayList<>();
                int size = workmateList.size();
                for (int i = 0; i < size; i++) {
                    Workmates workmates = workmateList.get(i).toObject(Workmates.class);
                    workmate.add(workmates);
                }
                mWorkmatesList.setValue(workmate);
            }
        });


    }

    // -----------------------------
    // Create a workmate in fireBase
    // -----------------------------
    public void createWorkmate(String uid, String email, String name, String urlPicture) {
        mWorkmatesRepository.createWorkmates(uid, email, name, urlPicture);
    }

    // ---------------------------------------------------------
    // Update in fireBase if workmate have a favorite restaurant
    // ---------------------------------------------------------
    public void updateIsRestaurantFavorite(String uid, List<Restaurant> listRestaurantFavorite) {
        mWorkmatesRepository.updateRestaurantFavorite(uid, listRestaurantFavorite);
    }

    // ------------------------------------------------------------
    // Update in fireBase if workmate choose a restaurant for lunch
    // ------------------------------------------------------------
    public void updateRestaurantChosen(String uid, Restaurant restaurantChoosen) {
        mWorkmatesRepository.updateRestaurantChosen(uid, restaurantChoosen);
    }

    public void CreateWorkmateFireBase(int requestCode, int resultCode, @Nullable Intent data) {
        mWorkmatesRepository.handleResponseAfterSignIn(requestCode, resultCode, data);

    }


}







