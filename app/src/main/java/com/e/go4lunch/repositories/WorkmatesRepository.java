package com.e.go4lunch.repositories;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.e.go4lunch.R;
import com.e.go4lunch.auth.AuthActivity;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.util.Event;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class WorkmatesRepository {

    private Workmates mWorkmates;
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
    public Task<Void> createWorkmates(String workmateName, String workmateMail, String urlPicture) {
        Workmates workmatesToCreate = new Workmates(workmateName, workmateMail, urlPicture);
        String uid = FirebaseAuth.getInstance().getUid();
        return getWorkmatesCollection().document(Objects.requireNonNull(uid)).set(workmatesToCreate);

    }

    // -----------------------------------------
    // ----- Create a workmate in fireBase -----
    // -----------------------------------------
    public void handleResponseAfterSignIn(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AuthActivity.RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                this.fetchCurrentUserFromFiresTore();
                if (user != null) {
                    Toast.makeText(getApplicationContext(), "" + user.getEmail(), Toast.LENGTH_SHORT).show();
                }
            } else { // ERRORS
                if (response == null) {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(getApplicationContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(getApplicationContext(), R.string.unknown_error, Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    private void fetchCurrentUserFromFiresTore() {
        if (isCurrentUserLogged()) {
            this.getCurrentWorkmate();
            if (mWorkmates == null) {
                createUserInFiresTore();
            } else {
                this.updateUserRepository(mWorkmates);

            }

        }
    }

    private void createUserInFiresTore() {
        String urlPicture = (Objects.requireNonNull(getCurrentUser()).getPhotoUrl() != null) ?
                Objects.requireNonNull(this.getCurrentUser().getPhotoUrl()).toString() : null;
        String email = getCurrentUser().getEmail();
        String username = getCurrentUser().getDisplayName();
        this.createWorkmates(email, username, urlPicture)
                .addOnFailureListener(this.onFailureListener())
                .addOnSuccessListener(aVoid -> {
                });

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
        getWorkmatesCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
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
    // ----- Login Workmates with email and password in fireBase -----
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

    private void updateUserRepository(Workmates workmates) {
        this.mWorkmates = workmates;
    }


    // -----------------
    // ----- UTILS -----
    // -----------------

    @Nullable
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    private OnFailureListener onFailureListener() {
        return e -> Toast.makeText(getApplicationContext(), "unknown_error", Toast.LENGTH_LONG).show();
    }


}

