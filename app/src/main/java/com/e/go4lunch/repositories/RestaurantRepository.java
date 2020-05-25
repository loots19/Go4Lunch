package com.e.go4lunch.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.e.go4lunch.Retrofit.ApiRequest;
import com.e.go4lunch.Retrofit.RetrofitRequest;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.placeDetail.PlaceDetail;
import com.e.go4lunch.models.placeDetail.ResultDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {


    private static RestaurantRepository instance;
    private List<Restaurant> mRestaurantList ;
    private MutableLiveData<Restaurant> mRestaurantMutableLiveData = new MutableLiveData<>();

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


    public Task<Void> createRestaurant (String placeId,String name,String address,List<Workmates> workmatesList){
        Restaurant restaurantToCreate = new Restaurant(placeId,name,address,workmatesList);
        return getRestaurantCollection().document(placeId).set(restaurantToCreate);
    }

    public Task<Void> updateRestaurantListFavorites (String uid, List<Restaurant> restaurantList){
        Log.e("work", "work2");
        return getRestaurantCollection().document(uid).update("workmatesList",restaurantList);
    }



}










