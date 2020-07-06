package com.e.go4lunch;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.restaurant.RestaurantViewModel;

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


@RunWith(JUnit4.class)
public class ExampleUnitTest {
    // Executes each task synchronously using Architecture Components.
    private RestaurantViewModel mRestaurantViewModel;


    @Mock
    private RestaurantRepository mRestaurantRepository;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setupAddEditTaskViewModel() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);
        mRestaurantViewModel = new RestaurantViewModel(mRestaurantRepository);


    }
    @Test
    public void get() {
       mRestaurantViewModel.getRestaurantList();
        Mockito.verify(mRestaurantRepository).getRestaurantList();
    }

}