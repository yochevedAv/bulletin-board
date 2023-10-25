package com.example.bulletinboard.ui.createPost;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bulletinboard.ui.registration.RegistrationViewModel;

public class createPostViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public createPostViewModelFactory(@NonNull Context context) {
        this.context = context.getApplicationContext(); // Use the application context.
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CreatePostViewModel.class)) {
            return (T) new CreatePostViewModel(new CreatePostDataSource(),context);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
