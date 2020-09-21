package com.e.go4lunch.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.e.go4lunch.Retrofit.ApiRequest;
import com.e.go4lunch.Retrofit.RetrofitRequest;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.placeDetail.Location;
import com.e.go4lunch.models.placeDetail.OpeningHours;
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
    private String restaurantSelected;
    private List<Workmates> mWorkmatesList = new ArrayList<>();
    private List<Restaurant> mRestaurants = new ArrayList<>();;
    private CollectionReference mCollectionReference;
    private com.e.go4lunch.models.placeDetail.Location mLocation;
    private String address, placeId, webSite, phoneNumber, placeId1, urlPhoto, name;
    private Boolean restaurantsExists = false;
    private boolean openNow;
    private double rating;
    private List<String> openHours;

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
        mCollectionReference = FirebaseFirestore.getInstance().collection("restaurant");

    }
    // -----------------------------
    // --- GET PLACE FROM NEARBY ---
    // -----------------------------

    public MutableLiveData<MyPlace> getNearbyPlace(String type, String location, int radius) {
        MutableLiveData<MyPlace> newData = new MutableLiveData<>();
        mApiRequest.getNearbyPlaces(type, location, radius).enqueue(new Callback<MyPlace>() {
            @Override
            public void onResponse(@NotNull Call<MyPlace> call, Response<MyPlace> response) {
                if (response.body() != null) {
                    int size = response.body().getResults().size();
                    for (int i = 0; i < size; i++) {
                        placeId = response.body().getResults().get(i).getPlaceId();

                        getRestaurantDetail(placeId);
                    }
                }
                newData.setValue(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<MyPlace> call, Throwable t) {
                newData.setValue(null);

            }
        });
        return newData;
    }
    // -----------------------------
    // --- GET PLACE DETAIL ---
    // -----------------------------

    public MutableLiveData<PlaceDetail> getRestaurantDetail(String placeId) {
        MutableLiveData<PlaceDetail> newData = new MutableLiveData<>();
        mApiRequest.getDetailsRestaurant(placeId).enqueue(new Callback<PlaceDetail>() {
            @Override
            public void onResponse(@NotNull Call<PlaceDetail> call, Response<PlaceDetail> response) {
                if (response.body() != null) {

                    placeId1 = response.body().getResult().getPlaceId();
                    webSite = response.body().getResult().getWebsite();
                    phoneNumber = response.body().getResult().getInternationalPhoneNumber();
                    name = response.body().getResult().getName();
                    address = response.body().getResult().getVicinity();
                    if (response.body().getResult().getPhotos() != null) {
                        urlPhoto = String.valueOf(response.body().getResult().getPhotos().get(0).getPhotoReference());
                    }
                    rating = response.body().getResult().getRating();
                    if (response.body().getResult().getOpeningHours() != null) {
                        openHours = response.body().getResult().getOpeningHours().getWeekdayText();
                    }
                    if (response.body().getResult().getOpeningHours() != null) {
                        openNow = response.body().getResult().getOpeningHours().getOpenNow();
                        mLocation = response.body().getResult().getGeometry().getLocation();

                        Restaurant restaurant = new Restaurant(placeId1, name, address, urlPhoto, openHours, openNow, mLocation, rating, webSite, phoneNumber, mWorkmatesList);
                        mRestaurants.add(restaurant);


                    }
                }
                checkRestaurantExist();
                newData.setValue(response.body());

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

    public void createRestaurant(String placeId, String name, String address, String urlPhoto, List<String> openSchedule,boolean openNow, Location location, double rating, String webSite, String phoneNumber, List<Workmates> workmatesList) {
        Restaurant restaurantToCreate = new Restaurant(placeId, name, address, urlPhoto, openSchedule,openNow, location, rating, webSite, phoneNumber, workmatesList);
        getRestaurantCollection().document(placeId).set(restaurantToCreate);
    }


    // -----------
    // --- GET ---
    // -----------

    public Task<DocumentSnapshot> getRestaurant1(String placeId) {
        return getRestaurantCollection().document(placeId).get();
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


    public MutableLiveData<List<Restaurant>> getRestaurantList() {
        MutableLiveData<List<Restaurant>> mRestaurantList = new MutableLiveData<>();
        mCollectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (queryDocumentSnapshots != null) {
                List<DocumentSnapshot> restaurantList = queryDocumentSnapshots.getDocuments();
                List<Restaurant> restaurants = new ArrayList<>();
                int size = restaurantList.size();
                for (int i = 0; i < size; i++) {
                    Restaurant restaurant = restaurantList.get(i).toObject(Restaurant.class);
                    restaurants.add(restaurant);
                }

                mRestaurantList.setValue(restaurants);

            }
        });
        return mRestaurantList;

    }

    // ----------------- Check if restaurants exists -----------------
    private void checkRestaurantExist() {
        if (mRestaurants != null) {
            int size = mRestaurants.size();
            for (int i = 0; i < size; i++) {
                if (mRestaurants.get(i).getPlaceId().equals(placeId)) {
                    Log.e("testResult", mRestaurants.get(i).getPlaceId() + " " + placeId1);
                    restaurantsExists = true;
                    break;
                }
                if (!restaurantsExists) {
                    createRestaurant(placeId1, name, address, urlPhoto, openHours,openNow, mLocation, rating, webSite, phoneNumber, mWorkmatesList);

                }
            }
        }
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


















