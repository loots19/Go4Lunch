package com.e.go4lunch.workmates;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class WorkmateViewModel extends ViewModel {
    private WorkmatesRepository mWorkmatesRepository;
    private RestaurantRepository mRestaurantRepository;


    //---- LIVE DATA ---
    private MutableLiveData<List<Workmates>> mWorkmatesList = new MutableLiveData<>();
    private MutableLiveData<Workmates>mWorkmatesMutableLiveData = new MutableLiveData<>();

    public WorkmateViewModel(RestaurantRepository restaurantRepository, WorkmatesRepository workmatesRepository) {
        this.mRestaurantRepository = restaurantRepository;
        this.mWorkmatesRepository = workmatesRepository;
    }

    public MutableLiveData<Workmates>getWorkmate(String uid){
        if (this.mWorkmatesMutableLiveData != null){
            this.setWorkmatesMutableLiveData(uid);
        }
        return this.mWorkmatesMutableLiveData;
    }
    private void setWorkmatesMutableLiveData(String uid){
        this.mWorkmatesRepository.getWorkmate(uid).addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                Workmates workmates = documentSnapshot.toObject(Workmates.class);
                mWorkmatesMutableLiveData.setValue(workmates);
            }
        });
    }


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

    public void createWorkmate (String uid,String email,String name, String urlPicture){
        mWorkmatesRepository.createWorkmates(uid, email, name, urlPicture);
    }

    public void updateIsRestaurantFavorite(String uid, List<Restaurant>listRestaurantFavorite ){
        mWorkmatesRepository.updateRestaurantFavorite(uid, listRestaurantFavorite);
    }
    public void updateRestaurantChoosen (String uid,Restaurant restaurantChoosen){
        mWorkmatesRepository.updateRestaurantChoosen(uid, restaurantChoosen);
    }





}

