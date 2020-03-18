package com.e.go4lunch.models;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class Restaurant {
    private String name ;
    private String distance;
    private String photurl;
    private String adress;
    private String phone_no;
    private String time;
    private String id;
    private Uri websiteUri;
    private LatLng latlng;

    public Restaurant(String name, String distance, String photurl, String adress, String phone_no, String time, String id, Uri websiteUri, LatLng latlng) {
        this.name = name;
        this.distance = distance;
        this.photurl = photurl;
        this.adress = adress;
        this.phone_no = phone_no;
        this.time = time;
        this.id = id;
        this.websiteUri = websiteUri;
        this.latlng = latlng;
    }

    public String getName() {
        return name;
    }

    public String getDistance() {
        return distance;
    }

    public String getPhoturl() {
        return photurl;
    }

    public String getAdress() {
        return adress;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getTime() {
        return time;
    }

    public String getId() {
        return id;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setPhoturl(String photurl) {
        this.photurl = photurl;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }
}
