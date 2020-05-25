package com.e.go4lunch.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesRepository {

    private static final String COLLECTION_NAME = "workmates";
    private List<Workmates> mWorkmatesList;


    // --- COLLECTION REFERENCE ---

    public static CollectionReference getWorkmatesCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public Task<Void> createWorkmates(String uid, String workmateName, String workmateMail, String urlPicture) {
        Workmates workmatesToCreate = new Workmates(workmateName, workmateMail, urlPicture);
        return getWorkmatesCollection().document(uid).set(workmatesToCreate);
    }

    // --- GET ---

    public Task<DocumentSnapshot> getWorkmate(String uid) {
        return getWorkmatesCollection().document(uid).get();
    }

    public Query getAllWorkmates() {
        Log.e("work", "work");
        return getWorkmatesCollection().orderBy("workmateName");
    }

    // --- UPDATE ---

    public Task<Void> updateRestaurantSelected(String uid, Boolean restaurantSelected) {

        return getWorkmatesCollection().document(uid).update("isSelected", restaurantSelected);
    }

    public Task<Void> updateListRestaurantSelected(String uid, List<Restaurant> restaurantListSelected) {
        return getWorkmatesCollection().document(uid).update("restaurantListSelected", restaurantListSelected);
    }



}
