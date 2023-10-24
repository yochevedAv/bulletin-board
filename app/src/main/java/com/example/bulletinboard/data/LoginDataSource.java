package com.example.bulletinboard.data;

import com.example.bulletinboard.ApiClient;
import com.example.bulletinboard.BulletinBoardService;
import com.example.bulletinboard.data.model.ErrorResponse;
import com.example.bulletinboard.data.model.User;
import com.example.bulletinboard.data.model.User;
import com.google.gson.Gson;

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
        Call<User> call = bulletinBoardService.UserLogin(new User(email, "", password,""));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {
                    // Handle a successful response here
                    User user = response.body();
                    User User = new User(user.getEmail(),"", password,"");
                    loginCallback.onLoginSuccess(User);
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


                    loginCallback.onLoginFailure(new Exception("Login failed: "),errorMessage);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loginCallback.onLoginFailure(t, "failed");
            }
        });
    }


//    public void login(String email, String password, LoginCallback loginCallback) {
//        Call<User> call = bulletinBoardService.UserLogin(new User(email, password));
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//
//                if (response.isSuccessful()) {
//
//                    String responseBody = response.raw().toString();
//                    Log.d("Response", responseBody);
//                    User user = response.body();
//                    User User = new User(user.getEmail(), password);
//                    loginCallback.onLoginSuccess(User);
//                } else {
//
//                    if (response.errorBody() != null) {
//                        try {
//                            String errorBodyString = response.errorBody().string();
//
//                            // Create a Gson instance
//                            Gson gson = new Gson();
//
//                            // Parse the error response string into an ErrorData object
//                            ErrorData errorData = gson.fromJson(errorBodyString, ErrorData.class);
//
//                            // Now you can access the error data
//                            String errorMessage = errorData.getError();
//
//                            // Handle the error data as needed
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//
//                    loginCallback.onLoginFailure(new Exception("Login failed")); // You can pass an appropriate exception here
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//    loginCallback.onLoginFailure(t);
//            }
//        });
//    }


    public void logout() {
        // TODO: revoke authentication
    }
}