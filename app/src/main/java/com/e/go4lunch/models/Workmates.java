package com.e.go4lunch.models;

import javax.annotation.Nullable;

public class Workmates {

    private String uid;
    private String workmateName;
    private String workmateEmail;
    @Nullable
    private String urlPicture;

    public Workmates(){

    }
    public Workmates(String uid, String workmateName, String workmateEmail, @Nullable String urlPicture) {
        this.uid = uid;
        this.workmateName = workmateName;
        this.workmateEmail = workmateEmail;
        this.urlPicture = urlPicture;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWorkmateName() {
        return workmateName;
    }

    public void setWorkmateName(String morkmateName) {
        this.workmateName = morkmateName;
    }

    public String getWorkmateEmail() {
        return workmateEmail;
    }

    public void setWorkmateEmail(String workmateEmail) {
        this.workmateEmail = workmateEmail;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }




}
