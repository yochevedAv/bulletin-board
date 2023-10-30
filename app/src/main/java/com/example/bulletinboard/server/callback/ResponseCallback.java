package com.example.bulletinboard.server.callback;

public interface ResponseCallback<T> {
    void onResponseSuccess(T response);
    void onResponseFailure(Throwable t, String message);
}

