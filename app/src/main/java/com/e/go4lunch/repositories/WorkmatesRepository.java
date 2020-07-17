package com.e.go4lunch.repositories;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.e.go4lunch.R;
import com.e.go4lunch.auth.AuthActivity;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class WorkmatesRepository {

    private static final String COLLECTION_NAME = "workmates";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
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
    // --------------
    // --- GET ---
    // --------------

    public Task<DocumentSnapshot> getWorkmate(String uid) {
        return getWorkmatesCollection().document(uid).get();
    }

    public Query getAllWorkmates() {
        return getWorkmatesCollection().orderBy("workmateName");
    }
    // --------------
    // --- UPDATE ---
    // --------------

    public Task<Void> updateRestaurantFavorite(String uid, List<Restaurant> listRestaurantFavorite) {
        return getWorkmatesCollection().document(uid).update("listRestaurantFavorite", listRestaurantFavorite);
    }

    public Task<Void> updateRestaurantChosen(String uid, Restaurant restaurantChoosen) {
        return getWorkmatesCollection().document(uid).update("restaurantChosen", restaurantChoosen);
    }

    private void updateUserRepository(Workmates workmates) {
        this.mWorkmates = workmates;
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
            this.getWorkmate(Objects.requireNonNull(getCurrentUser()).getUid())
                    .addOnFailureListener(this.onFailureListener())
                    .addOnSuccessListener(documentSnapshot -> {
                        Workmates workmates = documentSnapshot.toObject(Workmates.class);
                        if (workmates == null) {
                            createUserInFiresTore();
                        } else {
                            this.updateUserRepository(workmates);

                        }
                    });


        }
    }

    private void createUserInFiresTore() {
        String urlPicture = (Objects.requireNonNull(getCurrentUser()).getPhotoUrl() != null) ?
                Objects.requireNonNull(this.getCurrentUser().getPhotoUrl()).toString() : null;
        String email = getCurrentUser().getEmail();
        String username = getCurrentUser().getDisplayName();
        String uid = getCurrentUser().getUid();
        this.createWorkmates(uid,email,username, urlPicture)
                .addOnFailureListener(this.onFailureListener())
                .addOnSuccessListener(aVoid -> fetchCurrentUserFromFiresTore());


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

