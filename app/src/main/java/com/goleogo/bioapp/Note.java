package com.goleogo.bioapp;

/**
 * Created by leosss on 29/03/16.
 */
public class Note {
    private String title;
    private String description;
    private String filepathS3;
    private double latitude;
    private double longitude;
    private String id_user;
    private String type;
    private String user_firebase_key;

    public Note(){

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilepathS3() {
        return filepathS3;
    }

    public void setFilepathS3(String filepathS3) {
        this.filepathS3 = filepathS3;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double lat) {
        this.latitude = lat;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_firebase_key() {
        return user_firebase_key;
    }

    public void setUser_firebase_key(String user_firebase_key) {
        this.user_firebase_key = user_firebase_key;
    }
}
