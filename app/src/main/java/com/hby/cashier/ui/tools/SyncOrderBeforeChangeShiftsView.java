package com.hby.cashier.ui.tools;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.hby.cashier.app.DataKey;
import com.hby.cashier.bean.CreateOrderBean;
import com.hby.cashier.bean.LitePalOrderDetailsBean;
import com.hby.cashier.datas.SQLiteManage;
import com.hby.cashier.http.RequestConfig;
import com.hby.cashier.utils.DownloadUtil;
import com.hby.cashier.utils.LogUtils;
import com.hby.cashier.utils.PermissionUtils;
import com.hby.cashier.utils.ZipUtils;
import com.hby.cashier.view_model.MainViewModel;
import com.hby.cashier.weight.dialog.ChangeShiftsDialog;
import com.hyy.mvvm.bean.BaseBean;
import com.hyy.mvvm.http.HttpUtils;
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
 * 功能介绍 :在交接班记录之前上传订单
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/22
 */
public class SyncOrderBeforeChangeShiftsView {
    private Context context;
    private String sessionID;
    private MainViewModel viewModel;

    public SyncOrderBeforeChangeShiftsView(Context context, String sessionID, MainViewModel viewModel) {
        this.context = context;
        this.sessionID = sessionID;
        this.viewModel = viewModel;
    }

    /**
     * 上传订单数据
     */
    public SyncOrderBeforeChangeShiftsView updateData() {
        viewModel.showDialog();
        getServiceTime();
        return this;
    }

    /**
     * 获取服务器时间，如果服务器时间和本地时间超出10分钟就提示修改时间
     */
    private void getServiceTime() {
        HttpUtils.get(RequestConfig.request_getServiceTime)
                .headers("sessionID", sessionID)
                .execute(new SimpleCallBack<String>() {

                    @Override
                    public void onError(ApiException e) {
                        viewModel.dismissDialog();
                        goChangeShifts();
                    }

                    @Override
                    public void onSuccess(String data) {
                        BaseBean baseBean = JsonUtils.parseJson(data, BaseBean.class);
                        if (baseBean.getResult() != -1 && baseBean.getResult() != -2 && baseBean.getResult() != -4) {
                            String timeStamp = (String) baseBean.getResultObject();
                            long timeAbs = Math.abs(Long.parseLong(timeStamp) - System.currentTimeMillis());
                            int minutes = (int) ((timeAbs / (1000 * 60)));

                            if (minutes > 10) {
                                viewModel.dismissDialog();
                                ToastUtils.showShort("当前时间差过大，订单上传失败，请修改时间后重试");
                                return;
                            }
                            updateOrderData();
                        }
                    }
                });
    }

    private int orderPos = 0;

    private void updateOrderData() {
        List<LitePalOrderDetailsBean> orderList =
                LitePal.where("isupdate = ?", "0").find(LitePalOrderDetailsBean.class);
        LogUtils.i("/*******************上行订单数据大小:" + orderList.size());
        if (orderList.size() > 0) {
//            for (int i = 0; i < orderList.size(); i++) {
//                LitePalOrderDetailsBean orderDetailsBean = orderList.get(i);
//
//                CreateOrderBean createOrderBean = JsonUtils.parseJson(JsonUtils.Object2Json(orderDetailsBean),
//                        CreateOrderBean.class);
//
//                List<CreateOrderBean.OrderProductAoListBean> listBeans =
//                        JSON.parseArray(orderDetailsBean.getOrderProductListJson(),
//                                CreateOrderBean.OrderProductAoListBean.class);
//
//                createOrderBean.setOrderProductAoList(listBeans);
//
//                updateOrder(createOrderBean,i);
//            }
            updateOrder(orderList, orderPos);

        } else {
            goChangeShifts();
        }

    }

    private void goChangeShifts() {
        viewModel.dismissDialog();
        ChangeShiftsDialog changeShiftsDialog = new ChangeShiftsDialog(context);
        changeShiftsDialog
                .setViewModel(viewModel)
                .setOnDialogClickListener(new ChangeShiftsDialog.DialogClickListener() {
                    @Override
                    public void confirm() {
                        clickListener.confirm();
                    }
                }).show();
    }

    /**
     * 上传订单数据
     */
    private void updateOrder(List<LitePalOrderDetailsBean> orderList, int orderPos) {
        LogUtils.i("*******************当前上行订单数据POS:" + orderPos);
        LitePalOrderDetailsBean orderDetailsBean = orderList.get(orderPos);
        CreateOrderBean createOrderBean = JsonUtils.parseJson(JsonUtils.Object2Json(orderDetailsBean),
                CreateOrderBean.class);
        List<CreateOrderBean.OrderProductAoListBean> listBeans =
                JSON.parseArray(orderDetailsBean.getOrderProductListJson(),
                        CreateOrderBean.OrderProductAoListBean.class);
        createOrderBean.setOrderProductAoList(listBeans);

        HttpUtils.post(RequestConfig.request_createOrder)
                .headers("sessionID", sessionID)
                .upJson(JsonUtils.Object2Json(createOrderBean))
                .execute(new SimpleCallBack<String>() {

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        isUpdateFinish(orderList, orderPos);
                    }

                    @Override
                    public void onError(ApiException e) {
                        isUpdateFinish(orderList, orderPos);
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

    private void isUpdateFinish(List<LitePalOrderDetailsBean> orderList, int pos) {
        if (pos == orderList.size() - 1) {
            goChangeShifts();
        } else {
            pos++;
            updateOrder(orderList, pos);
        }
    }

    public abstract static class DialogClickListener {
        public abstract void confirm();
    }

    private DialogClickListener clickListener;

    public void setOnDialogClickListener(DialogClickListener clickListener) {
        this.clickListener = clickListener;
    }


}
