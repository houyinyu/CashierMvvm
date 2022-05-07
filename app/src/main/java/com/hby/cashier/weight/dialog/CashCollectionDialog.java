package com.hby.cashier.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hby.cashier.R;
import com.hby.cashier.app.DataKey;
import com.hby.cashier.bean.CreateOrderBean;
import com.hby.cashier.bean.EventMessage;
import com.hby.cashier.bean.LitePalOrderDetailsBean;
import com.hby.cashier.bean.PrintMenuBean;
import com.hby.cashier.bean.ShopCartBean;
import com.hby.cashier.printer.PrinterServiceView;
import com.hby.cashier.second_screen.SecondScreenPayDisplay;
import com.hby.cashier.ui.MainActivity;
import com.hby.cashier.ui.tools.PriceCalculationView;
import com.hby.cashier.utils.DeviceUtils;
import com.hby.cashier.utils.EnumUtils;
import com.hby.cashier.utils.NumUtils;
import com.hyy.mvvm.base.BaseActivity;
import com.hyy.mvvm.utils.JsonUtils;
import com.hyy.mvvm.utils.SPUtils;
import com.hyy.mvvm.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 现金收款点击了弹出
 */
public class CashCollectionDialog extends Dialog {

    private BaseActivity context;
    private LayoutInflater inflater;


    public abstract static class DialogClickListener {
        public void cancel() {
        }

        public abstract void confirm(double orderPrice
                , double actualPrice, double promotionPrice, double rebatePrice, double changePrice);
    }

    private DialogClickListener clickListener;

    public CashCollectionDialog setOnDialogClickListener(DialogClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public CashCollectionDialog(BaseActivity context) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        setCustomView();
    }


    private List<ShopCartBean> shopCartList = new ArrayList<>();
    private double orderPrice = 0;

    public CashCollectionDialog setShopCartList(List<ShopCartBean> shopCartList) {
        this.shopCartList = shopCartList;
        orderPrice = PriceCalculationView.getProductPriceDefault(shopCartList);
        orderPriceText.setText("￥" + NumUtils.getDoubleStr(orderPrice));
        return this;
    }

    private String payModel = EnumUtils.PayMethod.PAY_CASH;

    public CashCollectionDialog setPayModel(String payModel) {
        this.payModel = payModel;
        return this;
    }

    private SecondScreenPayDisplay payDisplay;

    public CashCollectionDialog setPayDisplay(SecondScreenPayDisplay payDisplay) {
        this.payDisplay = payDisplay;
        return this;
    }


    /**
     * 设置整个弹出框的视图
     */
    private TextView orderPriceText;
    private TextView giveChangeText;

    public void setCustomView() {
        View mView = inflater.inflate(R.layout.dialog_cash_collection_layout, null);
        ImageView dismissImage = mView.findViewById(R.id.dialogCashCollection_dismissImage);
        orderPriceText = mView.findViewById(R.id.dialogCashCollection_orderPriceText);
        EditText collectionEdit = mView.findViewById(R.id.dialogCashCollection_collectionEdit);
        giveChangeText = mView.findViewById(R.id.dialogCashCollection_giveChangeText);
        TextView resetBtn = mView.findViewById(R.id.dialogCashCollection_resetBtn);
        TextView confirmBtn = mView.findViewById(R.id.dialogCashCollection_confirmBtn);

        context.showKeyBoard(collectionEdit);

        dismissImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventMessage("payFinish"));
                EventBus.getDefault().post(new EventMessage("hideKeyBoardMessage"));
                dismiss();
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionEdit.setText("");
                giveChangeText.setText("￥0.00");
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(collectionEdit.getText().toString().trim())) {
                    ToastUtils.showShort("请输入收款金额");
                    return;
                }
                if (!NumUtils.isPrice(collectionEdit.getText().toString().trim())) {
                    ToastUtils.showShort("您输入的价格有误");
                    return;
                }
//                if (NumUtils.getDouble(collectionEdit.getText().toString().trim()) < orderPrice) {
//                    ToastUtils.showShort("收款金额小于订单金额");
//                    return;
//                }

                if (clickListener != null) {
                    clickListener.confirm(orderPrice, NumUtils.getDouble(collectionEdit.getText().toString().trim()),
                            0, 0, NumUtils.getDouble(collectionEdit.getText().toString().trim()) - orderPrice);
                }
                dismiss();
            }
        });

        collectionEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().equals("")) return;
                if (!NumUtils.isPrice(s.toString().trim())) {
                    giveChangeText.setText("￥0.00");
                    return;
                }
                if (NumUtils.getDouble(s.toString().trim()) < orderPrice) {
                    giveChangeText.setText("￥0.00");
                    return;
                }
                double changePrice = NumUtils.getDouble(s.toString().trim()) - orderPrice;
                giveChangeText.setText("￥" + NumUtils.getDoubleStr(changePrice));

                int isNeedSecond = SPUtils.getInstance().getInt(DataKey.KEY_IS_SECOND_SCREEN);
                if (isNeedSecond == 1) {
                    if (payDisplay != null && !payDisplay.isShow) {
                        payDisplay.show();
                        payDisplay.updateRightLayout(payModel, NumUtils.getDouble(s.toString().trim()), "");
                    } else if (null != payDisplay) {
                        payDisplay.updateRightLayout(payModel, NumUtils.getDouble(s.toString().trim()), "");
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        super.setContentView(mView);
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

