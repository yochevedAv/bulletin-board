package com.example.bulletinboard;

import android.app.Application;
import android.content.Intent;

import com.example.bulletinboard.data.model.User;
import com.example.bulletinboard.server.ApiClient;
import com.example.bulletinboard.ui.login.LoginActivity;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiClient.getInstance(); // Initialize the API client

        User user = SharedPreferencesManager.getUser(getApplicationContext());
        if (user != null) {
            openMainActivity();
        } else {
            openLoginActivity();
        }
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
