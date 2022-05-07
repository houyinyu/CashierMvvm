package com.hby.cashier.ui

import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Display
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.hby.cashier.BR
import com.hby.cashier.R
import com.hby.cashier.adapter.TakeOrderLeftAdapter
import com.hby.cashier.adapter.TakeOrderProductAdapter
import com.hby.cashier.app.AppConfig
import com.hby.cashier.app.DataKey
import com.hby.cashier.bean.*
import com.hby.cashier.databinding.ActivityTakeOrderBinding
import com.hby.cashier.printer.PrinterServiceView
import com.hby.cashier.second_screen.BasePresentationHelper
import com.hby.cashier.second_screen.SecondScreenBannerDisplay
import com.hby.cashier.second_screen.SecondScreenEmptyDisplay
import com.hby.cashier.second_screen.SecondScreenPayDisplay
import com.hby.cashier.ui.tools.PriceCalculationView
import com.hby.cashier.utils.EnumUtils
import com.hby.cashier.utils.NumUtils
import com.hby.cashier.utils.TimeUtils
import com.hby.cashier.view_model.TakeOrderModel
import com.hby.cashier.weight.dialog.CollectionDialog
import com.hyy.mvvm.base.BaseActivity
import com.hyy.mvvm.utils.JsonUtils
import com.hyy.mvvm.utils.SPUtils
import com.hyy.mvvm.utils.ToastUtils
import kotlinx.android.synthetic.main.include_home_left_layout.*
import kotlinx.android.synthetic.main.include_sales_record_left_layout.*
import kotlinx.android.synthetic.main.include_sales_record_right_layout.*
import kotlinx.android.synthetic.main.include_take_order_left_layout.*
import kotlinx.android.synthetic.main.include_take_order_right_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal
import org.litepal.LitePal.deleteAll
import java.io.File


/**
 * 取单
 */
class TakeOrderActivity : BaseActivity<ActivityTakeOrderBinding, TakeOrderModel>() {
    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_take_order
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewObservable() {
        //radio监听
        viewModel.uc.groupSelectEvent.observe(this, Observer<Int?> {
            includeTakeOrderLeft_searchEdit.visibility = View.GONE
            includeTakeOrderLeft_startTimeBtn.visibility = View.GONE
            includeTakeOrderLeft_endTimeBtn.visibility = View.GONE
            includeTakeOrderLeft_searchBtn.visibility = View.GONE
            when (it) {
                R.id.includeTakeOrderLeft_orderNoRadio -> {
                    includeTakeOrderLeft_searchEdit.visibility = View.VISIBLE
                }
                R.id.includeTakeOrderLeft_dateRadio -> {
                    includeTakeOrderLeft_startTimeBtn.visibility = View.VISIBLE
                    includeTakeOrderLeft_endTimeBtn.visibility = View.VISIBLE
                    includeTakeOrderLeft_searchBtn.visibility = View.VISIBLE
                }
            }
        })
        //页面关闭监听
        viewModel.uc.activityFinishEvent.observe(this, Observer<Any?> {
            finish()
        })
        //开始时间选择监听
        viewModel.uc.startTimeEvent.observe(this, Observer<Any?> {
            choseStartTimePicker(includeTakeOrderLeft_startTimeBtn)
        })
        //结束时间选择监听
        viewModel.uc.endTimeEvent.observe(this, Observer<Any?> {
            choseEndTimePicker(includeTakeOrderLeft_endTimeBtn)
        })
        //时间搜索监听
        viewModel.uc.searchEvent.observe(this, Observer<Any?> {
            hangingOrderList =
                LitePal.where(
                    "createtimestamp > ? and createtimestamp < ?",
                    TimeUtils.getStringToDate(startTime, TimeUtils.DEFAULT_DATE_PATTERN).toString(),
                    TimeUtils.getStringToDate(endTime, TimeUtils.DEFAULT_DATE_PATTERN).toString()
                )
                    .find(LitePalHangingOrderBean::class.java)

            if (hangingOrderList.size > 0) {
                setLeftOrderAdapter()
            } else {
                ToastUtils.showShort("未搜索到订单")
            }

        })

        //右侧作废监听
        viewModel.uc.voidEvent.observe(this, Observer<Any?> {
            deleteAll(LitePalHangingOrderBean::class.java, "orderno = ?", choseOrderNo)
            reSetList()
        })
        //右侧继续收银监听
        viewModel.uc.keepCashierEvent.observe(this, Observer<Any?> {
            deleteAll(LitePalHangingOrderBean::class.java, "orderno = ?", choseOrderNo)
            EventBus.getDefault()
                .post(EventMessage("keepCashierEvent", JsonUtils.Object2Json(shopCartList)))
            finish()
        })
        //右侧直接结算监听
        viewModel.uc.settlementEvent.observe(this, Observer<Any?> {
            CollectionDialog(this)
                .setModel(viewModel)
                .setShopCartList(shopCartList)
                .setPayDisplay(payDisplay)
                .show()
        })
        //右侧打印监听
        viewModel.uc.printEvent.observe(this, Observer<Any?> {
            val printView = PrinterServiceView.getInstance()

            val orderPrice = PriceCalculationView.getProductPriceDefault(shopCartList) //订单原本价格
            val productPrice = PriceCalculationView.getProductPrice(shopCartList) //计算了活动的价格
            val promotionPrice = orderPrice - productPrice

            printView.setOrderPrice(PriceCalculationView.getProductPriceDefault(shopCartList))
            printView.setPromotionPrice(promotionPrice)
            printView.setRebatePrice(0.0)
            printView.setActualPrice(0.0)
            printView.setChangePrice(0.0)
            val jsonData: String = printView.buildProductData(shopCartList, "取单打印")
            printView.paySuccessToPrinter(jsonData, EnumUtils.PayMethod.PAY_NON)
        })
    }

    private fun reSetList() {
        EventBus.getDefault().post(EventMessage("refHangingOrderLayout"))
        hangingOrderList = LitePal.findAll(LitePalHangingOrderBean::class.java)
        if (hangingOrderList.size == 0) {
            finish()
            return
        }
        setLeftOrderAdapter()
    }

    override fun initData() {
        hangingOrderList = LitePal.findAll(LitePalHangingOrderBean::class.java)

        includeTakeOrderLeft_searchEdit.setOnEditorActionListener { v, actionId, event ->
            if ((actionId == EditorInfo.IME_ACTION_SEARCH)) {

                val searchStr = includeTakeOrderLeft_searchEdit.text.toString().trim()
                if (!TextUtils.isEmpty(searchStr)) {
                    hangingOrderList = LitePal.where("orderno = ?", searchStr)
                        .find(LitePalHangingOrderBean::class.java)
                } else {
                    hangingOrderList = LitePal.findAll(LitePalHangingOrderBean::class.java)
                }
                if (hangingOrderList.size > 0) {
                    setLeftOrderAdapter()
                } else {
                    ToastUtils.showShort("未搜索到订单")
                }

            }
            return@setOnEditorActionListener false
        }
        setLeftOrderAdapter()
        initSecondScreen()
    }

    /**
     * 初始化副屏
     */
    private var payDisplay: SecondScreenPayDisplay? = null
    private var bannerDisplay: SecondScreenBannerDisplay? = null
    private var emptyDisplay: SecondScreenEmptyDisplay? = null
    private fun initSecondScreen() {
        if (getPresentationDisplays() != null) {
            payDisplay = SecondScreenPayDisplay(this, getPresentationDisplays())
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
     * 左侧订单列表
     */
    private var takeOrderLeftAdapter: TakeOrderLeftAdapter? = null
    private var hangingOrderList: MutableList<LitePalHangingOrderBean> = ArrayList()
    private var choseOrderNo = ""//判断现在选择的是哪个
    private fun setLeftOrderAdapter() {
        for (i in 0 until hangingOrderList.size) {
            hangingOrderList[i].isSelect = i == 0
        }

        if (takeOrderLeftAdapter == null) {
            takeOrderLeftAdapter = TakeOrderLeftAdapter(this, hangingOrderList)
            val leftManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            includeTakeOrderLeft_orderRecycler.layoutManager = leftManager
            includeTakeOrderLeft_orderRecycler.adapter = takeOrderLeftAdapter

            takeOrderLeftAdapter?.setOnItemClickListener { adapter, view, position ->
                if (hangingOrderList[position].isSelect) return@setOnItemClickListener
                for (i in 0 until hangingOrderList.size) {
                    hangingOrderList[i].isSelect = position == i
                }
                takeOrderLeftAdapter?.setList(hangingOrderList)

                setRightProductAdapter(hangingOrderList[position])
                choseOrderNo = hangingOrderList[position].orderNo
            }
        } else {
            takeOrderLeftAdapter?.setList(hangingOrderList)
        }
        if (hangingOrderList.size > 0) {
            setRightProductAdapter(hangingOrderList[0])
            choseOrderNo = hangingOrderList[0].orderNo
        }
    }

    /**
     * 右侧商品
     */
    private var takeOrderProductAdapter: TakeOrderProductAdapter? = null
    private var shopCartList: MutableList<ShopCartBean> = ArrayList()
    private fun setRightProductAdapter(hangingOrderBean: LitePalHangingOrderBean) {
        shopCartList = JSON.parseArray(hangingOrderBean.shopCartJson, ShopCartBean::class.java)

        val shopCartProductList: MutableList<ShopCartBean.ProductListBean> = ArrayList()
        for (i in 0 until shopCartList.size) {
            for (j in 0 until shopCartList[i].productList.size) {
                shopCartProductList.add(shopCartList[i].productList[j])
            }
        }

        if (takeOrderProductAdapter == null) {
            takeOrderProductAdapter = TakeOrderProductAdapter(this, shopCartProductList)
            val leftManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            includeTakeOrderRight_productRecycler.layoutManager = leftManager
            includeTakeOrderRight_productRecycler.adapter = takeOrderProductAdapter
        } else {
            takeOrderProductAdapter?.setList(shopCartProductList)
        }

        includeTakeOrderRight_totalNum.text = hangingOrderBean.totalNum
        includeTakeOrderRight_totalPrice.text =
            "￥" + NumUtils.getDoubleStr(hangingOrderBean.totalPrice)
    }

    /**
     * 时间选择
     */
    private var startTime = ""
    private var endTime = ""

    private fun choseStartTimePicker(timeText: TextView) {
        TimeUtils.showDatePickerDialog(context, "请选择开始时间", object : TimeUtils.OnDatePickerListener {
            override fun onConfirm(year: Int, month: Int, dayOfMonth: Int) {
                val timeShow = "$year-$month-$dayOfMonth 00:00:00"
                if (!TextUtils.isEmpty(endTime) && TimeUtils.subtractStamp(timeShow, endTime) < 0) {
                    ToastUtils.showShort("开始时间必须小于结束时间")
                    return
                }
                startTime = timeShow
                timeText.text = timeShow
            }

            override fun onCancel() {}
        })
    }

    private fun choseEndTimePicker(timeText: TextView) {
        TimeUtils.showDatePickerDialog(context, "请选择结束时间", object : TimeUtils.OnDatePickerListener {
            override fun onConfirm(year: Int, month: Int, dayOfMonth: Int) {
                val timeShow = "$year-$month-$dayOfMonth 00:00:00"
                if (!TextUtils.isEmpty(startTime) && TimeUtils.subtractStamp(
                        startTime,
                        timeShow
                    ) < 0
                ) {
                    ToastUtils.showShort("结束时间必须大于开始时间")
                    return
                }
                endTime = timeShow
                timeText.text = timeShow
            }

            override fun onCancel() {}
        })
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(message: EventMessage) {
        val code = message.messageCode
        if (code == "takeOrderPayFinish") {
            deleteAll(LitePalHangingOrderBean::class.java, "orderno = ?", choseOrderNo)
            reSetList()
        }
    }


    public override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            //加上判断
            EventBus.getDefault().unregister(this)
        }

        if (payDisplay != null && payDisplay!!.isShow) {
            payDisplay!!.dismiss()
        }
        if (bannerDisplay != null && bannerDisplay!!.isShow) {
            bannerDisplay!!.dismiss()
        }
        if (emptyDisplay != null && emptyDisplay!!.isShow) {
            emptyDisplay!!.dismiss()
        }

        super.onDestroy()
    }
}