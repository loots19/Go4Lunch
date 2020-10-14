package com.e.go4lunch.repositories.injection;

import android.content.Context;

import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;

public class Injection {

    //This class will be responsible for providing already built objects, centrally

    private static RestaurantRepository provideRestaurantRepository(Context context) {
        return RestaurantRepository.getInstance();
    }

    public static WorkmatesRepository provideWorkmatesRepository(Context context) {
        return new WorkmatesRepository();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        RestaurantRepository mRestaurantRepository = provideRestaurantRepository(context);
        WorkmatesRepository mWorkmatesRepository = provideWorkmatesRepository(context);
        return new ViewModelFactory(mRestaurantRepository, mWorkmatesRepository);


    }
}