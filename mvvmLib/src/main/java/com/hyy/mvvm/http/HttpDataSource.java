package com.hyy.mvvm.http;


import com.zhouyou.http.model.HttpHeaders;
import com.zhouyou.http.model.HttpParams;

import io.reactivex.disposables.Disposable;

/**
 * Created by goldze on 2019/3/26.
 */
public interface HttpDataSource {
    Disposable postRequest(String url, HttpHeaders headers, HttpParams params, String postData, boolean loadingShow,
                           HttpUtilsRequestCallBack callBack);

    Disposable getRequest(String url, HttpHeaders headers, HttpParams params, boolean loadingShow,
                          HttpUtilsRequestCallBack callBack);
}
