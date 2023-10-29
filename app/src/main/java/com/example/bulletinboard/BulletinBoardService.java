package com.example.bulletinboard;

import com.example.bulletinboard.data.model.Post;
import com.example.bulletinboard.data.model.User;
import com.example.bulletinboard.data.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

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



//    @GET("/posts")
//    Call<List<Post>> getPosts();
//
//    @POST("/posts")
//    Call<Post> createPost(@Body Post post);
//
//    @PUT("/posts/{id}")
//    Call<Post> updatePost(@Path("id") int postId, @Body Post post);
//

}
