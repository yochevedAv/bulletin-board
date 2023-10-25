package com.example.bulletinboard.data;

import com.example.bulletinboard.data.model.User;

public interface ResponseCallback<T> {
    void onResponseSuccess(T response);
    void onResponseFailure(Throwable t, String message);
}

