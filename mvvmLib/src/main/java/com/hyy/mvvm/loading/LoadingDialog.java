package com.hyy.mvvm.loading;

import android.app.Dialog;
import android.content.Context;

import androidx.core.content.ContextCompat;

import com.hyy.mvvm.R;


/**
 * 功能介绍:
 * 调用方式:
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/10/27 17:18
 * 最后编辑: 2020/10/27 - Hyy
 *
 * @author HouYinYu
 */
public class LoadingDialog extends Dialog {
    private AVLoadingIndicatorView indicatorView;

    public LoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
        setContentView(R.layout.loading_layout);
        indicatorView = findViewById(R.id.loadingView);
        indicatorView.setIndicatorColor(ContextCompat.getColor(context, R.color.themeColor));
        setCanceledOnTouchOutside(false);
    }

    public LoadingDialog(Context context, int colorID) {
        super(context, R.style.loading_dialog);
        setContentView(R.layout.loading_layout);
        indicatorView = findViewById(R.id.loadingView);
        indicatorView.setIndicatorColor(colorID);
        setCanceledOnTouchOutside(false);
    }


    @Override
    public void show() {
        super.show();
        indicatorView.smoothToShow();
    }


    @Override
    public void dismiss() {
        super.dismiss();
        indicatorView.smoothToHide();
    }

}
