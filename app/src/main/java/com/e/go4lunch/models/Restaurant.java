package com.e.go4lunch.models;

import com.e.go4lunch.models.placeDetail.Location;
import com.e.go4lunch.models.placeDetail.Period;

import java.util.List;
import java.util.Objects;

public class Restaurant {

    private String placeId;
    private String name;
    private String address;
    private String urlPhoto;
    private String phoneNumber;
    private String webSite;
    private com.e.go4lunch.models.placeDetail.Location mLocation;
    private double rating;
    private List<Workmates> mWorkmatesList;
    private List<Period> openHours;


    public Restaurant() {

    }

    // Constructor for my place
    public Restaurant(String placeId, String name, String address, String urlPhoto, List<Period> openHours, com.e.go4lunch.models.placeDetail.Location location, double rating) {
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.urlPhoto = urlPhoto;
        this.openHours = openHours;
        mLocation = location;
        this.rating = rating;
    }

    // Constructor for FireBase
    public Restaurant(String placeId, String name, String address, String urlPhoto, List<Period> openHours, com.e.go4lunch.models.placeDetail.Location location, double rating, String webSite, String phoneNumber, List<Workmates> workmatesList) {
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.urlPhoto = urlPhoto;
        this.openHours = openHours;
        mLocation = location;
        this.rating = rating;
        this.webSite = webSite;
        this.phoneNumber = phoneNumber;
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
        return webSite;
    }

    public List<Period> getOpenHours() {
        return openHours;
    }

    public com.e.go4lunch.models.placeDetail.Location getLocation() {
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
        webSite = webSite;
    }

    public void setOpenHours(List<Period> openHours) {
        this.openHours = openHours;
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