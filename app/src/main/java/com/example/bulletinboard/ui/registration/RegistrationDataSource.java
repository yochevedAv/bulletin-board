package com.example.bulletinboard.ui.registration;


import com.example.bulletinboard.ApiClient;
import com.example.bulletinboard.BulletinBoardService;
import com.example.bulletinboard.data.ResponseCallback;
import com.example.bulletinboard.data.model.ErrorResponse;
import com.example.bulletinboard.data.model.User;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class RegistrationDataSource {

    ApiClient apiClient = ApiClient.getInstance();
    BulletinBoardService bulletinBoardService = apiClient.getBulletinBoardService();

    public void register(String email, String password,String username, ResponseCallback<User> responseCallback) {
        Call<User> call = bulletinBoardService.UserRegistration(new User(email,username, password,""));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {
                    // Handle a successful response here
                    User user = response.body();
                    User User = new User(user.getEmail(),user.getUserName(), user.getPassword(),"");
                    responseCallback.onResponseSuccess(User);
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
            public void onFailure(Call<User> call, Throwable t) {
                responseCallback.onResponseFailure(t, "failed");
            }
        });
    }



    public void logout() {
        // TODO: revoke authentication
    }
}
