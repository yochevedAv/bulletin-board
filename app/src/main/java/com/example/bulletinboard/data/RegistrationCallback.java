package com.example.bulletinboard.data;

import com.example.bulletinboard.data.model.User;

public interface RegistrationCallback {
    void onRegistrationSuccess(User User);
    void onRegistrationFailure(Throwable t, String message);
}

