package com.lodz.android.agiledevkt.modules.location

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.service.BusService
import com.lodz.android.agiledevkt.modules.splash.CheckDialog
import com.lodz.android.corekt.anko.*
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import kotlinx.coroutines.GlobalScope
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.*
import permissions.dispatcher.ktx.LocationPermission
import permissions.dispatcher.ktx.constructLocationPermissionRequest
import permissions.dispatcher.ktx.constructPermissionsRequest

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

    /** 间隔时间 */
    private val mIntervalTv by bindView<TextView>(R.id.interval_tv)
    /** 更新时间 */
    private val mUpdateTimeTv by bindView<TextView>(R.id.update_time_tv)
    /** 经度 */
    private val mLongitudeTv by bindView<TextView>(R.id.longitude_tv)
    /** 纬度 */
    private val mLatitudeTv by bindView<TextView>(R.id.latitude_tv)
    /** 基站信息mcc */
    private val mMccTv by bindView<TextView>(R.id.mcc_tv)
    /** 基站信息mnc */
    private val mMncTv by bindView<TextView>(R.id.mnc_tv)
    /** 基站信息lac */
    private val mLacTv by bindView<TextView>(R.id.lac_tv)
    /** 基站信息cid */
    private val mCidTv by bindView<TextView>(R.id.cid_tv)

    /** 滚动控件 */
    private val mScrollView by bindView<ScrollView>(R.id.scroll_view)
    /** 日志信息 */
    private val mLogTv by bindView<TextView>(R.id.log_tv)
    /** 清空日志 */
    private val mCleanBtn by bindView<TextView>(R.id.clean_btn)

    /** 定位方式 */
    private val mRadioGroup by bindView<RadioGroup>(R.id.radio_group)

    /** 绑定定位服务 */
    private val mBindServiceBtn by bindView<MaterialButton>(R.id.bind_service_btn)
    /** 解绑定位服务 */
    private val mUnbindServiceBtn by bindView<MaterialButton>(R.id.unbind_service_btn)

    /** 定位方式 */
    private var mLocationType = LOCATION_GPS
    /** 是否绑定 */
    private var isBind = false

    private val hasFinePermissions = constructLocationPermissionRequest(
        LocationPermission.FINE,//后台定位
        onShowRationale = ::onShowRationaleBeforeRequest,
        onPermissionDenied = ::onDenied,
        onNeverAskAgain = ::onNeverAskAgain,
        requiresPermission = ::onRequestPermission
    )

    private val hasCoarsePermissions = constructLocationPermissionRequest(
        LocationPermission.COARSE,//后台定位
        onShowRationale = ::onShowRationaleBeforeRequest,
        onPermissionDenied = ::onDenied,
        onNeverAskAgain = ::onNeverAskAgain,
        requiresPermission = ::onRequestPermission
    )

    private val hasBackgroundPermissions = constructLocationPermissionRequest(
        LocationPermission.BACKGROUND,//后台定位
        onShowRationale = ::onShowRationaleBeforeRequest,
        onPermissionDenied = ::onDenied,
        onNeverAskAgain = ::onNeverAskAgain,
        requiresPermission = ::onRequestPermission
    )

    override fun getLayoutId(): Int = R.layout.activity_location_test

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

        mRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            mLocationType = when (checkedId) {
                R.id.gps_rb -> LOCATION_GPS
                R.id.tencent_rb -> LOCATION_TENCENT
                R.id.amap_rb -> LOCATION_AMAP
                else -> LOCATION_GPS
            }
        }

        mBindServiceBtn.setOnClickListener {
            if (mLocationType != LOCATION_TENCENT && !isGpsOpen()) {
                toastShort(R.string.location_open_gps)
                goLocationSetting()
                return@setOnClickListener
            }
            bind(mLocationType)
        }

        mUnbindServiceBtn.setOnClickListener {
            unbind()
        }

        mCleanBtn.setOnClickListener {
            mLogTv.text = ""
        }
    }

    /** 初始化 */
    private fun initLogic() {
        mIntervalTv.text = getString(R.string.location_interval, (LOCATION_INTERVAL_TIME / 1000).toString())
        mUpdateTimeTv.text = getString(R.string.location_update_time, "无")
        mLongitudeTv.text = getString(R.string.location_longitude, "无")
        mLatitudeTv.text = getString(R.string.location_latitude, "无")
        mMccTv.text = getString(R.string.location_mcc, "无")
        mMncTv.text = getString(R.string.location_mnc, "无")
        mLacTv.text = getString(R.string.location_lac, "无")
        mCidTv.text = getString(R.string.location_cid, "无")

        updateBindBtn()
        showStatusCompleted()
    }

    /** 更新按钮状态 */
    private fun updateBindBtn() {
        mBindServiceBtn.isEnabled = !isBind
        mUnbindServiceBtn.isEnabled = isBind
    }

    /** 打印信息[result] */
    private fun printResult(result: String) {
        mLogTv.text = (mLogTv.text.toString() + result + "\n")
        GlobalScope.runOnMainDelay(100){
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationUpdateEvent(event: LocationUpdateEvent) {
        if (event.isLocationData) {
            val time = DateUtils.getCurrentFormatString(DateUtils.TYPE_2)
            mUpdateTimeTv.text = getString(R.string.location_update_time, time)
            mLongitudeTv.text = getString(R.string.location_longitude, event.longitude)
            mLatitudeTv.text = getString(R.string.location_latitude, event.latitude)
            mMccTv.text = getString(R.string.location_mcc, event.mcc)
            mMncTv.text = getString(R.string.location_mnc, event.mnc)
            mLacTv.text = getString(R.string.location_lac, event.lac)
            mCidTv.text = getString(R.string.location_cid, event.cid)
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