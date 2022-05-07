package com.hby.cashier.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.hby.cashier.R;
import com.hby.cashier.utils.DensityUtils;


public class ConfirmDialog extends Dialog {

    private Context context;
    private LayoutInflater inflater;

    private TextView titleText;
    private TextView describeText;
    private TextView cancelBtn;
    private TextView cancelLine;
    private TextView confirmBtn;


    public abstract static class DialogClickListener {
        public void cancel() {
        }

        public abstract void confirm();
    }

    private DialogClickListener clickListener;

    public ConfirmDialog setOnDialogClickListener(DialogClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public ConfirmDialog setOnDialogCancelListener(OnCancelListener cancelListener) {
        this.setOnCancelListener(cancelListener);
        return this;
    }

    public ConfirmDialog(Context context) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        setCustomView();
    }

    /**
     * 设置整个弹出框的视图
     */
    public void setCustomView() {
        View mView = inflater.inflate(R.layout.dialog_confirm_layout, null);
        titleText = (TextView) mView.findViewById(R.id.confirmDialog_title);
        describeText = (TextView) mView.findViewById(R.id.confirmDialog_describe);
        cancelBtn = (TextView) mView.findViewById(R.id.confirmDialog_cancel);
        cancelLine = (TextView) mView.findViewById(R.id.confirmDialog_cancelLine);
        confirmBtn = (TextView) mView.findViewById(R.id.confirmDialog_confirm);
        cancelBtn.setText("取消");
        confirmBtn.setText("确认");
        setCanceledOnTouchOutside(true);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                clickListener.cancel();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                clickListener.confirm();
            }
        });
        super.setContentView(mView);
    }

    public ConfirmDialog setTitleText(String titleStr) {
        titleText.setText(titleStr);
        return this;
    }

    public ConfirmDialog setTitleTextBold() {
        titleText.getPaint().setFakeBoldText(true);
        return this;
    }

    public ConfirmDialog setDescribeText(String describeStr) {
        describeText.setText(describeStr);
        return this;
    }

    public ConfirmDialog setDescribeTextColor(int colorResources) {
        describeText.setTextColor(ContextCompat.getColor(context, colorResources));
        return this;
    }


    public ConfirmDialog setConfirmText(String confirmStr) {
        confirmBtn.setText(confirmStr);
        return this;
    }

    public ConfirmDialog setConfirmTextColor(int colorResources) {
        confirmBtn.setTextColor(ContextCompat.getColor(context, colorResources));
        return this;
    }

    private boolean isMerchant = true;

    public ConfirmDialog isMerchant(boolean isMerchant) {
        this.isMerchant = isMerchant;
        return this;
    }

    public ConfirmDialog isSingleBtn() {
        cancelBtn.setVisibility(View.GONE);
        cancelLine.setVisibility(View.GONE);
        confirmBtn.setTextColor(ContextCompat.getColorStateList(context, R.color.selector_white_60_press));
        confirmBtn.setBackgroundResource(R.drawable.selector_btn_back_8);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) confirmBtn.getLayoutParams();
        layoutParams.height = DensityUtils.dip2px(context, 40);
        confirmBtn.setLayoutParams(layoutParams);
        return this;
    }

    public ConfirmDialog setCancelText(String cancelStr) {
        cancelBtn.setText(cancelStr);
        return this;
    }

    public ConfirmDialog setNotCanceledOnOutside() {
        setCanceledOnTouchOutside(false);
        return this;
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

}

