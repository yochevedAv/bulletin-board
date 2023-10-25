package com.example.bulletinboard.ui.createPost;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bulletinboard.ui.registration.RegistrationViewModel;

public class createPostViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CreatePostViewModel.class)) {
            return (T) new CreatePostViewModel(new CreatePostDataSource());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
