package com.e.go4lunch.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class Workmates {


    private String workmateName;
    private String workmateEmail;
    private boolean restaurantLiked;
    private String urlPicture;
    private List<Restaurant> restaurantListFav;


    public Workmates(){

    }


    public Workmates(  String workmateEmail,String workmateName, String urlPicture) {
        this.workmateEmail = workmateEmail;
        this.workmateName = workmateName;
        this.restaurantLiked = false;
        this.urlPicture = urlPicture;
        this.restaurantListFav = new ArrayList<>();
    }


    //---------Getters---------

    public String getWorkmateName() {
        return workmateName;
    }

    public String getWorkmateEmail() {
        return workmateEmail;
    }

    public boolean isRestaurantSelected() {
        return restaurantLiked;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public boolean isRestaurantLiked() {
        return restaurantLiked;
    }

    public List<Restaurant> getRestaurantListFav() {
        return restaurantListFav;
    }

    //---------Setters---------

    public void setWorkmateName(String workmateName) {
        this.workmateName = workmateName;
    }

    public void setWorkmateEmail(String workmateEmail) {
        this.workmateEmail = workmateEmail;
    }

    public void setRestaurantSelected(boolean restaurantSelected) {
        this.restaurantLiked = restaurantSelected;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setRestaurantLiked(boolean restaurantLiked) {
        this.restaurantLiked = restaurantLiked;
    }

    public void setRestaurantListFav(List<Restaurant> restaurantListFav) {
        this.restaurantListFav = restaurantListFav;
    }
}
