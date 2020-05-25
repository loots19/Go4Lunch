package com.e.go4lunch.injection;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import static com.e.go4lunch.restaurant.MapsFragment.MY_PREF;

public class Globals extends Application {

    private String lat;
    private String lng;


    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences mSharedPreferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        lat = mSharedPreferences.getString("LAT", "");
        lng = mSharedPreferences.getString("LNG", "");


    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }


}

