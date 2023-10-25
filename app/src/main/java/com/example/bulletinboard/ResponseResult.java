package com.example.bulletinboard;

import androidx.annotation.Nullable;

import com.example.bulletinboard.data.model.User;

/**
 * Authentication result : success (user details) or error message.
 */
public class ResponseResult {
    @Nullable
    private User success;
    @Nullable
    private Integer error;

    @Nullable
    private String message;

    public ResponseResult(@Nullable Integer error, @Nullable String message) {
        this.error = error;
        this.message = message;
    }


    public ResponseResult(@Nullable User success) {
        this.success = success;
    }

    @Nullable
    public User getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }

    @Nullable
    public String getMessage() {
        return message;
    }
}