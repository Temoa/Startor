package com.temoa.startor.callback;

/**
 * Created by Temoa
 * on 2016/8/30 17:59
 */
public interface ICallback<T> {
    void onNetworkSucceed(T data);

    void onNetworkError(String error);
}
