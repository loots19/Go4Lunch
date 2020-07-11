package com.e.go4lunch.workmates;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.e.go4lunch.R;
import com.e.go4lunch.auth.AuthActivity;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class WorkmateViewModel extends ViewModel {
    private WorkmatesRepository mWorkmatesRepository;
    private RestaurantRepository mRestaurantRepository;


    //---- LIVE DATA ---
    private MutableLiveData<List<Workmates>> mWorkmatesList = new MutableLiveData<>();
    private MutableLiveData<Workmates> mWorkmatesMutableLiveData = new MutableLiveData<>();

    public WorkmateViewModel(RestaurantRepository restaurantRepository, WorkmatesRepository workmatesRepository) {
        this.mRestaurantRepository = restaurantRepository;
        this.mWorkmatesRepository = workmatesRepository;
    }
    // ---------------------------
    // Get workmates from fireBase
    // ---------------------------
    public MutableLiveData<Workmates> getWorkmate(String uid) {
        if (this.mWorkmatesMutableLiveData != null) {
            this.setWorkmatesMutableLiveData(uid);
        }
        return this.mWorkmatesMutableLiveData;
    }

    private void setWorkmatesMutableLiveData(String uid) {
        this.mWorkmatesRepository.getWorkmate(uid).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Workmates workmates = documentSnapshot.toObject(Workmates.class);
                mWorkmatesMutableLiveData.setValue(workmates);
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
            mWorkmatesRepository.getWorkmate(getCurrentUser().getUid())
                    .addOnFailureListener(this.onFailureListener())
                    .addOnSuccessListener(documentSnapshot -> {
                        Workmates workmates = documentSnapshot.toObject(Workmates.class);
                        if (workmates == null) {
                            createUserInFiresTore();
                        } else {
                            mWorkmatesRepository.updateUserRepository(workmates);

                        }
                    });


        }
    }

    private void createUserInFiresTore() {
        String urlPicture = (getCurrentUser().getPhotoUrl() != null) ?
                this.getCurrentUser().getPhotoUrl().toString() : null;
        String email = getCurrentUser().getEmail();
        String username = getCurrentUser().getDisplayName();
        String uid = getCurrentUser().getUid();
        mWorkmatesRepository.createWorkmates(uid,email,username, urlPicture)
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

    protected OnFailureListener onFailureListener() {
        return e -> Toast.makeText(getApplicationContext(), "unknown_error", Toast.LENGTH_LONG).show();


    }


}







