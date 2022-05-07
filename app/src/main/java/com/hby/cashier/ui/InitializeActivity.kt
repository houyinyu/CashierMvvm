package com.hby.cashier.ui

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import androidx.core.content.ContextCompat
import com.alibaba.fastjson.JSONObject
import com.hby.cashier.R
import com.hby.cashier.app.AppConfig
import com.hby.cashier.app.BaseActivityss
import com.hby.cashier.http.RequestConfig
import com.hby.cashier.utils.*
import com.hyy.mvvm.http.HttpUtils
import com.hyy.mvvm.loading.LoadingDialog
import com.yanzhenjie.permission.runtime.Permission
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.DownloadProgressCallBack
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.model.HttpHeaders
import kotlinx.android.synthetic.main.activity_initialize.*
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x

import java.io.File
import android.text.TextUtils
import androidx.lifecycle.Observer
import com.hby.cashier.BR
import com.hby.cashier.app.DataKey
import com.hby.cashier.bean.LitePalOrderDetailsBean
import com.hby.cashier.databinding.ActivityInitializeBinding
import com.hby.cashier.datas.SQLiteManage
import com.hby.cashier.view_model.InitializeModel
import com.hyy.mvvm.base.BaseActivity
import com.hyy.mvvm.bean.BaseBean
import com.hyy.mvvm.utils.JsonUtils
import com.hyy.mvvm.utils.SPUtils
import com.hyy.mvvm.utils.ToastUtils
import org.litepal.LitePal.updateAll


/**
 * 初始化界面
 */
class InitializeActivity : BaseActivity<ActivityInitializeBinding, InitializeModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_initialize;
    }


    override fun initVariableId(): Int {
        return BR.viewModel
    }


    override fun initViewObservable() {
        super.initViewObservable()
        //数据同步监听
        viewModel.uc.initializeEvent.observe(this, Observer<Any?> {
            showDownLoad()
        })
    }

    override fun initData() {
        super.initData()
        initializeCodeText.text = DeviceUtils.getSN(this)
    }


    private fun showDownLoad() {
        //权限没有授予
        PermissionUtils(context).showSinglePermission(
            "存储",
            Permission.WRITE_EXTERNAL_STORAGE,
            object : PermissionUtils.OnPermissionListener {
                override fun finish() {
                }

                override fun agree() {
                    //同意权限
                    goDownLoad()
                }

                override fun cancel() {
                    ToastUtils.showShort("请授予存储权限")
                }

            })
    }

    private fun goDownLoad() {
        viewModel.showDialog()
        val downUrl = AppConfig.BASE_DOWNLOAD_URL + RequestConfig.request_downZip
        DownloadUtil().download(context, downUrl, object : DownloadUtil.OnDownloadListener {
            override fun onDownloadSuccess(file: File) {
                ZipUtils.unZipFilesTest(file)
                SPUtils.getInstance().put(DataKey.KEY_FIRST, false)
                handler.sendEmptyMessage(0)
                startActivity(LoginActivity::class.java)
                LogUtils.i("*******************下载成功:" + file.absolutePath)
            }

            override fun onDownloading(progress: Int) {
                LogUtils.i("*******************progress:" + progress)
            }

            override fun onDownloadFailed() {
                handler.sendEmptyMessage(0)
                LogUtils.i("*******************onDownloadFailed")
            }
        })
    }

    private val handler = Handler { msg ->
        when (msg.what) {
            0 -> {
                viewModel.dismissDialog()
            }
        }
        false
    }


}