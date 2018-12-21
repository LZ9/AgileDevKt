package com.lodz.android.agiledevkt.modules.pic.picker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.componentkt.photopicker.contract.OnPhotoLoader
import com.lodz.android.componentkt.photopicker.contract.picker.OnPhotoPickerListener
import com.lodz.android.componentkt.photopicker.picker.PickerManager
import com.lodz.android.componentkt.photopicker.picker.PickerUIConfig
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.utils.isPermissionGranted
import com.lodz.android.corekt.utils.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import permissions.dispatcher.*

/**
 * 图片选择测试类
 * Created by zhouL on 2018/12/21.
 */
@RuntimePermissions
class PicPickerTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PicPickerTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 挑选手机相册 */
    private val PickPhoneBtn by bindView<MaterialButton>(R.id.pick_phone_btn)
    /** 挑选指定图片 */
    private val PickCustomBtn by bindView<MaterialButton>(R.id.pick_custom_btn)
    /** 加数量 */
    private val mPlusBtn by bindView<TextView>(R.id.plus_btn)
    /** 减数量 */
    private val mMinusBtn by bindView<TextView>(R.id.minus_btn)
    /** 数量 */
    private val mMaxTv by bindView<TextView>(R.id.max_tv)
    /** 预览缩放图片 */
    private val mScaleSwitch by bindView<Switch>(R.id.scale_switch)
    /** 显示拍照 */
    private val mShowCameraSwitch by bindView<Switch>(R.id.show_camera_switch)
    /** 单张预览 */
    private val mItemPreviewSwitch by bindView<Switch>(R.id.item_preview_switch)
    /** 点击关闭预览 */
    private val mClickClosePreviewSwitch by bindView<Switch>(R.id.click_close_preview_switch)
    /** 单选布局 */
    private val mRadioGroup by bindView<RadioGroup>(R.id.radio_group)
    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)

    /** 可选最大数 */
    private var mMaxCount = 6

    private var mConfig = PickerUIConfig.createDefault()

    override fun getLayoutId(): Int = R.layout.activity_pic_picker

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
        onRequestPermissionWithPermissionCheck()//申请权限
    }

    override fun setListeners() {
        super.setListeners()

        // 挑选手机相册
        PickPhoneBtn.setOnClickListener {
            PickerManager.create()
                    .setMaxCount(mMaxCount)
                    .setScale(mScaleSwitch.isChecked)
                    .setNeedCamera(mShowCameraSwitch.isChecked)
                    .setNeedItemPreview(mItemPreviewSwitch.isChecked)
                    .setClickClosePreview(mClickClosePreviewSwitch.isChecked)
                    .setPickerUIConfig(mConfig)
                    .setCameraSavePath(FileManager.getCacheFolderPath())
                    .setAuthority("com.lodz.android.agiledevkt.fileprovider")
                    .setImgLoader(object : OnPhotoLoader<String> {
                        override fun displayImg(context: Context, source: String, imageView: ImageView) {
                            ImageLoader.create(context).loadFilePath(source).setCenterCrop().into(imageView)
                        }
                    })
                    .setPreviewImgLoader(object : OnPhotoLoader<String> {
                        override fun displayImg(context: Context, source: String, imageView: ImageView) {
                            ImageLoader.create(context).loadFilePath(source).setFitCenter().into(imageView)
                        }
                    })
                    .setOnPhotoPickerListener(object : OnPhotoPickerListener {
                        override fun onPickerSelected(photos: List<String>) {
                            var str = ""
                            for (path in photos) {
                                str += "$path\n\n"
                            }
                            mResultTv.text = str
                        }
                    })
                    .build()
                    .open(getContext())
        }

        // 挑选指定图片
        PickCustomBtn.setOnClickListener {

        }

        // 加数量
        mPlusBtn.setOnClickListener {
            mMaxCount++
            mMaxTv.text = mMaxCount.toString()
        }

        // 减数量
        mMinusBtn.setOnClickListener {
            mMaxCount--
            if (mMaxCount < 1) {
                mMaxCount = 1
            }
            mMaxTv.text = mMaxCount.toString()
        }

        mRadioGroup.setOnCheckedChangeListener { group, checkedId ->
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
                .setNavigationBarColor(R.color.color_00a0e9)
                .setCameraImg(R.drawable.ic_wallet)
                .setCameraBgColor(R.color.white)
                .setItemBgColor(R.color.white)
                .setSelectedBtnUnselect(R.color.color_b3e5fc)
                .setSelectedBtnSelected(R.color.color_00a0e9)
                .setMaskColor(R.color.color_11000000)
                .setBackBtnColor(R.color.white)
                .setMainTextColor(R.color.white)
                .setMoreFolderImg(R.drawable.ic_update)
                .setTopLayoutColor(R.color.color_00a0e9)
                .setBottomLayoutColor(R.color.color_00a0e9)
                .setPreviewBtnNormal(R.color.white)
                .setPreviewBtnPressed(R.color.color_9a9a9a)
                .setPreviewBtnUnable(R.color.color_9a9a9a)
                .setConfirmBtnNormal(R.color.white)
                .setConfirmBtnPressed(R.color.color_b3e5fc)
                .setConfirmBtnUnable(R.color.color_b3e5fc)
                .setConfirmTextNormal(R.color.color_9a9a9a)
                .setConfirmTextPressed(R.color.color_cccccc)
                .setConfirmTextUnable(R.color.color_cccccc)
                .setFolderSelectColor(R.color.color_00a0e9)
    }

    /** 设置风格二 */
    private fun setTwoStyle() {

    }

    /** 设置风格三 */
    private fun setThreeStyle() {

    }

    override fun initData() {
        super.initData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0以上的手机对权限进行动态申请
            onRequestPermissionWithPermissionCheck()//申请权限
        } else {
            init()
        }
    }

    /** 权限申请成功 */
    @NeedsPermission(Manifest.permission.CAMERA)// 相机
    fun onRequestPermission() {
        if (!isPermissionGranted(Manifest.permission.CAMERA)) {
            return
        }
        init()
    }

    /** 用户拒绝后再次申请前告知用户为什么需要该权限 */
    @OnShowRationale(Manifest.permission.CAMERA)// 相机
    fun onShowRationaleBeforeRequest(request: PermissionRequest) {
        request.proceed()//请求权限
    }

    /** 被拒绝 */
    @OnPermissionDenied(Manifest.permission.CAMERA)// 相机
    fun onDenied() {
        onRequestPermissionWithPermissionCheck()//申请权限
    }

    /** 被拒绝并且勾选了不再提醒 */
    @OnNeverAskAgain(Manifest.permission.CAMERA)// 相机
    fun onNeverAskAgain() {
        toastShort(R.string.splash_check_permission_tips)
        goAppDetailSetting()
        showStatusError()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun init() {
        mMaxTv.text = mMaxCount.toString()
        showStatusCompleted()
    }
}