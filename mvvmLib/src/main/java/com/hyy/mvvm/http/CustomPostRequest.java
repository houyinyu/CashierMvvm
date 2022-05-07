package com.hyy.mvvm.http;

import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.request.PostRequest;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * 功能介绍 :
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/3/22
 */
public class CustomPostRequest  extends PostRequest {
    public CustomPostRequest(String url) {
        super(url);
    }

    @Override
    public <T> Observable<T> execute(Type type) {
        return super.execute(type);
    }

    @Override
    public <T> Observable<T> execute(Class<T> clazz) {
        return super.execute(clazz);
    }

    @Override
    public <T> Disposable execute(CallBack<T> callBack) {
        return super.execute(callBack);
    }
}
