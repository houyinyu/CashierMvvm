package com.hby.cashier.weight.dialog;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hby.cashier.R;
import com.hby.cashier.app.DataKey;
import com.hby.cashier.bean.ShopCartBean;
import com.hby.cashier.utils.NumUtils;
import com.hyy.mvvm.base.BaseActivity;
import com.hyy.mvvm.utils.SPUtils;
import com.hyy.mvvm.utils.ToastUtils;


/**
 * 无码商品
 */
public class CodelessProductDialog extends Dialog {

    private BaseActivity context;
    private LayoutInflater inflater;


    public abstract static class DialogClickListener {
        public void cancel() {
        }

        public abstract void confirm(ShopCartBean.ProductListBean productBean);
    }

    private DialogClickListener clickListener;

    public CodelessProductDialog setOnDialogClickListener(DialogClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public CodelessProductDialog(BaseActivity context) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        setCustomView();
    }


    /**
     * 设置整个弹出框的视图
     */
    public void setCustomView() {
        View mView = inflater.inflate(R.layout.dialog_code_less_product, null);
        ImageView dismissImage = mView.findViewById(R.id.dialogCodeLessProduct_dismissImage);
        EditText priceEdit = mView.findViewById(R.id.dialogCodeLessProduct_priceEdit);
        ImageButton subtractBtn = mView.findViewById(R.id.dialogCodeLessProduct_subtractBtn);
        TextView numText = mView.findViewById(R.id.dialogCodeLessProduct_numText);
        ImageButton addBtn = mView.findViewById(R.id.dialogCodeLessProduct_addBtn);
        TextView cancelBtn = mView.findViewById(R.id.dialogCodeLessProduct_cancelBtn);
        TextView confirmBtn = mView.findViewById(R.id.dialogCodeLessProduct_confirmBtn);

        dismissImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        subtractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productNum <= 1) {
                    return;
                }
                productNum--;
                numText.setText(String.valueOf(productNum));
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productNum++;
                numText.setText(String.valueOf(productNum));
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
                String priceStr = priceEdit.getText().toString().trim();
                if (TextUtils.isEmpty(priceStr)) {
                    ToastUtils.showShort("请输入正确的商品价格");
                    return;
                }
                productPrice = Double.parseDouble(priceStr);
                if (clickListener != null) clickListener.confirm(getShopCartData());
                dismiss();
            }
        });

        priceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().equals("")) return;
                //限制小数点输入2位数
                String text = s.toString();
                if (text.contains(".")) {
                    int index = text.indexOf(".");
                    if (index + 3 < text.length()) {
                        text = text.substring(0, index + 3);
                        priceEdit.setText(text);
                        priceEdit.setSelection(text.length());
                    }
                }

                if (Double.parseDouble(s.toString()) > 100) {
                    priceEdit.setText(String.valueOf(100));
                    priceEdit.setSelection(String.valueOf(100).length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        super.setContentView(mView);
    }

    /**
     * 获取购物车数据
     */
    private double productNum = 1;
    private double productPrice = 0;

    private ShopCartBean.ProductListBean getShopCartData() {
        ShopCartBean.ProductListBean productBean = new ShopCartBean.ProductListBean();

        String shopID = SPUtils.getInstance().getString(DataKey.KEY_SHOP_ID);
        productBean.setProductName("无码商品");
        productBean.setProductNum(productNum);
        productBean.setProductPrice(productPrice);
        productBean.setProductID("");
        productBean.setProductImage("");
        productBean.setSpecificationUnitID(String.valueOf(System.currentTimeMillis()));
        productBean.setShopID(shopID);
        productBean.setSpecName("");
        productBean.setSpecID("0");
        productBean.setUnitName("");
        productBean.setUnitID("0");
        productBean.setBarCode("");
        productBean.setBrandID("0");
        productBean.setBrandName("");
        productBean.setManufacturer("");
        productBean.setEventID(0);
        productBean.setEventType(0);
        productBean.setIsInBulk(0);
        productBean.setIsMinUnit("N");
        productBean.setIsStandard("Y");
        productBean.setOnlineRebate(0);
        productBean.setOnlineRebateUnit(1);

        return productBean;
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
        lp.height = (int) (d.heightPixels * 0.4);
        dialogWindow.setAttributes(lp);
    }

}

