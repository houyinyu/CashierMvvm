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

    private val HANDLER_HIDE_KEY_BOARD = 0//隐藏软键盘
    private val HANDLER_SQ_DATA = 1//本地数据库数据同步
    private val HANDLER_SQ_DATA_HIDE = 10//隐藏本地数据同步loading
    private val HANDLER_SET_TIME = 2//动态刷新时间
    private val HANDLER_SET_SCREEN = 3//支付完成后重新显示副屏
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
        //当前登录用户的MID
        val userMid = SPUtils.getInstance().getString(DataKey.KEY_USER_MID)

        //根据登录用户的mid查询当前登录的店铺信息
        val selectConfigurationList =
            where("symid = ?", userMid).find(LitePalConfigurationBean::class.java)

        if (selectConfigurationList.size > 0) {
            //是否需要无码商品
            val isCodeLess = selectConfigurationList[0].syNotProductCode
            //保存数据同步频率
            SPUtils.getInstance()
                .put(DataKey.KEY_DATA_SYNC, selectConfigurationList[0].syDeviceSyncFreq)
            //保存是否需要交接班
            SPUtils.getInstance()
                .put(DataKey.KEY_IS_CHANGE, selectConfigurationList[0].sySignOutIsTransfer)
            //是否需要支付语音提示
            SPUtils.getInstance()
                .put(DataKey.KEY_IS_VOICE, selectConfigurationList[0].syIsSpeak)
            //是否需要无码商品
            SPUtils.getInstance().put(DataKey.KEY_IS_CODE_LESS, isCodeLess)
            if (isCodeLess == 0) {
                includeHomeRight_codelessProductBtn.visibility = View.GONE
            } else if (isCodeLess == 1) {
                includeHomeRight_codelessProductBtn.visibility = View.VISIBLE
            }
            //是否需要副屏
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
        //重新设置活动
        getEventData()
        //重新设置商品
        setFirstCategoryAdapter()
    }


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_main
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewObservable() {
        //顶部数据同步监听
        viewModel.uc.dataSynchronizationEvent.observe(this, Observer<Any?> {
            showDownLoad()
        })
        //顶部锁屏监听
        viewModel.uc.lockScreenEvent.observe(this, Observer<Any?> {
            SPUtils.getInstance().put(DataKey.KEY_IS_LOCK, true)
            startActivity(LockScreenActivity::class.java)
        })
        //顶部最小化监听
        viewModel.uc.minimizeUIEvent.observe(this, Observer<Any?> {
            val home = Intent(Intent.ACTION_MAIN)
            home.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            home.addCategory(Intent.CATEGORY_HOME)
            startActivity(home)
        })

        //左侧清空按钮监听
        viewModel.uc.clearShopCartEvent.observe(this, Observer<Any?> {
            reSetShopCartList()
        })
        //左侧挂按钮监听
        viewModel.uc.hangingOrderEvent.observe(this, Observer<Any?> {
            createHangingOrder()
        })
        //左侧取单按钮监听
        viewModel.uc.takeOrderEvent.observe(this, Observer<Any?> {
            startActivity(TakeOrderActivity::class.java)
        })
        //左侧收款按钮监听
        viewModel.uc.collectionEvent.observe(this, Observer<Any?> {
            CollectionDialog(this)
                .setModel(viewModel)
                .setShopCartList(shopCartList)
                .setVoicePlay(voicePlay)
                .setPayDisplay(payDisplay)
                .show()
        })

        //右侧销售统计监听
        viewModel.uc.salesStatisticsEvent.observe(this, Observer<Any?> {
            SalesStatisticsDialog(context).setModel(viewModel).data.show()
        })
        //右侧销售记录监听
        viewModel.uc.salesRecordEvent.observe(this, Observer<Any?> {
            startActivity(SalesRecordActivity::class.java)
        })
        //右侧交接班监听
        viewModel.uc.changeShiftsEvent.observe(this, Observer<Any?> {
//            ChangeShiftsDialog(context).setViewModel(viewModel).setOnDialogClickListener(object :
//                ChangeShiftsDialog.DialogClickListener() {
//                override fun confirm() {
//                    //退出登录
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
                        //退出登录
                        loginOut()
                    }

                })

        })
        //右侧交接班记录监听
        viewModel.uc.changeShiftsRecordEvent.observe(this, Observer<Any?> {
            ChangeShiftsRecordDialog(context).show()
        })
        //右侧无码商品监听
        viewModel.uc.codeLessEvent.observe(this, Observer<Any?> {
            CodelessProductDialog(this).setOnDialogClickListener(object :
                CodelessProductDialog.DialogClickListener() {
                override fun confirm(productBean: ShopCartBean.ProductListBean?) {
                    addShopCart(productBean!!)
                    handler.sendEmptyMessageDelayed(HANDLER_HIDE_KEY_BOARD, 200)
                }

            }).show()
        })
        //右侧去皮监听
        viewModel.uc.peelEvent.observe(this, Observer<Any?> {
            scaleView?.scalePresenter!!.tare();
        })
        //右侧置零监听
        viewModel.uc.setZeroEvent.observe(this, Observer<Any?> {
            scaleView?.scalePresenter!!.zero();
        })

        //副屏banner保存后触发
        viewModel.uc.bannerImageEvent.observe(this, Observer<Any> {
            //初始化副屏
            initSecondScreen()
        })

    }

    /**
     * 初始化方法
     */
    private var wifiView: WifiSignalView? = null//wifi
    private var scaleView: ScaleView? = null//称重
    private var dataSyncView: DataSyncView? = null//数据同步
    private var voicePlay: VoicePlay? = null//语音播报
    override fun initData() {
        super.initData()

        LogUtils.i(
            "***********************sessionID:" + SPUtils.getInstance()
                .getString(DataKey.KEY_SESSION_ID)
        )
        LogUtils.i("设备编号：" + DeviceUtils.getSN(this))
        //显示店铺和收银员姓名
        includeHomeTop_shopNameText.text = SPUtils.getInstance().getString(DataKey.KEY_SHOP_NAME)
        includeHomeTop_cashierText.text = SPUtils.getInstance().getString(DataKey.KEY_USER_NAME)
        //显示时间
        setTime()
        //wifi显示初始化
        wifiView = WifiSignalView(this, includeHomeTop_wifiBtn)
        wifiView?.registerWifiReceiver()
        //称重
//        includeHomeLeft_tareText.text = SpannableUtils.getSpannableBold(this, "皮重(kg)")
//        includeHomeLeft_weightText.text = SpannableUtils.getSpannableBold(this, "计重(kg)")
        scaleView = ScaleView(this, window.decorView);
        scaleView?.initView()
        //语音同不
        voicePlay = VoicePlay.with(context)
        //扫码
        onKeyBoardListener()
        initScanning()
        //打印
        PrinterServiceView.init(this)
        PrinterServiceView.getInstance().initPrintService()

        includeHomeRight_searchEdit.setOnEditorActionListener { v, actionId, event ->
            if ((actionId == EditorInfo.IME_ACTION_SEARCH)) {
                val searchStr = includeHomeRight_searchEdit.text.toString().trim()
                //条码、拼音码、品名
                var searchProductList =
                    LitePal.where(
                        "barcode = ? or proname like ? or probrandnamespell like ? or pronamespell like ?",
                        searchStr,
                        "%" + searchStr + "%",
                        "%" + searchStr + "%",
                        "%" + searchStr + "%"
                    )
                        .find(LitePalProductBean::class.java)
                //匹配活动
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
        //数据同步以及上行数据
        dataSyncView = DataSyncView(this, SPUtils.getInstance().getString(DataKey.KEY_SESSION_ID))
        dataSyncView?.startSyncDownLoad()
        dataSyncView?.startUpdateDisposable()

        //是否需要无码商品
        val isCodeLess = SPUtils.getInstance()
            .getInt(DataKey.KEY_IS_CODE_LESS)
        if (isCodeLess == 0) {
            includeHomeRight_codelessProductBtn.visibility = View.GONE
        } else if (isCodeLess == 1) {
            includeHomeRight_codelessProductBtn.visibility = View.VISIBLE
        }
    }

    /**
     * 初始化副屏
     */
    private var payDisplay: SecondScreenPayDisplay? = null
    private var bannerDisplay: SecondScreenBannerDisplay? = null
    private var emptyDisplay: SecondScreenEmptyDisplay? = null
    private fun initSecondScreen() {
        //获取配置信息
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
     * 初始化活动信息
     */
    private var selectEventProductList: List<LitePalEventProductBean> = ArrayList()//所有活动商品列表
    private var selectEventInfoList: List<LitePalEventInfoBean> = ArrayList()//存在的活动列表
    private var selectEventRuleList: List<LitePalEventRuleBean> = ArrayList()//存在的活动规则列表
    private fun getEventData() {
        //查询所有活动商品
        selectEventProductList = LitePal.findAll(LitePalEventProductBean::class.java)
        //查询我现在所存在的活动
        selectEventInfoList = LitePal.findAll(LitePalEventInfoBean::class.java)
        //查询所有活动规则
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
     * 右侧布局输入框宽度
     */
    private fun setRightInputW() {
        val rightParentW = DensityUtils.getScreenW(context as Activity?) * 5 / 8
        val rightInputW = (rightParentW - DensityUtils.dip2px(context, 20f)) / 2
        val layoutP = includeHomeRight_searchEdit.layoutParams as LinearLayout.LayoutParams
        includeHomeRight_searchEdit.width = rightInputW
        includeHomeRight_searchEdit.layoutParams = layoutP
    }


    /**
     * 右侧一级分类列表
     */
    private var firstCategoryAdapter: RightFirstCategoryAdapter? = null
    private fun setFirstCategoryAdapter() {
        //一级分类的列表
        val selectFirstCategoryList =
            LitePal.where("pid = ?", "0").find(LitePalCategoryBean::class.java)
        //如果一级分类下面没有商品数据就不显示
        val firstCategoryList: MutableList<LitePalCategoryBean> = ArrayList()
        for (i in 0 until selectFirstCategoryList.size) {
            val firstID = selectFirstCategoryList[i].classid
            //二级分类的列表
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
     * 右侧二级分类列表
     */
    private var secondCategoryAdapter: RightSecondCategoryAdapter? = null
    private fun setSecondCategoryAdapter(classID: String) {
        //二级分类的列表
        val selectSecondCategoryList =
            LitePal.where("pid = ?", classID).find(LitePalCategoryBean::class.java)

        //查询如果二级分类下没有商品就不显示这个分类
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
     * 右侧商品列表Adapter
     */
    private var rightProductAdapter: RightProductAdapter? = null
    private fun setRightProductAdapter(categoryID: String) {
        //通过二级分类ID查询关系表所有商品数据
        val proGroupList =
            LitePal.where("groupid = ?", categoryID).find(LitePalProGroupBean::class.java)
        //查询出所有商品列表
        val selectProductList = LitePal.findAll(LitePalProductBean::class.java)

        //匹配该分类下的所有商品
        var rightProductList: MutableList<LitePalProductBean> = ArrayList()
        for (i in 0 until proGroupList.size) {
            for (j in 0 until selectProductList.size) {
                if (proGroupList[i].proId == selectProductList[j].proId) {
                    rightProductList.add(selectProductList[j])
                }
            }
        }

        //匹配活动
        rightProductList = getEventProduct(rightProductList)

        if (rightProductAdapter == null) {
            rightProductAdapter = RightProductAdapter(this, rightProductList)
            includeHomeRight_productRecycler.layoutManager = GridLayoutManager(context, 3)
            includeHomeRight_productRecycler.adapter = rightProductAdapter
            rightProductAdapter?.setOnItemClickListener { _, _, position ->
                if (rightProductList[position].isInBulk > 0) {
                    //是称重商品
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
     * 判断有没有活动商品
     * 商品列表/搜索/扫码--使用（凡是第一次加载出商品的地方）
     */
    private fun getEventProduct(productList: MutableList<LitePalProductBean>): MutableList<LitePalProductBean> {
        //所有商品去匹配活动信息
        for (i in 0 until selectEventProductList.size) {
            for (j in 0 until productList.size) {
                if (selectEventProductList[i].productProSpeUnitId == productList[j].proSpecificationUnitId) {
                    //该商品是活动商品
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
     * 加入购物车-商品列表点击加入
     */
    private fun addShopCart(productBean: LitePalProductBean) {
        shopCartList = ShopCartDataView.getShopCartList(shopCartList, productBean)
        showShopLayout()
    }

    /**
     * 加入购物车-称重商品加入
     */
    private fun addShopCart(productBean: ShopCartBean.ProductListBean) {
        shopCartList = ShopCartDataView.getShopCartList(shopCartList, productBean)
        showShopLayout()
    }

    /**
     * 重置购物车界面
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

        //购物车没有数据就隐藏购物车列表界面，然后查询有没有挂单
        if (shopCartList.size == 0) {
            includeHomeLeft_shopCartLayout.visibility = View.GONE
            //先查询挂单是否存在
            val hangingOrderList = findAll(LitePalHangingOrderBean::class.java)
            if (hangingOrderList != null && hangingOrderList.size > 0) {
                //有挂单
                includeHomeLeft_takeOrderBtn.isEnabled = true
                includeHomeLeft_takeOrderBtn.text = "取单(" + hangingOrderList.size + ")"
            } else {
                includeHomeLeft_takeOrderBtn.isEnabled = false
                includeHomeLeft_takeOrderBtn.text = "取单"
            }
            return
        }
        includeHomeLeft_shopCartLayout.visibility = View.VISIBLE

        includeHomeLeft_shopCartNum.text =
            PriceCalculationView.getProductNum(shopCartList).toString()

        includeHomeLeft_showCartPrice.text =
            "￥" + NumUtils.getDoubleStr(PriceCalculationView.getProductPrice(shopCartList))
    }

    /**
     * 购物车商品adapter
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
     * 创建挂单
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
     * 时间显示动态刷新
     */
    private fun setTime() {
        includeHomeTop_timeText.text =
            SimpleDateFormat.getDateTimeInstance().format(System.currentTimeMillis())
        handler.sendEmptyMessageDelayed(HANDLER_SET_TIME, 1000)
    }

    /**
     * 扫码相关
     */
    private var scanningView: ScanningView? = null
    private var isInput = false //是否是输入状态（输入时扫码监听不拦截）
    private fun initScanning() {
        //拦截扫码器回调,获取扫码内容
        scanningView = ScanningView {
            hideKeyBoard()
            includeHomeRight_searchEdit.clearFocus()

            var searchProductList =
                LitePal.where("barcode = ?", it.trim()).find(LitePalProductBean::class.java)

            //匹配活动
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

    //监听软件盘是否弹起
    private fun onKeyBoardListener() {
        SoftKeyBoardListener.setListener(
            this,
            object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
                override fun keyBoardShow(height: Int) {
                    Log.e("软键盘", "键盘显示 高度$height")
                    isInput = true
                }

                override fun keyBoardHide(height: Int) {
                    Log.e("软键盘", "键盘隐藏 高度$height")
                    isInput = false
                }
            })
    }

    /**
     * 扫码和输入搜索商品逻辑
     */
    private fun doSearchProduct(searchStr: String, productList: List<LitePalProductBean>) {
        if (productList.isEmpty()) {
            ToastUtils.showShort("未搜索到商品信息")
            return
        }
        if (productList.size == 1) {
            if (productList[0].isInBulk > 0) {
                //是称重商品
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
                        //是称重商品
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
     * 数据下载（同步）
     */
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
                handler.sendEmptyMessageDelayed(HANDLER_SQ_DATA, 1000)
                handler.sendEmptyMessageDelayed(HANDLER_SQ_DATA_HIDE, 1000)
                LogUtils.i("*******************下载成功:" + file.absolutePath)
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
            //刷新挂单界面UI
            showShopLayout()
        }
        if (code == "keepCashierEvent") {
            //从取单和销售记录那里来收银
            val jsonData = message.messageText
            shopCartList = JSON.parseArray(jsonData, ShopCartBean::class.java)
            showShopLayout()
        }
        if (code == "hideKeyBoardMessage") {
            //隐藏软键盘
            handler.sendEmptyMessageDelayed(HANDLER_HIDE_KEY_BOARD, 200)
        }
        if (code == "clearShopCartData") {
            //清空购物车界面--线下支付完成后/线上支付生成二维码后
            reSetShopCartList()
        }
        if (code == "payFinish") {
            //支付完成后副屏显示banner
            handler.sendEmptyMessageDelayed(HANDLER_SET_SCREEN, 10000)
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
        //wifi
        wifiView?.unregisterWifiReceiver()
        //打印
        PrinterServiceView.getInstance().unBindPrintService()
        //称重
        scaleView?.onDestroy()
        //双屏管理
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
        //停止上行数据和同步
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

        //重新进入登录页面
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
        finish()
    }

}