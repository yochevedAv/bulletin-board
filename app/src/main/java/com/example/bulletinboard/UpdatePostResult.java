package com.example.bulletinboard;

import androidx.annotation.Nullable;

import com.example.bulletinboard.data.model.Post;

import java.util.List;

public class UpdatePostResult {

    @Nullable
    private Post success;
    @Nullable
    private Integer error;

    @Nullable
    private String message;

    public UpdatePostResult(@Nullable Integer error, @Nullable String message) {
        this.error = error;
        this.message = message;
    }


    public UpdatePostResult(@Nullable Post success) {
        this.success = success;
    }

    @Nullable
    public Post getSuccess() {
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

