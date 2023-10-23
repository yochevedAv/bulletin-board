package com.example.bulletinboard.data;

import com.example.bulletinboard.data.model.LoggedInUser;

public interface LoginCallback {
    void onLoginSuccess(LoggedInUser loggedInUser);
    void onLoginFailure(Throwable t);
}
