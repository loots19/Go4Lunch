package com.e.go4lunch.repositories;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.util.Event;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;

public class WorkmatesRepository {

    private MutableLiveData<FirebaseUser> userLiveData;


    public WorkmatesRepository() {
        this.userLiveData = new MutableLiveData<>();
    }


    // --------------------------------
    // ----- COLLECTION REFERENCE -----
    // --------------------------------
    private static CollectionReference getWorkmatesCollection() {
        return FirebaseFirestore.getInstance().collection("workmates");
    }

    // ------------------
    // ----- CREATE -----
    // ------------------
    public void createWorkmates(String workmateName, String workmateMail, String urlPicture) {
        Workmates workmatesToCreate = new Workmates(workmateName, workmateMail, urlPicture);
        String uid = FirebaseAuth.getInstance().getUid();
        getWorkmatesCollection().document(Objects.requireNonNull(uid)).set(workmatesToCreate);

    }

    // ---------------
    // ----- GET -----
    // ---------------

    // -------------------------------------------------------------------------------------------
    // ----- Get currentWorkmate from firesTore and use it for notification and delete task  -----
    // -------------------------------------------------------------------------------------------
    public Task<DocumentSnapshot> getWorkmate() {
        String uid = FirebaseAuth.getInstance().getUid();
        return getWorkmatesCollection().document(Objects.requireNonNull(uid)).get();
    }


    // -------------------------------------------
    // ----- Get WorkmateName from firesTore -----
    // -------------------------------------------
    public MutableLiveData<Workmates> getWorkmateName() {
        MutableLiveData<Workmates> mutableLiveData = new MutableLiveData<>();
        String uid = FirebaseAuth.getInstance().getUid();
        getWorkmatesCollection().document(Objects.requireNonNull(uid)).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Workmates workmates = documentSnapshot.toObject(Workmates.class);
                mutableLiveData.setValue(workmates);
            }
        });
        return mutableLiveData;
    }


    // ----------------------------------------------
    // ----- Get currentWorkmate from firesTore -----
    // ----------------------------------------------
    public MutableLiveData<Event<Workmates>> getCurrentWorkmate() {
        MutableLiveData<Event<Workmates>> mutableLiveData = new MutableLiveData<>();
        String uid = FirebaseAuth.getInstance().getUid();
        getWorkmatesCollection().document(Objects.requireNonNull(uid)).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {

                Workmates workmates = documentSnapshot.toObject(Workmates.class);
                mutableLiveData.postValue(new Event<>(workmates));
            }
        });
        return mutableLiveData;
    }

    // -------------------------------------------
    // ----- Get workmateList from firesTore -----
    // -------------------------------------------
    public MutableLiveData<List<Workmates>> getWorkmateList() {
        MutableLiveData<List<Workmates>> mWorkmateList = new MutableLiveData<>();
        getWorkmatesCollection().addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (queryDocumentSnapshots != null) {
                List<DocumentSnapshot> workmateList = queryDocumentSnapshots.getDocuments();
                List<Workmates> workmate = new ArrayList<>();
                int size = workmateList.size();
                for (int i = 0; i < size; i++) {
                    Workmates workmates = workmateList.get(i).toObject(Workmates.class);
                    workmate.add(workmates);
                }
                mWorkmateList.setValue(workmate);

            }
        });

        return mWorkmateList;

    }

    // ------------------------------------------------------------------
    // ----- register Workmates with email and password in fireBase -----
    // ------------------------------------------------------------------
    public void register(String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getApplicationContext().getMainExecutor(), task -> {
                    if (task.isSuccessful()) {
                        userLiveData.postValue(FirebaseAuth.getInstance().getCurrentUser());
                    } else {
                        Toast.makeText(getApplicationContext().getApplicationContext(), "Registration Failure: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ------------------------------------------------------------------
    // ----- Login Workmates with email and password in fireBase --------
    // ------------------------------------------------------------------
    public void LogIn(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getApplicationContext().getMainExecutor(), task -> {
                    if (task.isSuccessful()) {
                        userLiveData.postValue(FirebaseAuth.getInstance().getCurrentUser());
                    } else {
                        Toast.makeText(getApplicationContext().getApplicationContext(), "Registration Failure: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ----- getter -----
    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }


    // ------------------
    // ----- UPDATE -----
    // ------------------

    public void updateRestaurantFavorite(List<Restaurant> listRestaurantFavorite) {
        String uid = FirebaseAuth.getInstance().getUid();
        getWorkmatesCollection().document(Objects.requireNonNull(uid)).update("listRestaurantFavorite", listRestaurantFavorite);
    }

    public void updateRestaurantChosen(Restaurant restaurantChosen) {
        String uid = FirebaseAuth.getInstance().getUid();
        getWorkmatesCollection().document(Objects.requireNonNull(uid)).update("restaurantChosen", restaurantChosen);

    }


}

