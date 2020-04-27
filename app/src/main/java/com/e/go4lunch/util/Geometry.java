package com.e.go4lunch.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.e.go4lunch.models.myPlace.Result;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Geometry {

    private static final Geometry ourInstance = new Geometry();

    public static Geometry getInstance() {
        return ourInstance;
    }

    private Geometry() {
    }

    private double lat =0;
    private double lng =0;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public LatLng getLocation(){
        return new LatLng(lat, lng);
    }

    public String toString(){
        String location;
        return location = lat + "," + lng;
    }
}
