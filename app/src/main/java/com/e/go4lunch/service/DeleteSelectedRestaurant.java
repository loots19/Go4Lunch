package com.e.go4lunch.service;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeleteSelectedRestaurant extends Worker {
    // -----------------FOR DATA ------------------
    private RestaurantRepository mRestaurantRepository;
    private WorkmatesRepository mWorkmatesRepository;
    private Workmates currentWorkmate;
    private Restaurant mRestaurant;
    private List<Workmates> mWorkmatesList = new ArrayList<>();


    public DeleteSelectedRestaurant(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mRestaurantRepository = new RestaurantRepository();
        this.mWorkmatesRepository = new WorkmatesRepository();
    }

    private void getCurrentWorkmate() {
        this.mWorkmatesRepository.getWorkmate().addOnSuccessListener(documentSnapshot -> {
            currentWorkmate = documentSnapshot.toObject(Workmates.class);
            if (Objects.requireNonNull(currentWorkmate).getRestaurantChosen() != null) {
                getRestaurant(currentWorkmate.getRestaurantChosen().getPlaceId());


            }
        });
    }

    private void getRestaurant(String placeId) {
        this.mRestaurantRepository.getRestaurant1(placeId).addOnSuccessListener(documentSnapshot -> {
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

    private void deleteTask() {
        Workmates workmatesChoice = new Workmates(currentWorkmate.getWorkmateEmail(), currentWorkmate.getWorkmateName(), currentWorkmate.getUrlPicture());
        if (this.currentWorkmate.getRestaurantChosen() != null) {
            if (currentWorkmate.getRestaurantChosen().equals(mRestaurant))
                this.mWorkmatesList.remove(workmatesChoice);

            this.mRestaurantRepository.updateRestaurantWorkmateList(mRestaurant.getPlaceId(), mWorkmatesList);
            this.currentWorkmate.setRestaurantChosen(null);
            this.mWorkmatesRepository.updateRestaurantChosen(currentWorkmate.getRestaurantChosen());


        }
    }
}
