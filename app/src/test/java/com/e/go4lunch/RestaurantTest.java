package com.e.go4lunch;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.e.go4lunch.restaurant.RestaurantViewModel;
import com.e.go4lunch.workmates.WorkmateViewModel;
import com.google.android.libraries.places.api.model.Place;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.rxjava3.subscribers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class RestaurantTest {
    private Workmates mWorkmates;
    private WorkmateViewModel mWorkmateViewModel ;

    @Mock
    private WorkmatesRepository mWorkmatesRepository;
    private RestaurantRepository mRestaurantRepository;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

}
