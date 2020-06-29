package com.e.go4lunch.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.e.go4lunch.models.myPlace.Geometry;
import com.e.go4lunch.models.myPlace.Location;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.myPlace.Result;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Objects;

public class Restaurant {

    private String placeId;
    private String name;
    private String address;
    private String urlPhoto;
    private String phoneNumber;
    private String WebSite;
    private Boolean openNow;
    private Location mLocation;
    private double rating;
    private List<Workmates> mWorkmatesList;


     public Restaurant(){

     }
    // Constructor for my place
    public Restaurant(String placeId, String name, String address, String urlPhoto, Boolean openNow, Location location, double rating) {
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.urlPhoto = urlPhoto;
        this.openNow = openNow;
        mLocation = location;
        this.rating = rating;
    }
    // Constructor for FireBase
    public Restaurant(String placeId, String name, String address, String urlPhoto,Boolean openNow,Location location, double rating, List<Workmates> workmatesList) {
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.urlPhoto = urlPhoto;
        this.openNow = openNow;
        mLocation = location;
        this.rating = rating;
        this.mWorkmatesList = workmatesList;

    }


    // ----------------- Getters -----------------

    public String getPlaceId() {
        return placeId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getWebSite() {
        return WebSite;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public Location getLocation() {
        return mLocation;
    }

    public double getRating() {
        return rating;
    }

    public List<Workmates> getWorkmatesList() {
        return mWorkmatesList;
    }

    // ----------------- Setters -----------------

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setWebSite(String webSite) {
        WebSite = webSite;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setWorkmatesList(List<Workmates> workmatesList) {
        mWorkmatesList = workmatesList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant restaurant = (Restaurant) o;
        return Objects.equals(placeId, restaurant.getPlaceId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPlaceId());
    }
}