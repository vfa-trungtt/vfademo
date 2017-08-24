package com.asai24.golf.listener;

public interface XMLRequestListener<T> {

    void onStart();

    void onFinish();

    void onError(String code, String message);

    void onSuccess(T response);
}
