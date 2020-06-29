package com.e.go4lunch.service;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeleteMyWorker extends Worker {

    private RestaurantRepository mRestaurantRepository;
    private WorkmatesRepository mWorkmatesRepository;
    private Workmates currentWorkmate;
    private Restaurant mRestaurant;
    private List<Workmates> mWorkmatesList = new ArrayList<>();
    private String workmateUid;


    public DeleteMyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mRestaurantRepository = new RestaurantRepository();
        this.mWorkmatesRepository = new WorkmatesRepository();
    }

    private void getCurrentWorkmate() {
        workmateUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        this.mWorkmatesRepository.getWorkmate(workmateUid).addOnSuccessListener(documentSnapshot -> {
            currentWorkmate = documentSnapshot.toObject(Workmates.class);
            if (Objects.requireNonNull(currentWorkmate).getRestaurantChoosen() != null) {
                getRestaurant(currentWorkmate.getRestaurantChoosen().getPlaceId());


            }
        });
    }

    private void getRestaurant(String placeId) {
        this.mRestaurantRepository.getRestaurant(placeId).addOnSuccessListener(documentSnapshot -> {
            mRestaurant = documentSnapshot.toObject(Restaurant.class);
            deleteTask();


        });
    }

    @NonNull
    @Override
    public Result doWork() {
        getCurrentWorkmate();
        return Result.success();
    }

    public void deleteTask() {
        Workmates workmatesChoice = new Workmates(currentWorkmate.getWorkmateEmail(), currentWorkmate.getWorkmateName(), currentWorkmate.getUrlPicture());

        if (this.currentWorkmate.getRestaurantChoosen() != null) {
            if (currentWorkmate.getRestaurantChoosen().equals(mRestaurant))

                this.mWorkmatesList.remove(workmatesChoice);
            this.mRestaurantRepository.updateRestaurantWorkmateList(mRestaurant.getPlaceId(), mWorkmatesList);
            this.currentWorkmate.setRestaurantChoosen(null);
            this.mWorkmatesRepository.updateRestaurantChoosen(workmateUid, currentWorkmate.getRestaurantChoosen());
        }
    }
}
