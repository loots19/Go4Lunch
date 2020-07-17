package com.e.go4lunch;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.e.go4lunch.restaurant.RestaurantViewModel;
import com.e.go4lunch.workmates.WorkmateViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;

@RunWith(JUnit4.class)
public class Go4LunchUnitTest {
    private Workmates mWorkmates;
    private Restaurant mRestaurant;
    private List<Workmates> workmatesList = new ArrayList<>();
    private List<Restaurant> restaurantList = new ArrayList<>();
    private RestaurantViewModel mRestaurantViewModel;
    private WorkmateViewModel mWorkmateViewModel;

    @Mock
    private RestaurantRepository mRestaurantRepository;

    @Mock
    private WorkmatesRepository mWorkmatesRepository;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mWorkmates = new Workmates("ludotracks@hotmail.com", "reloots loots", "https://lh4.googleusercontent.com/-hzcZGYiTWAY/AAAAAAAAAAI/AAAAAAAAAAA/AMZuucnP80n8qHJvJhVOfcB4xGdSrfsajw/s96-c/photo.jpg");
        mRestaurant = new Restaurant("ChIJJSKN0Ihe5kcRORctLjn_Npk", "Château de Méry - L'Hôtel", "9 Rue de l'Isle Adam, Méry-sur-Oise", "CmRaAAAAfJ9lwBAVN8Va0wPesjlfZL6o3ZQ0TY2PMeSBRujP2iR6d6mUXonK-3_U3vNBz1OxhhL_housk2BRuNl9P0wd8JsPOUONDpJm7g8Ut2m-3rFq2EJx3HIbA946B_KTRbQaEhDtw10IH4wySpy8th5d2iRrGhSkiM32Z2_lp1bbceHgdx2m8aN4qg",
                false, null, 4.5, workmatesList);
        mWorkmateViewModel = new WorkmateViewModel(mRestaurantRepository,mWorkmatesRepository);

    }

    @org.junit.Test
    public void getWorkmateInfo() {
        assertEquals("ludotracks@hotmail.com", mWorkmates.getWorkmateEmail());
        assertEquals("reloots loots", mWorkmates.getWorkmateName());
        assertEquals("https://lh4.googleusercontent.com/-hzcZGYiTWAY/AAAAAAAAAAI/AAAAAAAAAAA/AMZuucnP80n8qHJvJhVOfcB4xGdSrfsajw/s96-c/photo.jpg", mWorkmates.getUrlPicture());
    }

    @org.junit.Test
    public void addWorkmatesInList() {
        workmatesList.add(new Workmates("ludotracks@hotmail.com", "reloots loots", "https://lh4.googleusercontent.com/-hzcZGYiTWAY/AAAAAAAAAAI/AAAAAAAAAAA/AMZuucnP80n8qHJvJhVOfcB4xGdSrfsajw/s96-c/photo.jpg"));
        assertEquals("reloots loots", workmatesList.get(0).getWorkmateName());
    }
    @Test
    public void getRestaurantInfo(){
        assertEquals("ChIJJSKN0Ihe5kcRORctLjn_Npk",mRestaurant.getPlaceId());
        assertEquals("Château de Méry - L'Hôtel",mRestaurant.getName());
        assertEquals("9 Rue de l'Isle Adam, Méry-sur-Oise",mRestaurant.getAddress());
        assertEquals("CmRaAAAAfJ9lwBAVN8Va0wPesjlfZL6o3ZQ0TY2PMeSBRujP2iR6d6mUXonK-3_U3vNBz1OxhhL_housk2BRuNl9P0wd8JsPOUONDpJm7g8Ut2m-3rFq2EJx3HIbA946B_KTRbQaEhDtw10IH4wySpy8th5d2iRrGhSkiM32Z2_lp1bbceHgdx2m8aN4qg",
                mRestaurant.getUrlPhoto());
        assertFalse(mRestaurant.getOpenNow());
        assertNull(mRestaurant.getLocation());
        assertEquals(4.5,mRestaurant.getRating());
        assertEquals(workmatesList,mRestaurant.getWorkmatesList());
    }
    @Test
    public void addRestaurantList(){
        restaurantList.add(new Restaurant("ChIJJSKN0Ihe5kcRORctLjn_Npk", "Château de Méry - L'Hôtel", "9 Rue de l'Isle Adam, Méry-sur-Oise", "CmRaAAAAfJ9lwBAVN8Va0wPesjlfZL6o3ZQ0TY2PMeSBRujP2iR6d6mUXonK-3_U3vNBz1OxhhL_housk2BRuNl9P0wd8JsPOUONDpJm7g8Ut2m-3rFq2EJx3HIbA946B_KTRbQaEhDtw10IH4wySpy8th5d2iRrGhSkiM32Z2_lp1bbceHgdx2m8aN4qg",
                false, null, 4.5, workmatesList));
       assertEquals("ChIJJSKN0Ihe5kcRORctLjn_Npk",restaurantList.get(0).getPlaceId());
       assertEquals(4.5,restaurantList.get(0).getRating());
    }

    @org.junit.Test
    public void getWorkmateGoodResponse(){
        MutableLiveData<Workmates> mWorkmatesNameMutableLiveData = new MutableLiveData<>();
        mWorkmatesNameMutableLiveData.postValue(mWorkmates);
        assertEquals(mWorkmates,mWorkmatesNameMutableLiveData.getValue());

    }
    @org.junit.Test
    public void getWorkmatesListGoodResponse(){
        MutableLiveData<List<Workmates>> mWorkmatesList = new MutableLiveData<>();
        mWorkmatesList.postValue(mRestaurant.getWorkmatesList());
        assertEquals(workmatesList,mWorkmatesList.getValue());

    }
    @org.junit.Test
    public void getRestaurantListGoodResponse() {
        MutableLiveData<List<Restaurant>> mRestaurantList = new MutableLiveData<>();
        mRestaurantList.postValue(restaurantList);
        assertEquals(restaurantList, mRestaurantList.getValue());
    }
    @org.junit.Test
    public void getRestaurantGoodResponse() {
        MutableLiveData<Restaurant> mRestaurantAutoMutableLiveData = new MutableLiveData<>();
        mRestaurantAutoMutableLiveData.postValue(mRestaurant);
        assertEquals(mRestaurant,mRestaurantAutoMutableLiveData.getValue());

    }
}
