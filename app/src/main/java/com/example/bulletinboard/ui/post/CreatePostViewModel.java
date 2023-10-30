package com.example.bulletinboard.ui.post;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bulletinboard.R;
import com.example.bulletinboard.server.datasource.CreatePostDataSource;
import com.example.bulletinboard.server.result.ResponseResult;
import com.example.bulletinboard.server.callback.ResponseCallback;
import com.example.bulletinboard.data.model.Post;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreatePostViewModel extends ViewModel implements ResponseCallback<Post> {


    private MutableLiveData<CreatePostFormState> createPostFormState = new MutableLiveData<>();
    private MutableLiveData<ResponseResult> responseResult = new MutableLiveData<>();

    private CreatePostDataSource dataSource;
    private Context mContext;

    public CreatePostViewModel(CreatePostDataSource createPostDataSource, Context context) {
        this.dataSource = createPostDataSource;
        mContext = context;
    }

    public LiveData<CreatePostFormState> getCreatePostFormState() {
        return createPostFormState;
    }

    public LiveData<ResponseResult> getCreatePostResult() {
        return responseResult;
    }


    public void CreatePostDataChanged(String title, String creatorName, String date,String location, String description) {
        if (!isTitleValid(title)){
            createPostFormState.setValue(new CreatePostFormState(R.string.invalid_title,null,null, null,null));
        } else if(!isCreatorNameValid(creatorName)) {
            createPostFormState.setValue(new CreatePostFormState(null,R.string.invalid_creator_name,null,null,null));
        } else if  (!isValidDate(date,"yyyy-MM-dd")) {
            createPostFormState.setValue(new CreatePostFormState(null,null, R.string.invalid_date,null,null));
        }else if(!isValidAddress(mContext, location)){
            createPostFormState.setValue(new CreatePostFormState(null, null,null, R.string.invalid_location,null));
        } else if(!isDescriptionValid(description)){
            createPostFormState.setValue(new CreatePostFormState(null, null,null,null,R.string.invalid_description));
        }  else {
            createPostFormState.setValue(new CreatePostFormState(true));
        }
    }

    public static boolean isValidDate(String dateStr, String dateFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
            Date date = sdf.parse(dateStr);
            if (date != null) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isCreatorNameValid(String creatorName) {
        return creatorName != null && creatorName.trim().length() > 2;
    }

    private boolean isTitleValid(String title) {
        return title != null ;
    }

    private boolean isDescriptionValid(String description) {
        return description != null ;
    }

    public static boolean isValidAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            return addresses != null && !addresses.isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void createPost(String userId, String title, String creatorName, String date,String location, String description) {

        Post post = new Post(userId, title, creatorName ,date,location,description,null);
        dataSource.createPost(post,this);
    }

    public void editPost(String userId, String title, String creatorName, String date,String location, String description, String id) {
        Post post = new Post(userId, title, creatorName ,date,location,description,id);
        dataSource.updatePost(post,this);
    }

    @Override
    public void onResponseSuccess(Post response) {

    }

    @Override
    public void onResponseFailure(Throwable t, String message) {

    }
}
