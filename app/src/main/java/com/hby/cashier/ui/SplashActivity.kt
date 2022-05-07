package com.hby.cashier.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.hby.cashier.BuildConfig
import com.hby.cashier.R
import com.hby.cashier.app.AppConfig
import com.hby.cashier.app.BaseActivityss
import com.hby.cashier.app.DataKey
import com.hby.cashier.bean.RecordOrderDetailsBean
import com.hby.cashier.bean.UpdateBean
import com.hby.cashier.http.RequestConfig
import com.hby.cashier.utils.*
import com.hby.cashier.weight.dialog.UpdateProgressDialog
import com.hyy.mvvm.bean.BaseBean
import com.hyy.mvvm.http.HttpUtils
import com.hyy.mvvm.utils.JsonUtils
import com.hyy.mvvm.utils.SPUtils
import com.hyy.mvvm.utils.ToastUtils
import com.yanzhenjie.permission.runtime.Permission
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.include_sales_record_right_layout.*
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x
import java.io.File

class SplashActivity : BaseActivityss() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        getVersionData()
    }

    /**
     * 获取版本信息
     */

    private fun getVersionData() {
        HttpUtils.get(RequestConfig.getVersionsPlus)
            .execute(object : SimpleCallBack<String?>() {
                override fun onCompleted() {
                    super.onCompleted()
                }

                override fun onError(e: ApiException) {
                    goLoginType()
                }

                override fun onSuccess(data: String?) {
                    val baseBean = JsonUtils.parseJson(data, BaseBean::class.java)
                    if (baseBean.result != -1 && baseBean.result != -2 && baseBean.result != -4) {
                        val updateBean = JsonUtils.parseJson(data, UpdateBean::class.java)
                        val dataBean = updateBean.resultObject
                        checkUpdate(dataBean)
                    } else {
                        goLoginType()
                    }

                }
            })
    }

    private fun goLoginType() {
        val isFirst = SPUtils.getInstance().getBoolean(DataKey.KEY_FIRST, true)
        val isLogin = SPUtils.getInstance().getBoolean(DataKey.KEY_LOGIN, false)

        if (isFirst) {
            goIntent(InitializeActivity::class.java)
        } else {
            if (isLogin) {
                goIntent(MainActivity::class.java)
            } else {
                goIntent(LoginActivity::class.java)
            }
        }
    }


    private var updateDialog: UpdateProgressDialog? = null
    private fun checkUpdate(dataBean: UpdateBean.ResultObjectBean) {
        //需要更新
        if (dataBean.versionCode > PackageUtils.getVersionCode(context)) {
            updateDialog = UpdateProgressDialog(context)
            updateDialog?.setCustomView()
            updateDialog?.setUpdateMax(dataBean.fileSize)
            updateDialog?.setCanceledOnTouchOutside(false)
            updateDialog?.setFeature(dataBean.updateMsg)
            if (getApkFile(dataBean.url).exists()) {
                updateDialog?.setConfirmText("安装")
            }
            if (dataBean.forceUpdate == 1) {
                //强制更新 隐藏取消按钮
                updateDialog?.setForce(true)
            }
            updateDialog?.setConfirmClickListener(UpdateProgressDialog.ConfirmClickListener {
                if (getApkFile(dataBean.url).exists()) {
                    installApk(getApkFile(dataBean.url))
                    return@ConfirmClickListener
                }
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    showDownLoad(dataBean)
                } else {
                    goDownLoad(dataBean)
                }
            })
            updateDialog?.setCancelClickListener(UpdateProgressDialog.CancelClickListener {
                goLoginType()
                updateDialog?.dismiss()
            })
            if (!isFinishing) {
                updateDialog?.show()
            }
        } else {
            goLoginType()
        }
    }


    /**
     * 下载前判断读写权限
     */
    private fun showDownLoad(updateBean: UpdateBean.ResultObjectBean) {
        PermissionUtils(context).showSinglePermission(
            "存储",
            Permission.WRITE_EXTERNAL_STORAGE,
            object : PermissionUtils.OnPermissionListener {
                override fun finish() {
                }

                override fun agree() {
                    //同意权限
                    goDownLoad(updateBean)
                }

                override fun cancel() {
                    ToastUtils.showShort("请授予存储权限以安装最新版本")
                }

            })
    }

    /**
     * 下载apk
     *
     * @param dataBean
     */
    private var apkFile: File? = null
    private fun goDownLoad(dataBean: UpdateBean.ResultObjectBean) {
        updateDialog?.setConfirmEnabled(false)

        loadingDialog.show()
        val params = RequestParams(dataBean.url)
        params.isAutoResume = true

        val urlpath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
//        val urlpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/"


        params.saveFilePath = urlpath + getApkName(dataBean.url)
        x.http().get(params, object : Callback.ProgressCallback<File> {
            override fun onWaiting() {}

            override fun onStarted() {}

            override fun onLoading(total: Long, current: Long, isDownloading: Boolean) {
                loadingDialog.dismiss()
                updateDialog?.setUpdateProgress(current)
            }

            override fun onSuccess(result: File) {
                apkFile = result
                installApk(result)
            }

            override fun onError(ex: Throwable, isOnCallback: Boolean) {}

            override fun onCancelled(cex: Callback.CancelledException) {}

            override fun onFinished() {
                updateDialog?.setConfirmEnabled(true)
                updateDialog?.setConfirmText("安装")
            }
        })
    }

    /**
     * 安装apk
     *
     * @param result
     */
    private fun installApk(result: File) {
        //8.0以上安卓系统的未知应用安装权限需要特殊处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val haveInstallPermission = packageManager.canRequestPackageInstalls()
            if (!haveInstallPermission) {
                //没有权限让调到设置页面进行开启权限；
                val packageURI = Uri.parse("package:$packageName")
                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI)
                startActivityForResult(intent, 10086)
            } else {
                //有权限，执行自己的逻辑；
                installApp8(result)
            }
        } else {
            //其他android版本，可以直接执行安装逻辑；
            val i = Intent(Intent.ACTION_VIEW)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            //如果大于7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val apkFileUri = FileProvider.getUriForFile(
                    applicationContext,
                    BuildConfig.APPLICATION_ID + ".android7.fileprovider", result
                )
                i.setDataAndType(apkFileUri, "application/vnd.android.package-archive")
                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                //如果等于6.0
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                if (result.exists()) {
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val type = getMIMEType(result)
                    i.setDataAndType(Uri.fromFile(result), type)
                }
            } else {
                i.setDataAndType(
                    Uri.parse("file://" + result.path.toString()),
                    "application/vnd.android.package-archive"
                )// File.toString()会返回路径信息
            }
            try {
                startActivity(i)
            } catch (var5: Exception) {
                var5.printStackTrace()
                toast("没有找到打开此类文件的程序:请手动安装或者重新下载")
            }

        }

    }

    /**
     * 通过截取apk名来生成文件名
     *
     * @param url
     * @return
     */
    private fun getApkFile(url: String): File {
        val urlpath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
        return File(urlpath + getApkName(url))
    }

    /**
     * 截取apk名
     *
     * @param url
     * @return
     */
    private fun getApkName(url: String): String {
        return url.substring(url.indexOf("Clg_Cashier"))
    }


    private fun getMIMEType(var0: File): String? {
        var var1: String? = ""
        val var2 = var0.name
        val var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length).toLowerCase()
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3)
        return var1
    }

    /**
     * 安卓8.0版本之后由于安装未知应用权限更改，需要特殊处理
     *
     * @param address
     */
    private fun installApp8(address: File?) {
        if (address == null) {
            toast("安装应用失败,请到应用市场下载或者文件夹重新安装")
            return
        }
        val intent = Intent(Intent.ACTION_VIEW)
        val apkFileUri = FileProvider.getUriForFile(
            context,
            BuildConfig.APPLICATION_ID + ".android7.fileprovider", address
        )
        intent.setDataAndType(apkFileUri, "application/vnd.android.package-archive")
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


}