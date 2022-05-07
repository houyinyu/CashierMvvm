package com.hby.cashier.second_screen;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hby.cashier.R;
import com.hby.cashier.bean.ShopCartBean;
import com.hby.cashier.ui.tools.PriceCalculationView;
import com.hby.cashier.utils.DensityUtils;
import com.hby.cashier.utils.EnumUtils;
import com.hby.cashier.utils.LogUtils;
import com.hby.cashier.utils.NumUtils;
import com.hby.cashier.utils.QRCodeUtil;
import com.hyy.mvvm.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xutils.common.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付的时候显示
 * mail  gaolulin@sunmi.com
 */

public class SecondScreenPayDisplay extends BasePresentation {

    private Context context;

    public SecondScreenPayDisplay(Context outerContext, Display display) {
        super(outerContext, display);
        this.context = outerContext;
    }

    private RecyclerView productRecycler;
    private TextView totalNum;
    private TextView totalPrice1;
    private TextView totalPrice2;
    //线下收款UI
    private LinearLayout offLineLayout;
    private TextView offLineText;
    private TextView offLineOrderPrice;
    private TextView collectionPriceText;
    private TextView giveChangePriceText;
    private TextView giveChangePrice;

    //线上收款UI
    private LinearLayout onLineLayout;
    private TextView onLineText;
    private ImageView codeImage;
    private TextView errorCodeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_screen_pay_layout);
        productRecycler = findViewById(R.id.includeScreenPayLeft_productRecycler);
        totalNum = findViewById(R.id.includeScreenPayLeft_totalNum);
        totalPrice1 = findViewById(R.id.includeScreenPayLeft_totalPrice1);
        totalPrice2 = findViewById(R.id.includeScreenPayLeft_totalPrice2);

        offLineLayout = findViewById(R.id.includeScreenPayRight_offLineLayout);
        offLineText = findViewById(R.id.includeScreenPayRight_offLineText);
        offLineOrderPrice = findViewById(R.id.includeScreenPayRight_offLineOrderPrice);
        collectionPriceText = findViewById(R.id.includeScreenPayRight_collectionPrice);
        giveChangePriceText = findViewById(R.id.includeScreenPayRight_giveChangePriceText);
        giveChangePrice = findViewById(R.id.includeScreenPayRight_giveChangePrice);

        onLineLayout = findViewById(R.id.includeScreenPayRight_onLineLayout);
        onLineText = findViewById(R.id.includeScreenPayRight_onLineText);
        codeImage = findViewById(R.id.includeScreenPayRight_codeImage);
        errorCodeText = findViewById(R.id.includeScreenPayRight_errorCodeText);
    }


    private List<ShopCartBean> shopCartList = new ArrayList<>();

    public void updateListData(String paymentMethod, List<ShopCartBean> shopCartList) {
        this.shopCartList = shopCartList;
        setProductAdapter();

        offLineLayout.setVisibility(View.GONE);
        onLineLayout.setVisibility(View.GONE);
        switch (paymentMethod) {
            case EnumUtils.PayMethod.PAY_WXPAY:
                onLineLayout.setVisibility(View.VISIBLE);
                break;
            case EnumUtils.PayMethod.PAY_ALIPAY:
                onLineLayout.setVisibility(View.VISIBLE);
                break;
            case EnumUtils.PayMethod.PAY_CASH:
                offLineLayout.setVisibility(View.VISIBLE);
                Drawable drawableCash = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_pay_screen_cash, null);
                drawableCash.setBounds(0, 0, drawableCash.getMinimumWidth(),
                        drawableCash.getMinimumHeight());
                offLineText.setCompoundDrawables(drawableCash, null, null, null);

                double orderPrice = PriceCalculationView.getProductPriceDefault(shopCartList);
                offLineText.setText("现金付款");
                offLineOrderPrice.setText("￥" + NumUtils.getDoubleStr(orderPrice));
                giveChangePriceText.setVisibility(View.VISIBLE);
                giveChangePrice.setVisibility(View.VISIBLE);
                collectionPriceText.setText("￥0");
                giveChangePrice.setText("￥0");
                break;
            case EnumUtils.PayMethod.PAY_FREE:
                offLineLayout.setVisibility(View.VISIBLE);
                Drawable drawableFree = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_pay_screen_free, null);
                drawableFree.setBounds(0, 0, drawableFree.getMinimumWidth(),
                        drawableFree.getMinimumHeight());
                offLineText.setCompoundDrawables(drawableFree, null, null, null);

                offLineOrderPrice.setText("￥" + NumUtils.getDoubleStr(PriceCalculationView.getProductPriceDefault(shopCartList)));
                offLineText.setText("免单付款");
                collectionPriceText.setText("￥0");

                giveChangePriceText.setVisibility(View.GONE);
                giveChangePrice.setVisibility(View.GONE);
                break;
        }
    }

    public void updateRightLayout(String paymentMethod, double collectionPrice, String payUrl) {
        if (paymentMethod.equals(EnumUtils.PayMethod.PAY_WXPAY) || paymentMethod.equals(EnumUtils.PayMethod.PAY_ALIPAY)) {
            if (TextUtils.isEmpty(payUrl)) {
                errorCodeText.setVisibility(View.VISIBLE);
                return;
            }
        }
        offLineLayout.setVisibility(View.GONE);
        onLineLayout.setVisibility(View.GONE);
        int imageCodeW = DensityUtils.getScreenW((Activity) context) * 3 / 10 - DensityUtil.dip2px(44);
        switch (paymentMethod) {
            case EnumUtils.PayMethod.PAY_WXPAY:
                onLineLayout.setVisibility(View.VISIBLE);
                onLineText.setText("微信付款");
                Drawable drawableWx = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_pay_screen_wx, null);
                drawableWx.setBounds(0, 0, drawableWx.getMinimumWidth(),
                        drawableWx.getMinimumHeight());
                onLineText.setCompoundDrawables(drawableWx, null, null, null);

                codeImage.setImageBitmap(QRCodeUtil.createQRImage(payUrl,
                        imageCodeW, imageCodeW));
                codeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLineText.setText("我点击了");
                    }
                });
                break;
            case EnumUtils.PayMethod.PAY_ALIPAY:
                onLineLayout.setVisibility(View.VISIBLE);
                onLineText.setText("支付宝付款");
                Drawable drawableAli = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_pay_screen_ali, null);
                drawableAli.setBounds(0, 0, drawableAli.getMinimumWidth(),
                        drawableAli.getMinimumHeight());
                onLineText.setCompoundDrawables(drawableAli, null, null, null);

                codeImage.setImageBitmap(QRCodeUtil.createQRImage(payUrl,
                        imageCodeW, imageCodeW));
                break;
            case EnumUtils.PayMethod.PAY_CASH:
                offLineLayout.setVisibility(View.VISIBLE);
                double orderPrice = PriceCalculationView.getProductPriceDefault(shopCartList);
                collectionPriceText.setText("￥" + NumUtils.getDoubleStr(collectionPrice));
                giveChangePrice.setText("￥" + NumUtils.getDoubleStr(collectionPrice - orderPrice));
                break;
            case EnumUtils.PayMethod.PAY_FREE:
                offLineLayout.setVisibility(View.VISIBLE);
                break;
        }

    }

    /**
     * 设置商品数据
     */
    private ScreenPayProductAdapter productAdapter;

    private void setProductAdapter() {
        List<ShopCartBean.ProductListBean> productList = new ArrayList<>();
        for (int i = 0; i < shopCartList.size(); i++) {
            productList.addAll(shopCartList.get(i).getProductList());
        }
        if (productAdapter == null) {
            productAdapter = new ScreenPayProductAdapter(context, productList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            productRecycler.setLayoutManager(layoutManager);
            productRecycler.setAdapter(productAdapter);
        } else {
            productAdapter.setList(productList);
        }

        totalNum.setText(String.valueOf(PriceCalculationView.getProductNum(shopCartList)));
        totalPrice1.setText("￥" + NumUtils.getDoubleStr(PriceCalculationView.getProductPriceDefault(shopCartList)));
        totalPrice2.setText("￥" + NumUtils.getDoubleStr(PriceCalculationView.getProductPriceDefault(shopCartList)));
    }

    public class ScreenPayProductAdapter extends BaseQuickAdapter<ShopCartBean.ProductListBean, BaseViewHolder> {
        private Context context;

        public ScreenPayProductAdapter(Context context, @Nullable List<ShopCartBean.ProductListBean> data) {
            super(R.layout.item_screen_pay_product, data);
            this.context = context;
        }

        @Override
        protected void convert(@NotNull BaseViewHolder holder, ShopCartBean.ProductListBean bean) {
            holder.setText(R.id.itemScreenPyProduct_itemPos, String.valueOf(holder.getAdapterPosition() + 1))
                    .setText(R.id.itemScreenPyProduct_productName, bean.getProductName())
                    .setText(R.id.itemScreenPyProduct_productNum, String.valueOf(bean.getProductNum()))
                    .setText(R.id.itemScreenPyProduct_productPrice, "￥" + NumUtils.getDoubleStr(bean.getProductPrice()));

            LinearLayout parentLayout = holder.getView(R.id.itemScreenPyProduct_layout);
            parentLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.color_f5));
            if (holder.getAdapterPosition() % 2 == 0) {
                //偶数
                parentLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.color_white));
            }
        }
    }


    @Override
    public void show() {
        super.show();

    }


    @Override
    public void onSelect(boolean isShow) {

    }
}
