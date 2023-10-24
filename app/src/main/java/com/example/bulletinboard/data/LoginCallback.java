package com.example.bulletinboard.data;

import com.example.bulletinboard.data.model.User;

public interface LoginCallback {
    void onLoginSuccess(User User);
    void onLoginFailure(Throwable t, String message);
}
