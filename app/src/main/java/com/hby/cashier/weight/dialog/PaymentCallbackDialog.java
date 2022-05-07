package com.hby.cashier.weight.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.hby.cashier.R;
import com.hby.cashier.app.DataKey;
import com.hby.cashier.bean.EventMessage;
import com.hby.cashier.bean.PollingOrderStatusBean;
import com.hby.cashier.bean.ShopCartBean;
import com.hby.cashier.http.RequestConfig;
import com.hby.cashier.second_screen.SecondScreenPayDisplay;
import com.hby.cashier.ui.tools.LocalSaveOrderView;
import com.hby.cashier.utils.EnumUtils;
import com.hby.cashier.utils.LogUtils;
import com.hby.cashier.utils.NumUtils;
import com.hby.cashier.views.ScanningView;
import com.hby.cashier.voice.VoicePlay;
import com.hyy.mvvm.bean.BaseBean;
import com.hyy.mvvm.http.HttpUtils;
import com.hyy.mvvm.utils.DialogUtils;
import com.hyy.mvvm.utils.JsonUtils;
import com.hyy.mvvm.utils.SPUtils;
import com.hyy.mvvm.utils.ToastUtils;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * 支付回调弹窗
 * 这里支付完成或者取消才去创建本地订单和打印
 */
public class PaymentCallbackDialog extends Dialog {

    private Context context;
    private LayoutInflater inflater;


    public PaymentCallbackDialog(Context context) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        setCustomView();
    }

    private String orderNo;
    private String paymentMethod;
    private String paymentType;
    private double orderPrice;
    private double actualPrice;
    private double promotionPrice;
    private double rebatePrice;
    private double changePrice;

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setPrice(double orderPrice, double actualPrice
            , double promotionPrice, double rebatePrice, double changePrice) {
        this.orderPrice = orderPrice;
        this.actualPrice = actualPrice;
        this.promotionPrice = promotionPrice;
        this.rebatePrice = rebatePrice;
        this.changePrice = changePrice;
    }

    private boolean isTakeOrder = false;

    public void setTakeOrder(boolean isTakeOrder) {
        this.isTakeOrder = isTakeOrder;
    }

    private LocalSaveOrderView saveOrderView;

    public void setSaveOrderView(LocalSaveOrderView saveOrderView) {
        this.saveOrderView = saveOrderView;
    }

    private VoicePlay voicePlay;

    public void setVoicePlay(VoicePlay voicePlay) {
        this.voicePlay = voicePlay;
    }


    private SecondScreenPayDisplay payDisplay;

    public void setPayDisplay(SecondScreenPayDisplay display, List<ShopCartBean> shopCartList, String urlData) {
        this.payDisplay = display;
        int isNeedSecond = SPUtils.getInstance().getInt(DataKey.KEY_IS_SECOND_SCREEN);
        if (isNeedSecond == 1) {
            if (payDisplay != null && !payDisplay.isShow) {
                payDisplay.show();
                payDisplay.updateListData(paymentMethod, shopCartList);
                payDisplay.updateRightLayout(paymentMethod, 0, urlData);
            } else if (null != payDisplay) {
                payDisplay.updateListData(paymentMethod, shopCartList);
                payDisplay.updateRightLayout(paymentMethod, 0, urlData);
            }
        }

        startPolling();
        saveOrderView = new LocalSaveOrderView();
        saveOrderView.setShopCartList(shopCartList);
        saveOrderView.setPaymentMethod(paymentMethod);
        saveOrderView.setPaymentType(paymentType);
    }

    private void dismissDisplay() {
        if (payDisplay != null && payDisplay.isShow) {
            payDisplay.dismiss();
        }
    }

    /**
     * 设置整个弹出框的视图
     */
    private String sessionID;

    public void setCustomView() {
        sessionID = SPUtils.getInstance().getString(DataKey.KEY_SESSION_ID);
        View mView = inflater.inflate(R.layout.dialog_payment_callback_layout, null);
        ImageView dismissImage = mView.findViewById(R.id.dialogPaymentCallback_dismissImage);
        TextView textView = mView.findViewById(R.id.dialogPaymentCallback_textView);

        dismissImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndPrint(EnumUtils.PayStatus.PAY_FAIL);
            }
        });
        initScanning();
        setCanceledOnTouchOutside(false);
        super.setContentView(mView);
    }

    /**
     * 扫码相关
     */
    private ScanningView scanningView;

    private void initScanning() {
        //拦截扫码器回调,获取扫码内容
        scanningView = new ScanningView(new ScanningView.OnScanValueListener() {
            @Override
            public void onScanValue(String value) {
                //扫码支付
                stopPolling();
                LogUtils.i("********************读取到支付内容***********************：" + value);

                String userMid = SPUtils.getInstance().getString(DataKey.KEY_USER_MID);
                String sessionID = SPUtils.getInstance().getString(DataKey.KEY_SESSION_ID);
                JSONObject postData = new JSONObject();
                postData.put("auth_code", value.trim());
                postData.put("mid", userMid);
                postData.put("rodno", orderNo);
                postData.put("service_code", "PAYSERVICE_07");
                postData.put("total_amount", NumUtils.getDoubleStr(actualPrice));
                HttpUtils.post(RequestConfig.request_scanGunPay)
                        .headers("sessionID", sessionID)
                        .upJson(postData.toJSONString())
                        .execute(new SimpleCallBack<String>() {

                            @Override
                            public void onError(ApiException e) {
                            }

                            @Override
                            public void onSuccess(String data) {
                                LogUtils.i("扫码枪支付完成后返回：" + data);
                                try {
                                    org.json.JSONObject object = new org.json.JSONObject(data);
                                    if (object.getLong("code") == 10000 &&
                                            object.getString("trade_status").equals("TRADE_SUCCESS")) {
                                        //支付成功
                                        saveAndPrint(EnumUtils.PayStatus.PAY_SUCCESS);
                                    } else {
                                        startPolling();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        if (event.getKeyCode() != KeyEvent.KEYCODE_BACK) {
            scanningView.analysisKeyEvent(event);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private Disposable disposable;

    //开始轮询-轮询该订单是否支付
    private void startPolling() {
        if (TextUtils.isEmpty(orderNo)) {
            ToastUtils.showShort("订单号查询失败");
            return;
        }
        //首次执行延时时间 、 每次轮询间隔时间 、 时间类型
        disposable = Observable.interval(1, 3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
                        // 逻辑代码
                        HttpUtils.get(RequestConfig.request_getPayStatus)
                                .headers("sessionID", sessionID)
                                .params("ordNo", orderNo)
                                .execute(new SimpleCallBack<String>() {

                                    @Override
                                    public void onError(ApiException e) {
                                        stopPolling();
                                    }

                                    @Override
                                    public void onSuccess(String data) {
                                        BaseBean baseBean = JsonUtils.parseJson(data, BaseBean.class);
                                        if (baseBean.getResult() != -1 && baseBean.getResult() != -2 && baseBean.getResult() != -4) {
                                            PollingOrderStatusBean statusBean = JsonUtils.parseJson(data, PollingOrderStatusBean.class);
                                            if (statusBean != null && statusBean.getResultObject() != null) {
                                                if (statusBean.getResultObject().getIsPayed() == 1) {
                                                    //支付成功-本地保存订单和打印
                                                    paymentMethod = statusBean.getResultObject().getPaymentMethod();
                                                    saveAndPrint(EnumUtils.PayStatus.PAY_SUCCESS);
                                                }
                                            }
                                        } else {
                                            DialogUtils.singleDialog((Activity) context, "提示", baseBean.getMessage(), "确定", null)
                                                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                        @Override
                                                        public void onDismiss(DialogInterface dialog) {
                                                        }
                                                    });
                                        }

                                    }
                                });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

    }

    //结束轮询
    private void stopPolling() {
        //停止轮询，销毁这个Disposable;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private void saveAndPrint(int payStatus) {
        stopPolling();
        int needVoice = SPUtils.getInstance().getInt(DataKey.KEY_IS_VOICE);

        String payTypeStr = "";
        if (paymentMethod.equals(EnumUtils.PayMethod.PAY_WXPAY)) {
            payTypeStr = "微信支付";
        } else if (paymentMethod.equals(EnumUtils.PayMethod.PAY_ALIPAY)) {
            payTypeStr = "支付宝支付";
        }
        if (saveOrderView.localSaveOrderDetail(orderNo, payStatus,
                orderPrice, actualPrice, promotionPrice, rebatePrice, changePrice, true)) {

            if (payStatus == EnumUtils.PayStatus.PAY_SUCCESS) {
                saveOrderView.showPrint(payTypeStr, orderNo, orderPrice,
                        actualPrice, promotionPrice, rebatePrice, changePrice);
                if (needVoice == 1 && voicePlay != null) {
                    voicePlay.play(paymentMethod, NumUtils.getDoubleStr(actualPrice));
                }
            }
        } else {
            ToastUtils.showShort("本地订单创建失败");
        }
        EventBus.getDefault().post(new EventMessage("clearShopCartData"));
        EventBus.getDefault().post(new EventMessage("payFinish"));
        if (isTakeOrder) {
            EventBus.getDefault().post(new EventMessage("takeOrderPayFinish"));
        }

        dismissDisplay();

        dismiss();
    }


    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用

        lp.width = (int) (d.widthPixels * 0.4);
        lp.height = (int) (d.heightPixels * 0.5);
        dialogWindow.setAttributes(lp);
    }

}

