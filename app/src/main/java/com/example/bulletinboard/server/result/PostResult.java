package com.example.bulletinboard.server.result;

import androidx.annotation.Nullable;

import com.example.bulletinboard.data.model.Post;

import java.util.List;

public class PostResult {
    @Nullable
    private List<Post> success;
    @Nullable
    private Integer error;

    @Nullable
    private String message;

    public PostResult(@Nullable Integer error, @Nullable String message) {
        this.error = error;
        this.message = message;
    }


    public PostResult(@Nullable List<Post> success) {
        this.success = success;
    }

    @Nullable
    public List<Post> getSuccess() {
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

