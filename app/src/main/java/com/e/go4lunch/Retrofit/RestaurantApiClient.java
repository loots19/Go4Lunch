package com.e.go4lunch.Retrofit;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.e.go4lunch.AppExecutors;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.myPlace.Result;
import com.e.go4lunch.models.placedetail.PlaceDetail;
import com.e.go4lunch.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.e.go4lunch.util.Constants.NETWORK_TIMEOUT;

public class RestaurantApiClient {

    private static final String TAG = "RestaurantApiClient";

    private MutableLiveData<List<Result>> mResults;
    private static RestaurantApiClient instance;
    private RetrieveRestaurantRunnable mRetrieveRestaurantRunnable;


    public static RestaurantApiClient getInstance() {
        if (instance == null) {
            instance = new RestaurantApiClient();
        }
        return instance;
    }

    private RestaurantApiClient() {
        mResults = new MutableLiveData<>();

    }

    public LiveData<List<Result>> getResults() {
        return mResults;
    }

    public void searchRestaurantApi(String type,String location,int radius) {
        if (mRetrieveRestaurantRunnable != null) {
            mRetrieveRestaurantRunnable = null;
        }
        mRetrieveRestaurantRunnable = new RetrieveRestaurantRunnable(type,location,radius);
        final Future handler = AppExecutors.get().networkIO().submit(mRetrieveRestaurantRunnable);

        AppExecutors.get().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // let user know its timed out
                handler.cancel(true);

            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private class RetrieveRestaurantRunnable implements Runnable {

        private String type;
        private String location;
        int radius;
        boolean cancelRequest;

        public RetrieveRestaurantRunnable(String type, String location, int radius) {
            this.type = type;
            this.location = location;
            this.radius = radius;
            cancelRequest = false;
        }


        @Override
        public void run() {
            try {
                Response response = getRestaurant(type,location,radius).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    List<Result> list = new ArrayList<>(((MyPlace)response.body()).getResults());
                    mResults.postValue(list);

                    List<Result> currentRestaurant = mResults.getValue();
                    currentRestaurant.addAll(list);
                    mResults.postValue(currentRestaurant);
                } else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: error: " + error);
                    mResults.postValue(null);
                }


            } catch (IOException e) {
                e.printStackTrace();
                mResults.postValue(null);
            }

        }

        private Call<MyPlace> getRestaurant(String type,String location,int radius) {
            return RetrofitRequest.getApiRequest().getNearbyPlaces(type,location,radius);
        }

        private void cancelRequest() {
            cancelRequest = true;
        }
    }
}
