package com.lodz.android.agiledevkt.modules.pic.take

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityTakePhotoTestBinding
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.corekt.album.PicInfo
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.anko.isPermissionGranted
import com.lodz.android.corekt.anko.runOnMainDelay
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.FileUtils.filePathToUri
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.photopicker.take.TakePhotoManager
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import kotlinx.coroutines.MainScope
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

    private val hasPermissions by lazy {
        constructPermissionsRequest(
            Manifest.permission.CAMERA,// 相机
            onShowRationale = ::onShowRationaleBeforeRequest,
            onPermissionDenied = ::onDenied,
            onNeverAskAgain = ::onNeverAskAgain,
            requiresPermission = ::onRequestPermission
        )
    }

    private val mBinding: ActivityTakePhotoTestBinding by bindingLayout(ActivityTakePhotoTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

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
        // 立即返回路径按钮
        mBinding.immediatelyBtn.setOnClickListener {
            mBinding.resultTv.text = ""
            TakePhotoManager.create()
                .setImmediately(true)
                .setCameraSavePath(FileManager.getCacheFolderPath())
                .setAuthority("com.lodz.android.agiledevkt.fileprovider")
                .setOnPhotoTakeListener { photo ->
                    runOnMainDelay(100) {//拍照后如果马上获取URI需要延迟一下，等待手机数据库更新一下
                        if (photo.isNotEmpty()) {
                            val picInfo = PicInfo(photo, filePathToUri(getContext(), photo))
                            mBinding.resultTv.text = "photo : $photo \n uri : ${picInfo.uri}"
                            ImageLoader.create(getContext())
                                .loadFilePath(photo)
                                .setCenterInside()
                                .into(mBinding.pathImg)

                            ImageLoader.create(getContext())
                                .loadUri(picInfo.uri)
                                .setCenterInside()
                                .into(mBinding.uriImg)
                        }
                    }
                }
                .build()
                .take(getContext())
        }

        // 拍照后确认按钮
        mBinding.confirmPhotoBtn.setOnClickListener {
            mBinding.resultTv.text = ""
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
                        mBinding.resultTv.text = photo
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