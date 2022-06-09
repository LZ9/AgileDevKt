package com.lodz.android.agiledevkt.modules.location

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityLocationTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.service.BusService
import com.lodz.android.agiledevkt.modules.splash.CheckDialog
import com.lodz.android.corekt.anko.*
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.*
import permissions.dispatcher.ktx.LocationPermission
import permissions.dispatcher.ktx.constructLocationPermissionRequest

/**
 * 定位测试
 * Created by zhouL on 2018/9/28.
 */
class LocationTestActivity : BaseActivity() {

    companion object {

        /** 间隔时间 */
        const val LOCATION_INTERVAL_TIME = 2 * 1000L

        /** 原生GPS */
        const val LOCATION_GPS = 1
        /** 腾讯定位 */
        const val LOCATION_TENCENT = 2
        /** 高德定位 */
        const val LOCATION_AMAP = 3

        /** 定位方式 */
        const val EXTRA_TYPE = "extra_type"

        fun start(context: Context) {
            val intent = Intent(context, LocationTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityLocationTestBinding by bindingLayout(ActivityLocationTestBinding::inflate)

    /** 定位方式 */
    private var mLocationType = LOCATION_GPS
    /** 是否绑定 */
    private var isBind = false

    private val hasFinePermissions by lazy {
        constructLocationPermissionRequest(
            LocationPermission.FINE,//后台定位
            onShowRationale = ::onShowRationaleBeforeRequest,
            onPermissionDenied = ::onDenied,
            onNeverAskAgain = ::onNeverAskAgain,
            requiresPermission = ::onRequestPermission
        )
    }

    private val hasCoarsePermissions by lazy {
        constructLocationPermissionRequest(
            LocationPermission.COARSE,//后台定位
            onShowRationale = ::onShowRationaleBeforeRequest,
            onPermissionDenied = ::onDenied,
            onNeverAskAgain = ::onNeverAskAgain,
            requiresPermission = ::onRequestPermission
        )
    }

    private val hasBackgroundPermissions by lazy {
        constructLocationPermissionRequest(
            LocationPermission.BACKGROUND,//后台定位
            onShowRationale = ::onShowRationaleBeforeRequest,
            onPermissionDenied = ::onDenied,
            onNeverAskAgain = ::onNeverAskAgain,
            requiresPermission = ::onRequestPermission
        )
    }

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        onRequestPermission()
    }

    override fun initData() {
        super.initData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0以上的手机对权限进行动态申请
            onRequestPermission()//申请权限
        } else {
            initLogic()
        }
    }

    /** 权限申请成功 */
    private fun onRequestPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            hasFinePermissions.launch()
            return
        }
        if (!isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            hasCoarsePermissions.launch()
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !isPermissionGranted(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            hasBackgroundPermissions.launch()
            return
        }
        initLogic()
    }


    /** 用户拒绝后再次申请前告知用户为什么需要该权限 */
    private fun onShowRationaleBeforeRequest(request: PermissionRequest) {
        request.proceed()//请求权限
    }

    /** 被拒绝 */
    private fun onDenied() {
        toastShort(R.string.location_denied_permission_tips)
        showStatusError()
    }

    /** 被拒绝并且勾选了不再提醒 */
    private fun onNeverAskAgain() {
        toastShort(R.string.location_check_permission_tips)
        showPermissionCheckDialog()
        goAppDetailSetting()
    }

    /** 显示权限核对弹框 */
    private fun showPermissionCheckDialog() {
        val checkDialog = CheckDialog(getContext())
        checkDialog.setContentMsg(R.string.location_check_permission_title)
        checkDialog.setPositiveText(R.string.splash_check_permission_confirm, DialogInterface.OnClickListener { dialog, which ->
            onRequestPermission()
            dialog.dismiss()
        })
        checkDialog.setNegativeText(R.string.splash_check_permission_unconfirmed, DialogInterface.OnClickListener { dialog, which ->
            goAppDetailSetting()
        })
        checkDialog.setCanceledOnTouchOutside(false)
        checkDialog.setOnCancelListener {
            finish()
        }
        checkDialog.show()
    }

    override fun setListeners() {
        super.setListeners()

        // 定位方式
        mBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            mLocationType = when (checkedId) {
                R.id.gps_rb -> LOCATION_GPS
                R.id.tencent_rb -> LOCATION_TENCENT
                R.id.amap_rb -> LOCATION_AMAP
                else -> LOCATION_GPS
            }
        }

        // 绑定定位服务
        mBinding.bindServiceBtn.setOnClickListener {
            if (mLocationType != LOCATION_TENCENT && !isGpsOpen()) {
                toastShort(R.string.location_open_gps)
                goLocationSetting()
                return@setOnClickListener
            }
            bind(mLocationType)
        }

        // 解绑定位服务
        mBinding.unbindServiceBtn.setOnClickListener {
            unbind()
        }

        // 清空日志
        mBinding.cleanBtn.setOnClickListener {
            mBinding.logTv.text = ""
        }
    }

    /** 初始化 */
    private fun initLogic() {
        mBinding.intervalTv.text = getString(R.string.location_interval, (LOCATION_INTERVAL_TIME / 1000).toString())
        mBinding.updateTimeTv.text = getString(R.string.location_update_time, "无")
        mBinding.longitudeTv.text = getString(R.string.location_longitude, "无")//经度
        mBinding.latitudeTv.text = getString(R.string.location_latitude, "无")//纬度
        mBinding.mccTv.text = getString(R.string.location_mcc, "无")// 基站信息mcc
        mBinding.mncTv.text = getString(R.string.location_mnc, "无")// 基站信息mnc
        mBinding.lacTv.text = getString(R.string.location_lac, "无") // 基站信息lac
        mBinding.cidTv.text = getString(R.string.location_cid, "无") // 基站信息cid

        updateBindBtn()
        showStatusCompleted()
    }

    /** 更新按钮状态 */
    private fun updateBindBtn() {
        mBinding.bindServiceBtn.isEnabled = !isBind
        mBinding.unbindServiceBtn.isEnabled = isBind
    }

    /** 打印信息[result] */
    private fun printResult(result: String) {
        mBinding.logTv.text = (mBinding.logTv.text.toString() + result + "\n")
        runOnMainDelay(100){
            mBinding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationUpdateEvent(event: LocationUpdateEvent) {
        if (event.isLocationData) {
            val time = DateUtils.getCurrentFormatString(DateUtils.TYPE_2)
            mBinding.updateTimeTv.text = getString(R.string.location_update_time, time)
            mBinding.longitudeTv.text = getString(R.string.location_longitude, event.longitude)
            mBinding.latitudeTv.text = getString(R.string.location_latitude, event.latitude)
            mBinding.mccTv.text = getString(R.string.location_mcc, event.mcc)
            mBinding.mncTv.text = getString(R.string.location_mnc, event.mnc)
            mBinding.lacTv.text = getString(R.string.location_lac, event.lac)
            mBinding.cidTv.text = getString(R.string.location_cid, event.cid)
            printResult(event.log + "    " + time)
        } else {
            printResult(event.log)
        }
    }

    /** 绑定服务 */
    private fun bind(type: Int) {
        try {
            val intent = Intent(getContext(), BusService::class.java)
            intent.putExtra(EXTRA_TYPE, type)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            isBind = true
            printResult("已绑定，当前定位方式${getTypeName(type)}，请到室外无遮蔽处进行测试")
        } catch (e: Exception) {
            e.printStackTrace()
            isBind = false
            printResult("绑定服务失败，${e.cause}")
        }
        updateBindBtn()
    }

    /** 解绑服务 */
    private fun unbind() {
        try {
            stopService(Intent(getContext(), BusService::class.java))
            printResult("已解绑")
            isBind = false
        } catch (e: Exception) {
            e.printStackTrace()
            printResult("解绑服务失败，${e.cause}")
        }
        updateBindBtn()
    }

    /** 根据定位类型[type]获取名称 */
    private fun getTypeName(type: Int): String = when (type) {
        LOCATION_GPS -> "原生GPS"
        LOCATION_TENCENT -> "腾讯定位"
        LOCATION_AMAP -> "高德定位"
        else -> "无定位方式"
    }

    override fun finish() {
        if (isBind){
            unbind()
        }
        super.finish()
    }
}