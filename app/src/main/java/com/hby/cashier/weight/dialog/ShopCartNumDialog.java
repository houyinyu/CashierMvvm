package com.hby.cashier.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.hby.cashier.R;
import com.hby.cashier.utils.LogUtils;


/**
 * 购物车选择数量的dialog
 */
public class ShopCartNumDialog extends Dialog {

    private Context context;
    private LayoutInflater inflater;

    private TextView titleTv;  //标题
    private TextView descriptionText;  //说明
    private EditText remarkEdit;
    private TextView okBtn;   //确定按钮
    private TextView cancelBtn;   //取消按钮


    public ShopCartNumDialog(Context context) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * 设置整个弹出框的视图
     */
    public void setCustomView() {
        View mView = inflater.inflate(R.layout.dialog_shop_cart_num, null);
        titleTv = (TextView) mView.findViewById(R.id.shopCartNumTitle);
        descriptionText = (TextView) mView.findViewById(R.id.shopCartNumDescription);
        remarkEdit = (EditText) mView.findViewById(R.id.shopCartNumEdit);
        cancelBtn = (TextView) mView.findViewById(R.id.shopCartNumCancelBtn);
        okBtn = (TextView) mView.findViewById(R.id.shopCartNumOkBtn);

        descriptionText.setVisibility(View.GONE);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okClickListener.onConfirm(remarkEdit.getText().toString().trim());
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okClickListener.onCancel();
                dismiss();
            }
        });
        remarkEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().equals("")) return;
                if (!isDecimal && Double.parseDouble(s.toString()) > maxNum) {
                    setDescriptionShow(maxNum);
                    s = String.valueOf(Double.valueOf(maxNum).intValue());
                    remarkEdit.setText(s);
                    remarkEdit.setSelection(s.length());
                }
                if (isStandard) {
                    //限制小数点输入2位数
                    String text = s.toString();
                    if (text.contains(".")) {
                        int index = text.indexOf(".");
                        if (index + 3 < text.length()) {
                            text = text.substring(0, index + 3);
                            remarkEdit.setText(text);
                            remarkEdit.setSelection(text.length());
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        super.setContentView(mView);
    }

    public void setTitleStr(String str) {
        titleTv.setText(str);
    }

    public void setDescriptionStr(String str) {
        descriptionText.setVisibility(View.VISIBLE);
        descriptionText.setText(str);
    }

    public void setDescriptionColor(int color) {
        descriptionText.setTextColor(ContextCompat.getColor(context, color));
    }

    private double maxNum = 5000;

    public void setMaxNum(double maxNum) {
        this.maxNum = maxNum;
    }


    public void setDescriptionShow(double maxNum) {
        descriptionText.setVisibility(View.VISIBLE);
        descriptionText.setText("已输入最大数量" + maxNum);
    }

    public void setEditStr(String str) {
        if (!isDecimal) {
            str = String.valueOf(Double.valueOf(str).intValue());
        }
        remarkEdit.setText(str);
        remarkEdit.setSelection(str.length());
    }

    public void setEditHint(String str) {
        remarkEdit.setHint(str);
    }

    //是否可输入小数
    private boolean isDecimal = false;

    public void setIsDecimal(boolean isDecimal) {
        this.isDecimal = isDecimal;
        if (isDecimal) {
            remarkEdit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
    }

    //是否非标品
    private boolean isStandard = false;

    public void setIsStandard(boolean isStandard) {
        this.isStandard = isStandard;
    }

    public interface OkClickListener {
        public void onConfirm(String remark);

        public void onCancel();
    }

    private OkClickListener okClickListener;

    public void setOnOkClickListener(OkClickListener clickListener) {
        okClickListener = clickListener;
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

        lp.width = (int) (d.widthPixels * 0.8); // 宽度设置为屏幕的0.8
//        lp.height = (int) (d.heightPixels * 0.3); // 高度设置为屏幕的0.3
        dialogWindow.setAttributes(lp);

    }

    public void setHeight() {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.height = (int) (d.heightPixels * 0.25); // 高度设置为屏幕的0.3
        dialogWindow.setAttributes(lp);
    }

}

