package com.example.first_firebase.Models;

import com.google.firebase.database.ServerValue;

public class Post {


    private String Title;
    private String postKey ;
    private String description;
    private String picture;
    private String userId;
    private Object timeStamp;

    public Post(String title, String description, String picture, String userId ) {

        Title = title;
        this.description = description;
        this.picture = picture;
        this.userId = userId;
        this.timeStamp = ServerValue.TIMESTAMP;
    }
    public  Post(){

    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getPostKey() {
        return postKey;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
