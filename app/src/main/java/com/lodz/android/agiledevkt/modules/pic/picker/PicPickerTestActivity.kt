package com.lodz.android.agiledevkt.modules.pic.picker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.corekt.album.PicInfo
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.anko.isPermissionGranted
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.photopicker.contract.preview.PreviewController
import com.lodz.android.pandora.photopicker.picker.PickerManager
import com.lodz.android.pandora.photopicker.picker.PickerUIConfig
import com.lodz.android.pandora.photopicker.preview.AbsImageView
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

    /** 图片列表 */
    private val IMG_URLS = arrayOf(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135406740&di=ad56c6b92e5d9888a04f0b724e5219d0&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F3801213fb80e7beca9004ec5252eb9389b506b38.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135426954&di=45834427b6f8ec30f1d7e1d99f59ee5c&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F0b7b02087bf40ad1cd0f99c55d2c11dfa9ecce29.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135506833&di=6b22dd2085f18b3643fe62b0f8b8955f&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F242dd42a2834349b8d289fafcbea15ce36d3beea.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135457903&di=e107c45dd449126ae54f0f665c558d05&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Df49943efb8119313d34ef7f30d5166a2%2Fb17eca8065380cd736f92fc0ab44ad345982813c.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135473894&di=27b040e674c4f9ac8b499f38612cab39&imgtype=0&src=http%3A%2F%2Fb.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fd788d43f8794a4c2fc3e95eb07f41bd5ac6e39d4.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135496262&di=5cef907ceff298c8d5d6c79841a72696&imgtype=0&src=http%3A%2F%2Fimg4q.duitang.com%2Fuploads%2Fitem%2F201409%2F07%2F20140907224542_h4HvW.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135447478&di=90ddcac4604965af5d9bc744237a27aa&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fd52a2834349b033b1c4bcdcf1fce36d3d439bde7.jpg")

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
    private val mScaleSwitch by bindView<SwitchMaterial>(R.id.scale_switch)
    /** 显示拍照 */
    private val mShowCameraSwitch by bindView<SwitchMaterial>(R.id.show_camera_switch)
    /** 单张预览 */
    private val mItemPreviewSwitch by bindView<SwitchMaterial>(R.id.item_preview_switch)
    /** 点击关闭预览 */
    private val mClickClosePreviewSwitch by bindView<SwitchMaterial>(R.id.click_close_preview_switch)
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
            PickerManager.create<ImageView>()
                    .setMaxCount(mMaxCount)
                    .setNeedCamera(mShowCameraSwitch.isChecked)
                    .setNeedItemPreview(mItemPreviewSwitch.isChecked)
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
                    .setImageView(object : AbsImageView<ImageView, PicInfo>(mScaleSwitch.isChecked) {
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
                                if (mClickClosePreviewSwitch.isChecked) {
                                    controller.close()
                                }
                            }
                        }

                        override fun onViewDetached(view: ImageView, isScale: Boolean) {
                            super.onViewDetached(view, isScale)
                            if (isScale && view is PhotoView){
                                view.attacher.update()
                            }
                        }
                    })
                    .setOnPhotoPickerListener { photos ->
                        var str = ""
                        for (path in photos) {
                            str += "$path\n\n"
                        }
                        mResultTv.text = str
                    }
                    .build()
                    .open(getContext())
        }

        // 挑选指定图片
        PickCustomBtn.setOnClickListener {
            PickerManager.create<ImageView>()
                    .setMaxCount(mMaxCount)
                    .setNeedItemPreview(mItemPreviewSwitch.isChecked)
                    .setPickerUIConfig(mConfig)
                    .setImgLoader { context, source, imageView ->
                        ImageLoader.create(context).loadUrl(source.path).setCenterCrop().into(imageView)
                    }
                    .setImageView(object : AbsImageView<ImageView, PicInfo>(mScaleSwitch.isChecked) {
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
                                if (mClickClosePreviewSwitch.isChecked) {
                                    controller.close()
                                }
                            }
                        }

                        override fun onViewDetached(view: ImageView, isScale: Boolean) {
                            super.onViewDetached(view, isScale)
                            if (isScale && view is PhotoView){
                                view.attacher.update()
                            }
                        }
                    })
                    .setOnPhotoPickerListener { photos ->
                        var str = ""
                        for (path in photos) {
                            str += "$path\n\n"
                        }
                        mResultTv.text = str
                    }
                    .build(IMG_URLS)
                    .open(getContext())
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