package com.example.bulletinboard.data;

import com.example.bulletinboard.ApiClient;
import com.example.bulletinboard.BulletinBoardService;
import com.example.bulletinboard.data.model.LoggedInUser;
import com.example.bulletinboard.data.model.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    ApiClient apiClient = ApiClient.getInstance();
    BulletinBoardService bulletinBoardService = apiClient.getBulletinBoardService();


    public void login(String email, String password, LoginCallback loginCallback) {
        Call<User> call = bulletinBoardService.UserLogin(new LoggedInUser(email, password));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    LoggedInUser loggedInUser = new LoggedInUser(user.getEmail(), password);
                    loginCallback.onLoginSuccess(loggedInUser);
                } else {
                    loginCallback.onLoginFailure(new Exception("Login failed")); // You can pass an appropriate exception here
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loginCallback.onLoginFailure(t);
            }
        });
    }


    public void logout() {
        // TODO: revoke authentication
    }
}