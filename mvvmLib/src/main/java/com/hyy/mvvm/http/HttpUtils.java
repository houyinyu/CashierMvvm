package com.hyy.mvvm.http;

import com.zhouyou.http.request.GetRequest;
import com.zhouyou.http.request.PostRequest;

/**
 * 功能介绍 :
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/3/22
 */
public class HttpUtils {
    public static GetRequest get(String url) {
        return new CustomGetRequest(url);
    }

    public static PostRequest post(String url) {
        return new CustomPostRequest(url);
    }
}
