package com.example.bulletinboard.ui;

import androidx.annotation.Nullable;

import com.example.bulletinboard.data.model.Post;
import com.example.bulletinboard.data.model.User;

import java.util.List;

public class BulletinBoardPostsResult {


    @Nullable
    private List<Post> success;
    @Nullable
    private Integer error;

    @Nullable
    private String message;

    public BulletinBoardPostsResult(@Nullable Integer error, @Nullable String message) {
        this.error = error;
        this.message = message;
    }


    public BulletinBoardPostsResult(@Nullable List<Post> success) {
        this.success = success;
    }

    @Nullable
    List<Post> getSuccess() {
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

