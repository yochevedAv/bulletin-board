package com.example.bulletinboard;

import com.example.bulletinboard.data.model.User;
import com.example.bulletinboard.data.model.User;

import retrofit2.Call;
import retrofit2.http.*;

public interface BulletinBoardService {
    @POST("register")
    Call<User> UserRegistration(@Body User user);

    @POST("login")
    Call<User> UserLogin(@Body User user);



//    @GET("/posts")
//    Call<List<Post>> getPosts();
//
//    @POST("/posts")
//    Call<Post> createPost(@Body Post post);
//
//    @PUT("/posts/{id}")
//    Call<Post> updatePost(@Path("id") int postId, @Body Post post);
//
//    @DELETE("/posts/{id}")
//    Call<Void> deletePost(@Path("id") int postId);
}
