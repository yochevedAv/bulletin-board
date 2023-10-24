package com.example.bulletinboard.ui.registration;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bulletinboard.data.LoginDataSource;
import com.example.bulletinboard.ui.login.LoginViewModel;

public class RegistrationViewModelFactory implements ViewModelProvider.Factory  {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RegistrationViewModel.class)) {
            return (T) new RegistrationViewModel(new RegistrationDataSource());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

}
