package com.example.bulletinboard.server;

import com.example.bulletinboard.data.model.Post;
import com.example.bulletinboard.data.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BulletinBoardService {
    @POST("register")
    Call<User> UserRegistration(@Body User user);

    @POST("login")
    Call<User> UserLogin(@Body User user);

    @POST("createPost")
    Call<Post> CreatePost(@Body Post post);

    @GET("getPosts")
    Call<List<Post>> GetPosts();

    @DELETE("deletePost/{postId}")
    Call<ResponseBody> DeletePost(@Path("postId") String postId);

    @PUT("updatePost")
    Call<Post> UpdatePost(@Body Post updatedPost);

}
