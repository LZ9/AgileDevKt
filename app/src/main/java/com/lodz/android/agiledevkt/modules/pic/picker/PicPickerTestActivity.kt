package com.lodz.android.agiledevkt.modules.pic.picker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.chrisbanes.photoview.PhotoView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.config.Constant
import com.lodz.android.agiledevkt.databinding.ActivityPicPickerBinding
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.corekt.album.PicInfo
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.anko.isPermissionGranted
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.photopicker.contract.preview.PreviewController
import com.lodz.android.pandora.photopicker.picker.PickerManager
import com.lodz.android.pandora.photopicker.picker.PickerUIConfig
import com.lodz.android.pandora.photopicker.preview.AbsImageView
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import permissions.dispatcher.*
import permissions.dispatcher.ktx.constructPermissionsRequest

/**
 * 图片选择测试类
 * Created by zhouL on 2018/12/21.
 */
class PicPickerTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PicPickerTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityPicPickerBinding by bindingLayout(ActivityPicPickerBinding::inflate)

    /** 可选最大数 */
    private var mMaxCount = 6

    private var mConfig = PickerUIConfig.createDefault()

    private val hasPermissions = constructPermissionsRequest(
        Manifest.permission.CAMERA,// 相机
        onShowRationale = ::onShowRationaleBeforeRequest,
        onPermissionDenied = ::onDenied,
        onNeverAskAgain = ::onNeverAskAgain,
        requiresPermission = ::onRequestPermission
    )

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.pic_picker)
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

        // 挑选手机相册
        mBinding.pickPhoneBtn.setOnClickListener {
            PickerManager.create<ImageView>()
                .setMaxCount(mMaxCount)
                .setNeedCamera(mBinding.showCameraSwitch.isChecked)
                .setNeedItemPreview(mBinding.itemPreviewSwitch.isChecked)
                .setPickerUIConfig(mConfig)
                .setCameraSavePath(FileManager.getCacheFolderPath())
                .setAuthority("com.lodz.android.agiledevkt.fileprovider")
                .setImgLoader { context, source, imageView ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ImageLoader.create(context).loadUri(source.uri).setCenterCrop().into(imageView)
                    } else {
                        ImageLoader.create(context).loadFilePath(source.path).setCenterCrop().into(imageView)
                    }
                }
                .setImageView(object : AbsImageView<ImageView, PicInfo>(mBinding.scaleSwitch.isChecked) {
                    override fun onCreateView(context: Context, isScale: Boolean): ImageView {
                        val img = if (isScale) PhotoView(context) else ImageView(context)
                        img.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        return img
                    }

                    override fun onDisplayImg(context: Context, source: PicInfo, view: ImageView) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            ImageLoader.create(context).loadUri(source.uri).setFitCenter().into(view)
                        } else {
                            ImageLoader.create(context).loadFilePath(source.path).setFitCenter().into(view)
                        }
                    }
                    override fun onClickImpl(viewHolder: RecyclerView.ViewHolder, view: ImageView, item: PicInfo, position: Int, controller: PreviewController) {
                        super.onClickImpl(viewHolder, view, item, position, controller)
                        view.setOnClickListener {
                            if (mBinding.clickClosePreviewSwitch.isChecked) {
                                controller.close()
                            }
                        }
                    }

                    override fun onViewDetached(view: ImageView, isScale: Boolean) {
                        super.onViewDetached(view, isScale)
                        if (isScale && view is PhotoView) {
                            view.attacher.update()
                        }
                    }
                })
                .setOnPhotoPickerListener { photos ->
                    var str = ""
                    for (path in photos) {
                        str += "$path\n\n"
                    }
                    mBinding.resultTv.text = str
                }
                .build()
                .open(getContext())
        }

        // 挑选指定图片
        mBinding.pickCustomBtn.setOnClickListener {
            PickerManager.create<ImageView>()
                .setMaxCount(mMaxCount)
                .setNeedItemPreview(mBinding.itemPreviewSwitch.isChecked)
                .setPickerUIConfig(mConfig)
                .setImgLoader { context, source, imageView ->
                    ImageLoader.create(context).loadUrl(source.path).setCenterCrop().into(imageView)
                }
                .setImageView(object : AbsImageView<ImageView, PicInfo>(mBinding.scaleSwitch.isChecked) {
                    override fun onCreateView(context: Context, isScale: Boolean): ImageView {
                        val img = if (isScale) PhotoView(context) else ImageView(context)
                        img.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        return img
                    }

                    override fun onDisplayImg(context: Context, source: PicInfo, view: ImageView) {
                        ImageLoader.create(context).loadUrl(source.path).setFitCenter().into(view)
                    }

                    override fun onClickImpl(viewHolder: RecyclerView.ViewHolder, view: ImageView, item: PicInfo, position: Int, controller: PreviewController) {
                        super.onClickImpl(viewHolder, view, item, position, controller)
                        view.setOnClickListener {
                            if (mBinding.clickClosePreviewSwitch.isChecked) {
                                controller.close()
                            }
                        }
                    }

                    override fun onViewDetached(view: ImageView, isScale: Boolean) {
                        super.onViewDetached(view, isScale)
                        if (isScale && view is PhotoView) {
                            view.attacher.update()
                        }
                    }
                })
                .setOnPhotoPickerListener { photos ->
                    var str = ""
                    for (path in photos) {
                        str += "$path\n\n"
                    }
                    mBinding.resultTv.text = str
                }
                .build(Constant.IMG_URLS)
                .open(getContext())
        }

        // 加数量
        mBinding.plusBtn.setOnClickListener {
            mMaxCount++
            mBinding.maxTv.text = mMaxCount.toString()
        }

        // 减数量
        mBinding.minusBtn.setOnClickListener {
            mMaxCount--
            if (mMaxCount < 1) {
                mMaxCount = 1
            }
            mBinding.maxTv.text = mMaxCount.toString()
        }

        // 单选布局
        mBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.style_default_rb -> setDefaultStyle()
                R.id.style_one_rb -> setOneStyle()
                R.id.style_two_rb -> setTwoStyle()
                R.id.style_three_rb -> setThreeStyle()
            }
        }
    }

    /** 设置默认风格 */
    private fun setDefaultStyle() {
        mConfig = PickerUIConfig.createDefault()
    }

    /** 设置风格一 */
    private fun setOneStyle() {
        mConfig = PickerUIConfig.createDefault()
            .setStatusBarColor(R.color.color_00a0e9)
            .setNavigationBarColor(R.color.black)
            .setCameraImg(R.drawable.ic_wallet)
            .setCameraBgColor(R.color.white)
            .setItemBgColor(R.color.white)
            .setSelectedBtnUnselect(R.color.color_b3e5fc)
            .setSelectedBtnSelected(R.color.color_00a0e9)
            .setMaskColor(R.color.color_aa9a9a9a)
            .setBackBtnColor(R.color.white)
            .setMainTextColor(R.color.white)
            .setMoreFolderImg(R.drawable.ic_update)
            .setTopLayoutColor(R.color.color_00a0e9)
            .setBottomLayoutColor(R.color.color_00a0e9)
            .setPreviewBtnNormal(R.color.white)
            .setPreviewBtnPressed(R.color.color_b3e5fc)
            .setPreviewBtnUnable(R.color.color_cccccc)
            .setConfirmBtnNormal(R.color.white)
            .setConfirmBtnPressed(R.color.white)
            .setConfirmBtnUnable(R.color.white)
            .setConfirmTextNormal(R.color.color_00a0e9)
            .setConfirmTextPressed(R.color.color_b3e5fc)
            .setConfirmTextUnable(R.color.color_cccccc)
            .setFolderSelectColor(R.color.color_00a0e9)
            .setPreviewBgColor(R.color.white)
    }

    /** 设置风格二 */
    private fun setTwoStyle() {
        mConfig = PickerUIConfig.createDefault()
            .setStatusBarColor(R.color.black)
            .setNavigationBarColor(R.color.black)
//                .setCameraImg(R.drawable.ic_update)
            .setCameraBgColor(R.color.black)
            .setItemBgColor(R.color.black)
            .setSelectedBtnUnselect(R.color.white)
            .setSelectedBtnSelected(R.color.white)
//                .setMaskColor(R.color.color_aa9a9a9a)
            .setBackBtnColor(R.color.white)
            .setMainTextColor(R.color.white)
//                .setMoreFolderImg(R.drawable.ic_update)
            .setTopLayoutColor(R.color.black)
            .setBottomLayoutColor(R.color.black)
            .setPreviewBtnNormal(R.color.white)
            .setPreviewBtnPressed(R.color.color_999999)
            .setPreviewBtnUnable(R.color.color_999999)
            .setConfirmBtnNormal(R.color.white)
            .setConfirmBtnPressed(R.color.white)
            .setConfirmBtnUnable(R.color.white)
            .setConfirmTextNormal(R.color.black)
            .setConfirmTextPressed(R.color.color_cccccc)
            .setConfirmTextUnable(R.color.color_999999)
            .setFolderSelectColor(R.color.color_00a0e9)
            .setPreviewBgColor(R.color.black)
    }

    /** 设置风格三 */
    private fun setThreeStyle() {
        mConfig = PickerUIConfig.createDefault()
            .setStatusBarColor(R.color.color_d43450)
            .setNavigationBarColor(R.color.black)
            .setCameraImg(R.drawable.ic_wallet)
            .setCameraBgColor(R.color.white)
            .setItemBgColor(R.color.white)
            .setSelectedBtnUnselect(R.color.color_d43450)
            .setSelectedBtnSelected(R.color.color_d43450)
//                .setMaskColor(R.color.color_aa9a9a9a)
            .setBackBtnColor(R.color.white)
            .setMainTextColor(R.color.white)
            .setMoreFolderImg(R.drawable.ic_update)
            .setTopLayoutColor(R.color.color_d43450)
            .setBottomLayoutColor(R.color.color_d43450)
            .setPreviewBtnNormal(R.color.white)
            .setPreviewBtnPressed(R.color.color_cccccc)
            .setPreviewBtnUnable(R.color.color_cccccc)
            .setConfirmBtnNormal(R.color.white)
            .setConfirmBtnPressed(R.color.white)
            .setConfirmBtnUnable(R.color.white)
            .setConfirmTextNormal(R.color.color_d43450)
            .setConfirmTextPressed(R.color.color_cccccc)
            .setConfirmTextUnable(R.color.color_cccccc)
            .setFolderSelectColor(R.color.color_d43450)
            .setPreviewBgColor(R.color.white)
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
        mBinding.maxTv.text = mMaxCount.toString()
        showStatusCompleted()
    }
}