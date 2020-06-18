package com.e.go4lunch.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.e.go4lunch.Retrofit.ApiRequest;
import com.e.go4lunch.Retrofit.RetrofitRequest;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.models.myPlace.Location;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.placeDetail.PlaceDetail;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {


    private static RestaurantRepository instance;

    // --- COLLECTION REFERENCE ---

    public CollectionReference getRestaurantCollection() {
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
            public void onResponse(Call<MyPlace> call, Response<MyPlace> response) {
                if (response.isSuccessful()) {
                    newData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MyPlace> call, Throwable t) {

                newData.setValue(null);

            }
        });
        return newData;
    }

    public MutableLiveData<PlaceDetail> getRestaurantDetail(String placeId) {
        MutableLiveData<PlaceDetail> newData = new MutableLiveData<>();
        mApiRequest.getDetailsRestaurant(placeId).enqueue(new Callback<PlaceDetail>() {
            @Override
            public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                if (response.isSuccessful()) {
                    newData.setValue(response.body());

                }
            }

            @Override
            public void onFailure(Call<PlaceDetail> call, Throwable t) {

                newData.setValue(null);

            }
        });
        return newData;
    }
    // --- CREATE ---

    public Task<Void> createRestaurant(String placeId, String name, String address, String urlPhoto, List<Workmates>workmatesList){
        Restaurant restaurantToCreate = new Restaurant(placeId,name,address,urlPhoto,workmatesList);
        return getRestaurantCollection().document(placeId).set(restaurantToCreate);
    }

    // --- GET ---

    public Task<DocumentSnapshot> getRestaurant(String placeId) {
        return getRestaurantCollection().document(placeId).get();
    }

    public Query getRestaurantList() {
        return getRestaurantCollection().orderBy("name");
    }

    // --- UPDATE ---

    public Task<Void> updateRestaurantWorkmateList(String placeId,List<Workmates> workmatesList){
        return  getRestaurantCollection().document(placeId).update("workmatesList",workmatesList);
    }














}










