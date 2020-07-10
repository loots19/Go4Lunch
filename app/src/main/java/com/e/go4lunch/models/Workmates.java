package com.e.go4lunch.models;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Workmates {


    private String workmateName;
    private String workmateEmail;
    private String urlPicture;
    private List<Restaurant> listRestaurantFavorite;
    private Restaurant restaurantChosen;


    public Workmates() {

    }


    public Workmates(String workmateEmail, String workmateName, String urlPicture) {
        this.workmateEmail = workmateEmail;
        this.workmateName = workmateName;
        this.urlPicture = urlPicture;
        this.listRestaurantFavorite = new ArrayList<>();

    }


    // ----------------- Getters -----------------

    public String getWorkmateName() {
        return workmateName;
    }

    public String getWorkmateEmail() {
        return workmateEmail;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public List<Restaurant> getListRestaurantFavorite() {
        return listRestaurantFavorite;
    }

    public Restaurant getRestaurantChosen() {
        return restaurantChosen;
    }

    // ----------------- Setters -----------------

    public void setWorkmateName(String workmateName) {
        this.workmateName = workmateName;
    }

    public void setWorkmateEmail(String workmateEmail) {
        this.workmateEmail = workmateEmail;
    }

    public void setListRestaurantFavorite(List<Restaurant> listRestaurantFavorite) {
        this.listRestaurantFavorite = listRestaurantFavorite;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setRestaurantChosen(Restaurant restaurantChoosen) {
        this.restaurantChosen = restaurantChoosen;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workmates workmates = (Workmates) o;
        return Objects.equals(workmateEmail, workmates.getWorkmateEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getWorkmateEmail());
    }
}