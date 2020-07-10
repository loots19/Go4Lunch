package com.e.go4lunch.injection;

import android.app.Application;
import android.content.SharedPreferences;

import static com.e.go4lunch.restaurant.MapsFragment.MY_PREF;

public class App extends Application {

    private static App instance;

    private String lat;
    private String lng;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SharedPreferences mSharedPreferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        lat = mSharedPreferences.getString("LAT", "");
        lng = mSharedPreferences.getString("LNG", "");

    }

    public static App getInstance() {
        return instance;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}

