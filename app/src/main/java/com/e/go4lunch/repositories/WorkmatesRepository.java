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
    private Workmates mWorkmates;

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getWorkmatesCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public Task<Void> createWorkmates(String uid, String workmateName, String workmateMail, String urlPicture) {
        Workmates workmatesToCreate = new Workmates(workmateName, workmateMail, urlPicture);

        return getWorkmatesCollection().document(uid).set(workmatesToCreate);
        //todo verifier si user deja présent.
    }

    // --- GET ---

    public Task<DocumentSnapshot> getWorkmate(String uid) {
        return getWorkmatesCollection().document(uid).get();
    }

    public Query getAllWorkmates() {
        return getWorkmatesCollection().orderBy("workmateName");
    }

    // --- UPDATE ---

    public Task<Void> updateRestaurantFavorite(String uid,List<Restaurant>listRestaurantFavorite){
        return  getWorkmatesCollection().document(uid).update("listRestaurantFavorite",listRestaurantFavorite);
    }
    public Task<Void> updateRestaurantChoosen(String uid, Restaurant restaurantChoosen){
        return getWorkmatesCollection().document(uid).update("restaurantChoosen",restaurantChoosen);
    }


}
