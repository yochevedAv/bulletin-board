package com.example.bulletinboard.ui.registration;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.bulletinboard.ResponseResult;
import com.example.bulletinboard.data.ResponseCallback;
import com.example.bulletinboard.data.model.User;
import com.example.bulletinboard.R;

public class RegistrationViewModel extends ViewModel implements ResponseCallback<User> {

    private MutableLiveData<RegistrationFormState> registrationFormState = new MutableLiveData<>();
    private MutableLiveData<ResponseResult> responseResult = new MutableLiveData<>();
    private RegistrationDataSource dataSource;


    RegistrationViewModel(RegistrationDataSource registrationDataSource) {
        this.dataSource = registrationDataSource;
    }

    LiveData<RegistrationFormState> getRegistrationFormState() {
        return registrationFormState;
    }

    LiveData<ResponseResult> getResponseResult() {
        return responseResult;
    }

    public void register(String username, String password, String email) {
        dataSource.register(email, password ,username, this);
    }

    public void RegistrationDataChanged(String username, String password, String email) {
        if (!isUserNameValid(username)) {
            registrationFormState.setValue(new RegistrationFormState(R.string.invalid_username, null,null));
        } else if(!isEmailValid(email)){
            registrationFormState.setValue(new RegistrationFormState(null, null,R.string.invalid_email));
        } else if (!isPasswordValid(password)) {
            registrationFormState.setValue(new RegistrationFormState(null, R.string.invalid_password,null));
        } else {
            registrationFormState.setValue(new RegistrationFormState(true));
        }
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

    @Override
    public void onResponseSuccess(User response) {
        responseResult.setValue(new ResponseResult(response));

    }

    @Override
    public void onResponseFailure(Throwable t, String message) {
        responseResult.setValue(new ResponseResult(R.string.login_failed, message));
    }
}
