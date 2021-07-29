package com.lodz.android.agiledevkt.modules.splash

import android.Manifest
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.App
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivitySplashBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.*
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayoutLazy
import kotlinx.coroutines.GlobalScope
import permissions.dispatcher.*
import permissions.dispatcher.ktx.constructPermissionsRequest

/**
 * 启动页
 * Created by zhouL on 2018/6/20.
 */
class SplashActivity : AbsActivity() {

    private val mBinding: ActivitySplashBinding by bindingLayoutLazy(ActivitySplashBinding::inflate)

    private val hasReadPhoneStatePermissions = constructPermissionsRequest(
        Manifest.permission.READ_PHONE_STATE,// 手机状态
        onShowRationale = ::onShowRationaleBeforeRequest,
        onPermissionDenied = ::onDenied,
        onNeverAskAgain = ::onNeverAsk,
        requiresPermission = ::onRequestPermission
    )

    private val hasWriteExternalStoragePermissions = constructPermissionsRequest(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,// 存储卡读写
        onShowRationale = ::onShowRationaleBeforeRequest,
        onPermissionDenied = ::onDenied,
        onNeverAskAgain = ::onNeverAsk,
        requiresPermission = ::onRequestPermission
    )

    private val hasReadExternalStoragePermissions = constructPermissionsRequest(
        Manifest.permission.READ_EXTERNAL_STORAGE,// 存储卡读写
        onShowRationale = ::onShowRationaleBeforeRequest,
        onPermissionDenied = ::onDenied,
        onNeverAskAgain = ::onNeverAsk,
        requiresPermission = ::onRequestPermission
    )

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        StatusBarUtil.setColor(window, getColorCompat(R.color.color_00a0e9))
    }

    override fun initData() {
        super.initData()
        if (!isTopAndBottomActivityTheSame()){
            // 非常规启动则关闭当前页面
            finish()
            return
        }
        GlobalScope.runOnMainDelay(1000) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0以上的手机对权限进行动态申请
                onRequestPermission()
            } else {
                init()
            }
        }
    }

    /** 权限申请成功 */
    private fun onRequestPermission() {
        if (!isPermissionGranted(Manifest.permission.READ_PHONE_STATE)){
            hasReadPhoneStatePermissions.launch()
            return
        }
        if (!isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            hasWriteExternalStoragePermissions.launch()
            return
        }
        if (!isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)){
            hasReadExternalStoragePermissions.launch()
            return
        }
        init()
    }

    /** 用户拒绝后再次申请前告知用户为什么需要该权限 */
    private fun onShowRationaleBeforeRequest(request: PermissionRequest) {
        request.proceed()//请求权限
    }

    /** 被拒绝 */
    private fun onDenied() {
        onRequestPermission()//申请权限
    }

    /** 被拒绝并且勾选了不再提醒 */
    private fun onNeverAsk() {
        toastShort(R.string.splash_check_permission_tips)
        showPermissionCheckDialog()
        goAppDetailSetting()
    }

    /** 显示权限核对弹框 */
    private fun showPermissionCheckDialog(){
        val checkDialog = CheckDialog(getContext())
        checkDialog.setContentMsg(R.string.splash_check_permission_title)
        checkDialog.setPositiveText(R.string.splash_check_permission_confirm, DialogInterface.OnClickListener { dialog, which ->
            onRequestPermission()
            dialog.dismiss()
        })
        checkDialog.setNegativeText(R.string.splash_check_permission_unconfirmed, DialogInterface.OnClickListener { dialog, which ->
            goAppDetailSetting()
        })
        checkDialog.setCanceledOnTouchOutside(false)
        checkDialog.setOnCancelListener {
            toastShort(R.string.splash_check_permission_cancel)
            App.get().exit()
        }
        checkDialog.show()
    }

    /** 初始化 */
    private fun init() {
        goMianActivity()
    }

    private fun goMianActivity() {
        MainActivity.start(getContext())
        finish()
    }

}