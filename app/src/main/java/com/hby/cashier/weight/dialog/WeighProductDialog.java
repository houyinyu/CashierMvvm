package com.hby.cashier.weight.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hby.cashier.R;
import com.hby.cashier.bean.LitePalProductBean;
import com.hby.cashier.bean.ShopCartBean;
import com.hby.cashier.scale.ScaleView;
import com.hby.cashier.utils.LogUtils;
import com.hby.cashier.utils.NumUtils;
import com.hyy.mvvm.base.BaseActivity;
import com.hyy.mvvm.utils.ToastUtils;

import java.text.DecimalFormat;


/**
 * 称重商品
 */
public class WeighProductDialog extends Dialog {

    private BaseActivity context;
    private LayoutInflater inflater;


    public abstract static class DialogClickListener {
        public void cancel() {
        }

        public abstract void confirm(ShopCartBean.ProductListBean productBean);
    }

    private DialogClickListener clickListener;

    public WeighProductDialog setOnDialogClickListener(DialogClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public WeighProductDialog(BaseActivity context) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        setCustomView();
    }


    private LitePalProductBean productBean;

    @SuppressLint("SetTextI18n")
    public WeighProductDialog setProductData(LitePalProductBean productBean) {
        this.productBean = productBean;
        priceText.setText(NumUtils.getProductPrice(productBean).toString());
        return this;
    }

    private boolean isStable = false;
    private ScaleView scaleView;
    private String weighStr = "";

    public WeighProductDialog setScalePresenter(ScaleView scaleView) {
        this.scaleView = scaleView;
        scaleView.setScaleChangeListener(new ScaleView.ScaleChangeListener() {

            @Override
            public void scaleChange(int net, int pnet, int status) {
                weighStr = formatQuality(net);

                isStable = (status & 1) == 1;
                if (productBean != null) {
                    payPriceText.setText(formatTotalMoney(net, NumUtils.getProductPrice(productBean)));//总价
                }

                //超载
                if ((status & 4) == 4) {
                    weighStr = "0";
                    payPriceText.setText("￥0");
                } else if ((pnet + net) < 0) {
                    //欠载
                    weighStr = "0";
                    payPriceText.setText("￥0");
                }
                weightText.setText(weighStr + "kg");
            }

            @Override
            public void scaleUser(boolean canUse) {
                if (!canUse) {
                    weightText.setText("0kg");
                    payPriceText.setText("￥0");
                }
            }

        });
        return this;
    }


    /**
     * 设置整个弹出框的视图
     */
    private TextView priceText;
    private TextView weightText;
    private TextView payPriceText;

    public void setCustomView() {
        View mView = inflater.inflate(R.layout.dialog_weigh_product, null);
        ImageView dismissImage = mView.findViewById(R.id.dialogNonStandardProduct_dismissImage);
        priceText = mView.findViewById(R.id.dialogNonStandardProduct_priceText);
        weightText = mView.findViewById(R.id.dialogNonStandardProduct_weightText);
        payPriceText = mView.findViewById(R.id.dialogNonStandardProduct_payPriceText);
        TextView cancelBtn = mView.findViewById(R.id.dialogNonStandardProduct_cancelBtn);
        TextView confirmBtn = mView.findViewById(R.id.dialogNonStandardProduct_confirmBtn);

        dismissImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(weighStr) || weighStr.equals("0")) {
                    ToastUtils.showShort("未获取到称重信息");
                    return;
                }
                if (!isStable) {
                    ToastUtils.showShort("请等待称稳定");
                    return;
                }
                if (clickListener != null) clickListener.confirm(getShopCartData(productBean));
                dismiss();
            }
        });
        super.setContentView(mView);
    }

    /**
     * 获取购物车数据
     *
     * @param palProductBean
     * @return
     */
    private ShopCartBean.ProductListBean getShopCartData(LitePalProductBean palProductBean) {
        ShopCartBean.ProductListBean productBean = new ShopCartBean.ProductListBean();
        productBean.setProductName(palProductBean.getProName());
        productBean.setProductNum(Double.parseDouble(weighStr));
        productBean.setProductPrice(NumUtils.getProductPrice(palProductBean));
        productBean.setProductID(palProductBean.getProId());
        productBean.setProductImage(palProductBean.getImgUrl());
        productBean.setSpecificationUnitID(palProductBean.getProSpecificationUnitId());
        productBean.setShopID(palProductBean.getShopId());
        productBean.setSpecName(palProductBean.getSpecification());
        productBean.setSpecID(palProductBean.getSpecificationId());
        productBean.setUnitName(palProductBean.getUnitName());
        productBean.setUnitID(palProductBean.getUnitId());
        productBean.setBarCode(palProductBean.getBarCode());
        productBean.setBrandID(palProductBean.getProBrandId());
        productBean.setBrandName(palProductBean.getProBrandName());
        productBean.setManufacturer(palProductBean.getProManufacturersName());
        productBean.setEventID(palProductBean.getEventID());
        productBean.setEventType(palProductBean.getEventType());
        productBean.setIsInBulk(palProductBean.getIsInBulk());
        productBean.setIsMinUnit(productBean.getIsMinUnit());
        productBean.setIsStandard(palProductBean.getIsStandardProduct());
        productBean.setOnlineRebate(palProductBean.getGroupOnlineRebate());
        productBean.setOnlineRebateUnit(palProductBean.getGroupOnlineRebateUnit());

        return productBean;
    }


    private DecimalFormat decimalFormat = new DecimalFormat("0.000");

    public String formatQuality(int net) {
        return decimalFormat.format(net * 1.0f / 1000);
    }

    public String formatTotalMoney(int net, double price) {
        return NumUtils.getDoubleStr(net * price / 1000);
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

        lp.width = (int) (d.widthPixels * 0.3);
        lp.height = (int) (d.heightPixels * 0.5);
        dialogWindow.setAttributes(lp);
    }

}

