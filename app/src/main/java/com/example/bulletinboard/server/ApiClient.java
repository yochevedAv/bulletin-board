package com.example.bulletinboard.server;

import com.example.bulletinboard.data.model.UserDeserializer;
import com.example.bulletinboard.data.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static ApiClient instance;
    private Retrofit retrofit;
    private BulletinBoardService bulletinBoardService;

    private ApiClient() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new UserDeserializer())
                .create();

        retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/api/auth/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        bulletinBoardService = retrofit.create(BulletinBoardService.class);
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public BulletinBoardService getBulletinBoardService() {
        return bulletinBoardService;
    }
}
