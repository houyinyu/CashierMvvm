package com.hyy.mvvm.data;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.hyy.mvvm.base.BaseModel;
import com.hyy.mvvm.http.HttpDataSource;
import com.hyy.mvvm.http.HttpUtilsRequestCallBack;
import com.zhouyou.http.model.HttpHeaders;
import com.zhouyou.http.model.HttpParams;

import io.reactivex.disposables.Disposable;

/**
 * MVVM的Model层，统一模块的数据仓库，包含网络数据和本地数据（一个应用可以有多个Repositor）
 * Created by goldze on 2019/3/26.
 */
public class DemoRepository extends BaseModel implements HttpDataSource, LocalDataSource {
    private volatile static DemoRepository INSTANCE = null;
    private final HttpDataSource mHttpDataSource;

    private final LocalDataSource mLocalDataSource;

    private DemoRepository(@NonNull HttpDataSource httpDataSource,
                           @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static DemoRepository getInstance(HttpDataSource httpDataSource,
                                             LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (DemoRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DemoRepository(httpDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }


    private String requestUrl = "";
    private boolean showLoading = true;
    private HttpHeaders requestHeaders = new HttpHeaders();
    private HttpParams requestParam = new HttpParams();
    private String postData = "";

    public DemoRepository post(String url) {
        this.requestUrl = url;
        return this;
    }

    public DemoRepository showLoading(boolean showLoading) {
        this.showLoading = showLoading;
        return this;
    }

    public DemoRepository setHeader(HttpHeaders header) {
        this.requestHeaders = header;
        return this;
    }

    public DemoRepository setParams(HttpParams params) {
        this.requestParam = params;
        return this;
    }

    public DemoRepository setPostData(String postData) {
        this.postData = postData;
        return this;
    }

    public Disposable postRequest(HttpUtilsRequestCallBack callBack) {
        return postRequest(requestUrl, requestHeaders, requestParam, postData, showLoading, callBack);
    }

    public Disposable getRequest(HttpUtilsRequestCallBack callBack) {
        return getRequest(requestUrl, requestHeaders, requestParam, showLoading, callBack);
    }

    @Override
    public Disposable postRequest(String url, HttpHeaders headers,
                                  HttpParams params, String postData, boolean loadingShow, HttpUtilsRequestCallBack callBack) {
        return mHttpDataSource.postRequest(url, headers, params, postData, loadingShow, callBack);
    }

    @Override
    public Disposable getRequest(String url, HttpHeaders headers, HttpParams params, boolean loadingShow, HttpUtilsRequestCallBack callBack) {
        return mHttpDataSource.getRequest(url, headers, params, loadingShow, callBack);
    }

//
//    @Override
//    public void saveUserName(String userName) {
//        mLocalDataSource.saveUserName(userName);
//    }
//
//    @Override
//    public void savePassword(String password) {
//        mLocalDataSource.savePassword(password);
//    }
//
//    @Override
//    public String getUserName() {
//        return mLocalDataSource.getUserName();
//    }
//
//    @Override
//    public String getPassword() {
//        return mLocalDataSource.getPassword();
//    }
}
