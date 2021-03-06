package com.hby.cashier.ui

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.hby.cashier.BR
import com.hby.cashier.R
import com.hby.cashier.adapter.RightFirstCategoryAdapter
import com.hby.cashier.adapter.RightProductAdapter
import com.hby.cashier.adapter.RightSecondCategoryAdapter
import com.hby.cashier.adapter.ShopCartAdapter
import com.hby.cashier.app.DataKey
import com.hby.cashier.bean.*


import com.hby.cashier.databinding.ActivityMainBinding
import com.hby.cashier.datas.SQLiteManage
import com.hby.cashier.http.RequestConfig
import com.hby.cashier.printer.PrinterServiceView
import com.hby.cashier.view_model.MainViewModel
import com.hby.cashier.views.WifiSignalView
import com.hyy.mvvm.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_home_left_layout.*
import kotlinx.android.synthetic.main.include_home_top_layout.*
import java.text.SimpleDateFormat
import com.hby.cashier.scale.ScaleView
import com.hby.cashier.ui.tools.PriceCalculationView
import com.hby.cashier.ui.tools.ShopCartDataView
import com.hby.cashier.utils.*
import com.hby.cashier.views.ScanningView
import com.hby.cashier.views.SoftKeyBoardListener
import com.hby.cashier.weight.dialog.*
import com.hyy.mvvm.base.BaseViewModel
import com.hyy.mvvm.utils.JsonUtils
import com.hyy.mvvm.utils.SPUtils
import com.hyy.mvvm.utils.ToastUtils
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.include_home_right_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal
import org.litepal.LitePal.findAll
import java.io.File
import android.view.Display


import android.hardware.display.DisplayManager
import com.hby.cashier.app.AppConfig
import com.hby.cashier.second_screen.*
import com.hby.cashier.ui.tools.DataSyncView
import com.hby.cashier.ui.tools.SyncOrderBeforeChangeShiftsView
import com.hby.cashier.voice.VoicePlay
import org.litepal.LitePal.where
import org.xutils.common.util.LogUtil


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    private val HANDLER_HIDE_KEY_BOARD = 0//???????????????
    private val HANDLER_SQ_DATA = 1//???????????????????????????
    private val HANDLER_SQ_DATA_HIDE = 10//????????????????????????loading
    private val HANDLER_SET_TIME = 2//??????????????????
    private val HANDLER_SET_SCREEN = 3//?????????????????????????????????
    private val handler = Handler { msg ->
        when (msg.what) {
            HANDLER_HIDE_KEY_BOARD -> {
                hideKeyBoard()
            }
            HANDLER_SQ_DATA -> {
                SQLiteManage().initData()

                dataSyncRef()
            }
            HANDLER_SQ_DATA_HIDE -> {
                viewModel.dismissDialog()
            }
            HANDLER_SET_TIME -> {
                setTime()
            }
            HANDLER_SET_SCREEN -> {
                showSecondScreen()
            }
        }
        false
    }

    fun dataSyncRef() {
        //?????????????????????MID
        val userMid = SPUtils.getInstance().getString(DataKey.KEY_USER_MID)

        //?????????????????????mid?????????????????????????????????
        val selectConfigurationList =
            where("symid = ?", userMid).find(LitePalConfigurationBean::class.java)

        if (selectConfigurationList.size > 0) {
            //????????????????????????
            val isCodeLess = selectConfigurationList[0].syNotProductCode
            //????????????????????????
            SPUtils.getInstance()
                .put(DataKey.KEY_DATA_SYNC, selectConfigurationList[0].syDeviceSyncFreq)
            //???????????????????????????
            SPUtils.getInstance()
                .put(DataKey.KEY_IS_CHANGE, selectConfigurationList[0].sySignOutIsTransfer)
            //??????????????????????????????
            SPUtils.getInstance()
                .put(DataKey.KEY_IS_VOICE, selectConfigurationList[0].syIsSpeak)
            //????????????????????????
            SPUtils.getInstance().put(DataKey.KEY_IS_CODE_LESS, isCodeLess)
            if (isCodeLess == 0) {
                includeHomeRight_codelessProductBtn.visibility = View.GONE
            } else if (isCodeLess == 1) {
                includeHomeRight_codelessProductBtn.visibility = View.VISIBLE
            }
            //??????????????????
            val isNeedSecond = selectConfigurationList[0].sySecondScreenStatus
            SPUtils.getInstance().put(DataKey.KEY_IS_SECOND_SCREEN, isNeedSecond)
            if (isNeedSecond == 1) {
                showSecondScreen()
            } else {
                if (payDisplay != null && payDisplay!!.isShow) {
                    payDisplay!!.dismiss()
                }
                if (bannerDisplay != null && bannerDisplay!!.isShow) {
                    bannerDisplay!!.dismiss()
                }
                if (emptyDisplay != null && emptyDisplay!!.isShow) {
                    emptyDisplay!!.dismiss()
                }
            }
        }
        //??????????????????
        getEventData()
        //??????????????????
        setFirstCategoryAdapter()
    }


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_main
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewObservable() {
        //????????????????????????
        viewModel.uc.dataSynchronizationEvent.observe(this, Observer<Any?> {
            showDownLoad()
        })
        //??????????????????
        viewModel.uc.lockScreenEvent.observe(this, Observer<Any?> {
            SPUtils.getInstance().put(DataKey.KEY_IS_LOCK, true)
            startActivity(LockScreenActivity::class.java)
        })
        //?????????????????????
        viewModel.uc.minimizeUIEvent.observe(this, Observer<Any?> {
            val home = Intent(Intent.ACTION_MAIN)
            home.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            home.addCategory(Intent.CATEGORY_HOME)
            startActivity(home)
        })

        //????????????????????????
        viewModel.uc.clearShopCartEvent.observe(this, Observer<Any?> {
            reSetShopCartList()
        })
        //?????????????????????
        viewModel.uc.hangingOrderEvent.observe(this, Observer<Any?> {
            createHangingOrder()
        })
        //????????????????????????
        viewModel.uc.takeOrderEvent.observe(this, Observer<Any?> {
            startActivity(TakeOrderActivity::class.java)
        })
        //????????????????????????
        viewModel.uc.collectionEvent.observe(this, Observer<Any?> {
            CollectionDialog(this)
                .setModel(viewModel)
                .setShopCartList(shopCartList)
                .setVoicePlay(voicePlay)
                .setPayDisplay(payDisplay)
                .show()
        })

        //????????????????????????
        viewModel.uc.salesStatisticsEvent.observe(this, Observer<Any?> {
            SalesStatisticsDialog(context).setModel(viewModel).data.show()
        })
        //????????????????????????
        viewModel.uc.salesRecordEvent.observe(this, Observer<Any?> {
            startActivity(SalesRecordActivity::class.java)
        })
        //?????????????????????
        viewModel.uc.changeShiftsEvent.observe(this, Observer<Any?> {
//            ChangeShiftsDialog(context).setViewModel(viewModel).setOnDialogClickListener(object :
//                ChangeShiftsDialog.DialogClickListener() {
//                override fun confirm() {
//                    //????????????
//                    loginOut()
//                }
//
//            }).show()

            val sessionID = SPUtils.getInstance().getString(DataKey.KEY_SESSION_ID)
            SyncOrderBeforeChangeShiftsView(context, sessionID, viewModel)
                .updateData()
                .setOnDialogClickListener(object :
                    SyncOrderBeforeChangeShiftsView.DialogClickListener() {
                    override fun confirm() {
                        //????????????
                        loginOut()
                    }

                })

        })
        //???????????????????????????
        viewModel.uc.changeShiftsRecordEvent.observe(this, Observer<Any?> {
            ChangeShiftsRecordDialog(context).show()
        })
        //????????????????????????
        viewModel.uc.codeLessEvent.observe(this, Observer<Any?> {
            CodelessProductDialog(this).setOnDialogClickListener(object :
                CodelessProductDialog.DialogClickListener() {
                override fun confirm(productBean: ShopCartBean.ProductListBean?) {
                    addShopCart(productBean!!)
                    handler.sendEmptyMessageDelayed(HANDLER_HIDE_KEY_BOARD, 200)
                }

            }).show()
        })
        //??????????????????
        viewModel.uc.peelEvent.observe(this, Observer<Any?> {
            scaleView?.scalePresenter!!.tare();
        })
        //??????????????????
        viewModel.uc.setZeroEvent.observe(this, Observer<Any?> {
            scaleView?.scalePresenter!!.zero();
        })

        //??????banner???????????????
        viewModel.uc.bannerImageEvent.observe(this, Observer<Any> {
            //???????????????
            initSecondScreen()
        })

    }

    /**
     * ???????????????
     */
    private var wifiView: WifiSignalView? = null//wifi
    private var scaleView: ScaleView? = null//??????
    private var dataSyncView: DataSyncView? = null//????????????
    private var voicePlay: VoicePlay? = null//????????????
    override fun initData() {
        super.initData()

        LogUtils.i(
            "***********************sessionID:" + SPUtils.getInstance()
                .getString(DataKey.KEY_SESSION_ID)
        )
        LogUtils.i("???????????????" + DeviceUtils.getSN(this))
        //??????????????????????????????
        includeHomeTop_shopNameText.text = SPUtils.getInstance().getString(DataKey.KEY_SHOP_NAME)
        includeHomeTop_cashierText.text = SPUtils.getInstance().getString(DataKey.KEY_USER_NAME)
        //????????????
        setTime()
        //wifi???????????????
        wifiView = WifiSignalView(this, includeHomeTop_wifiBtn)
        wifiView?.registerWifiReceiver()
        //??????
//        includeHomeLeft_tareText.text = SpannableUtils.getSpannableBold(this, "??????(kg)")
//        includeHomeLeft_weightText.text = SpannableUtils.getSpannableBold(this, "??????(kg)")
        scaleView = ScaleView(this, window.decorView);
        scaleView?.initView()
        //????????????
        voicePlay = VoicePlay.with(context)
        //??????
        onKeyBoardListener()
        initScanning()
        //??????
        PrinterServiceView.init(this)
        PrinterServiceView.getInstance().initPrintService()

        includeHomeRight_searchEdit.setOnEditorActionListener { v, actionId, event ->
            if ((actionId == EditorInfo.IME_ACTION_SEARCH)) {
                val searchStr = includeHomeRight_searchEdit.text.toString().trim()
                //???????????????????????????
                var searchProductList =
                    LitePal.where(
                        "barcode = ? or proname like ? or probrandnamespell like ? or pronamespell like ?",
                        searchStr,
                        "%" + searchStr + "%",
                        "%" + searchStr + "%",
                        "%" + searchStr + "%"
                    )
                        .find(LitePalProductBean::class.java)
                //????????????
                searchProductList = getEventProduct(searchProductList)

                doSearchProduct(searchStr, searchProductList)

                hideKeyBoard()
            }
            return@setOnEditorActionListener false
        }

        getEventData()
        showShopLayout()
        setFirstCategoryAdapter()
        //
        setRightInputW()
        //??????????????????????????????
        dataSyncView = DataSyncView(this, SPUtils.getInstance().getString(DataKey.KEY_SESSION_ID))
        dataSyncView?.startSyncDownLoad()
        dataSyncView?.startUpdateDisposable()

        //????????????????????????
        val isCodeLess = SPUtils.getInstance()
            .getInt(DataKey.KEY_IS_CODE_LESS)
        if (isCodeLess == 0) {
            includeHomeRight_codelessProductBtn.visibility = View.GONE
        } else if (isCodeLess == 1) {
            includeHomeRight_codelessProductBtn.visibility = View.VISIBLE
        }
    }

    /**
     * ???????????????
     */
    private var payDisplay: SecondScreenPayDisplay? = null
    private var bannerDisplay: SecondScreenBannerDisplay? = null
    private var emptyDisplay: SecondScreenEmptyDisplay? = null
    private fun initSecondScreen() {
        //??????????????????
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
     * ?????????????????????
     */
    private var selectEventProductList: List<LitePalEventProductBean> = ArrayList()//????????????????????????
    private var selectEventInfoList: List<LitePalEventInfoBean> = ArrayList()//?????????????????????
    private var selectEventRuleList: List<LitePalEventRuleBean> = ArrayList()//???????????????????????????
    private fun getEventData() {
        //????????????????????????
        selectEventProductList = LitePal.findAll(LitePalEventProductBean::class.java)
        //?????????????????????????????????
        selectEventInfoList = LitePal.findAll(LitePalEventInfoBean::class.java)
        //????????????????????????
        selectEventRuleList = LitePal.findAll(LitePalEventRuleBean::class.java)
    }

    private fun getEventType(eventId: Long): Int {
        for (i in 0 until selectEventInfoList.size) {
            if (eventId == selectEventInfoList[i].eventId) {
                return selectEventInfoList[i].eventType
            }
        }
        return 0
    }


    /**
     * ???????????????????????????
     */
    private fun setRightInputW() {
        val rightParentW = DensityUtils.getScreenW(context as Activity?) * 5 / 8
        val rightInputW = (rightParentW - DensityUtils.dip2px(context, 20f)) / 2
        val layoutP = includeHomeRight_searchEdit.layoutParams as LinearLayout.LayoutParams
        includeHomeRight_searchEdit.width = rightInputW
        includeHomeRight_searchEdit.layoutParams = layoutP
    }


    /**
     * ????????????????????????
     */
    private var firstCategoryAdapter: RightFirstCategoryAdapter? = null
    private fun setFirstCategoryAdapter() {
        //?????????????????????
        val selectFirstCategoryList =
            LitePal.where("pid = ?", "0").find(LitePalCategoryBean::class.java)
        //??????????????????????????????????????????????????????
        val firstCategoryList: MutableList<LitePalCategoryBean> = ArrayList()
        for (i in 0 until selectFirstCategoryList.size) {
            val firstID = selectFirstCategoryList[i].classid
            //?????????????????????
            val selectSecondCategoryList =
                LitePal.where("pid = ?", firstID).find(LitePalCategoryBean::class.java)
            var hasProduct = false
            for (j in 0 until selectSecondCategoryList.size) {
                val categoryID = selectSecondCategoryList[j].categoryId
                val proGroupList =
                    LitePal.where("groupid = ?", categoryID).find(LitePalProGroupBean::class.java)
                if (proGroupList.size > 0) {
                    hasProduct = true
                    break
                }
            }
            if (hasProduct) {
                firstCategoryList.add(selectFirstCategoryList[i])
            }
        }

        if (firstCategoryList.size > 0) {
            firstCategoryList[0].isSelect = true
            setSecondCategoryAdapter(firstCategoryList[0].classid)
        }

        if (firstCategoryAdapter == null) {
            firstCategoryAdapter = RightFirstCategoryAdapter(context, firstCategoryList)
            val firstCategoryManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            includeHomeRight_firstCategoryRecycler.layoutManager = firstCategoryManager
            includeHomeRight_firstCategoryRecycler.adapter = firstCategoryAdapter

            firstCategoryAdapter?.setOnItemClickListener { adapter, view, position ->
                if (firstCategoryList[position].isSelect) return@setOnItemClickListener
                for (i in 0 until firstCategoryList.size) {
                    firstCategoryList[i].isSelect = position == i
                }
                firstCategoryAdapter?.setList(firstCategoryList)
                setSecondCategoryAdapter(firstCategoryList[position].classid)
            }
        } else {
            firstCategoryAdapter?.setList(firstCategoryList)
        }

    }

    /**
     * ????????????????????????
     */
    private var secondCategoryAdapter: RightSecondCategoryAdapter? = null
    private fun setSecondCategoryAdapter(classID: String) {
        //?????????????????????
        val selectSecondCategoryList =
            LitePal.where("pid = ?", classID).find(LitePalCategoryBean::class.java)

        //???????????????????????????????????????????????????????????????
        val secondCategoryList: MutableList<LitePalCategoryBean> = ArrayList()
        for (i in 0 until selectSecondCategoryList.size) {
            val categoryID = selectSecondCategoryList[i].categoryId
            val proGroupList =
                LitePal.where("groupid = ?", categoryID).find(LitePalProGroupBean::class.java)
            if (proGroupList.size > 0) {
                secondCategoryList.add(selectSecondCategoryList[i])
            }
        }

        if (secondCategoryList.size > 0) {
            secondCategoryList[0].isSelect = true
            setRightProductAdapter(secondCategoryList[0].categoryId)
        } else {
            setRightProductAdapter("0")
        }

        if (secondCategoryAdapter == null) {
            secondCategoryAdapter = RightSecondCategoryAdapter(context, secondCategoryList)
            val secondCategoryManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            includeHomeRight_secondCategoryRecycler.layoutManager = secondCategoryManager
            includeHomeRight_secondCategoryRecycler.adapter = secondCategoryAdapter

            secondCategoryAdapter?.setOnItemClickListener { adapter, view, position ->
                if (secondCategoryList[position].isSelect) return@setOnItemClickListener
                for (i in 0 until secondCategoryList.size) {
                    secondCategoryList[i].isSelect = position == i
                }
                secondCategoryAdapter?.setList(secondCategoryList)

                setRightProductAdapter(secondCategoryList[position].categoryId)
            }
        } else {
            secondCategoryAdapter?.setList(secondCategoryList)
        }
    }

    /**
     * ??????????????????Adapter
     */
    private var rightProductAdapter: RightProductAdapter? = null
    private fun setRightProductAdapter(categoryID: String) {
        //??????????????????ID?????????????????????????????????
        val proGroupList =
            LitePal.where("groupid = ?", categoryID).find(LitePalProGroupBean::class.java)
        //???????????????????????????
        val selectProductList = LitePal.findAll(LitePalProductBean::class.java)

        //?????????????????????????????????
        var rightProductList: MutableList<LitePalProductBean> = ArrayList()
        for (i in 0 until proGroupList.size) {
            for (j in 0 until selectProductList.size) {
                if (proGroupList[i].proId == selectProductList[j].proId) {
                    rightProductList.add(selectProductList[j])
                }
            }
        }

        //????????????
        rightProductList = getEventProduct(rightProductList)

        if (rightProductAdapter == null) {
            rightProductAdapter = RightProductAdapter(this, rightProductList)
            includeHomeRight_productRecycler.layoutManager = GridLayoutManager(context, 3)
            includeHomeRight_productRecycler.adapter = rightProductAdapter
            rightProductAdapter?.setOnItemClickListener { _, _, position ->
                if (rightProductList[position].isInBulk > 0) {
                    //???????????????
                    WeighProductDialog(this)
                        .setProductData(rightProductList[position])
                        .setScalePresenter(scaleView)
                        .setOnDialogClickListener(object :
                            WeighProductDialog.DialogClickListener() {
                            override fun confirm(productBean: ShopCartBean.ProductListBean?) {
                                addShopCart(productBean!!)
                            }

                        })
                        .show()
                    return@setOnItemClickListener
                }
                addShopCart(rightProductList[position])
            }
        } else {
            rightProductAdapter?.setList(rightProductList)
        }
    }

    /**
     * ???????????????????????????
     * ????????????/??????/??????--???????????????????????????????????????????????????
     */
    private fun getEventProduct(productList: MutableList<LitePalProductBean>): MutableList<LitePalProductBean> {
        //?????????????????????????????????
        for (i in 0 until selectEventProductList.size) {
            for (j in 0 until productList.size) {
                if (selectEventProductList[i].productProSpeUnitId == productList[j].proSpecificationUnitId) {
                    //????????????????????????
                    productList[j].eventID = selectEventProductList[i].eventId
                    productList[j].eventType = getEventType(selectEventProductList[i].eventId)
                    if (selectEventProductList[i].productSpecialPrice > 0) {
                        productList[j].specialPrice =
                            NumUtils.getProductPrice(selectEventProductList[i].productSpecialPrice)
                    }
                    break
                }
            }
        }
        return productList
    }

    /**
     * ???????????????-????????????????????????
     */
    private fun addShopCart(productBean: LitePalProductBean) {
        shopCartList = ShopCartDataView.getShopCartList(shopCartList, productBean)
        showShopLayout()
    }

    /**
     * ???????????????-??????????????????
     */
    private fun addShopCart(productBean: ShopCartBean.ProductListBean) {
        shopCartList = ShopCartDataView.getShopCartList(shopCartList, productBean)
        showShopLayout()
    }

    /**
     * ?????????????????????
     */
    fun showShopLayout() {
        for (i in shopCartList.size - 1 downTo 0) {
            for (j in shopCartList[i].productList.size - 1 downTo 0) {
                if (shopCartList[i].productList[j].productNum <= 0) {
                    shopCartList[i].productList.removeAt(j)
                }
            }
        }
        for (i in shopCartList.size - 1 downTo 0) {
            if (shopCartList[i].productList.isEmpty()) {
                shopCartList.removeAt(i)
            }
        }
        setShopCartAdapter()

        //?????????????????????????????????????????????????????????????????????????????????
        if (shopCartList.size == 0) {
            includeHomeLeft_shopCartLayout.visibility = View.GONE
            //???????????????????????????
            val hangingOrderList = findAll(LitePalHangingOrderBean::class.java)
            if (hangingOrderList != null && hangingOrderList.size > 0) {
                //?????????
                includeHomeLeft_takeOrderBtn.isEnabled = true
                includeHomeLeft_takeOrderBtn.text = "??????(" + hangingOrderList.size + ")"
            } else {
                includeHomeLeft_takeOrderBtn.isEnabled = false
                includeHomeLeft_takeOrderBtn.text = "??????"
            }
            return
        }
        includeHomeLeft_shopCartLayout.visibility = View.VISIBLE

        includeHomeLeft_shopCartNum.text =
            PriceCalculationView.getProductNum(shopCartList).toString()

        includeHomeLeft_showCartPrice.text =
            "???" + NumUtils.getDoubleStr(PriceCalculationView.getProductPrice(shopCartList))
    }

    /**
     * ???????????????adapter
     */
    private var shopCartAdapter: ShopCartAdapter? = null
    private var shopCartList: MutableList<ShopCartBean> = ArrayList()
    private fun setShopCartAdapter() {
        if (shopCartAdapter == null) {
            shopCartAdapter =
                ShopCartAdapter(this, shopCartList)
            val leftManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            includeHomeLeft_shopCartRecycler.layoutManager = leftManager
            includeHomeLeft_shopCartRecycler.adapter = shopCartAdapter
        } else {
            shopCartAdapter?.setList(shopCartList)
        }
    }

    private fun reSetShopCartList() {
        shopCartList.clear()
        showShopLayout()
    }

    /**
     * ????????????
     */
    private fun createHangingOrder() {
        if (shopCartList.size == 0) {
            return
        }

        val nowTime = System.currentTimeMillis()
        val hangingOrderBean = LitePalHangingOrderBean()
        hangingOrderBean.orderNo = "GD$nowTime"
        hangingOrderBean.createTime = TimeUtils.getTime(nowTime)
        hangingOrderBean.createTimeStamp = nowTime
        hangingOrderBean.totalPrice = PriceCalculationView.getProductPrice(shopCartList)
        hangingOrderBean.totalNum = PriceCalculationView.getProductNum(shopCartList).toString()
        hangingOrderBean.shopCartJson = JsonUtils.Object2Json(shopCartList)
        hangingOrderBean.save()
        reSetShopCartList()
    }

    /**
     * ????????????????????????
     */
    private fun setTime() {
        includeHomeTop_timeText.text =
            SimpleDateFormat.getDateTimeInstance().format(System.currentTimeMillis())
        handler.sendEmptyMessageDelayed(HANDLER_SET_TIME, 1000)
    }

    /**
     * ????????????
     */
    private var scanningView: ScanningView? = null
    private var isInput = false //?????????????????????????????????????????????????????????
    private fun initScanning() {
        //?????????????????????,??????????????????
        scanningView = ScanningView {
            hideKeyBoard()
            includeHomeRight_searchEdit.clearFocus()

            var searchProductList =
                LitePal.where("barcode = ?", it.trim()).find(LitePalProductBean::class.java)

            //????????????
            searchProductList = getEventProduct(searchProductList)

            LogUtil.i("************************hahahahah")
            doSearchProduct(it.trim(), searchProductList)
        }
    }


    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode != KeyEvent.KEYCODE_BACK && !isInput) {
            scanningView?.analysisKeyEvent(event)
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    //???????????????????????????
    private fun onKeyBoardListener() {
        SoftKeyBoardListener.setListener(
            this,
            object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
                override fun keyBoardShow(height: Int) {
                    Log.e("?????????", "???????????? ??????$height")
                    isInput = true
                }

                override fun keyBoardHide(height: Int) {
                    Log.e("?????????", "???????????? ??????$height")
                    isInput = false
                }
            })
    }

    /**
     * ?????????????????????????????????
     */
    private fun doSearchProduct(searchStr: String, productList: List<LitePalProductBean>) {
        if (productList.isEmpty()) {
            ToastUtils.showShort("????????????????????????")
            return
        }
        if (productList.size == 1) {
            if (productList[0].isInBulk > 0) {
                //???????????????
                WeighProductDialog(this)
                    .setProductData(productList[0])
                    .setScalePresenter(scaleView)
                    .setOnDialogClickListener(object : WeighProductDialog.DialogClickListener() {
                        override fun confirm(productBean: ShopCartBean.ProductListBean?) {
                            addShopCart(productBean!!)
                        }

                    })
                    .show()
                return
            }
            addShopCart(productList[0])
            return
        }
        ProductSearchDialog(context)
            .setSearchText(searchStr)
            .setProductList(productList)
            .setOnItemClickListener(object : ProductSearchDialog.DialogClickListener() {
                override fun itemClick(productBean: LitePalProductBean?) {
                    if (productBean?.isInBulk!! > 0) {
                        //???????????????
                        WeighProductDialog(context as BaseActivity<*, out BaseViewModel<*>>?)
                            .setProductData(productBean)
                            .setScalePresenter(scaleView)
                            .setOnDialogClickListener(object :
                                WeighProductDialog.DialogClickListener() {
                                override fun confirm(productBean: ShopCartBean.ProductListBean?) {
                                    addShopCart(productBean!!)
                                }
                            })
                            .show()
                        return
                    }
                    addShopCart(productBean)
                }

            }).show()
    }


    /**
     * ????????????????????????
     */
    private fun showDownLoad() {
        //??????????????????
        PermissionUtils(context).showSinglePermission(
            "??????",
            Permission.WRITE_EXTERNAL_STORAGE,
            object : PermissionUtils.OnPermissionListener {
                override fun finish() {
                }

                override fun agree() {
                    //????????????
                    goDownLoad()
                }

                override fun cancel() {
                    ToastUtils.showShort("?????????????????????")
                }

            })
    }

    private fun goDownLoad() {
        viewModel.showDialog()
        val downUrl = AppConfig.BASE_DOWNLOAD_URL + RequestConfig.request_downZip
        DownloadUtil().download(context, downUrl, object : DownloadUtil.OnDownloadListener {
            override fun onDownloadSuccess(file: File) {
                ZipUtils.unZipFilesTest(file)
                handler.sendEmptyMessageDelayed(HANDLER_SQ_DATA, 1000)
                handler.sendEmptyMessageDelayed(HANDLER_SQ_DATA_HIDE, 1000)
                LogUtils.i("*******************????????????:" + file.absolutePath)
            }

            override fun onDownloading(progress: Int) {
                LogUtils.i("*******************progress:" + progress)
            }

            override fun onDownloadFailed() {
                handler.sendEmptyMessageDelayed(HANDLER_SQ_DATA_HIDE, 1000)
                LogUtils.i("*******************onDownloadFailed")
            }
        })
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(message: EventMessage) {
        val code = message.messageCode
        if (code == "refHangingOrderLayout") {
            //??????????????????UI
            showShopLayout()
        }
        if (code == "keepCashierEvent") {
            //???????????????????????????????????????
            val jsonData = message.messageText
            shopCartList = JSON.parseArray(jsonData, ShopCartBean::class.java)
            showShopLayout()
        }
        if (code == "hideKeyBoardMessage") {
            //???????????????
            handler.sendEmptyMessageDelayed(HANDLER_HIDE_KEY_BOARD, 200)
        }
        if (code == "clearShopCartData") {
            //?????????????????????--?????????????????????/??????????????????????????????
            reSetShopCartList()
        }
        if (code == "payFinish") {
            //???????????????????????????banner
            handler.sendEmptyMessageDelayed(HANDLER_SET_SCREEN, 10000)
        }
    }


    public override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {//????????????
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            //????????????
            EventBus.getDefault().unregister(this)
        }
        //wifi
        wifiView?.unregisterWifiReceiver()
        //??????
        PrinterServiceView.getInstance().unBindPrintService()
        //??????
        scaleView?.onDestroy()
        //????????????
//        BasePresentationHelper.getInstance().dismissAll();
        if (payDisplay != null && payDisplay!!.isShow) {
            payDisplay!!.dismiss()
        }
        if (bannerDisplay != null && bannerDisplay!!.isShow) {
            bannerDisplay!!.dismiss()
        }
        if (emptyDisplay != null && emptyDisplay!!.isShow) {
            emptyDisplay!!.dismiss()
        }
        //???????????????????????????
        dataSyncView?.stopSyncPolling()
        dataSyncView?.stopUpdatePolling()
        super.onDestroy()
    }


    override fun onResume() {
        super.onResume()
        val isLock = SPUtils.getInstance().getBoolean(DataKey.KEY_IS_LOCK)
        if (isLock) {
            startActivity(LockScreenActivity::class.java)
        }
        val isNeedSecond = SPUtils.getInstance().getInt(DataKey.KEY_IS_SECOND_SCREEN)
        if (isNeedSecond == 1)
            showSecondScreen()
    }

    fun loginOut() {
        SPUtils.getInstance().clear()
        SPUtils.getInstance().put(DataKey.KEY_FIRST, false)

        //????????????????????????
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
        finish()
    }

}