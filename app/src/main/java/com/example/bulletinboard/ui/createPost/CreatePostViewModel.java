package com.example.bulletinboard.ui.createPost;

import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bulletinboard.R;
import com.example.bulletinboard.ResponseResult;
import com.example.bulletinboard.data.ResponseCallback;
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

    CreatePostViewModel(CreatePostDataSource createPostDataSource, Context context) {
        this.dataSource = createPostDataSource;
        mContext = context;
    }

    LiveData<CreatePostFormState> getCreatePostFormState() {
        return createPostFormState;
    }

    LiveData<ResponseResult> getCreatePostResult() {
        return responseResult;
    }

//    public void register(String username, String password, String email) {
//        dataSource.register(email, password ,username, this);
//    }

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
                // The date is valid
                return true;
            }
        } catch (ParseException e) {
            // Date parsing failed, indicating an invalid date
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
            // Attempt to geocode the address
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            return addresses != null && !addresses.isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
            return false; // An error occurred during geocoding
        }
    }


    public void createPost(String userId, String title, String creatorName, String date,String location, String description) {

        dataSource.createPost(userId, title, creatorName ,date,location,description,this);
    }

    @Override
    public void onResponseSuccess(Post response) {

    }

    @Override
    public void onResponseFailure(Throwable t, String message) {

    }
}
