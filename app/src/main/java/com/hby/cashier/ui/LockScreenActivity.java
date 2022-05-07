//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hby.cashier.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;


import androidx.appcompat.app.AppCompatActivity;

import com.hby.cashier.R;
import com.hby.cashier.app.DataKey;
import com.hby.cashier.printer.ResourcesUtils;
import com.hyy.mvvm.utils.SPUtils;
import com.hyy.mvvm.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LockScreenActivity extends AppCompatActivity {

    public void hideNavigationBar() {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }


    protected void onCreate(Bundle var1) {
        super.onCreate(var1);
        setContentView(R.layout.activity_lock_screen);
        hideNavigationBar();
        initView();
    }

    private EditText passEdit;
    private TextView unLockBtn;

    private void initView() {
        passEdit = findViewById(R.id.lockScreen_passEdit);
        unLockBtn = findViewById(R.id.lockScreen_unLockBtn);

        unLockBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginPass = SPUtils.getInstance().getString(DataKey.KEY_LOGIN_PASS);
                if (!passEdit.getText().toString().equals(loginPass)) {
                    ToastUtils.showShort("密码错误");
                    return;
                }
                SPUtils.getInstance().put(DataKey.KEY_IS_LOCK, false);
                finish();
            }
        });
    }


}
