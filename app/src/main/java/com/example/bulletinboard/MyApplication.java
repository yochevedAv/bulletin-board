package com.example.bulletinboard;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.bulletinboard.ui.login.LoginActivity;
import com.example.bulletinboard.ui.registration.RegistrationActivity;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiClient.getInstance(); // Initialize the API client

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        String email = sharedPreferences.getString("email", null);


        if (username != null && email != null) {
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
