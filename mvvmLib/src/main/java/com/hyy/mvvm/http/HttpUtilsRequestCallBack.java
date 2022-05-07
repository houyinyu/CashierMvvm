package com.hyy.mvvm.http;

/**
 * 自定义请求回调
 */
public abstract class HttpUtilsRequestCallBack {

    public void onError(String error){};

    public void onFinish(){};

    public abstract void onSucceed(String data);
}
