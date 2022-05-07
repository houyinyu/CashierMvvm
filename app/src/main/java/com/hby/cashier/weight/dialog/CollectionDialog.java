package com.hby.cashier.weight.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hby.cashier.R;
import com.hby.cashier.app.DataKey;
import com.hby.cashier.bean.CreateOrderReturnBean;
import com.hby.cashier.bean.EventMessage;
import com.hby.cashier.bean.ShopCartBean;
import com.hby.cashier.http.RequestConfig;
import com.hby.cashier.second_screen.SecondScreenPayDisplay;
import com.hby.cashier.ui.MainActivity;
import com.hby.cashier.ui.tools.LocalSaveOrderView;
import com.hby.cashier.ui.tools.PriceCalculationView;
import com.hby.cashier.utils.EnumUtils;
import com.hby.cashier.utils.LogUtils;
import com.hby.cashier.utils.NetworkUtils;
import com.hby.cashier.utils.NumUtils;
import com.hby.cashier.view_model.TakeOrderModel;
import com.hby.cashier.voice.VoicePlay;
import com.hyy.mvvm.base.BaseActivity;
import com.hyy.mvvm.base.BaseViewModel;
import com.hyy.mvvm.bean.BaseBean;
import com.hyy.mvvm.http.HttpUtils;
import com.hyy.mvvm.utils.DialogUtils;
import com.hyy.mvvm.utils.JsonUtils;
import com.hyy.mvvm.utils.SPUtils;
import com.hyy.mvvm.utils.ToastUtils;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * 首页收款点击了弹出
 */
public class CollectionDialog extends Dialog {

    private BaseActivity context;
    private LayoutInflater inflater;


    public abstract static class DialogClickListener {
        public void cancel() {
        }

        public abstract void confirm();
    }

    private DialogClickListener clickListener;

    public CollectionDialog setOnDialogClickListener(DialogClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public CollectionDialog setOnDialogCancelListener(OnCancelListener cancelListener) {
        this.setOnCancelListener(cancelListener);
        return this;
    }

    public CollectionDialog(BaseActivity context) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        setCustomView();
    }

    private BaseViewModel viewModel;
    private boolean isTakeOrder = false;

    public CollectionDialog setModel(BaseViewModel viewModel) {
        this.viewModel = viewModel;
        if (viewModel instanceof TakeOrderModel) {
            isTakeOrder = true;
        }
        return this;
    }

    private SecondScreenPayDisplay payDisplay;

    public CollectionDialog setPayDisplay(SecondScreenPayDisplay payDisplay) {
        this.payDisplay = payDisplay;
        return this;
    }

    private VoicePlay voicePlay;

    public CollectionDialog setVoicePlay(VoicePlay voicePlay) {
        this.voicePlay = voicePlay;
        return this;
    }

    private List<ShopCartBean> shopCartList = new ArrayList<>();
    private LocalSaveOrderView saveOrderView;

    double orderPrice = 0;
    double actualPrice = 0;
    double promotionPrice = 0;
    double rebatePrice = 0;
    double changePrice = 0;

    public CollectionDialog setShopCartList(List<ShopCartBean> shopCartList) {
        this.shopCartList = shopCartList;
        orderPrice = PriceCalculationView.getProductPriceDefault(shopCartList);//订单原本价格
        double productPrice = PriceCalculationView.getProductPrice(shopCartList);//计算了活动的价格
        rebatePrice = PriceCalculationView.getRebatePrice(shopCartList);//返利价格
        promotionPrice = orderPrice - productPrice;
        actualPrice = productPrice - rebatePrice;

        orderPriceText.setText("￥" + NumUtils.getDoubleStr(orderPrice));
        if (promotionPrice > 0) {
            promotionPriceText.setText("￥" + NumUtils.getDoubleStr(promotionPrice));
        }

        rebatePriceText.setText("￥" + NumUtils.getDoubleStr(rebatePrice));
        payPriceText.setText("￥" + NumUtils.getDoubleStr(actualPrice));

        saveOrderView = new LocalSaveOrderView();
        saveOrderView.setShopCartList(shopCartList);
        return this;
    }

    private double getRebate(List<ShopCartBean> shopCartList) {
        return shopCartList.get(0).getProductList().get(0).getOnlineRebate();
    }

    private double getRebateUnit(List<ShopCartBean> shopCartList) {
        return shopCartList.get(0).getProductList().get(0).getOnlineRebateUnit();
    }

    /**
     * 设置整个弹出框的视图
     */
    private TextView orderPriceText;
    private TextView promotionPriceText;
    private TextView rebatePriceText;
    private TextView payPriceText;
    private String paymentMethod = "";
    private String paymentType = "";

    public void setCustomView() {
        View mView = inflater.inflate(R.layout.dialog_collection_layout, null);
        ImageView dismissImage = mView.findViewById(R.id.dialogCollection_dismissImage);
        orderPriceText = mView.findViewById(R.id.dialogCollection_orderPriceText);
        promotionPriceText = mView.findViewById(R.id.dialogCollection_promotionPriceText);
        rebatePriceText = mView.findViewById(R.id.dialogCollection_rebatePriceText);
        payPriceText = mView.findViewById(R.id.dialogCollection_payPriceText);
        LinearLayout wxPayBtn = mView.findViewById(R.id.dialogCollection_wxPayBtn);
        LinearLayout aliPayBtn = mView.findViewById(R.id.dialogCollection_aliPayBtn);
        LinearLayout cashPayBtn = mView.findViewById(R.id.dialogCollection_cashPayBtn);
        LinearLayout freeOrderBtn = mView.findViewById(R.id.dialogCollection_freeOrderBtn);

        dismissImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //微信支付
        wxPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isNetworkConnected(context) || !NetworkUtils.isWifiConnected(context)) {
                    ToastUtils.showShort("微信支付仅支持线上收款，请检查您的网络状态");
                    return;
                }
//                if (TextUtils.isEmpty(payPriceEdit.getText().toString().trim())) {
//                    ToastUtils.showShort("请输入收款金额");
//                    return;
//                }
//                if (!NumUtils.isPrice(payPriceEdit.getText().toString().trim())) {
//                    ToastUtils.showShort("您输入的价格有误");
//                    return;
//                }
                paymentMethod = EnumUtils.PayMethod.PAY_WXPAY;
                paymentType = EnumUtils.PayType.PAY_ONLINE;
                saveOrderView.setPaymentMethod(paymentMethod);
                saveOrderView.setPaymentType(paymentType);

                createOrderOnLine();
            }
        });

        //支付宝支付
        aliPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isNetworkConnected(context) || !NetworkUtils.isWifiConnected(context)) {
                    ToastUtils.showShort("支付宝支付仅支持线上收款，请检查您的网络状态");
                    return;
                }
//                if (TextUtils.isEmpty(payPriceEdit.getText().toString().trim())) {
//                    ToastUtils.showShort("请输入收款金额");
//                    return;
//                }
//                if (!NumUtils.isPrice(payPriceEdit.getText().toString().trim())) {
//                    ToastUtils.showShort("您输入的价格有误");
//                    return;
//                }
                paymentMethod = EnumUtils.PayMethod.PAY_ALIPAY;
                paymentType = EnumUtils.PayType.PAY_ONLINE;
                saveOrderView.setPaymentMethod(paymentMethod);
                saveOrderView.setPaymentType(paymentType);

                createOrderOnLine();
            }
        });
        //现金支付
        cashPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasEventProduct()) {
                    ToastUtils.showShort("活动商品仅支持线上支付");
                    return;
                }

                paymentMethod = EnumUtils.PayMethod.PAY_CASH;
                paymentType = EnumUtils.PayType.PAY_OFFLINE;

                int isNeedSecond = SPUtils.getInstance().getInt(DataKey.KEY_IS_SECOND_SCREEN);
                if (isNeedSecond == 1) {
                    if (payDisplay != null && !payDisplay.isShow) {
                        payDisplay.show();
                        payDisplay.updateListData(paymentMethod, shopCartList);
                    } else if (null != payDisplay) {
                        payDisplay.updateListData(paymentMethod, shopCartList);
                    }
                }

                saveOrderView.setPaymentMethod(paymentMethod);
                saveOrderView.setPaymentType(paymentType);
                CashCollectionDialog dialog = new CashCollectionDialog(context);
                dialog.setShopCartList(shopCartList);
                dialog.setPayModel(paymentMethod);
                dialog.setPayDisplay(payDisplay);
                dialog.setOnDialogClickListener(new CashCollectionDialog.DialogClickListener() {
                    @Override
                    public void confirm(double orderPrice
                            , double actualPrice, double promotionPrice, double rebatePrice, double changePrice) {
                        String orderNo = getOffLineOrderNo();
                        if (saveOrderView.localSaveOrderDetail(orderNo, EnumUtils.PayStatus.PAY_SUCCESS
                                , orderPrice, actualPrice, promotionPrice, rebatePrice, changePrice, false)) {
                            saveOrderView.showPrint("现金收款", orderNo, orderPrice,
                                    actualPrice, promotionPrice, rebatePrice, changePrice);
                        } else {
                            ToastUtils.showShort("本地订单创建失败");
                        }

                        EventBus.getDefault().post(new EventMessage("payFinish"));
                        EventBus.getDefault().post(new EventMessage("clearShopCartData"));
                        EventBus.getDefault().post(new EventMessage("hideKeyBoardMessage"));
                        if (isTakeOrder) {
                            EventBus.getDefault().post(new EventMessage("takeOrderPayFinish"));
                        }
                        dismiss();
                    }
                });
                dialog.show();
            }
        });

        //免单支付
        freeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasEventProduct()) {
                    ToastUtils.showShort("活动商品仅支持线上支付");
                    return;
                }
//                if (TextUtils.isEmpty(payPriceEdit.getText().toString().trim())) {
//                    ToastUtils.showShort("请输入收款金额");
//                    return;
//                }
//                if (!NumUtils.isPrice(payPriceEdit.getText().toString().trim())) {
//                    ToastUtils.showShort("您输入的价格有误");
//                    return;
//                }

                int isNeedSecond = SPUtils.getInstance().getInt(DataKey.KEY_IS_SECOND_SCREEN);
                if (isNeedSecond == 1) {
                    if (payDisplay != null && !payDisplay.isShow) {
                        payDisplay.show();
                        payDisplay.updateListData(paymentMethod, shopCartList);
                    } else if (null != payDisplay) {
                        payDisplay.updateListData(paymentMethod, shopCartList);
                    }
                }

                paymentMethod = EnumUtils.PayMethod.PAY_FREE;
                paymentType = EnumUtils.PayType.PAY_OFFLINE;
                saveOrderView.setPaymentMethod(paymentMethod);
                saveOrderView.setPaymentType(paymentType);

//                double orderPrice = PriceCalculationView.getProductPriceDefault(shopCartList);
//                double actualPrice = NumUtils.getDouble(payPriceEdit.getText().toString().trim());
//                double promotionPrice = PriceCalculationView.getProductPriceDefault(shopCartList) -
//                        PriceCalculationView.getProductPrice(shopCartList);
//                double changePrice = 0;
                actualPrice = 0;
                promotionPrice = 0;
                rebatePrice = 0;
                changePrice = 0;
                String orderNo = getOffLineOrderNo();
                if (saveOrderView.localSaveOrderDetail(orderNo, EnumUtils.PayStatus.PAY_SUCCESS,
                        orderPrice, actualPrice, promotionPrice, rebatePrice, changePrice, false)) {
                    saveOrderView.showPrint("免单", orderNo, orderPrice,
                            actualPrice, promotionPrice, rebatePrice, changePrice);
                } else {
                    ToastUtils.showShort("本地订单创建失败");
                }

                if (payDisplay != null && payDisplay.isShow) {
                    payDisplay.dismiss();
                }

                EventBus.getDefault().post(new EventMessage("clearShopCartData"));
                EventBus.getDefault().post(new EventMessage("payFinish"));
                if (isTakeOrder) {
                    EventBus.getDefault().post(new EventMessage("takeOrderPayFinish"));
                }
                dismiss();
            }
        });
        super.setContentView(mView);
    }


    /**
     * 获取线上支付数据-订单和二维码
     * 获取成功后轮询支付状态
     */
    private void createOrderOnLine() {
//        double orderPrice = PriceCalculationView.getProductPriceDefault(shopCartList);
//        double actualPrice = NumUtils.getDouble(payPriceEdit.getText().toString().trim());
//        double promotionPrice = PriceCalculationView.getProductPriceDefault(shopCartList) -
//                PriceCalculationView.getProductPrice(shopCartList);
//        double changePrice = 0;

        viewModel.showDialog();
        String sessionID = SPUtils.getInstance().getString(DataKey.KEY_SESSION_ID);
        HttpUtils.post(RequestConfig.request_createOrder)
                .headers("sessionID", sessionID)
                .upJson(JsonUtils.Object2Json(saveOrderView.getCreateOrderData("")))
                .execute(new SimpleCallBack<String>() {

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        viewModel.dismissDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        viewModel.dismissDialog();
                    }

                    @Override
                    public void onSuccess(String data) {
                        BaseBean baseBean = JsonUtils.parseJson(data, BaseBean.class);
                        if (baseBean.getResult() != -1 && baseBean.getResult() != -2 && baseBean.getResult() != -4) {
                            CreateOrderReturnBean returnBean = JsonUtils.parseJson(data, CreateOrderReturnBean.class);

                            String orderNo = getOffLineOrderNo();
                            String urlData = "";

                            if (returnBean != null && returnBean.getResultObject() != null && returnBean.getResultObject().getCheckoutPaymentResult() != null) {
                                urlData = returnBean.getResultObject().getCheckoutPaymentResult().getData();
                                orderNo = returnBean.getResultObject().getCheckoutPaymentResult().getOrderNo();
                            }
                            PaymentCallbackDialog callbackDialog = new PaymentCallbackDialog(context);
                            callbackDialog.setOrderNo(orderNo);
                            callbackDialog.setPaymentMethod(paymentMethod);
                            callbackDialog.setPaymentType(paymentType);
                            callbackDialog.setTakeOrder(isTakeOrder);
                            callbackDialog.setVoicePlay(voicePlay);
                            callbackDialog.setPayDisplay(payDisplay, shopCartList, urlData);
                            callbackDialog.setPrice(orderPrice, actualPrice, promotionPrice, rebatePrice, changePrice);
                            callbackDialog.show();
                            dismiss();
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

    /**
     * 创建订单号
     *
     * @return
     */
    private String getOffLineOrderNo() {
        String serviceID = SPUtils.getInstance().getString(DataKey.KEY_SERVICE_ID);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("DD");
        stringBuffer.append(simpleDateFormat.format(new Date()));
        stringBuffer.append(serviceID);
        stringBuffer.append(System.currentTimeMillis() - (System.currentTimeMillis() / 86400000L * 86400000L - (long) TimeZone.getDefault().getRawOffset()));
        return stringBuffer.toString();
    }

    /**
     * 判断是否有活动商品，有活动商品只能走线上
     */
    private boolean hasEventProduct() {
        for (int i = 0; i < shopCartList.size(); i++) {
            if (shopCartList.get(i).getEventType() > 0) {
                //有活动
                return true;
            }
        }
        return false;
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

        lp.width = (int) (d.widthPixels * 0.5);
        lp.height = (int) (d.heightPixels * 0.6);
        dialogWindow.setAttributes(lp);
    }

}

