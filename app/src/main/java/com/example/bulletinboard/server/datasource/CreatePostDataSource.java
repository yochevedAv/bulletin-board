package com.example.bulletinboard.server.datasource;

import com.example.bulletinboard.server.ApiClient;
import com.example.bulletinboard.server.BulletinBoardService;
import com.example.bulletinboard.R;
import com.example.bulletinboard.server.callback.ResponseCallback;
import com.example.bulletinboard.server.ErrorResponse;
import com.example.bulletinboard.data.model.Post;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostDataSource {
    ApiClient apiClient = ApiClient.getInstance();
    BulletinBoardService bulletinBoardService = apiClient.getBulletinBoardService();

    public void createPost(Post post, ResponseCallback<Post> responseCallback) {
        Call<Post> call = bulletinBoardService.CreatePost (post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (response.isSuccessful()) {

                    Post post = response.body();
                    responseCallback.onResponseSuccess(post);
                } else {
                    String errorBodyString = "";

                    if (response.errorBody() != null) {
                        try {
                            errorBodyString = response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    ErrorResponse errorData = new Gson().fromJson(errorBodyString, ErrorResponse.class);
                    String errorMessage = errorData.getError();
                    responseCallback.onResponseFailure(new Exception(String.valueOf(R.string.save_post_failed)), errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                responseCallback.onResponseFailure(t, "failed");
            }
        });
    }

    public void updatePost(Post updatedPost, ResponseCallback<Post> responseCallback) {
        Call<Post> call = bulletinBoardService.UpdatePost(updatedPost);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Post updated = response.body();
                    responseCallback.onResponseSuccess(updated);
                } else {

                    String errorBodyString = "";

                    if (response.errorBody() != null) {
                        try {
                            errorBodyString = response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    ErrorResponse errorData = new Gson().fromJson(errorBodyString, ErrorResponse.class);

                    String errorMessage = errorData.getError();

                    responseCallback.onResponseFailure(new Exception(String.valueOf(R.string.update_post_failed)), errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                responseCallback.onResponseFailure(new Exception(String.valueOf(R.string.update_post_failed)), t.getMessage());
            }
        });
    }
}
