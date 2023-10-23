package com.example.bulletinboard;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static ApiClient instance;
    private Retrofit retrofit;
    private BulletinBoardService bulletinBoardService;

    private ApiClient() {
        retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/api/auth/")
            .addConverterFactory(GsonConverterFactory.create())
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
