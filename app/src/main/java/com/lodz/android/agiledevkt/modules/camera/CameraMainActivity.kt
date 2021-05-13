package com.lodz.android.agiledevkt.modules.camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.event.TakePhotoEvent
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.anko.isPermissionGranted
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import  permissions.dispatcher.*
import permissions.dispatcher.ktx.constructPermissionsRequest

/**
 * 相机测试类
 * @author zhouL
 * @date 2021/1/13
 */
class CameraMainActivity : BaseActivity() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, CameraMainActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 拍照 */
    private val mTakeBtn by bindView<Button>(R.id.take_btn)
    /** 录像 */
    private val mVideoBtn by bindView<Button>(R.id.video_btn)
    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)

    private val hasCameraPermissions = constructPermissionsRequest(
        Manifest.permission.CAMERA,// 相机
        onShowRationale = ::onShowRationaleBeforeRequest,
        onPermissionDenied = ::onDenied,
        onNeverAskAgain = ::onNeverAskAgain,
        requiresPermission = ::onRequestPermission
    )

    private val hasRecordAudioPermissions = constructPermissionsRequest(
        Manifest.permission.RECORD_AUDIO,//录音
        onShowRationale = ::onShowRationaleBeforeRequest,
        onPermissionDenied = ::onDenied,
        onNeverAskAgain = ::onNeverAskAgain,
        requiresPermission = ::onRequestPermission
    )

    override fun getLayoutId(): Int = R.layout.activity_camera_main

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        onRequestPermission()//申请权限
    }

    override fun setListeners() {
        super.setListeners()
        mTakeBtn.setOnClickListener {
            CameraTakeActivity.start(getContext())
        }

        mVideoBtn.setOnClickListener {


        }
    }

    override fun initData() {
        super.initData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0以上的手机对权限进行动态申请
            onRequestPermission()//申请权限
        } else {
            init()
        }
    }

    private fun init() {
        showStatusCompleted()
    }

    /** 权限申请成功 */
    private fun onRequestPermission() {
        if (!isPermissionGranted(Manifest.permission.CAMERA)) {
            hasCameraPermissions.launch()
            return
        }
        if (!isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
            hasRecordAudioPermissions.launch()
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
    private fun onNeverAskAgain() {
        toastShort(R.string.splash_check_permission_tips)
        goAppDetailSetting()
        showStatusError()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTakePhotoEvent(event: TakePhotoEvent) {
        mResultTv.text = event.path
    }
}