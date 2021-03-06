package com.lodz.android.agiledevkt.modules.pic.take

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.anko.isPermissionGranted
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.photopicker.take.TakePhotoManager
import permissions.dispatcher.*
import permissions.dispatcher.ktx.constructPermissionsRequest

/**
 * 拍照测试类
 * Created by zhouL on 2018/12/25.
 */
class TakePhotoTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, TakePhotoTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 立即返回路径按钮 */
    private val mImmediatelyBtn by bindView<MaterialButton>(R.id.immediately_btn)
    /** 拍照后确认按钮 */
    private val mConfirmPhotoBtn by bindView<MaterialButton>(R.id.confirm_photo_btn)
    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)

    private val hasPermissions = constructPermissionsRequest(
        Manifest.permission.CAMERA,// 相机
        onShowRationale = ::onShowRationaleBeforeRequest,
        onPermissionDenied = ::onDenied,
        onNeverAskAgain = ::onNeverAskAgain,
        requiresPermission = ::onRequestPermission
    )

    override fun getLayoutId(): Int = R.layout.activity_take_photo_test

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.pic_take_photo)
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
        mImmediatelyBtn.setOnClickListener {
            mResultTv.text = ""
            TakePhotoManager.create()
                    .setImmediately(true)
                    .setCameraSavePath(FileManager.getCacheFolderPath())
                    .setAuthority("com.lodz.android.agiledevkt.fileprovider")
                    .setOnPhotoTakeListener { photo ->
                        if (photo.isNotEmpty()) {
                            mResultTv.text = photo
                        }
                    }
                    .build()
                    .take(getContext())
        }

        mConfirmPhotoBtn.setOnClickListener {
            mResultTv.text = ""
            TakePhotoManager.create()
                    .setStatusBarColor(R.color.black)
                    .setNavigationBarColor(R.color.black)
                    .setPreviewBgColor(R.color.black)
                    .setImmediately(false)
                    .setCameraSavePath(FileManager.getCacheFolderPath())
                    .setAuthority("com.lodz.android.agiledevkt.fileprovider")
                    .setOnImgLoader { context, source, imageView ->
                        ImageLoader.create(context).loadFilePath(source).setFitCenter().into(imageView)
                    }
                    .setOnPhotoTakeListener { photo ->
                        if (photo.isNotEmpty()) {
                            mResultTv.text = photo
                        }
                    }
                    .build()
                    .take(getContext())
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

    /** 权限申请成功 */
    private fun onRequestPermission() {
        if (!isPermissionGranted(Manifest.permission.CAMERA)) {
            hasPermissions.launch()
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

    private fun init() {
        showStatusCompleted()
    }
}