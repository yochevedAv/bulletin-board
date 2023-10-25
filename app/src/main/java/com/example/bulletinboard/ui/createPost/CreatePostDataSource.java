package com.example.bulletinboard.ui.createPost;

import com.example.bulletinboard.ApiClient;
import com.example.bulletinboard.BulletinBoardService;
import com.example.bulletinboard.data.ResponseCallback;
import com.example.bulletinboard.data.model.ErrorResponse;
import com.example.bulletinboard.data.model.Post;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostDataSource {
    ApiClient apiClient = ApiClient.getInstance();
    BulletinBoardService bulletinBoardService = apiClient.getBulletinBoardService();

    public void createPost(String userId, String title, String creatorName, String date,String location, String description, ResponseCallback<Post> responseCallback) {
        Call<Post> call = bulletinBoardService.CreatePost (new Post(userId,title,creatorName,date,location,description));
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (response.isSuccessful()) {
                    // Handle a successful response here
                    Post post = response.body();
                    //User User = new User(user.getEmail(),user.getUserName(), user.getPassword(),"");
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


                    responseCallback.onResponseFailure(new Exception("Login failed: "), errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                responseCallback.onResponseFailure(t, "failed");
            }
        });
    }
}
