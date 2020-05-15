package com.e.go4lunch.models;

import javax.annotation.Nullable;

public class Workmates {


    private String workmateName;
    private String workmateEmail;
    private boolean restaurantSelected;
    private String urlPicture;

    public Workmates(){

    }


    public Workmates(  String workmateEmail,String workmateName, String urlPicture) {
        this.workmateEmail = workmateEmail;
        this.workmateName = workmateName;
        this.restaurantSelected = false;
        this.urlPicture = urlPicture;
    }

    public Workmates(String workmateName, String workmateEmail) {
        this.workmateName = workmateName;
        this.workmateEmail = workmateEmail;
        this.restaurantSelected = false;
    }
    //---------Getters---------

    public String getWorkmateName() {
        return workmateName;
    }

    public String getWorkmateEmail() {
        return workmateEmail;
    }

    public boolean isRestaurantSelected() {
        return restaurantSelected;
    }

    public String getUrlPicture() {
        return urlPicture;
    }
    //---------Setters---------

    public void setWorkmateName(String workmateName) {
        this.workmateName = workmateName;
    }

    public void setWorkmateEmail(String workmateEmail) {
        this.workmateEmail = workmateEmail;
    }

    public void setRestaurantSelected(boolean restaurantSelected) {
        this.restaurantSelected = restaurantSelected;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }
}
