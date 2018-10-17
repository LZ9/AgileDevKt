package com.lodz.android.agiledevkt.modules.location

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.button.MaterialButton
import android.widget.ScrollView
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.splash.CheckDialog
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.utils.UiHandler
import com.lodz.android.corekt.utils.isPermissionGranted
import com.lodz.android.corekt.utils.toastShort
import permissions.dispatcher.*

/**
 * 定位测试
 * Created by zhouL on 2018/9/28.
 */
@RuntimePermissions
class LocationTestActivity : BaseActivity() {

    companion object {
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

    /** 绑定定位服务 */
    private val mBindServiceBtn by bindView<MaterialButton>(R.id.bind_service_btn)
    /** 解绑定位服务 */
    private val mUnbindServiceBtn by bindView<MaterialButton>(R.id.unbind_service_btn)

    override fun getLayoutId() = R.layout.activity_location_test

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        onRequestPermissionWithPermissionCheck()
    }

    override fun initData() {
        super.initData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0以上的手机对权限进行动态申请
            onRequestPermissionWithPermissionCheck()//申请权限
        } else {
            initLogic()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    /** 权限申请成功 */
    @NeedsPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,// 定位
            Manifest.permission.ACCESS_COARSE_LOCATION// 定位
    )
    fun onRequestPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            return
        }
        if (!isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            return
        }
        initLogic()
    }

    /** 用户拒绝后再次申请前告知用户为什么需要该权限 */
    @OnShowRationale(
            Manifest.permission.ACCESS_FINE_LOCATION,// 定位
            Manifest.permission.ACCESS_COARSE_LOCATION// 定位
    )
    fun onShowRationaleBeforeRequest(request: PermissionRequest) {
        request.proceed()//请求权限
    }

    /** 被拒绝 */
    @OnPermissionDenied(
            Manifest.permission.ACCESS_FINE_LOCATION,// 定位
            Manifest.permission.ACCESS_COARSE_LOCATION// 定位
    )
    fun onDenied() {
        toastShort(R.string.location_denied_permission_tips)
        showStatusError()
    }

    /** 被拒绝并且勾选了不再提醒 */
    @OnNeverAskAgain(
            Manifest.permission.ACCESS_FINE_LOCATION,// 定位
            Manifest.permission.ACCESS_COARSE_LOCATION// 定位
    )
    fun onNeverAskAgain() {
        toastShort(R.string.location_check_permission_tips)
        showPermissionCheckDialog()
        goAppDetailSetting()
    }

    /** 显示权限核对弹框 */
    private fun showPermissionCheckDialog() {
        val checkDialog = CheckDialog(getContext())
        checkDialog.setContentMsg(R.string.location_check_permission_title)
        checkDialog.setPositiveText(R.string.splash_check_permission_confirm, DialogInterface.OnClickListener { dialog, which ->
            onRequestPermissionWithPermissionCheck()
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
        mBindServiceBtn.setOnClickListener {

        }

        mUnbindServiceBtn.setOnClickListener {

        }
    }

    /** 初始化 */
    fun initLogic() {
        showStatusCompleted()
    }

    /** 打印信息[result] */
    private fun printResult(result: String) {
        mLogTv.text = (mLogTv.text.toString() + "\n" + result)
        UiHandler.postDelayed(Runnable {
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }, 100)
    }
}