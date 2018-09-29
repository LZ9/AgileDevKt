package com.lodz.android.agiledevkt.modules.location

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.splash.CheckDialog
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.utils.isPermissionGranted
import com.lodz.android.corekt.utils.toastShort
import permissions.dispatcher.*

/**
 * 定位测试
 * Created by zhouL on 2018/9/28.
 */
@RuntimePermissions
class LocationTestActivity :BaseActivity(){
    
    companion object {
        fun start(context: Context){
            val intent = Intent(context, LocationTestActivity::class.java)
            context.startActivity(intent)
        }
    }

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
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
            return
        }
        if (!isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)){
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
    private fun showPermissionCheckDialog(){
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

    /** 初始化 */
    fun initLogic() {
        showStatusCompleted()
    }
}