package com.e.go4lunch;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.models.placeDetail.PlaceDetail;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.e.go4lunch.restaurant.RestaurantDetailViewModel;
import com.e.go4lunch.restaurant.RestaurantViewModel;
import com.e.go4lunch.workmates.WorkmateViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(JUnit4.class)
public class ExampleUnitTest {
    // Executes each task synchronously using Architecture Components.
    private RestaurantViewModel mRestaurantViewModel;
    private WorkmateViewModel mWorkmateViewModel;
    private RestaurantDetailViewModel mRestaurantDetailViewModel;
    private Workmates mWorkmates;
    private String uid;

    @Mock
    private WorkmatesRepository mWorkmatesRepository;

    @Mock
    private RestaurantRepository mRestaurantRepository;

    @Mock
    private Restaurant mRestaurant;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);
        mRestaurantViewModel = new RestaurantViewModel(mRestaurantRepository, mWorkmatesRepository);
    }


    @Test
    public void openDetailActivity() {


    }
}