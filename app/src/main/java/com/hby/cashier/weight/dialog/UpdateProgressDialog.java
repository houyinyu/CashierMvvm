package com.hby.cashier.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.hby.cashier.R;

import org.xutils.common.util.DensityUtil;

import java.util.Locale;

/**
 * 功能介绍:apk更新弹窗
 * 调用方式:
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/7/27 10:51
 * 最后编辑: 2020/7/27 - Hyy
 *
 * @author HouYinYu
 */
public class UpdateProgressDialog extends Dialog {


    public interface ConfirmClickListener {
        void click();
    }

    private ConfirmClickListener confirmClickListener;

    public void setConfirmClickListener(ConfirmClickListener clickListener) {
        confirmClickListener = clickListener;
    }

    public interface CancelClickListener {
        void click();
    }

    private CancelClickListener cancelClickListener;

    public void setCancelClickListener(CancelClickListener clickListener) {
        cancelClickListener = clickListener;
    }

    private Context context;

    public UpdateProgressDialog(Context context) {
        super(context, R.style.DialogTheme);
        this.context = context;
    }

    private LinearLayout update_allLayout;
    private TextView update_title;
    private TextView update_feature;
    private TextView update_confirmBtn;
    private TextView update_cancelBtn;

    public void setCustomView() {
        View updateView = LayoutInflater.from(context).inflate(R.layout.show_update_upgrade_dialog, null);

        update_allLayout = (LinearLayout) updateView.findViewById(R.id.dialog_update_allLayout);
        update_title = (TextView) updateView.findViewById(R.id.dialog_update_title);
        update_feature = (TextView) updateView.findViewById(R.id.dialog_update_feature);
        update_confirmBtn = (TextView) updateView.findViewById(R.id.dialog_update_confirmBtn);
        update_cancelBtn = (TextView) updateView.findViewById(R.id.dialog_update_cancelBtn);
        update_title.setText("版本更新");
        update_confirmBtn.setText("立即更新");
        update_cancelBtn.setText("下次再说");

        update_confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmClickListener.click();
            }
        });
        update_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelClickListener.click();
            }
        });
        super.setContentView(updateView);
    }


    /**
     * 是否强制更新
     *
     * @param isForce
     */
    public void setForce(boolean isForce) {
        if (isForce) update_cancelBtn.setVisibility(View.GONE);
    }

    public void setTitle(String title) {
        update_title.setText(title);
    }

    public void setConfirmText(String confirm) {
        update_confirmBtn.setText(confirm);
    }

    public void setFeature(String feature) {
        update_feature.setText(feature);
    }

    public void setConfirmEnabled(boolean click) {
        update_confirmBtn.setEnabled(click);
    }

    /**
     * 设置最大值
     *
     * @param max
     */
    private long max = 0;

    public void setUpdateMax(long max) {
        this.max = max;
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setUpdateProgress(long progress) {
        Message message = new Message();
        message.obj = getPercentage(progress);
        message.what = 0;
        handler.sendMessage(message);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                String progress = msg.obj.toString();
                update_confirmBtn.setText(progress);
            }
            return false;
        }
    });

    @Override
    public void show() {
        super.show();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) update_allLayout.getLayoutParams();
        layoutParams.width = (int) (DensityUtil.getScreenWidth() * 0.5);
        update_allLayout.setLayoutParams(layoutParams);
    }

    private String getPercentage(long progress) {
        double percentage = (double) progress / (double) max * 100;
        return String.format(Locale.CHINA, "%.1f", percentage) + "%";
    }

}
