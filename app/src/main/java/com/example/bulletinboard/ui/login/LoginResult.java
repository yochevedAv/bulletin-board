package com.example.bulletinboard.ui.login;

import androidx.annotation.Nullable;

import com.example.bulletinboard.data.model.User;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private User success;
    @Nullable
    private Integer error;

    @Nullable
    private String message;

    LoginResult(@Nullable Integer error, @Nullable String message) {
        this.error = error;
        this.message = message;
    }


    LoginResult(@Nullable User success) {
        this.success = success;
    }

    @Nullable
    User getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }

    @Nullable
    public String getMessage() {
        return message;
    }
}