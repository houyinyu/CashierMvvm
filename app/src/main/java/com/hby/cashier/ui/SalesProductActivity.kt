package com.hby.cashier.ui

import android.content.Context
import android.hardware.display.DisplayManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.hby.cashier.BR
import com.hby.cashier.R
import com.hby.cashier.adapter.SalesProductAdapter
import com.hby.cashier.adapter.SalesRecordProductAdapter
import com.hby.cashier.app.AppConfig
import com.hby.cashier.app.DataKey
import com.hby.cashier.bean.LitePalChangeShiftRecordBean
import com.hby.cashier.bean.LitePalOrderDetailsBean
import com.hby.cashier.databinding.ActivitySalesProductBinding
import com.hby.cashier.second_screen.SecondScreenBannerDisplay
import com.hby.cashier.second_screen.SecondScreenEmptyDisplay
import com.hby.cashier.second_screen.SecondScreenPayDisplay
import com.hby.cashier.utils.NumUtils
import com.hby.cashier.view_model.SalesProductModel
import com.hyy.mvvm.base.BaseActivity
import com.hyy.mvvm.base.BaseViewModel
import com.hyy.mvvm.data.DemoRepository
import com.hyy.mvvm.utils.JsonUtils
import com.hyy.mvvm.utils.SPUtils
import kotlinx.android.synthetic.main.activity_sales_product.*
import kotlinx.android.synthetic.main.include_sales_record_right_layout.*
import org.litepal.LitePal.where
import java.io.File

/**
 * 销售商品界面
 * 交接班记录进入
 */
class SalesProductActivity : BaseActivity<ActivitySalesProductBinding, SalesProductModel>() {
    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_sales_product
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewObservable() {
        super.initViewObservable()
        //页面关闭监听
        viewModel.uc.activityFinishEvent.observe(this, Observer<Any?> {
            finish()
        })
    }

    override fun initData() {
        initSecondScreen()
        salesProduct_cashier.text = SPUtils.getInstance().getString(DataKey.KEY_USER_NAME)
        if (intent != null) {
            val changeShiftRecordBean = JsonUtils.parseJson(
                intent.getStringExtra("recordJson"),
                LitePalChangeShiftRecordBean::class.java
            )

            val productList: MutableList<LitePalOrderDetailsBean.OrderProductAoListBean> =
                ArrayList()
            //获取订单号列表
            val orderNoList: List<String> =
                JSON.parseArray(changeShiftRecordBean.orderNoJson, String::class.java)
            //根据订单号列表获取订单
            for (i in 0 until orderNoList.size) {
                val orderDetails =
                    where("ordno = ?", orderNoList[i]).find(LitePalOrderDetailsBean::class.java)
                for (j in 0 until orderDetails.size) {
                    //根据订单获取商品列表
                    val orderProductList: List<LitePalOrderDetailsBean.OrderProductAoListBean> =
                        JSON.parseArray(
                            orderDetails[j].orderProductListJson,
                            LitePalOrderDetailsBean.OrderProductAoListBean::class.java
                        )
                    for (m in 0 until orderProductList.size) {

                        var isExist = false
                        for (n in 0 until productList.size) {
                            if (productList[n].ordProductProSpeUnitId == orderProductList[m].ordProductProSpeUnitId) {
                                isExist = true;
                                productList[n].ordProductNum =
                                    productList[n].ordProductNum + orderProductList[m].ordProductNum
                                break
                            }
                        }
                        if (!isExist) {
                            productList.add(orderProductList[m])
                        }
                    }

                }
            }
            setProductAdapter(productList)
        }

    }


    /**
     * 初始化副屏
     */
    private var bannerDisplay: SecondScreenBannerDisplay? = null
    private var emptyDisplay: SecondScreenEmptyDisplay? = null
    private fun initSecondScreen() {
        if (getPresentationDisplays()!=null){
            bannerDisplay = SecondScreenBannerDisplay(this, getPresentationDisplays())
            emptyDisplay = SecondScreenEmptyDisplay(this, getPresentationDisplays())
            val isNeedSecond = SPUtils.getInstance().getInt(DataKey.KEY_IS_SECOND_SCREEN)
            if (isNeedSecond == 1) {
                showSecondScreen()
            }
        }
    }

    private fun showSecondScreen() {
        //传入指定文件夹的路径
        var hasImage = false
        val imagePath = File(AppConfig.CASHIER_SAVE_PATH, AppConfig.IMAGE_FILE_PATH)
        val imageFile = File(imagePath.path)
        if (imageFile.exists()) {
            if (imageFile.listFiles().isNotEmpty()) {
                hasImage = true
            }
        }
        if (!hasImage) {
            emptyDisplay?.show()
        } else {
            bannerDisplay?.show()
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

    /**
     * 右侧商品
     */
    private var salesProductAdapter: SalesProductAdapter? = null
    private fun setProductAdapter(productList: List<LitePalOrderDetailsBean.OrderProductAoListBean>) {

        if (salesProductAdapter == null) {
            salesProductAdapter = SalesProductAdapter(this, productList)
            val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            salesProduct_productRecycler.layoutManager = layoutManager
            salesProduct_productRecycler.adapter = salesProductAdapter
        } else {
            salesProductAdapter?.setList(productList)
        }

        setBoomView(productList)
    }

    private fun setBoomView(productList: List<LitePalOrderDetailsBean.OrderProductAoListBean>) {
        var productNum = 0.0
        var productPrice = 0.0
        for (i in 0 until productList.size) {
            productNum += productList[i].ordProductNum
            productPrice += productList[i].ordProductNum * productList[i].ordProductPrice
        }
        salesProduct_totalNum.text = productNum.toString()
        salesProduct_totalPrice.text = "￥" + NumUtils.getDoubleStr(productPrice)
    }

    override fun onDestroy() {
        if (bannerDisplay != null && bannerDisplay!!.isShow) {
            bannerDisplay!!.dismiss()
        }
        if (emptyDisplay != null && emptyDisplay!!.isShow) {
            emptyDisplay!!.dismiss()
        }
        super.onDestroy()
    }
}