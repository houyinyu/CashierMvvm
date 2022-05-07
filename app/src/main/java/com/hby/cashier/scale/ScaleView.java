package com.hby.cashier.scale;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.hby.cashier.R;
import com.hby.cashier.ui.MainActivity;
import com.hby.cashier.weight.dialog.WeighProductDialog;
import com.hyy.mvvm.base.BaseActivity;

/**
 * 功能介绍 :称重自定义view
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/3/31
 */
public class ScaleView {
    BaseActivity context;
    private LayoutInflater inflater;
    private View parentView;

    public ScaleView(BaseActivity context, View parentView) {
        this.context = context;
        this.parentView = parentView;
        inflater = LayoutInflater.from(context);
    }

    private TextView scaleTareNum;//皮重
    private TextView scaleWeightText;//计重text
    private TextView scaleWeightNum;//计重
    private TextView scaleOverMaxText;//超载text
    private ImageView scaleOverMaxImage;//超载

    private ImageView scaleStableImage;//稳定
    private ImageView scaleNetImage;//净重
    private ImageView scaleZeroImage;//零位
    private ScalePresenter scalePresenter;

    public void initView() {
        scaleTareNum = parentView.findViewById(R.id.includeHomeLeft_tareNum);
        scaleWeightText = parentView.findViewById(R.id.includeHomeLeft_weightText);
        scaleWeightNum = parentView.findViewById(R.id.includeHomeLeft_weightNum);
        scaleOverMaxText = parentView.findViewById(R.id.includeHomeLeft_overMaxText);
        scaleOverMaxImage = parentView.findViewById(R.id.includeHomeLeft_overMaxImage);

        scaleStableImage = parentView.findViewById(R.id.includeHomeLeft_scaleStableImage);
        scaleNetImage = parentView.findViewById(R.id.includeHomeLeft_scaleNetImage);
        scaleZeroImage = parentView.findViewById(R.id.includeHomeLeft_scaleZeroImage);

        scalePresenter = new ScalePresenter(context, new ScalePresenter.ScalePresenterCallback() {
            @Override
            public void getData(final int net, final int pnet, final int statu) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateScaleInfo(net, pnet, statu);
                    }
                });
            }

            @Override
            public void isScaleCanUse(boolean isCan) {
                if (changeListener != null) changeListener.scaleUser(isCan);
                if (!isCan) {
                    scaleWeightNum.setText("---");
                    scaleWeightNum.setTextColor(ContextCompat.getColor(context, R.color.color_orange));
                    scaleTareNum.setText("---");
                    scaleTareNum.setTextColor(ContextCompat.getColor(context, R.color.color_orange));
                }
            }
        });
    }

    public ScalePresenter getScalePresenter() {
        return scalePresenter;
    }

    /**
     * 更新秤显示信息
     *
     * @param net
     * @param pnet
     * @param statu
     */
    private void updateScaleInfo(final int net, int pnet, int statu) {
        if (changeListener != null) changeListener.scaleChange(net, pnet, statu);
        if (pnet == 0) {
            scaleWeightText.setText(R.string.scale_net_nopnet);
        } else {
            scaleWeightText.setText(R.string.scale_net_kg);
        }
        scaleWeightNum.setText(scalePresenter.formatQuality(net));//  tvScaleNet
        scaleTareNum.setText(scalePresenter.formatQuality(pnet));//tvScalePnet
//        tvScaleTotal.setText(scalePresenter.formatTotalMoney(net));//总价

        scaleZeroImage.setActivated(net == 0);//ivScaleZero
        scaleStableImage.setActivated((statu & 1) == 1);//ivScaleStable
        scaleNetImage.setActivated(pnet > 0);//ivScaleNet
        if ((statu & 1) != 1) {
            //重量不稳定
            scaleStableImage.setActivated(false);
            scaleNetImage.setActivated(false);
            scaleZeroImage.setActivated(false);
        }
        //超载
        if ((statu & 4) == 4) {
            scaleStableImage.setActivated(false);
            scaleNetImage.setActivated(false);
            scaleZeroImage.setActivated(false);

            if (scaleOverMaxText.getVisibility() == View.GONE) {//tvScaleOverMax
                scaleOverMaxText.setVisibility(View.VISIBLE);
                scaleOverMaxText.setSelected(true);

                scaleOverMaxImage.setVisibility(View.VISIBLE);
                scaleOverMaxImage.setSelected(true);

                scaleWeightNum.setVisibility(View.INVISIBLE);


                scaleOverMaxText.setText(R.string.scale_over_max);
            }
        } else if ((pnet + net) < 0) {
//            欠载
            scaleStableImage.setActivated(false);
            scaleNetImage.setActivated(false);
            scaleZeroImage.setActivated(false);
            if (scaleOverMaxText.getVisibility() == View.GONE) {
                scaleOverMaxText.setVisibility(View.VISIBLE);
                scaleOverMaxText.setSelected(false);

                scaleOverMaxImage.setVisibility(View.VISIBLE);
                scaleOverMaxImage.setSelected(false);

                scaleWeightNum.setVisibility(View.INVISIBLE);

                scaleOverMaxText.setText(R.string.scale_over_mix);
            }
        } else {
            if (scaleOverMaxText.getVisibility() == View.VISIBLE) {
                scaleOverMaxText.setVisibility(View.GONE);
                scaleOverMaxImage.setVisibility(View.GONE);
            }
            if (scaleWeightNum.getVisibility() == View.INVISIBLE) {
                scaleWeightNum.setVisibility(View.VISIBLE);
            }
        }


    }

    public abstract static class ScaleChangeListener {
        public abstract void scaleChange(int net, int pnet, int status);

        public abstract void scaleUser(boolean canUse);
    }

    private ScaleChangeListener changeListener;

    public void setScaleChangeListener(ScaleChangeListener changeListener) {
        this.changeListener = changeListener;
    }


    public void onDestroy() {
        if (scalePresenter != null && scalePresenter.isScaleSuccess()) {
            scalePresenter.onDestroy();
        }
    }

}
