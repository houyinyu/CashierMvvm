package com.hby.cashier.ui.tools;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.hby.cashier.app.AppConfig;
import com.hby.cashier.app.DataKey;
import com.hby.cashier.bean.CreateOrderBean;
import com.hby.cashier.bean.CreateOrderReturnBean;
import com.hby.cashier.bean.LitePalConfigurationBean;
import com.hby.cashier.bean.LitePalOrderDetailsBean;
import com.hby.cashier.bean.PollingOrderStatusBean;
import com.hby.cashier.datas.SQLiteManage;
import com.hby.cashier.http.RequestConfig;
import com.hby.cashier.ui.MainActivity;
import com.hby.cashier.utils.DownloadUtil;
import com.hby.cashier.utils.EnumUtils;
import com.hby.cashier.utils.LogUtils;
import com.hby.cashier.utils.NumUtils;
import com.hby.cashier.utils.PermissionUtils;
import com.hby.cashier.utils.TimeUtils;
import com.hby.cashier.utils.ZipUtils;
import com.hby.cashier.weight.dialog.PaymentCallbackDialog;
import com.hyy.mvvm.bean.BaseBean;
import com.hyy.mvvm.http.HttpUtils;
import com.hyy.mvvm.utils.DialogUtils;
import com.hyy.mvvm.utils.JsonUtils;
import com.hyy.mvvm.utils.SPUtils;
import com.hyy.mvvm.utils.ToastUtils;
import com.yanzhenjie.permission.runtime.Permission;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 功能介绍 :上行数据和数据同步
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/22
 */
public class DataSyncView {
    private MainActivity context;
    private String sessionID;
    private int updateTime = 60;

    public DataSyncView(MainActivity context, String sessionID) {
        this.context = context;
        this.sessionID = sessionID;
    }

    private Disposable syncDisposable;

    //开始数据同步轮询
    private void startSyncPolling() {
        int syncTime = SPUtils.getInstance().getInt(DataKey.KEY_DATA_SYNC, updateTime);
        //首次执行延时时间 、 每次轮询间隔时间 、 时间类型
        syncDisposable = Observable.interval(syncTime, syncTime, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
                        // 逻辑代码
                        goDownLoad();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

    }

    private void goDownLoad() {
        LogUtils.i("开始动态数据同步************************************");
        String downUrl = AppConfig.BASE_DOWNLOAD_URL + RequestConfig.request_downZip;
        new DownloadUtil().download(context, downUrl, new DownloadUtil.OnDownloadListener() {

            @Override
            public void onDownloadSuccess(@NonNull File file) {
                try {
                    ZipUtils.unZipFilesTest(file);
                    handler.sendEmptyMessageDelayed(HANDLER_SQ_DATA, 1000);
                    LogUtils.i("*******************下载成功:" + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onDownloading(int progress) {

            }

            @Override
            public void onDownloadFailed() {
                LogUtils.i("*******************onDownloadFailed:");
            }
        });

    }

    /**
     * 数据下载（同步）
     */
    public void startSyncDownLoad() {
        new PermissionUtils(context).showSinglePermission("存储",
                Permission.WRITE_EXTERNAL_STORAGE, new PermissionUtils.OnPermissionListener() {
                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void agree() {
                        //同意权限
                        startSyncPolling();
                    }

                    @Override
                    public void finish() {

                    }
                });
    }


    private Disposable updateDisposable;

    //开始数据同步轮询
    public void startUpdateDisposable() {
        //首次执行延时时间 、 每次轮询间隔时间 、 时间类型
        updateDisposable = Observable.interval(updateTime, updateTime, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
                        // 逻辑代码
                        getServiceTime();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

    }

    /**
     * 获取服务器时间，如果服务器时间和本地时间超出10分钟就提示修改时间
     */
    public void getServiceTime() {
        HttpUtils.get(RequestConfig.request_getServiceTime)
                .headers("sessionID", sessionID)
                .execute(new SimpleCallBack<String>() {

                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String data) {
                        BaseBean baseBean = JsonUtils.parseJson(data, BaseBean.class);
                        if (baseBean.getResult() != -1 && baseBean.getResult() != -2 && baseBean.getResult() != -4) {
                            String timeStamp = (String) baseBean.getResultObject();
                            long timeAbs = Math.abs(Long.parseLong(timeStamp) - System.currentTimeMillis());
                            int minutes = (int) ((timeAbs / (1000 * 60)));

                            if (minutes > 10) {
                                ToastUtils.showShort("当前时间差过大，订单上传失败，请修改时间后重新登录");
                                stopUpdatePolling();
                                return;
                            }
                            updateData();
                        }
                    }
                });
    }

    private void updateData() {
        List<LitePalOrderDetailsBean> orderList =
                LitePal.where("isupdate = ?", "0").find(LitePalOrderDetailsBean.class);
        LogUtils.i("/*******************上行数据成功大小:" + orderList.size());
        if (orderList.size() > 0) {
            for (int i = 0; i < orderList.size(); i++) {
                LitePalOrderDetailsBean orderDetailsBean = orderList.get(i);

                CreateOrderBean createOrderBean = JsonUtils.parseJson(JsonUtils.Object2Json(orderDetailsBean),
                        CreateOrderBean.class);

                List<CreateOrderBean.OrderProductAoListBean> listBeans =
                        JSON.parseArray(orderDetailsBean.getOrderProductListJson(),
                                CreateOrderBean.OrderProductAoListBean.class);

                createOrderBean.setOrderProductAoList(listBeans);

                updateOrder(createOrderBean);
            }
        }
    }

    /**
     * 上传订单数据
     */
    private void updateOrder(CreateOrderBean createOrderBean) {

        HttpUtils.post(RequestConfig.request_createOrder)
                .headers("sessionID", sessionID)
                .upJson(JsonUtils.Object2Json(createOrderBean))
                .execute(new SimpleCallBack<String>() {

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String data) {
                        BaseBean baseBean = JsonUtils.parseJson(data, BaseBean.class);
                        if (baseBean.getResult() == 2) {
                            //上行数据成功
                            LogUtils.i("/*******************上行数据成功:" + createOrderBean.getOrdNo());

                            ContentValues values = new ContentValues();
                            values.put("isupdate", "1");

                            LitePal.updateAll(LitePalOrderDetailsBean.class, values,
                                    "ordno = ? ", createOrderBean.getOrdNo());
                        }
                    }
                });

    }

    //结束数据同步轮询
    public void stopSyncPolling() {
        //停止轮询，销毁这个Disposable;
        if (syncDisposable != null && !syncDisposable.isDisposed()) {
            syncDisposable.dispose();
        }
    }

    //结束上行数据轮询
    public void stopUpdatePolling() {
        //停止轮询，销毁这个Disposable;
        if (updateDisposable != null && !updateDisposable.isDisposed()) {
            updateDisposable.dispose();
        }
    }

    private final int HANDLER_SQ_DATA = 1;//本地数据库数据同步
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case HANDLER_SQ_DATA:
                    new SQLiteManage().initData();
                    context.dataSyncRef();
                    break;
            }
            return false;
        }
    });

}
