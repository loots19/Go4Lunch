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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class WorkmatesRepository {

    private static final String COLLECTION_NAME = "workmates";
    private Workmates mWorkmates;


    // ----------------------------
    // --- COLLECTION REFERENCE ---
    // ----------------------------

    private static CollectionReference getWorkmatesCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }
    // --------------
    // --- CREATE ---
    // --------------

    public Task<Void> createWorkmates(String uid, String workmateName, String workmateMail, String urlPicture) {
        Workmates workmatesToCreate = new Workmates(workmateName, workmateMail, urlPicture);
        return getWorkmatesCollection().document(uid).set(workmatesToCreate);

    }

    // -----------------------------
    // Create a workmate in fireBase
    // -----------------------------
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
            this.getWorkmate(Objects.requireNonNull(getCurrentUser()).getUid());
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
        String uid = getCurrentUser().getUid();
        this.createWorkmates(uid, email, username, urlPicture)
                .addOnFailureListener(this.onFailureListener())
                .addOnSuccessListener(aVoid -> fetchCurrentUserFromFiresTore());

    }
    // --------------
    // --- GET ---
    // --------------

    public Task<DocumentSnapshot> getWorkmate1(String uid) {
        return getWorkmatesCollection().document(uid).get();
    }

    public MutableLiveData<Workmates> getWorkmateName(String uid) {
        MutableLiveData<Workmates> mutableLiveData = new MutableLiveData<>();
        getWorkmatesCollection().document(uid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Workmates workmates = documentSnapshot.toObject(Workmates.class);
                mutableLiveData.setValue(workmates);
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<Event<Workmates>> getWorkmate(String uid) {
        MutableLiveData<Event<Workmates>> mutableLiveData = new MutableLiveData<>();
        getWorkmatesCollection().document(uid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Workmates workmates = documentSnapshot.toObject(Workmates.class);
                mutableLiveData.setValue(new Event<>(workmates));
            }
        });
        return mutableLiveData;
    }


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
    // --------------
    // --- UPDATE ---
    // --------------

    public void updateRestaurantFavorite(String uid, List<Restaurant> listRestaurantFavorite) {
        getWorkmatesCollection().document(uid).update("listRestaurantFavorite", listRestaurantFavorite);
    }

    public void updateRestaurantChosen(String uid, Restaurant restaurantChosen) {
        getWorkmatesCollection().document(uid).update("restaurantChosen", restaurantChosen);
    }

    private void updateUserRepository(Workmates workmates) {
        this.mWorkmates = workmates;
    }


    // --------------------
    // UTILS
    // --------------------

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

