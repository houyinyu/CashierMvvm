package com.hby.cashier.ui

import android.content.Context
import android.content.DialogInterface
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Display
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hby.cashier.BR


import com.hby.cashier.R
import com.hby.cashier.app.AppConfig
import com.hby.cashier.databinding.ActivityLoginBinding
import com.hby.cashier.view_model.LoginViewModel
import com.hby.cashier.app.AppViewModelFactory
import com.hby.cashier.app.DataKey
import com.hby.cashier.bean.LitePalUserBean
import com.hby.cashier.datas.SQLiteManage
import com.hby.cashier.second_screen.SecondScreenBannerDisplay
import com.hby.cashier.second_screen.SecondScreenEmptyDisplay
import com.hby.cashier.second_screen.SecondScreenPayDisplay
import com.hby.cashier.utils.DeviceUtils
import com.hby.cashier.utils.EncryptionUtils
import com.hyy.mvvm.base.BaseActivity
import com.hyy.mvvm.utils.DialogUtils
import com.hyy.mvvm.utils.SPUtils
import com.hyy.mvvm.utils.ToastUtils
import com.hyy.mvvm.utils.Utils
import kotlinx.android.synthetic.main.activity_login.*
import org.litepal.LitePal.findAll
import java.io.File


class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {
    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_login
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): LoginViewModel {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        val factory: AppViewModelFactory = AppViewModelFactory.getInstance(application, this)
        return ViewModelProvider(this, factory).get(LoginViewModel::class.java)
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.uc.callBackEvent.observe(this, Observer<String> {
            DialogUtils.singleDialog(this, "提示", it, "确定", null)
                .setOnDismissListener { }
        })
    }


    override fun initData() {
        super.initData()
        login_deviceSnCode.text = DeviceUtils.getSN(this)

        SQLiteManage().initData()
        initSecondScreen()
    }


    /**
     * 初始化副屏
     */
    private var emptyDisplay: SecondScreenEmptyDisplay? = null
    private fun initSecondScreen() {
        //获取配置信息
        if (getPresentationDisplays() != null) {
            emptyDisplay = SecondScreenEmptyDisplay(this, getPresentationDisplays())
            emptyDisplay?.show()
        }
    }

    private fun getPresentationDisplays(): Display? {
        val mDisplayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val displays = mDisplayManager.displays
        for (i in displays.indices) {
            Log.e("TAG", "屏幕" + displays[i] + " Flag: " + displays[i].flags)
            if (displays[i].flags and Display.FLAG_SECURE != 0 && displays[i].flags and Display.FLAG_SUPPORTS_PROTECTED_BUFFERS != 0 && displays[i].flags and Display.FLAG_PRESENTATION != 0) {
                Log.e("TAG", "第一个真实存在的副屏屏幕" + displays[i])
                return displays[i]
            }
        }
        return null
    }

    override fun onResume() {
        super.onResume()
       if (emptyDisplay!=null){
           emptyDisplay?.show()
       }
    }
}