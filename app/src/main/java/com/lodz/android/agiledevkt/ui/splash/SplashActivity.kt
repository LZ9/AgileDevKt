package com.lodz.android.agiledevkt.ui.splash

import android.Manifest
import android.os.Bundle
import com.lodz.android.agiledevkt.R
import com.lodz.android.componentkt.base.activity.AbsActivity
import permissions.dispatcher.*

/**
 * 启动页
 * Created by zhouL on 2018/6/20.
 */
@RuntimePermissions
class SplashActivity : AbsActivity() {

    override fun getAbsLayoutId() = R.layout.activity_splash

    override fun findViews(savedInstanceState: Bundle?) {
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


    }

    /** 用户拒绝后再次申请前告知用户为什么需要该权限 */
    @OnShowRationale(
            Manifest.permission.READ_PHONE_STATE,// 手机状态
            Manifest.permission.WRITE_EXTERNAL_STORAGE// 存储卡读写
    )
    fun onShowRationaleBeforeRequest(request: PermissionRequest) {


    }

    /** 被拒绝 */
    @OnPermissionDenied(
            Manifest.permission.READ_PHONE_STATE,// 手机状态
            Manifest.permission.WRITE_EXTERNAL_STORAGE// 存储卡读写
    )
    fun onDenied() {


    }

    /** 被拒绝并且勾选了不再提醒 */
    @OnNeverAskAgain(
            Manifest.permission.READ_PHONE_STATE,// 手机状态
            Manifest.permission.WRITE_EXTERNAL_STORAGE// 存储卡读写
    )
    fun onNeverAskAgain() {


    }
}