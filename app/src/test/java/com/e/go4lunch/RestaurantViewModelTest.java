package com.e.go4lunch;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.restaurant.RestaurantViewModel;
import com.e.go4lunch.util.Event;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.when;


@RunWith(JUnit4.class)
public class RestaurantViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    private RestaurantViewModel mRestaurantViewModel;
    @Mock
    public RestaurantRepository mRestaurantRepository;

    @Mock
    private Observer<List<Restaurant>> mObserver;
    @Mock
    private MutableLiveData<Event<Restaurant>> mutableLiveData = new MutableLiveData<>();

    @Mock
    private MutableLiveData<List<Restaurant>> newData = new MutableLiveData<>();


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mRestaurantViewModel = new RestaurantViewModel(mRestaurantRepository);
        mRestaurantViewModel.getRestaurantList().observeForever(mObserver);
    }

    @Test
    public void getRestaurantListAfterSetPlaceNull() {
        String type = "restaurant";
        String location = "lat + \" \" + lng";
        int radius = 5000;
        mRestaurantViewModel.setPlace(type, location, radius);
        when(mRestaurantViewModel.getRestaurantList()).thenReturn(null);
        assertNotNull(mRestaurantViewModel.getRestaurantList());
        assertTrue(mRestaurantViewModel.getRestaurantList().hasObservers());



    }

    @Test
    public void getRestaurantListSuccess() {
        String type = "restaurant";
        String location = "lat + \" \" + lng";
        int radius = 5000;
        when(mRestaurantRepository.getRestaurantList(type, location, radius)).thenReturn(newData);
        mRestaurantViewModel.setPlace(type, location, radius);
        assertNotNull(mRestaurantViewModel.getRestaurantList());
        assertTrue(mRestaurantViewModel.getRestaurantList().hasObservers());
        Mockito.verify(mRestaurantRepository).getRestaurantList(type, location, radius);


    }

    @Test
    public void getRestaurantSuccess() {
        String placeId = "ChIJ1yMMhHdp5kcRam7Bk-KDI8M";
        when(mRestaurantRepository.getRestaurant(placeId)).thenReturn(mutableLiveData);
        mRestaurantViewModel.getRestaurant(placeId);
        assertNotNull(mRestaurantViewModel.getRestaurant(placeId));
        Mockito.verify(mRestaurantRepository,atLeast(1)).getRestaurant(placeId);
    }


}
