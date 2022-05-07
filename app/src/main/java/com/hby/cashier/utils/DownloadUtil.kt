package com.hby.cashier.utils

import android.content.Context
import android.os.Environment
import com.alibaba.fastjson.JSONObject
import com.hby.cashier.app.AppConfig
import com.hby.cashier.app.DataKey
import com.hyy.mvvm.utils.SPUtils
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * 功能介绍 :
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/12
 */
class DownloadUtil {
    private val okHttpClient: OkHttpClient = OkHttpClient()
    private var context: Context? = null
    private val TAG = javaClass.simpleName

    /**
     * 适用于需要传参数和json对象的接口
     * Post 异步请求
     *
     * @param apiId
     * @param map        参数
     * @param jsonString json字符串
     * @param callback   异步回调
     */


    /**
     * @param url 下载连接
     * @param listener 下载监听
     */
  public  fun download(context: Context?, url: String?, listener: OnDownloadListener) {
        this.context = context
        val jsonOb = JSONObject()
        jsonOb["Devid"] = "dev_" + DeviceUtils.getSN(context)
        // 需要token的时候可以这样做
        // Request request = new Request.Builder().header("token",token).url(url).build();
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val requestBody = RequestBody.create(JSON, jsonOb.toString())
        val request: Request = Request.Builder().url(url).post(requestBody).build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                e?.printStackTrace()
                listener.onDownloadFailed()
            }

            override fun onResponse(call: Call?, response: Response) {
                var inputStream: InputStream? = null
                val buf = ByteArray(2048)
                var len = 0
                var fos: FileOutputStream? = null
                try {
                    inputStream = response.body()?.byteStream()
                    val total: Long = response.body()?.contentLength()!!
                    var downloadPath =
                        File(AppConfig.CASHIER_SAVE_PATH, AppConfig.ZIP_PARENT_PATH + "/")
                    if (!downloadPath.mkdirs()) {
                        downloadPath.createNewFile()
                    }
                    val file = File(downloadPath.absoluteFile, AppConfig.ZIP_FILE_NAME)
                    LogUtils.i("*******************************:最终路径：$file")
                    fos = FileOutputStream(file)
                    var sum: Long = 0
                    while (inputStream?.read(buf).also { len = it!! } != -1) {
                        fos.write(buf, 0, len)
                        sum += len.toLong()
                        val progress = (sum * 1.0f / total * 100).toInt()
                        listener.onDownloading(progress)
                    }
                    fos.flush()
                    listener.onDownloadSuccess(file)
                } catch (e: Exception) {
                    LogUtils.i("***********************:"+e.message)
                    listener.onDownloadFailed()
                } finally {
                    try {
                        inputStream?.close()
                        fos?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }


    interface OnDownloadListener {
        /**
         * 下载成功
         */
        fun onDownloadSuccess(file: File)

        /**
         * @param progress
         * 下载进度
         */
        fun onDownloading(progress: Int)

        /**
         * 下载失败
         */
        fun onDownloadFailed()
    }
}