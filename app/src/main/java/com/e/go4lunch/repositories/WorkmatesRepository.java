package com.e.go4lunch.repositories;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class WorkmatesRepository {

    private static final String COLLECTION_NAME = "workmates";


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
        return getWorkmatesCollection().orderBy("urlPicture");
    }

    // --- UPDATE ---

    public Task<Void> updateRestaurantSelected(String uid, Boolean restaurantSelected) {
        return getWorkmatesCollection().document(uid).update("isSelected", restaurantSelected);
    }

    public Task<Void> updateListRestaurantSelected(String uid, List<Restaurant> restaurantListSelected) {
        return getWorkmatesCollection().document(uid).update("restaurantListSelected", restaurantListSelected);
    }


}
