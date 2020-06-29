package com.e.go4lunch.injection;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.e.go4lunch.restaurant.MapsFragment;

public class Injection {

    private static RestaurantRepository provideRestaurantRepository(Context context) {
        return RestaurantRepository.getInstance();
    }

    private static WorkmatesRepository provideWorkmatesRepository(Context context) {
        return new WorkmatesRepository();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        RestaurantRepository mRestaurantRepository = provideRestaurantRepository(context);
        WorkmatesRepository mWorkmatesRepository = provideWorkmatesRepository(context);
        return new ViewModelFactory(mRestaurantRepository, mWorkmatesRepository);


    }
}