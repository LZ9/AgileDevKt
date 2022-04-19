package com.lodz.android.agiledevkt.modules.pic.take

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.documentfile.provider.DocumentFile
import com.lodz.android.agiledevkt.BuildConfig
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityTakePhotoTestBinding
import com.lodz.android.corekt.anko.*
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.picker.take.TakePhotoManager
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
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
            reset()
            TakePhotoManager.create()
                .setImmediately(true)
                .setPublicDirectoryName(Environment.DIRECTORY_DCIM)
                .setAuthority(BuildConfig.FILE_AUTHORITY)
                .setOnPhotoTakeListener { file ->
                    if (file == null){
                        mBinding.resultTv.text = "取消拍照"
                        return@setOnPhotoTakeListener
                    }
                    mBinding.resultTv.text = getLog(file)

                    mBinding.imgLayout.visibility = View.VISIBLE
                    ImageLoader.create(getContext())
                        .loadUri(file.uri)
                        .setCenterInside()
                        .into(mBinding.uriImg)

                    ImageLoader.create(getContext())
                        .loadBase64(file.uri.toBase64(getContext()))
                        .setCenterInside()
                        .into(mBinding.base64Img)
                }
                .build()
                .take(getContext())
        }

        // 拍照后确认按钮
        mBinding.confirmPhotoBtn.setOnClickListener {
            reset()
            TakePhotoManager.create()
                .setStatusBarColor(getContext(), R.color.black)
                .setNavigationBarColor(getContext(), R.color.black)
                .setPreviewBgColor(getContext(), R.color.black)
                .setImmediately(false)
                .setAuthority(BuildConfig.FILE_AUTHORITY)
                .setOnImgLoader { context, source, imageView ->
                    ImageLoader.create(context).loadUri(source.uri).setFitCenter().into(imageView)
                }
                .setOnPhotoTakeListener { file ->
                    if (file == null){
                        mBinding.resultTv.text = "取消拍照"
                        return@setOnPhotoTakeListener
                    }
                    mBinding.resultTv.text = getLog(file)

                    mBinding.imgLayout.visibility = View.VISIBLE
                    ImageLoader.create(getContext())
                        .loadUri(file.uri)
                        .setCenterInside()
                        .into(mBinding.uriImg)

                    ImageLoader.create(getContext())
                        .loadBase64(file.uri.toBase64(getContext()))
                        .setCenterInside()
                        .into(mBinding.base64Img)
                }
                .build()
                .take(getContext())
        }
    }

    /** 生成日志 */
    private fun getLog(file: DocumentFile): String =
        "name : ".append(file.name).append("\n")
            .append("uri : ").append(file.uri).append("\n")
            .append("MimeType : ").append(file.type)

    private fun reset(){
        mBinding.resultTv.text = ""
        mBinding.imgLayout.visibility = View.GONE
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
        reset()
        showStatusCompleted()
    }
}