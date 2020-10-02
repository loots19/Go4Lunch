package com.e.go4lunch.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.e.go4lunch.Retrofit.ApiRequest;
import com.e.go4lunch.Retrofit.RetrofitRequest;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.placeDetail.Location;
import com.e.go4lunch.models.placeDetail.Period;
import com.e.go4lunch.models.placeDetail.PlaceDetail;
import com.e.go4lunch.util.Event;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {

    private static RestaurantRepository instance;
    private List<Workmates> mWorkmatesList = new ArrayList<>();
    private CollectionReference mCollectionReference;
    private com.e.go4lunch.models.placeDetail.Location mLocation;
    private String address, placeId, webSite, phoneNumber, placeId1, urlPhoto, name;
    private double rating;
    private List<Period> openHours;

    // --------------------------------
    // ----- COLLECTION REFERENCE -----
    // --------------------------------

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
        mCollectionReference = FirebaseFirestore.getInstance().collection("restaurant");

    }
    // ---------------------------------
    // ----- GET PLACE FROM NEARBY -----
    // ---------------------------------
    // check if restaurant list is not empty, else get nearby result and combine this with place detail.
    public MutableLiveData<List<Restaurant>> getRestaurantList(String type, String location, int radius) {
        MutableLiveData<List<Restaurant>> newData = new MutableLiveData<>();
        mCollectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (queryDocumentSnapshots != null) {
                List<DocumentSnapshot> restaurantList = queryDocumentSnapshots.getDocuments();
                List<Restaurant> restaurants = new ArrayList<>();
                int size = restaurantList.size();
                for (int i = 0; i < size; i++) {
                    Restaurant restaurant = restaurantList.get(i).toObject(Restaurant.class);
                    restaurants.add(restaurant);

                }
                if (!restaurants.isEmpty()) {
                    newData.setValue(restaurants);
                } else {
                    mApiRequest.getNearbyPlaces(type, location, radius).enqueue(new Callback<MyPlace>() {
                        @Override
                        public void onResponse(@NotNull Call<MyPlace> call, @NotNull Response<MyPlace> response) {
                            if (response.body() != null) {
                                int size = response.body().getResults().size();
                                List<Restaurant> restaurants = new ArrayList<>();
                                for (int i = 0; i < size; i++) {
                                    placeId = response.body().getResults().get(i).getPlaceId();

                                    getRestaurantDetail(placeId, newData, restaurants, size);

                                }

                            }

                        }

                        @Override
                        public void onFailure(@NotNull Call<MyPlace> call, @NotNull Throwable t) {
                            newData.setValue(null);

                        }
                    });


                }

            }
        });

        return newData;
    }
    // ----------------------------
    // ----- GET PLACE DETAIL -----
    // ----------------------------
    private void getRestaurantDetail(String placeId, MutableLiveData<List<Restaurant>> newData, List<Restaurant> restaurants, int size) {
        mApiRequest.getDetailsRestaurant(placeId).enqueue(new Callback<PlaceDetail>() {
            @Override
            public void onResponse(@NotNull Call<PlaceDetail> call, @NotNull Response<PlaceDetail> response) {
                if (response.body() != null) {
                    webSite = response.body().getResult().getWebsite();
                    placeId1 = response.body().getResult().getPlaceId();
                    phoneNumber = response.body().getResult().getInternationalPhoneNumber();
                    mLocation = response.body().getResult().getGeometry().getLocation();
                    name = response.body().getResult().getName();
                    address = response.body().getResult().getVicinity();
                    rating = response.body().getResult().getRating();

                    if (response.body().getResult().getPhotos() != null) {
                        urlPhoto = String.valueOf(response.body().getResult().getPhotos().get(0).getPhotoReference());
                    }
                    if (response.body().getResult().getOpeningHours() != null) {
                        openHours = response.body().getResult().getOpeningHours().getPeriods();
                    }
                    Restaurant restaurant = new Restaurant(placeId1, name, address, urlPhoto, openHours, mLocation, rating, webSite, phoneNumber, mWorkmatesList);
                    restaurants.add(restaurant);

                    Log.e("restaurants", restaurants.size() + "," + size);
                    if (restaurants.size() == size) {
                        newData.setValue(restaurants);
                    }
                    // create restaurant in firesTore
                    createRestaurant(placeId1, name, address, urlPhoto, openHours, mLocation, rating, webSite, phoneNumber, mWorkmatesList);
                }
            }

            @Override
            public void onFailure(@NotNull Call<PlaceDetail> call, @NotNull Throwable t) {
                newData.setValue(null);

            }
        });
    }

    // ------------------
    // ----- CREATE -----
    // ------------------
    private void createRestaurant(String placeId, String name, String address, String urlPhoto, List<Period> openHours, Location location, double rating, String webSite, String phoneNumber, List<Workmates> workmatesList) {
        Restaurant restaurantToCreate = new Restaurant(placeId, name, address, urlPhoto, openHours, location, rating, webSite, phoneNumber, workmatesList);
        getRestaurantCollection().document(placeId).set(restaurantToCreate);
    }

    // ---------------
    // ----- GET -----
    // ---------------

    public Task<DocumentSnapshot> getRestaurant1(String placeId) {
        return getRestaurantCollection().document(placeId).get();
    }

    // this method cause event can not be null, so for result of autocomplete i need this one.
    public MutableLiveData<Restaurant> getRestaurantAuto(String placeId) {
        MutableLiveData<Restaurant> mutableLiveData = new MutableLiveData<>();
        mCollectionReference.document(placeId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);
                mutableLiveData.postValue(restaurant);

            } else {
                mutableLiveData.postValue(null);


            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<Event<Restaurant>> getRestaurant(String placeId) {
        MutableLiveData<Event<Restaurant>> mutableLiveData = new MutableLiveData<>();
        mCollectionReference.document(placeId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);
                mutableLiveData.postValue(new Event<>(restaurant));

            }
        });
        return mutableLiveData;
    }

    // ------------------
    // ----- UPDATE -----
    // ------------------

    public void updateRestaurantWorkmateList(String placeId, List<Workmates> workmatesList) {
        getRestaurantCollection().document(placeId).update("workmatesList", workmatesList);
    }

    public void setRestaurantSelected(String restaurantUid) {
    }


}


















