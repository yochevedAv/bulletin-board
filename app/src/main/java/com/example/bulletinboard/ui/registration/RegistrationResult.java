package com.example.bulletinboard.ui.registration;

import androidx.annotation.Nullable;

import com.example.bulletinboard.data.model.User;

public class RegistrationResult {

    @Nullable
    private User success;
    @Nullable
    private Integer error;

    @Nullable
    private String message;

    RegistrationResult(@Nullable Integer error, @Nullable String message) {
        this.error = error;
        this.message = message;
    }


    RegistrationResult(@Nullable User success) {
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
