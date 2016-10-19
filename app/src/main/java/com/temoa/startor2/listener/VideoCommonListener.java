package com.temoa.startor2.listener;

/**
 * Created by Temoa
 * on 2016/10/17 18:32
 */

public interface VideoCommonListener<T> {

    void onSucceed(T data, int flag);

    void onError(String error);
}
