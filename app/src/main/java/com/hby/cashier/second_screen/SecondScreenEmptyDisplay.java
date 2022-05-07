package com.hby.cashier.second_screen;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.Display;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hby.cashier.R;

/**
 * 没有副屏banner图的时候显示
 * mail  gaolulin@sunmi.com
 */

public class SecondScreenEmptyDisplay extends BasePresentation {

    private LinearLayout root;
    private TextView tvTitle;
    private TextView tv;
    private LinearLayout llPresentChoosePayMode;
    private LinearLayout llPresentInfo;
    private TextView tvPaySuccess;
    private TextView paymodeOne;
    private TextView paymodeTwo;
    private TextView paymodeThree;
    private ImageView ivTitle;
    private ProgressBar presentProgress;


    private LinearLayout llPresentPayFail;
    private TextView presentFailOne;
    private TextView presentFailTwo;
    private TextView presentFailThree;
    public int state;

    public SecondScreenEmptyDisplay(Context outerContext, Display display) {
        super(outerContext, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.second_screen_empty_layout);
    }




    private SpannableString zoomString(String strings){
        SpannableString ss = new SpannableString(strings);
        ss.setSpan(new RelativeSizeSpan(0.65f), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE); // set size
        return  ss;
    }

    void playAnim(){
        AnimationDrawable animationDrawable = (AnimationDrawable) ivTitle.getDrawable();
        animationDrawable.start();
    }


    public void setSelect(int index) {
        paymodeOne.setSelected(index == 0 ? true : false);
        paymodeTwo.setSelected(index == 1 ? true : false);
        paymodeThree.setSelected(index == 2 ? true : false);

    }

    @Override
    public void show() {
        super.show();

    }


    @Override
    public void onSelect(boolean isShow) {

    }
}
