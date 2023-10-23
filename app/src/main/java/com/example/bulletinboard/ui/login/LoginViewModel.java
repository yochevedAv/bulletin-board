package com.example.bulletinboard.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Intent;
import android.util.Patterns;

import com.example.bulletinboard.MainActivity;
import com.example.bulletinboard.data.LoginCallback;
import com.example.bulletinboard.data.LoginDataSource;
import com.example.bulletinboard.data.LoginRepository;
import com.example.bulletinboard.data.Result;
import com.example.bulletinboard.data.model.LoggedInUser;
import com.example.bulletinboard.R;

public class LoginViewModel extends ViewModel implements LoginCallback {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    private LoginDataSource dataSource;


    LoginViewModel(LoginDataSource loginDataSource) {
        this.dataSource = loginDataSource;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {

         dataSource.login(username, password,this);
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    @Override
    public void onLoginSuccess(LoggedInUser loggedInUser) {
        loginResult.setValue(new LoginResult(new LoggedInUserView(loggedInUser.getEmail())));
    }

    @Override
    public void onLoginFailure(Throwable t) {
        loginResult.setValue(new LoginResult(R.string.login_failed));
    }
}