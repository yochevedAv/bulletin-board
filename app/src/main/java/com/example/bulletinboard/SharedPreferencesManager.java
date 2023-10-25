package com.example.bulletinboard;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bulletinboard.data.model.User;
import com.google.gson.Gson;

public class SharedPreferencesManager {
    private static final String USER_PREFS = "UserPrefs";
    private static final String USER_KEY = "user";

    public static void saveUser(Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString(USER_KEY, userJson);
        editor.apply();
    }

    public static User getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(USER_KEY, null);
        if (userJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(userJson, User.class);
        }
        return null;
    }

    public static void clearUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_KEY);
        editor.apply();
    }
}
