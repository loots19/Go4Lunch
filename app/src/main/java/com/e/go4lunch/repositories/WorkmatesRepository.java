package com.e.go4lunch.repositories;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class WorkmatesRepository {

    private static final String COLLECTION_NAME = "workmates";
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();


    // --- COLLECTION REFERENCE ---

    public static CollectionReference getWorkmatesCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

   public Task<Void> createWorkmates(String uid, String workmateName, String workmateMail, String urlPicture) {
        Workmates workmatesToCreate = new Workmates(workmateName, workmateMail, urlPicture);
       return getWorkmatesCollection().document(uid).set(workmatesToCreate);

   }

 // MutableLiveData<Workmates> createWorkmates(AuthCredential authCredential){
 //     MutableLiveData<Workmates> newData = new MutableLiveData<>();
 //     mFirebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
 //         if(task.isSuccessful()){
 //             FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
 //             if(firebaseUser != null){
 //                 String name = firebaseUser.getDisplayName();
 //                 String email = firebaseUser.getEmail();
 //                 String urlPhoto = String.valueOf(firebaseUser.getPhotoUrl());
 //                 Workmates workmates = new Workmates(name,email,urlPhoto);
 //                 newData.setValue(workmates);
 //             }
 //         }else{
 //             Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), android.widget.Toast.LENGTH_LONG).show();

 //         }
 //     });
 //     return newData;
 // }


    // --- GET ---

    public Task<DocumentSnapshot> getWorkmate(String uid) {
        return getWorkmatesCollection().document(uid).get();
    }

    public Query getAllWorkmates() {
        return getWorkmatesCollection().orderBy("workmateName");
    }

    // --- UPDATE ---

    public Task<Void> updateRestaurantFavorite(String uid, List<Restaurant> listRestaurantFavorite) {
        return getWorkmatesCollection().document(uid).update("listRestaurantFavorite", listRestaurantFavorite);
    }

    public Task<Void> updateRestaurantChoosen(String uid, Restaurant restaurantChoosen) {
        return getWorkmatesCollection().document(uid).update("restaurantChoosen", restaurantChoosen);
    }



}
