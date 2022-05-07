package com.hyy.mvvm.http;


import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;


import com.hyy.mvvm.base.BaseActivity;
import com.hyy.mvvm.bean.BaseBean;
import com.hyy.mvvm.loading.LoadingDialog;
import com.hyy.mvvm.utils.DialogUtils;
import com.hyy.mvvm.utils.JsonUtils;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpHeaders;
import com.zhouyou.http.model.HttpParams;
import com.zhouyou.http.subsciber.IProgressDialog;



import io.reactivex.disposables.Disposable;

/**
 * 网络请求
 */
public class HttpDataSourceImpl implements HttpDataSource {
    /**
     * loading
     */
    private IProgressDialog ipd;
    protected LoadingDialog loading;

    protected IProgressDialog getIpd() {
        if (ipd == null) {
            ipd = new IProgressDialog() {
                @Override
                public Dialog getDialog() {
                    return new LoadingDialog(activity);
                }
            };
        }
        return ipd;
    }

    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(BaseActivity activity) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(activity);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private BaseActivity activity;

    private HttpDataSourceImpl(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public Disposable postRequest(String url, HttpHeaders headers, HttpParams params, String postData,
                                  boolean loadingShow, HttpUtilsRequestCallBack callBack) {
        Disposable disposable = HttpUtils.post(url)
                .headers(headers)
                .params(params)
                .upJson(postData)
                .execute(new ProgressDialogCallBack<String>(loadingShow ? getIpd() : null) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        callBack.onFinish();
                    }

                    @Override
                    public void onError(ApiException e) {
                        callBack.onError(e.getMessage());
                    }

                    @Override
                    public void onSuccess(String data) {
                        if (TextUtils.isEmpty(data)) {
                            callBack.onError("数据错误");
                            return;
                        }
                        BaseBean baseBean = JsonUtils.parseJson(data, BaseBean.class);
                        if (baseBean.getResult() != -1 && baseBean.getResult() != -2 && baseBean.getResult() != -4) {
                            callBack.onSucceed(data);
                        } else {
                            //重复登录
                            if (baseBean.getResult() == -4) {
                                showLogOutDialog(baseBean.getMessage());
                            } else {
                                DialogUtils.singleDialog(activity, "提示", baseBean.getMessage(), "确定", null)
                                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                callBack.onError(data);
                                            }
                                        });
                            }
                        }
                    }
                });
        return disposable;
    }

    @Override
    public Disposable getRequest(String url, HttpHeaders headers, HttpParams params, boolean loadingShow,
                                 HttpUtilsRequestCallBack callBack) {
        Disposable disposable = HttpUtils.get(url)
                .headers(headers)
                .params(params)
                .execute(new ProgressDialogCallBack<String>(loadingShow ? getIpd() : null) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        callBack.onFinish();
                    }

                    @Override
                    public void onError(ApiException e) {
                        callBack.onError(e.getMessage());
                    }

                    @Override
                    public void onSuccess(String data) {
                        if (TextUtils.isEmpty(data)) {
                            callBack.onError("数据错误");
                            return;
                        }
                        BaseBean baseBean = JsonUtils.parseJson(data, BaseBean.class);
                        if (baseBean.getResult() != -1 && baseBean.getResult() != -2 && baseBean.getResult() != -4) {
                            callBack.onSucceed(data);
                        } else {
                            //重复登录
                            if (baseBean.getResult() == -4) {
                                showLogOutDialog(baseBean.getMessage());
                            } else {
                                DialogUtils.singleDialog(activity, "提示", baseBean.getMessage(), "确定", null)
                                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                callBack.onError(data);
                                            }
                                        });
                            }
                        }
                    }
                });
        return disposable;
    }

    /**
     * 退出登录dialog
     */
    private AlertDialog logOutDialog;

    private void showLogOutDialog(String message) {
        if (logOutDialog != null) return;
        logOutDialog = DialogUtils.singleDialog(activity, "提示", message, "退出登录", new DialogUtils.ButtomCallBack() {
            @Override
            public void left() {

            }

            @Override
            public void right() {
//                context.loginOut();
            }

            @Override
            public void min() {

            }
        });
        logOutDialog.setCanceledOnTouchOutside(false);
    }


}
