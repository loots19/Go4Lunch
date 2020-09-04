package com.e.go4lunch.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.e.go4lunch.Retrofit.ApiRequest;
import com.e.go4lunch.Retrofit.RetrofitRequest;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.models.myPlace.Location;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.myPlace.Result;
import com.e.go4lunch.models.placeDetail.PlaceDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {


    private static RestaurantRepository instance;
    private ArrayList<Restaurant> mRestaurants = new ArrayList<>();
    private String restaurantSelected;
    private List<Workmates> mWorkmatesList = new ArrayList<>();
    private Location mLocation;


    // ----------------------------
    // --- COLLECTION REFERENCE ---
    // ----------------------------

    private CollectionReference getRestaurantCollection() {
        return FirebaseFirestore.getInstance().collection("restaurant");
    }



    public static RestaurantRepository getInstance() {
        if (instance == null) {
            instance = new RestaurantRepository();
        }
        return instance;
    }

    private ApiRequest mApiRequest;

    public RestaurantRepository() {
        mApiRequest = RetrofitRequest.getApiRequest();
    }

    public MutableLiveData<MyPlace> getNearbyPlace(String type, String location, int radius) {
        MutableLiveData<MyPlace> newData = new MutableLiveData<>();
        mApiRequest.getNearbyPlaces(type, location, radius).enqueue(new Callback<MyPlace>() {
            @Override
            public void onResponse(@NotNull Call<MyPlace> call, Response<MyPlace> response) {
                if (response.isSuccessful()) {
                    newData.setValue(response.body());
                    assert response.body() != null;
                    List<Result> results = response.body().getResults();
                    int size = results.size();
                    for (int i = 0; i < size; i++) {
                        String placeId = response.body().getResults().get(i).getPlaceId();
                        String name = response.body().getResults().get(i).getName();
                        String address = response.body().getResults().get(i).getVicinity();
                        String urlPhoto = String.valueOf(response.body().getResults().get(i).getPhotos());
                        Boolean openNow = (results.get(i).getOpeningHours() != null ? results.get(i).getOpeningHours().getOpenNow() : false);
                        double rating = response.body().getResults().get(i).getRating();
                        //createRestaurant(placeId, name, address, urlPhoto, openNow, mLocation, rating, mWorkmatesList);

                    }

                }

            }


            @Override
            public void onFailure(@NotNull Call<MyPlace> call, Throwable t) {

                newData.setValue(null);

            }
        });
        return newData;
    }

    public MutableLiveData<PlaceDetail> getRestaurantDetail(String placeId) {
        MutableLiveData<PlaceDetail> newData = new MutableLiveData<>();
        mApiRequest.getDetailsRestaurant(placeId).enqueue(new Callback<PlaceDetail>() {
            @Override
            public void onResponse(@NotNull Call<PlaceDetail> call, Response<PlaceDetail> response) {
                if (response.isSuccessful()) {
                    newData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NotNull Call<PlaceDetail> call, Throwable t) {

                newData.setValue(null);

            }
        });
        return newData;
    }

    // --------------
    // --- CREATE ---
    // --------------

    public void createRestaurant(String placeId, String name, String address, String urlPhoto, Boolean openNow, Location location, double rating, List<Workmates> workmatesList) {
        Restaurant restaurantToCreate = new Restaurant(placeId, name, address, urlPhoto, openNow, location, rating, workmatesList);
        getRestaurantCollection().document(placeId).set(restaurantToCreate);
    }


    // -----------
    // --- GET ---
    // -----------

    public Task<DocumentSnapshot> getRestaurant(String placeId) {
        return getRestaurantCollection().document(placeId).get();
    }


    public Query getRestaurantList() {
        return getRestaurantCollection().orderBy("name");
    }


    // --------------
    // --- UPDATE ---
    // --------------

    public void updateRestaurantWorkmateList(String placeId, List<Workmates> workmatesList) {
        getRestaurantCollection().document(placeId).update("workmatesList", workmatesList);
    }

    public void setRestaurantSelected(String restaurantUid) {
        this.restaurantSelected = restaurantUid;
    }



}















