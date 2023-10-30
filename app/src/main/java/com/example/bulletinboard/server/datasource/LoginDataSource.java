package com.example.bulletinboard.server.datasource;

import com.example.bulletinboard.data.model.User;
import com.example.bulletinboard.server.ApiClient;
import com.example.bulletinboard.server.BulletinBoardService;
import com.example.bulletinboard.server.ErrorResponse;
import com.example.bulletinboard.server.callback.ResponseCallback;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginDataSource {

    ApiClient apiClient = ApiClient.getInstance();
    BulletinBoardService bulletinBoardService = apiClient.getBulletinBoardService();

    public void login(String email, String password, ResponseCallback responseCallback) {
        Call<User> call = bulletinBoardService.UserLogin(new User(email, "", password,""));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {
                    User user = response.body();
                    responseCallback.onResponseSuccess(user);
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
                    responseCallback.onResponseFailure(new Exception("Login failed: "),errorMessage);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                responseCallback.onResponseFailure(t, "failed");
            }
        });
    }

    public void logout() {
        // TODO: revoke authentication
    }
}