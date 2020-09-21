package com.e.go4lunch.workmates;

import android.content.Intent;

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
    public LiveData<Event<Workmates>> getWorkmate(String uid) {
        return mWorkmatesRepository.getWorkmate(uid);
    }
    // public MutableLiveData<Event<Workmates>> getWorkmate(String uid) {
    //     if (this.mWorkmatesMutableLiveData != null) {
    //         this.setWorkmatesMutableLiveData(uid);
    //     }
    //     return this.mWorkmatesMutableLiveData;
    // }

    // private void setWorkmatesMutableLiveData(String uid) {
    //     this.mWorkmatesRepository.getWorkmate(uid).addOnSuccessListener(documentSnapshot -> {
    //         if (documentSnapshot.exists()) {
    //             Workmates workmates = documentSnapshot.toObject(Workmates.class);
    //             mWorkmatesMutableLiveData.setValue(new Event<>(workmates));
    //         }
    //     });
    // }

    // --------------------------------
    // Get workmatesNames from fireBase
    // --------------------------------
    public LiveData<Workmates> getWorkmateNames(String uid) {
        return mWorkmatesRepository.getWorkmateName(uid);
    }
    //public MutableLiveData<Workmates> getWorkmateNames(String uid) {
    //    if (this.mWorkmatesNameMutableLiveData != null) {
    //        this.setWorkmatesNameMutableLiveData(uid);
    //    }
    //    return this.mWorkmatesNameMutableLiveData;
    //}

    //private void setWorkmatesNameMutableLiveData(String uid) {
    //    this.mWorkmatesRepository.getWorkmate(uid).addOnSuccessListener(documentSnapshot -> {
    //        if (documentSnapshot.exists()) {
    //            Workmates workmates = documentSnapshot.toObject(Workmates.class);
    //            mWorkmatesNameMutableLiveData.setValue(workmates);
    //        }
    //    });
    //}

    // -------------------------------------
    // Get a list of Workmates from fireBase
    // -------------------------------------

    public LiveData<List<Workmates>> getAllWorkmates() {
        return mWorkmatesRepository.getWorkmateList();
    }
    //public MutableLiveData<List<Workmates>> getWorkmatesList() {
    //    if (mWorkmatesList != null) {

    //        loadWorkmatesList();
    //    }
    //    return mWorkmatesList;
    //}

    //private void loadWorkmatesList() {
    //    mWorkmatesRepository.getAllWorkmates().addSnapshotListener((queryDocumentSnapshots, e) -> {
    //        if (queryDocumentSnapshots != null) {
    //            List<DocumentSnapshot> workmateList = queryDocumentSnapshots.getDocuments();
    //            List<Workmates> workmate = new ArrayList<>();
    //            int size = workmateList.size();
    //            for (int i = 0; i < size; i++) {
    //                Workmates workmates = workmateList.get(i).toObject(Workmates.class);
    //                workmate.add(workmates);
    //            }
    //            mWorkmatesList.setValue(workmate);
    //        }
    //    });


    //}
    // -----------------------------
    // Create a workmate in fireBase
    // -----------------------------
    public void CreateWorkmate(int requestCode, int resultCode, @Nullable Intent data) {
        mWorkmatesRepository.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // -----------------------------------------------
    // Create a workmate in fireBase register activity
    // -----------------------------------------------
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



}







