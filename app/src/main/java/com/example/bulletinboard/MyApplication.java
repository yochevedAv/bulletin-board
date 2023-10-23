package com.example.bulletinboard;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiClient.getInstance(); // Initialize the API client
    }
}
