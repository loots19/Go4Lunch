package com.e.go4lunch.injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.e.go4lunch.restaurant.RestaurantAutocompleteViewModel;
import com.e.go4lunch.restaurant.RestaurantDetailViewModel;
import com.e.go4lunch.restaurant.RestaurantViewModel;
import com.e.go4lunch.workmates.WorkmateViewModel;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static io.reactivex.rxjava3.processors.MulticastProcessor.create;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final RestaurantRepository mRestaurantRepository;
    private final WorkmatesRepository mWorkmatesRepository;

    public ViewModelFactory(RestaurantRepository restaurantRepository, WorkmatesRepository workmatesRepository) {
        this.mRestaurantRepository = restaurantRepository;
        this.mWorkmatesRepository = workmatesRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantViewModel.class)) {
            return (T) new RestaurantViewModel(mRestaurantRepository);
        }

        if (modelClass.isAssignableFrom(RestaurantDetailViewModel.class)) {
            return (T) new RestaurantDetailViewModel(mRestaurantRepository);
        }
        if (modelClass.isAssignableFrom(WorkmateViewModel.class)) {
            return (T) new WorkmateViewModel(mRestaurantRepository, mWorkmatesRepository);
        }
        if (modelClass.isAssignableFrom(RestaurantAutocompleteViewModel.class)) {
            return (T) new RestaurantAutocompleteViewModel(mRestaurantRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
