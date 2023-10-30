package com.example.bulletinboard.data.model;

import androidx.annotation.Nullable;

public class User {

    @Nullable
    private String id;
    private String email;
    @Nullable
    private String username;
    private String password;

    public User(String email, @Nullable String username, String password,@Nullable String id) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String _id) {
        this.id = _id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
