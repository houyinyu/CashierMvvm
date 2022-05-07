package com.hby.cashier.ui;

import android.os.Bundle;

import com.hby.cashier.R;
import com.hby.cashier.databinding.ActivityLoginBinding;
import com.hby.cashier.view_model.LoginViewModel;
import com.hyy.mvvm.base.BaseActivity;

public class TestActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {


    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_test;
    }

    @Override
    public int initVariableId() {
        return 0;
    }
}