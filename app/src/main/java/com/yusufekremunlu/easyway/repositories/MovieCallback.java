package com.yusufekremunlu.easyway.repositories;

public interface MovieCallback<T> {
    void onSuccess(T data);
    void onError(String message);
}
