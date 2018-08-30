package com.lodz.android.agiledevkt.modules.splash

import android.Manifest
import android.os.Build
import android.os.Bundle
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.componentkt.base.activity.AbsActivity
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.corekt.utils.UiHandler
import com.lodz.android.corekt.utils.isPermissionGranted
import com.lodz.android.corekt.utils.toastShort
import permissions.dispatcher.*

/**
 * 启动页
 * Created by zhouL on 2018/6/20.
 */
@RuntimePermissions
class SplashActivity : AbsActivity() {

    override fun getAbsLayoutId() = R.layout.activity_splash

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        StatusBarUtil.setTransparent(this)
    }

    override fun initData() {
        super.initData()
        UiHandler.postDelayed(Runnable {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0以上的手机对权限进行动态申请
                onRequestPermissionWithPermissionCheck();//申请权限
            } else {
                init()
            }
        }, 1000)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    /** 权限申请成功 */
    @NeedsPermission(
            Manifest.permission.READ_PHONE_STATE,// 手机状态
            Manifest.permission.WRITE_EXTERNAL_STORAGE// 存储卡读写
    )
    fun onRequestPermission() {
        if (!isPermissionGranted(Manifest.permission.READ_PHONE_STATE)){
            return
        }
        if (!isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            return
        }
        init()
    }

    /** 用户拒绝后再次申请前告知用户为什么需要该权限 */
    @OnShowRationale(
            Manifest.permission.READ_PHONE_STATE,// 手机状态
            Manifest.permission.WRITE_EXTERNAL_STORAGE// 存储卡读写
    )
    fun onShowRationaleBeforeRequest(request: PermissionRequest) {
        request.proceed()//请求权限
    }

    /** 被拒绝 */
    @OnPermissionDenied(
            Manifest.permission.READ_PHONE_STATE,// 手机状态
            Manifest.permission.WRITE_EXTERNAL_STORAGE// 存储卡读写
    )
    fun onDenied() {
        onRequestPermissionWithPermissionCheck()//申请权限
    }

    /** 被拒绝并且勾选了不再提醒 */
    @OnNeverAskAgain(
            Manifest.permission.READ_PHONE_STATE,// 手机状态
            Manifest.permission.WRITE_EXTERNAL_STORAGE// 存储卡读写
    )
    fun onNeverAskAgain() {
        toastShort(R.string.splash_check_permission_tips)
        // todo checkdialog
        goAppDetailSetting()
    }

    /** 初始化 */
    fun init() {
        // todo 各种初始化
        FileManager.init()
        goMianActivity()
    }

    private fun goMianActivity() {
        MainActivity.start(getContext())
        finish()
    }
}