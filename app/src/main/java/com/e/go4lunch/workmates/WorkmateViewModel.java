package com.e.go4lunch.workmates;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;

import java.util.List;

public class WorkmateViewModel extends ViewModel {
    private  WorkmatesRepository mWorkmatesRepository;
    private RestaurantRepository mRestaurantRepository;


    //---- LIVE DATA ---
    private MutableLiveData<List<Workmates>> mWorkmates = new MutableLiveData<>();

    public WorkmateViewModel(RestaurantRepository restaurantRepository, WorkmatesRepository workmatesRepository) {
        this.mRestaurantRepository = restaurantRepository;
        this.mWorkmatesRepository = workmatesRepository;
    }

    public MutableLiveData<List<Workmates>> getWorkmates() {
        return mWorkmates;
    }
    public WorkmateViewModel(WorkmatesRepository workmatesRepository){
        this.mWorkmatesRepository = workmatesRepository;

    }








}

