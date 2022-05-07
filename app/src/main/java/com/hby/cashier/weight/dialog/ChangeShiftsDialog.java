package com.hby.cashier.weight.dialog;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.annotations.SerializedName;
import com.hby.cashier.R;
import com.hby.cashier.adapter.DialogChangeShiftsAdapter;
import com.hby.cashier.app.DataKey;
import com.hby.cashier.bean.CreateOrderBean;
import com.hby.cashier.bean.DialogChangeShiftBean;
import com.hby.cashier.bean.LitePalChangeShiftRecordBean;
import com.hby.cashier.bean.LitePalOrderDetailsBean;
import com.hby.cashier.bean.PrintMenuBean;
import com.hby.cashier.bean.UpdateChangeShiftsBean;
import com.hby.cashier.http.RequestConfig;
import com.hby.cashier.printer.PrinterServiceView;
import com.hby.cashier.ui.LoginActivity;
import com.hby.cashier.ui.tools.DataSyncView;
import com.hby.cashier.utils.EnumUtils;
import com.hby.cashier.utils.LogUtils;
import com.hby.cashier.utils.NumUtils;
import com.hby.cashier.utils.TimeUtils;
import com.hby.cashier.view_model.MainViewModel;
import com.hyy.mvvm.bean.BaseBean;
import com.hyy.mvvm.http.HttpUtils;
import com.hyy.mvvm.utils.JsonUtils;
import com.hyy.mvvm.utils.SPUtils;
import com.hyy.mvvm.utils.ToastUtils;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


/**
 * 交接班弹框
 */
public class ChangeShiftsDialog extends Dialog {

    private Context context;
    private LayoutInflater inflater;


    public abstract static class DialogClickListener {
        public void cancel() {
        }

        public abstract void confirm();
    }

    private DialogClickListener clickListener;

    public ChangeShiftsDialog setOnDialogClickListener(DialogClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public ChangeShiftsDialog setOnDialogCancelListener(OnCancelListener cancelListener) {
        this.setOnCancelListener(cancelListener);
        return this;
    }

    public ChangeShiftsDialog(Context context) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        setCustomView();
    }

    private MainViewModel viewModel;

    public ChangeShiftsDialog setViewModel(MainViewModel viewModel) {
        this.viewModel = viewModel;
        return this;
    }

    /**
     * 设置整个弹出框的视图
     */
    private RecyclerView recordRecycler;
    private TextView cashierUser;
    private TextView cashierTime;

    private long loginTimeStamp;
    private long nowTimeStamp;
    private boolean isPrint = false;

    public void setCustomView() {
        View mView = inflater.inflate(R.layout.dialog_change_shifts_layout, null);
        ImageView dismissImage = mView.findViewById(R.id.dialogChangeShifts_dismissImage);
        cashierUser = mView.findViewById(R.id.dialogChangeShifts_cashier);
        cashierTime = mView.findViewById(R.id.dialogChangeShifts_cashierTime);
        recordRecycler = mView.findViewById(R.id.dialogChangeShifts_recordRecycler);
        TextView recordBtn = mView.findViewById(R.id.dialogChangeShifts_recordBtn);
        TextView changeBtn = mView.findViewById(R.id.dialogChangeShifts_changeBtn);
        TextView loginOutBtn = mView.findViewById(R.id.dialogChangeShifts_loginOutBtn);
        CheckBox printBtn = mView.findViewById(R.id.dialogChangeShifts_printBtn);

        //设置收银员和时间
        cashierUser.setText(SPUtils.getInstance().getString(DataKey.KEY_USER_NAME));
        loginTimeStamp = SPUtils.getInstance().getLong(DataKey.KEY_LOGIN_TIME_STAMP);
        nowTimeStamp = System.currentTimeMillis();
        cashierTime.setText(TimeUtils.getTime(loginTimeStamp) + "~" + TimeUtils.getTime(nowTimeStamp));

        int isNeedChange = SPUtils.getInstance().getInt(DataKey.KEY_IS_CHANGE);
        changeBtn.setVisibility(View.GONE);
        loginOutBtn.setVisibility(View.GONE);
        if (isNeedChange == 0) {
            loginOutBtn.setVisibility(View.VISIBLE);
        } else {
            changeBtn.setVisibility(View.VISIBLE);
        }

        dismissImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChangeShiftsRecordDialog(context).show();
            }
        });
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.showDialog();
                if (isPrint) {
                    if (changeShiftList.size() == 0) {
                        ToastUtils.showShort("暂无信息可打印");
                        return;
                    }
                    List<PrintMenuBean.ChangeShiftListBean> changeShiftPrintList = new ArrayList<>();
                    for (int i = 0; i < changeShiftList.size(); i++) {
                        PrintMenuBean.ChangeShiftListBean printBean = new PrintMenuBean.ChangeShiftListBean();
                        printBean.setCollectionType(EnumUtils.getPaymentMethodStr(changeShiftList.get(i).getOrdPaymentMethod()));
                        printBean.setCollectionNum(changeShiftList.get(i).getTotalNum());
                        printBean.setCollectionPrice(changeShiftList.get(i).getTotalPrice());
                        changeShiftPrintList.add(printBean);
                    }
                    PrinterServiceView printView = PrinterServiceView.getInstance();
                    String jsonData = printView.buildChangeShiftData(loginTimeStamp, nowTimeStamp, changeShiftPrintList);
                    printView.paySuccessToPrinter(jsonData, "");
                }

                //把记录保存在本地
                LitePalChangeShiftRecordBean recordBean = new LitePalChangeShiftRecordBean();
                recordBean.setUserName(SPUtils.getInstance().getString(DataKey.KEY_USER_NAME));
                recordBean.setUserMid(SPUtils.getInstance().getString(DataKey.KEY_USER_MID));
                recordBean.setUserId(SPUtils.getInstance().getString(DataKey.KEY_USER_ID));
                recordBean.setShopName(SPUtils.getInstance().getString(DataKey.KEY_SHOP_NAME));
                recordBean.setShopId(SPUtils.getInstance().getString(DataKey.KEY_SHOP_ID));
                recordBean.setSyDeviceId("dev_" + SPUtils.getInstance().getString(DataKey.KEY_DEVICE_ID));
                recordBean.setLoginTimeStamp(loginTimeStamp);
                recordBean.setOutTimeStamp(nowTimeStamp);
                List<String> orderNos = new ArrayList<>();
                for (int i = 0; i < cashierOrderList.size(); i++) {
                    orderNos.add(cashierOrderList.get(i).getOrdNo());
                }
                recordBean.setOrderNoJson(JsonUtils.Object2Json(orderNos));
                recordBean.setPrintJson(JsonUtils.Object2Json(changeShiftList));
                recordBean.setUpdate(false);
                recordBean.save();

                getUpdateChangeShiftRecord();
            }
        });

        //没有配置交接班-直接退出
        loginOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPrint) {
                    if (changeShiftList.size() == 0) {
                        ToastUtils.showShort("暂无信息可打印");
                        return;
                    }
                    List<PrintMenuBean.ChangeShiftListBean> changeShiftPrintList = new ArrayList<>();
                    for (int i = 0; i < changeShiftList.size(); i++) {
                        PrintMenuBean.ChangeShiftListBean printBean = new PrintMenuBean.ChangeShiftListBean();
                        printBean.setCollectionType(EnumUtils.getPaymentMethodStr(changeShiftList.get(i).getOrdPaymentMethod()));
                        printBean.setCollectionNum(changeShiftList.get(i).getTotalNum());
                        printBean.setCollectionPrice(changeShiftList.get(i).getTotalPrice());
                        changeShiftPrintList.add(printBean);
                    }
                    PrinterServiceView printView = PrinterServiceView.getInstance();
                    String jsonData = printView.buildChangeShiftData(loginTimeStamp, nowTimeStamp, changeShiftPrintList);
                    printView.paySuccessToPrinter(jsonData, "");
                }
                //保存后退出登录
                if (clickListener != null) clickListener.confirm();
            }
        });
        printBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPrint = isChecked;
            }
        });
        initRecord();

        super.setContentView(mView);
    }




    /**
     * 上传交接班记录到服务器
     */
    private void getUpdateChangeShiftRecord() {
        List<LitePalChangeShiftRecordBean> changeShiftRecordList =
                LitePal.where("isupdate = ?", "0").find(LitePalChangeShiftRecordBean.class);

        for (int i = 0; i < changeShiftRecordList.size(); i++) {
            updateChangeShift(changeShiftRecordList.get(i));
        }

        viewModel.dismissDialog();

        //上传后退出登录
        if (clickListener != null) clickListener.confirm();
    }

    private void updateChangeShift(LitePalChangeShiftRecordBean changeShiftRecordBean) {

        String sessionID = SPUtils.getInstance().getString(DataKey.KEY_SESSION_ID);
        HttpUtils.post(RequestConfig.request_uploadChangeShifts)
                .headers("sessionID", sessionID)
                .upJson(JsonUtils.Object2Json(getUpdateChangeShiftRecordData(changeShiftRecordBean)))
                .execute(new SimpleCallBack<String>() {

                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String data) {
                        BaseBean baseBean = JsonUtils.parseJson(data, BaseBean.class);
                        if (baseBean.getResult() != -1 && baseBean.getResult() != -2 && baseBean.getResult() != -4) {
                            //交接班记录上传成功
                            changeShiftRecordBean.setUpdate(true);
                            changeShiftRecordBean.save();
//                            ContentValues values = new ContentValues();
//                            values.put("isupdate", "1");
//
//                            LitePal.updateAll(LitePalChangeShiftRecordBean.class, values,
//                                    "ordno = ? ", createOrderBean.getOrdNo());
                        }

                    }
                });
    }

    private UpdateChangeShiftsBean getUpdateChangeShiftRecordData(LitePalChangeShiftRecordBean changeShiftRecordBean) {
        UpdateChangeShiftsBean updateChangeShiftsBean = new UpdateChangeShiftsBean();
        List<DialogChangeShiftBean> upShiftList = JSON.parseArray(changeShiftRecordBean.getPrintJson(), DialogChangeShiftBean.class);

        UpdateChangeShiftsBean.ChangeShiftsDTO shiftsDTO = new UpdateChangeShiftsBean.ChangeShiftsDTO();
        shiftsDTO.setLoginTime(TimeUtils.getTime(changeShiftRecordBean.getLoginTimeStamp()));
        shiftsDTO.setLogoutTime(TimeUtils.getTime(changeShiftRecordBean.getOutTimeStamp()));
        shiftsDTO.setUserName(changeShiftRecordBean.getUserName());
        shiftsDTO.setUserId(changeShiftRecordBean.getUserId());
        shiftsDTO.setMid(changeShiftRecordBean.getUserMid());
        shiftsDTO.setShopId(changeShiftRecordBean.getShopId());
        shiftsDTO.setShopName(changeShiftRecordBean.getShopName());
        shiftsDTO.setSyDeviceId(changeShiftRecordBean.getSyDeviceId());

        if (upShiftList.size() == 0) {
            updateChangeShiftsBean.setChangeShifts(shiftsDTO);
            return updateChangeShiftsBean;
        }

        for (int i = 0; i < upShiftList.size(); i++) {
            int totalNum = upShiftList.get(i).getTotalNum();
            double totalPrice = upShiftList.get(i).getTotalPrice();
            switch (upShiftList.get(i).getOrdPaymentMethod()) {
                case EnumUtils.PayMethod.PAY_WXPAY:
                    shiftsDTO.setWxOrderAmt(totalPrice);
                    shiftsDTO.setWxOrderNum(totalNum);
                    break;
                case EnumUtils.PayMethod.PAY_ALIPAY:
                    shiftsDTO.setAliOrderAmt(totalPrice);
                    shiftsDTO.setAliOrderNum(totalNum);
                    break;
                case EnumUtils.PayMethod.PAY_CASH:
                    shiftsDTO.setCashOrderAmt(totalPrice);
                    shiftsDTO.setCashOrderNum(totalNum);
                    break;
                case EnumUtils.PayMethod.PAY_FREE:
                    shiftsDTO.setFreeOrderAmt(totalPrice);
                    shiftsDTO.setFreeOrderNum(totalNum);
                    break;
                case EnumUtils.PayMethod.PAY_RETURN:
                    shiftsDTO.setRepayOrderAmt(totalPrice);
                    shiftsDTO.setRepayOrderNum(totalNum);
                    break;
            }
        }
        updateChangeShiftsBean.setChangeShifts(shiftsDTO);
        updateChangeShiftsBean.setOrdNos(JSON.parseArray(changeShiftRecordBean.getOrderNoJson(), String.class));
        return updateChangeShiftsBean;
    }

    /**
     * 获取订单数据
     */
    private List<LitePalOrderDetailsBean> cashierOrderList = new ArrayList<>();
    private List<DialogChangeShiftBean> changeShiftList = new ArrayList<>();

    private void initRecord() {
        //获取登录时间到现在的数据库订单
        cashierOrderList =
                LitePal.where("createtimestamp > ? and createtimestamp < ?", String.valueOf(loginTimeStamp),
                        String.valueOf(nowTimeStamp)).find(LitePalOrderDetailsBean.class);

        changeShiftList = new ArrayList<>();
        for (int i = 0; i < cashierOrderList.size(); i++) {
            if (changeShiftList.size() == 0) {
                DialogChangeShiftBean changeShiftBean = new DialogChangeShiftBean();
                changeShiftBean.setOrdPaymentMethod(cashierOrderList.get(i).getOrdPaymentMethod());
                changeShiftBean.setTotalNum(1);
                changeShiftBean.setTotalPrice(getOrderPrice(cashierOrderList.get(i)));
                changeShiftList.add(changeShiftBean);
            } else {
                boolean paymentExist = false;
                for (int j = 0; j < changeShiftList.size(); j++) {
                    if (changeShiftList.get(j).getOrdPaymentMethod().equals(cashierOrderList.get(i).getOrdPaymentMethod())) {
                        paymentExist = true;
                        changeShiftList.get(j).setTotalNum(changeShiftList.get(j).getTotalNum() + 1);
                        changeShiftList.get(j).setTotalPrice(changeShiftList.get(j).getTotalPrice()
                                + getOrderPrice(cashierOrderList.get(i)));
                        break;
                    }
                }
                if (!paymentExist) {
                    DialogChangeShiftBean changeShiftBean = new DialogChangeShiftBean();
                    changeShiftBean.setOrdPaymentMethod(cashierOrderList.get(i).getOrdPaymentMethod());
                    changeShiftBean.setTotalNum(1);
                    changeShiftBean.setTotalPrice(getOrderPrice(cashierOrderList.get(i)));
                    changeShiftList.add(changeShiftBean);
                }
            }
        }
        setRecordAdapter();
    }

    /**
     * 这里如果是微信或者支付宝支付就取实收价
     * 如果是现金、免单，就取订单价
     */
    private double getOrderPrice(LitePalOrderDetailsBean orderDetailsBean) {
        switch (orderDetailsBean.getOrdPaymentMethod()) {
            case EnumUtils.PayMethod.PAY_ALIPAY:
            case EnumUtils.PayMethod.PAY_WXPAY:
                return orderDetailsBean.getActualPrice();
            case EnumUtils.PayMethod.PAY_CASH:
            case EnumUtils.PayMethod.PAY_FREE:
                return orderDetailsBean.getOrderPrice();
        }
        return 0;
    }


    /**
     * 交接班-收银记录
     */
    private void setRecordAdapter() {
        DialogChangeShiftsAdapter recordAdapter = new DialogChangeShiftsAdapter(context, changeShiftList);
        recordRecycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        recordRecycler.setAdapter(recordAdapter);
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
        lp.height = (int) (d.heightPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }

}

