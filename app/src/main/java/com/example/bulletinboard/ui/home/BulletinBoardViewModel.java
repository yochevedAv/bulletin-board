package com.example.bulletinboard.ui.home;

import android.util.Patterns;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bulletinboard.ApiClient;
import com.example.bulletinboard.BulletinBoardService;
import com.example.bulletinboard.PostResult;
import com.example.bulletinboard.R;
import com.example.bulletinboard.ResponseResult;
import com.example.bulletinboard.data.ResponseCallback;
import com.example.bulletinboard.data.model.ErrorResponse;
import com.example.bulletinboard.data.model.Post;
import com.example.bulletinboard.data.model.User;
import com.example.bulletinboard.ui.BulletinBoardPostsResult;
import com.example.bulletinboard.ui.registration.RegistrationDataSource;
import com.example.bulletinboard.ui.registration.RegistrationFormState;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BulletinBoardViewModel extends ViewModel implements ResponseCallback<List<Post>> {

    MutableLiveData<List<Post>> postsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> deletePostResult = new MutableLiveData<>();
    private MutableLiveData<PostResult> postResult = new MutableLiveData<>();

    public LiveData<List<Post>> getPostsLiveData() {
        return postsLiveData;
    }

    public LiveData<Boolean> getDeletePostResult() {
        return deletePostResult;
    }

    public LiveData<PostResult> getResponseResult() {
        return postResult;
    }

    ApiClient apiClient = ApiClient.getInstance();
    BulletinBoardService bulletinBoardService = apiClient.getBulletinBoardService();

    public void getPosts() {
        Call<List<Post>> call = bulletinBoardService.GetPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (response.isSuccessful()) {
                    // Handle a successful response here
                    postsLiveData.setValue(response.body());

                    postResult.setValue(new PostResult(response.body()));
                    //responseCallback.onResponseSuccess(User);
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

                    postResult.setValue(new PostResult(R.string.get_posts_failed, errorMessage));

                    //responseCallback.onResponseFailure(new Exception("Login failed: "), errorMessage);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

                postResult.setValue(new PostResult(R.string.get_posts_failed, t.getMessage()));

            }
        });
    }

    public void deletePost(String postId) {

            Call<ResponseBody> deleteCall =bulletinBoardService.DeletePost(postId);
            deleteCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if(response.body()!=null) {
                            JSONObject jsonResponse = new JSONObject(response.body().string());
                            boolean success = jsonResponse.optBoolean("success", false);
                            if (success) {
                                deletePostResult.setValue(true);
                            } else {
                                deletePostResult.setValue(false);
                            }
                        }
                        else{
                            deletePostResult.setValue(false);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        // Handle exceptions
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    deletePostResult.setValue(false);
                }
            });
        }

    public void onDeleteButtonClick(Post post) {
        deletePost(post.getId());
    }







    @Override
    public void onResponseSuccess(List<Post> response) {
        postResult.setValue(new PostResult(response));

    }

    @Override
    public void onResponseFailure(Throwable t, String message) {
        postResult.setValue(new PostResult(R.string.get_posts_failed, message));
    }
}