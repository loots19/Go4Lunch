package com.e.go4lunch.injection;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.e.go4lunch.restaurant.MapsFragment;

public class Injection {

    public static RestaurantRepository provideRestaurantRepository(Context context) {
        return RestaurantRepository.getInstance();
    }

    public static WorkmatesRepository provideWorkmatesRepository(Context context) {
        return WorkmatesRepository.getInstance();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        RestaurantRepository mRestaurantRepository = provideRestaurantRepository(context);
        WorkmatesRepository mWorkmatesRepository = provideWorkmatesRepository(context);
        return new ViewModelFactory(mRestaurantRepository, mWorkmatesRepository);


    }
}