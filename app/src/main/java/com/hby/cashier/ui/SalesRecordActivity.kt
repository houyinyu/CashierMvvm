package com.hby.cashier.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
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
import com.alibaba.fastjson.JSONObject
import com.hby.cashier.BR
import com.hby.cashier.R
import com.hby.cashier.adapter.SalesRecordOrderAdapter
import com.hby.cashier.adapter.SalesRecordProductAdapter
import com.hby.cashier.app.AppConfig
import com.hby.cashier.app.DataKey
import com.hby.cashier.bean.*
import com.hby.cashier.databinding.ActivitySalesRecordBinding
import com.hby.cashier.http.RequestConfig
import com.hby.cashier.second_screen.SecondScreenBannerDisplay
import com.hby.cashier.second_screen.SecondScreenEmptyDisplay
import com.hby.cashier.ui.tools.ShopCartDataView
import com.hby.cashier.utils.EnumUtils
import com.hby.cashier.utils.NumUtils
import com.hby.cashier.utils.TimeUtils
import com.hby.cashier.utils.TimeUtils.OnDatePickerListener
import com.hby.cashier.view_model.SalesRecordModel
import com.hyy.mvvm.base.BaseActivity
import com.hyy.mvvm.bean.BaseBean
import com.hyy.mvvm.http.HttpUtils
import com.hyy.mvvm.utils.DialogUtils
import com.hyy.mvvm.utils.JsonUtils
import com.hyy.mvvm.utils.SPUtils
import com.hyy.mvvm.utils.ToastUtils
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.include_home_left_layout.*
import kotlinx.android.synthetic.main.include_home_right_layout.*
import kotlinx.android.synthetic.main.include_sales_record_left_layout.*
import kotlinx.android.synthetic.main.include_sales_record_right_layout.*
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal.updateAll
import java.io.File

/**
 * ????????????
 */
class SalesRecordActivity : BaseActivity<ActivitySalesRecordBinding, SalesRecordModel>() {

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_sales_record
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewObservable() {
        //??????????????????
        viewModel.uc.activityFinishEvent.observe(this, Observer<Any?> {
            finish()
        })

        //radio??????
        viewModel.uc.groupSelectEvent.observe(this, Observer<Int?> {
            includeSalesRecordLeft_searchEdit.visibility = View.GONE
            includeSalesRecordLeft_startTimeBtn.visibility = View.GONE
            includeSalesRecordLeft_endTimeBtn.visibility = View.GONE
            includeSalesRecordLeft_searchBtn.visibility = View.GONE
            when (it) {
                R.id.includeSalesRecordLeft_orderNoRadio -> {
                    includeSalesRecordLeft_searchEdit.visibility = View.VISIBLE
                }
                R.id.includeSalesRecordLeft_dateRadio -> {
                    includeSalesRecordLeft_startTimeBtn.visibility = View.VISIBLE
                    includeSalesRecordLeft_endTimeBtn.visibility = View.VISIBLE
                    includeSalesRecordLeft_searchBtn.visibility = View.VISIBLE
                }
            }
        })
        //????????????????????????
        viewModel.uc.startTimeEvent.observe(this, Observer<Any?> {
            choseStartTimePicker(includeSalesRecordLeft_startTimeBtn)
        })
        //????????????????????????
        viewModel.uc.endTimeEvent.observe(this, Observer<Any?> {
            choseEndTimePicker(includeSalesRecordLeft_endTimeBtn)
        })
        //??????????????????
        viewModel.uc.searchEvent.observe(this, Observer<Any?> {
            if (TextUtils.isEmpty(startTime)) {
                ToastUtils.showShort("?????????????????????")
                return@Observer
            }
            if (TextUtils.isEmpty(endTime)) {
                ToastUtils.showShort("?????????????????????")
                return@Observer
            }
            orderSearchNo = ""
            reSetList()
            getSalesOrderRecord(true);
        })

        //???????????????
        viewModel.uc.refOrderDetailsEvent.observe(this, Observer<Any?> {
            if (TextUtils.isEmpty(orderDetailsNo)) {
                ToastUtils.showShort("??????????????????")
                return@Observer
            }
            getOrderDetails()
        })
        //??????????????????
        viewModel.uc.keepCashierEvent.observe(this, Observer<Any?> {
//            if (TextUtils.isEmpty(orderDetailsNo) || salesRecordProductList.size == 0) {
//                ToastUtils.showShort("??????????????????")
//                return@Observer
//            }
//            val shopCartList = ShopCartDataView.getShopCartList(salesRecordProductList)
//            EventBus.getDefault()
//                .post(EventMessage("keepCashierEvent", JsonUtils.Object2Json(shopCartList)))
            finish()
        })
        //???????????????(??????)
        viewModel.uc.returnOrderEvent.observe(this, Observer<Any?> {
            if (TextUtils.isEmpty(orderDetailsNo) || salesRecordProductList.size == 0) {
                ToastUtils.showShort("??????????????????")
                return@Observer
            }
            if (orderStatus == 0 || orderStatus == EnumUtils.OrderType.ORDER_FINISH) {
                ToastUtils.showShort("???????????????????????????")
                return@Observer
            }
            if (ordPaymentMethod == EnumUtils.PayMethod.PAY_CASH || ordPaymentMethod == EnumUtils.PayMethod.PAY_FREE) {
                orderDrop()
            } else {
                ToastUtils.showShort("???????????????????????????")
            }
//            if (ordIsBackType == 1) {
//                ToastUtils.showShort("?????????????????????")
//                return@Observer
//            }

//            createReturnOrder(JsonUtils.Object2Json(getRequestData()))
        })
    }

    override fun initData() {
        loadPtr()
        initSecondScreen()
        includeSalesRecordLeft_searchEdit.setOnEditorActionListener { v, actionId, event ->
            if ((actionId == EditorInfo.IME_ACTION_SEARCH)) {
                orderSearchNo = includeSalesRecordLeft_searchEdit.text.toString().trim()
                reSetList()
                startTime = ""
                endTime = ""
                getSalesOrderRecord(true);
            }
            return@setOnEditorActionListener false
        }

        getSalesOrderRecord(true)
    }

    /**
     * ???????????????
     */
    private var bannerDisplay: SecondScreenBannerDisplay? = null
    private var emptyDisplay: SecondScreenEmptyDisplay? = null
    private fun initSecondScreen() {
        if (getPresentationDisplays() != null) {
            bannerDisplay = SecondScreenBannerDisplay(this, getPresentationDisplays())
            emptyDisplay = SecondScreenEmptyDisplay(this, getPresentationDisplays())
            val isNeedSecond = SPUtils.getInstance().getInt(DataKey.KEY_IS_SECOND_SCREEN)
            if (isNeedSecond == 1) {
                showSecondScreen()
            }
        }
    }

    private fun showSecondScreen() {
        //??????????????????????????????
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
            Log.e("TAG", "??????" + displays[i] + " Flag: " + displays[i].flags)
            if (displays[i].flags and Display.FLAG_SECURE != 0 && displays[i].flags and Display.FLAG_SUPPORTS_PROTECTED_BUFFERS != 0 && displays[i].flags and Display.FLAG_PRESENTATION != 0) {
                Log.e("TAG", "????????????????????????????????????" + displays[i])
                return displays[i]
            }
        }
        return null
    }


    /**
     * ??????????????????
     */
    private var orderList: MutableList<RecordOrderResultBean.ResultObjectDTO.RowsDTO> = ArrayList()
    private var orderSearchNo = ""
    private var pageNum = 1
    private var isLoadMore = false
    private fun getSalesOrderRecord(loadingShow: Boolean) {
        if (loadingShow) viewModel.showDialog()
        val sessionID = SPUtils.getInstance().getString(DataKey.KEY_SESSION_ID)
        val deviceID = SPUtils.getInstance().getString(DataKey.KEY_DEVICE_ID)

        val queryBean = RecordOrderQueryBean()
        queryBean.stime = startTime
        queryBean.etime = endTime
        queryBean.ordNo = orderSearchNo
        queryBean.syDeviceId = "dev_$deviceID"
        val pageBean = RecordOrderQueryBean.PagingDTO()
        pageBean.page = pageNum
        pageBean.pageSize = 20
        queryBean.paging = pageBean

        HttpUtils.post(RequestConfig.request_orderQuery)
            .headers("sessionID", sessionID)
            .upJson(JsonUtils.Object2Json(queryBean))
            .execute(object : SimpleCallBack<String?>() {
                override fun onCompleted() {
                    super.onCompleted()
                    isLoadMore = false
                    includeSalesRecordLeft_refLayout.finishLoadMore()
                    includeSalesRecordLeft_refLayout.finishRefresh()
                    if (loadingShow) viewModel.dismissDialog()
                }

                override fun onError(e: ApiException) {
                    includeSalesRecordLeft_refLayout.finishLoadMoreWithNoMoreData()
                    if (loadingShow) viewModel.dismissDialog()
                }

                override fun onSuccess(data: String?) {
                    val baseBean = JsonUtils.parseJson(data, BaseBean::class.java)
                    if (baseBean.result != -1 && baseBean.result != -2 && baseBean.result != -4) {
                        val orderBean: RecordOrderResultBean =
                            JsonUtils.parseJson(data, RecordOrderResultBean::class.java)
                        val orderDataDataList = orderBean.resultObject.rows

                        if (orderDataDataList != null && orderDataDataList.size > 0) {
                            orderList.addAll(orderDataDataList)
                        } else {
                            orderList.clear()
                        }

                        setLeftOrderAdapter()

                        //????????????
                        if (orderList.size >= orderBean.resultObject.records) {
                            includeSalesRecordLeft_refLayout.finishLoadMoreWithNoMoreData()
                        } else {
                            includeSalesRecordLeft_refLayout.resetNoMoreData()
                        }

                        if (isLoadMore) {
                            pageNum += 1 // ?????????????????????,????????????,????????????
                        }
                    } else {
                        DialogUtils.singleDialog(
                            context as Activity,
                            "??????",
                            baseBean.message,
                            "??????",
                            null
                        )
                            .setOnDismissListener { }
                    }
                }
            })
    }


    /**
     * ??????????????????
     */
    private var salesRecordOrderAdapter: SalesRecordOrderAdapter? = null
    private fun setLeftOrderAdapter() {
        includeSalesRecordRight_topLayout.visibility = View.GONE
        includeSalesRecordRight_productLayout.visibility = View.GONE
        includeSalesRecordRight_totalLayout.visibility = View.GONE
        includeSalesRecordRight_boomLayout.visibility = View.GONE
        if (orderList.size > 0) {
            includeSalesRecordRight_topLayout.visibility = View.VISIBLE
            includeSalesRecordRight_productLayout.visibility = View.VISIBLE
            includeSalesRecordRight_totalLayout.visibility = View.VISIBLE
            includeSalesRecordRight_boomLayout.visibility = View.VISIBLE
            orderList[0].isCheck = true
            orderDetailsNo = orderList[0].ordNo
            getOrderDetails()
        }

        if (salesRecordOrderAdapter == null) {
            salesRecordOrderAdapter = SalesRecordOrderAdapter(this, orderList)
            val leftManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            includeSalesRecordLeft_orderRecycler.layoutManager = leftManager
            includeSalesRecordLeft_orderRecycler.adapter = salesRecordOrderAdapter

            salesRecordOrderAdapter?.setOnItemClickListener { adapter, view, position ->
                if (orderList[position].isCheck) return@setOnItemClickListener
                for (i in 0 until orderList.size) {
                    orderList[i].isCheck = position == i
                }
                orderDetailsNo = orderList[position].ordNo
                getOrderDetails()
                salesRecordOrderAdapter?.setList(orderList)
            }
        } else {
            salesRecordOrderAdapter?.setList(orderList)
        }
    }


    /**
     * ????????????????????????
     */
    private var orderDetailsNo = ""
    private var orderStatus = 0
    private var ordPaymentMethod = ""
    private var orderDetailsBean: RecordOrderDetailsBean? = null
    private fun getOrderDetails() {
        if (TextUtils.isEmpty(orderDetailsNo)) return
//        viewModel.showDialog()
        val sessionID = SPUtils.getInstance().getString(DataKey.KEY_SESSION_ID)
        HttpUtils.get(RequestConfig.request_selectOrderDetail)
            .headers("sessionID", sessionID)
            .params("ordNo", orderDetailsNo)
            .execute(object : SimpleCallBack<String?>() {
                override fun onCompleted() {
                    super.onCompleted()
//                    viewModel.dismissDialog()
                }

                override fun onError(e: ApiException) {
//                    viewModel.dismissDialog()
                }

                override fun onSuccess(data: String?) {
                    orderDetailsBean = JsonUtils.parseJson(data, RecordOrderDetailsBean::class.java)
                    if (orderDetailsBean != null && orderDetailsBean?.resultObject != null) {
                        val detailsInfo = orderDetailsBean?.resultObject?.orderMainVo!!
                        orderStatus = detailsInfo.ordStatus
                        ordPaymentMethod = detailsInfo.ordPaymentMethod
                        includeSalesRecordRight_orderStarus.text = EnumUtils.getOrderStatusStr(detailsInfo.ordStatus)
                        includeSalesRecordRight_totalPrice.text = "???" + NumUtils.getDoubleStr(detailsInfo.ordPrice)
                        salesRecordProductList = orderDetailsBean?.resultObject?.orderProductVo!!
                        setRightProductAdapter()
                    } else {
                        ToastUtils.showShort("????????????????????????")
                    }
                }
            })
    }

    /**
     * ????????????
     */
    private var salesRecordProductAdapter: SalesRecordProductAdapter? = null
    private var salesRecordProductList: MutableList<RecordOrderDetailsBean.ResultObjectDTO.OrderProductVoDTO> =
        ArrayList()

    private fun setRightProductAdapter() {
        if (salesRecordProductAdapter == null) {
            salesRecordProductAdapter = SalesRecordProductAdapter(this, salesRecordProductList)
            val leftManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            includeSalesRecordRight_productRecycler.layoutManager = leftManager
            includeSalesRecordRight_productRecycler.adapter = salesRecordProductAdapter
        } else {
            salesRecordProductAdapter?.setList(salesRecordProductList)
        }

        var totalNum = 0.0
        var totalPrice = 0.0
        var totalBackNum = 0
        for (i in 0 until salesRecordProductList.size) {
            totalNum += salesRecordProductList[i].ordProductNum
            totalPrice += (salesRecordProductList[i].ordProductNum * salesRecordProductList[i].ordProductPrice)
            totalBackNum += salesRecordProductList[i].ordProductBackNum
        }
        includeSalesRecordRight_totalNum.text = totalNum.toString()
//        includeSalesRecordRight_totalPrice.text = "???" + NumUtils.getDoubleStr(totalPrice)
        includeSalesRecordRight_totalBackNum.text = totalBackNum.toString()
    }

    /**
     * ????????????
     */
    private var startTime = ""
    private var endTime = ""

    private fun choseStartTimePicker(timeText: TextView) {
        TimeUtils.showDatePickerDialog(context, "?????????????????????", object : OnDatePickerListener {
            override fun onConfirm(year: Int, month: Int, dayOfMonth: Int) {
                val timeShow = "$year-$month-$dayOfMonth 00:00:00"
                if (!TextUtils.isEmpty(endTime) && TimeUtils.subtractStamp(timeShow, endTime) < 0) {
                    ToastUtils.showShort("????????????????????????????????????")
                    return
                }
                startTime = timeShow
                timeText.text = timeShow
            }

            override fun onCancel() {}
        })
    }

    private fun choseEndTimePicker(timeText: TextView) {
        TimeUtils.showDatePickerDialog(context, "?????????????????????", object : OnDatePickerListener {
            override fun onConfirm(year: Int, month: Int, dayOfMonth: Int) {
                val timeShow = "$year-$month-$dayOfMonth 00:00:00"
                if (!TextUtils.isEmpty(startTime) && TimeUtils.subtractStamp(
                        startTime,
                        timeShow
                    ) < 0
                ) {
                    ToastUtils.showShort("????????????????????????????????????")
                    return
                }
                endTime = timeShow
                timeText.text = timeShow
            }

            override fun onCancel() {}
        })
    }


    /**
     * ????????????
     */
    private fun createReturnOrder(data: String) {
        val sessionID = SPUtils.getInstance().getString(DataKey.KEY_SESSION_ID)
        HttpUtils.post(RequestConfig.request_orderBackCreate)
            .headers("sessionID", sessionID)
            .upJson(data)
            .execute(object : SimpleCallBack<String?>() {
                override fun onCompleted() {
                    super.onCompleted()

                }

                override fun onError(e: ApiException) {

                }

                override fun onSuccess(data: String?) {
                    val baseBean = JsonUtils.parseJson(data, BaseBean::class.java)
                    if (baseBean.result != -1 && baseBean.result != -2 && baseBean.result != -4) {
                        //??????????????????-??????????????????????????????
                        ToastUtils.showShort("??????????????????")
                        val values = ContentValues()
                        values.put("ordPaymentMethod", EnumUtils.PayMethod.PAY_RETURN)
                        updateAll(
                            LitePalOrderDetailsBean::class.java, values,
                            "ordno = ? ", orderDetailsNo
                        )

                        getOrderDetails()
                    }
                }
            })
    }

    private fun getRequestData(): CreateReturnOrderBean {
        val createReturnOrderBean = CreateReturnOrderBean()
        createReturnOrderBean.ordNo = orderDetailsNo
        createReturnOrderBean.ordBackComment = "?????????"
        for (i in 0 until salesRecordProductList.size) {
            val createReturnProduct = CreateReturnOrderBean.OrderBackProductListBean()
            createReturnProduct.id = salesRecordProductList[i].id
            createReturnProduct.ordBackProductBrandId = salesRecordProductList[i].ordProductBrandId
            createReturnProduct.ordBackProductBrandName =
                salesRecordProductList[i].ordProductBrandName
            createReturnProduct.ordBackProductId = salesRecordProductList[i].ordProductId
            createReturnProduct.ordBackProductImage = salesRecordProductList[i].ordProductImage
            createReturnProduct.ordBackProductManufacturer =
                salesRecordProductList[i].ordProductManufacturer
            createReturnProduct.ordBackProductName = salesRecordProductList[i].ordProductName
            createReturnProduct.ordBackProductNum = salesRecordProductList[i].ordProductNum
            createReturnProduct.ordBackProductPriceBack = salesRecordProductList[i].ordProductPrice
            createReturnProduct.ordBackProductPricePay = salesRecordProductList[i].ordProductPrice
            createReturnProduct.ordBackProductPriceOnline =
                salesRecordProductList[i].ordProductOnlinePrice
            createReturnProduct.ordBackProductProSpeUnitId =
                salesRecordProductList[i].ordProductProSpeUnitId
            createReturnProduct.ordBackProductSpecId = salesRecordProductList[i].ordProductSpecId
            createReturnProduct.ordBackProductSpecName =
                salesRecordProductList[i].ordProductSpecName
            createReturnProduct.ordBackProductUnitId = salesRecordProductList[i].ordProductUnitId
            createReturnProduct.ordBackProductUnitName =
                salesRecordProductList[i].ordProductUnitName
            createReturnProduct.ordBackShopId = salesRecordProductList[i].ordProductShopId
            createReturnOrderBean.orderBackProductList.add(createReturnProduct)
        }
        return createReturnOrderBean
    }


    /**
     * ????????????
     */
    private fun orderDrop() {
        val sessionID = SPUtils.getInstance().getString(DataKey.KEY_SESSION_ID)

        val postData = JSONObject()
        postData["ordNo"] = orderDetailsNo
        HttpUtils.post(RequestConfig.request_orderDrop)
            .headers("sessionID", sessionID)
            .upJson(postData.toJSONString())
            .execute(object : SimpleCallBack<String?>() {
                override fun onCompleted() {
                    super.onCompleted()

                }

                override fun onError(e: ApiException) {

                }

                override fun onSuccess(data: String?) {
                    val baseBean = JsonUtils.parseJson(data, BaseBean::class.java)
                    if (baseBean.result != -1 && baseBean.result != -2 && baseBean.result != -4) {
                        //??????????????????-??????????????????????????????
                        DialogUtils.singleDialog(
                            context as Activity,
                            "??????",
                            "??????????????????",
                            "??????",
                            null
                        )
                            .setOnDismissListener {

                                val values = ContentValues()
                                values.put("ordPaymentMethod", EnumUtils.PayMethod.PAY_RETURN)
                                updateAll(
                                    LitePalOrderDetailsBean::class.java, values,
                                    "ordno = ? ", orderDetailsNo
                                )
                                getOrderDetails()

                            }

                    } else {
                        DialogUtils.singleDialog(
                            context as Activity,
                            "??????",
                            baseBean.message,
                            "??????",
                            null
                        )
                            .setOnDismissListener {

                            }
                    }
                }
            })
    }

    /**
     * ????????????
     */
    private fun loadPtr() {
        includeSalesRecordLeft_refLayout.setEnableAutoLoadMore(false)//???????????????????????????????????????
        includeSalesRecordLeft_refLayout.setEnableLoadMore(true)
        includeSalesRecordLeft_refLayout.setOnRefreshListener(OnRefreshListener {
            reSetList()
            getSalesOrderRecord(false)
        })
        includeSalesRecordLeft_refLayout.setOnLoadMoreListener {
            if (!isLoadMore) {
                isLoadMore = true
                getSalesOrderRecord(false)
            }
        }
    }


    private fun reSetList() {
        isLoadMore = false
        pageNum = 1
        orderList = ArrayList()
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