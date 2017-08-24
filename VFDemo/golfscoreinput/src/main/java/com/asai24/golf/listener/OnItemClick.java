package com.asai24.golf.listener;
public interface OnItemClick<Result extends Object> {
    void onAdapterItemClick(Result result);
}
