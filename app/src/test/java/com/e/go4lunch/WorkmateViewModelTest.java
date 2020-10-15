package com.e.go4lunch;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.e.go4lunch.util.Event;
import com.e.go4lunch.workmates.WorkmateViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(JUnit4.class)
public class WorkmateViewModelTest {
    private Workmates mWorkmates;
    private Restaurant mRestaurant;
    private List<Restaurant> mRestaurantList;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    private WorkmateViewModel mWorkmateViewModel;

    @Mock
    private WorkmatesRepository mWorkmatesRepository;

    @Mock
    private MutableLiveData<Workmates> mutableLiveData = new MutableLiveData<>();

    @Mock
    private MutableLiveData<Event<Workmates>> event = new MutableLiveData<>();

    @Mock
    private MutableLiveData<List<Workmates>> mWorkmateList = new MutableLiveData<>();


    @Before
    public void setUp() {
        mWorkmates = new Workmates("email", "name", "url");
        MockitoAnnotations.initMocks(this);
        mWorkmateViewModel = new WorkmateViewModel(mWorkmatesRepository);

    }

    @Test
    public void updateRestaurantSelected() {
        mWorkmates.setRestaurantChosen(mRestaurant);
        mWorkmateViewModel.updateRestaurantChosen(mRestaurant);
        verify(mWorkmatesRepository).updateRestaurantChosen(mRestaurant);
    }

    @Test
    public void updateRestaurantFavorite() {
        mWorkmates.setListRestaurantFavorite(mRestaurantList);
        mWorkmateViewModel.updateIsRestaurantFavorite(mRestaurantList);
        verify(mWorkmatesRepository).updateRestaurantFavorite(mRestaurantList);

    }


    @Test
    public void getAllWorkmates() {
        when(mWorkmatesRepository.getWorkmateList()).thenReturn(mWorkmateList);
        mWorkmateViewModel.getAllWorkmates();
        assertNotNull(mWorkmateViewModel.getAllWorkmates());
        verify(mWorkmatesRepository, atLeast(1)).getWorkmateList();
    }

    @Test
    public void getCurrentWorkmate() {
        when(mWorkmatesRepository.getCurrentWorkmate()).thenReturn(event);
        mWorkmateViewModel.getCurrentWorkmate();
        assertNotNull(mWorkmateViewModel.getCurrentWorkmate());
        verify(mWorkmatesRepository, atLeast(1)).getCurrentWorkmate();
    }

    @Test
    public void getWorkmateName() {
        when(mWorkmatesRepository.getWorkmateName()).thenReturn(mutableLiveData);
        mWorkmateViewModel.getWorkmateNames();
        assertNotNull(mWorkmateViewModel.getWorkmateNames());
        verify(mWorkmatesRepository, atLeast(1)).getWorkmateName();
    }


    @Test
    public void getGoodLogin() {
        String email = "denis@gmail.com";
        String password = "*********";
        mWorkmateViewModel.logIn(email, password);
        verify(mWorkmatesRepository).LogIn(email, password);
    }

    @Test
    public void createWorkmate() {
        String email = mWorkmates.getWorkmateEmail();
        String name = mWorkmates.getWorkmateName();
        String url = mWorkmates.getUrlPicture();
        mWorkmateViewModel.createWorkmate(email, name, url);
        verify(mWorkmatesRepository).createWorkmates(email, name, url);

    }


}

