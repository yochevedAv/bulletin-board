package com.example.bulletinboard.data.model;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Post implements Serializable {

    @Nullable
    private String id;
    private String title;
    private String creatorName;
    private String userId;
    private String date;
    private String location;
    private String description;

    public Post(String userId,String title, String creatorName, String date, String location, String description, @Nullable String id ) {
        this.userId = userId;
        this.title = title;
        this.creatorName = creatorName;
        this.date = date;
        this.location = location;
        this.description = description;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }
}
