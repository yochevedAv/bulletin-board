package com.example.bulletinboard.ui.createPost;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bulletinboard.ui.registration.RegistrationFormState;
import com.example.bulletinboard.ui.registration.RegistrationResult;

public class CreatePostViewModel extends ViewModel {
    public MutableLiveData<String> title = new MutableLiveData<>();
    public MutableLiveData<String> description = new MutableLiveData<>();
    public MutableLiveData<String> date = new MutableLiveData<>();
    public MutableLiveData<String> location = new MutableLiveData<>();
    public MutableLiveData<String> creatorName = new MutableLiveData<>();
    public MutableLiveData<String> imagePath = new MutableLiveData<>();

    private MutableLiveData<RegistrationFormState> registrationFormState = new MutableLiveData<>();
    private MutableLiveData<RegistrationResult> registrationResult = new MutableLiveData<>();

    private CreatePostDataSource dataSource;


    CreatePostViewModel(CreatePostDataSource createPostDataSource) {
        this.dataSource = createPostDataSource;
    }

    LiveData<RegistrationFormState> getRegistrationFormState() {
        return registrationFormState;
    }

    LiveData<RegistrationResult> getRegistrationResult() {
        return registrationResult;
    }

//    public void register(String username, String password, String email) {
//        dataSource.register(email, password ,username, this);
//    }

    public void RegistrationDataChanged(String username, String password, String email) {
//        if (!isUserNameValid(username)) {
//            registrationFormState.setValue(new RegistrationFormState(R.string.invalid_username, null,null));
//        } else if(!isEmailValid(email)){
//            registrationFormState.setValue(new RegistrationFormState(null, null,R.string.invalid_email));
//        } else if (!isPasswordValid(password)) {
//            registrationFormState.setValue(new RegistrationFormState(null, R.string.invalid_password,null));
//        } else {
//            registrationFormState.setValue(new RegistrationFormState(true));
//        }
    }

    // A placeholder email validation check
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return !email.trim().isEmpty();
        }
    }

    // A placeholder email validation check
    private boolean isUserNameValid(String userName) {
        return userName != null && userName.trim().length() > 2;
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }


    // Implement the logic for showing a date picker
    public void showDatePicker() {
        // Initialize and show a date picker dialog
    }

    // Implement the logic for uploading an image
    public void uploadImage() {
        // Implement image upload logic here
    }

    // Implement the logic for creating a post
    public void createPost() {
        String postTitle = title.getValue();
        String postDescription = description.getValue();
        String postDate = date.getValue();
        String postLocation = location.getValue();
        String postCreatorName = creatorName.getValue();
        String postImagePath = imagePath.getValue();

        // Implement the logic to create a post with the entered data
    }
}
