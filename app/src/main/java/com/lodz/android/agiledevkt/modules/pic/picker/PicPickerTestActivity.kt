package com.lodz.android.agiledevkt.modules.pic.picker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.lodz.android.agiledevkt.BuildConfig
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.config.Constant
import com.lodz.android.agiledevkt.databinding.ActivityPicPickerBinding
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.anko.installApk
import com.lodz.android.corekt.anko.isPermissionGranted
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.file.DocumentWrapper
import com.lodz.android.corekt.file.getFileSuffix
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.corekt.media.getAllFiles
import com.lodz.android.corekt.media.isApkSuffix
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.picker.file.PickerManager
import com.lodz.android.pandora.picker.file.PickerUIConfig
import com.lodz.android.pandora.picker.preview.vh.ImagePreviewAgent
import com.lodz.android.pandora.utils.coroutines.CoroutinesWrapper
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import permissions.dispatcher.*
import permissions.dispatcher.ktx.constructPermissionsRequest

/**
 * 文件选择测试类
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

    private val hasPermissions by lazy {
        constructPermissionsRequest(
            Manifest.permission.CAMERA,// 相机
            onShowRationale = ::onShowRationaleBeforeRequest,
            onPermissionDenied = ::onDenied,
            onNeverAskAgain = ::onNeverAskAgain,
            requiresPermission = ::onRequestPermission
        )
    }

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

        // 挑选指定资源图片
        mBinding.pickerCustomResBtn.setOnClickListener {
            PickerManager.pickAny(getResIdList())
                .setMaxCount(mMaxCount)
                .setNeedPreview(mBinding.itemPreviewSwitch.isChecked)
                .setPickerUIConfig(mConfig)
                .setImgLoader { context, source, imageView ->
                    ImageLoader.create(context)
                        .loadResId(source)
                        .setCenterCrop()
                        .into(imageView)
                }
                .setPreviewView(object :ImagePreviewAgent<Int>(){
                    override fun displayImag(context: Context, source: Int, imageView: ImageView, position: Int) {
                        ImageLoader.create(context)
                            .loadResId(source)
                            .setPlaceholder(R.drawable.pandora_ic_img)
                            .setError(R.drawable.pandora_ic_img)
                            .setFitCenter()
                            .into(imageView)
                    }
                })
                .setOnFilePickerListener { list ->
                    var str = ""
                    for (item in list) {
                        str += "${item}\n\n"
                    }
                    mBinding.resultTv.text = str
                }
                .open(getContext())
        }

        // 挑选指定网络图片
        mBinding.pickerCustomUrlBtn.setOnClickListener {
            PickerManager.pickAny(Constant.IMG_URLS)
                .setMaxCount(mMaxCount)
                .setNeedPreview(mBinding.itemPreviewSwitch.isChecked)
                .setPickerUIConfig(mConfig)
                .setImgLoader { context, source, imageView ->
                    ImageLoader.create(context)
                        .loadUrl(source)
                        .setCenterCrop()
                        .into(imageView)
                }
                .setPreviewView(object :ImagePreviewAgent<String>(){
                    override fun displayImag(context: Context, source: String, imageView: ImageView, position: Int) {
                        ImageLoader.create(context)
                            .loadUrl(source)
                            .setPlaceholder(R.drawable.pandora_ic_img)
                            .setError(R.drawable.pandora_ic_img)
                            .setFitCenter()
                            .into(imageView)
                    }
                })
                .setOnFilePickerListener { list ->
                    var str = ""
                    for (item in list) {
                        str += "${item}\n\n"
                    }
                    mBinding.resultTv.text = str
                }
                .open(getContext())
        }

        // 挑选指定文件
        mBinding.pickerCustomFileBtn.setOnClickListener {
            CoroutinesWrapper.create(this)
                .request { getCustomList() }
                .actionPg(getContext()) {
                    onSuccess {
                        PickerManager.pickAny(it)
                            .setMaxCount(mMaxCount)
                            .setNeedPreview(false)
                            .setPickerUIConfig(mConfig)
                            .setImgLoader { context, source, imageView ->
                                if (source.type == CustomPickBean.URL_IMG_TYPE){
                                    ImageLoader.create(context)
                                        .loadUrl(source.url)
                                        .setPlaceholder(R.drawable.pandora_ic_img)
                                        .setError(R.drawable.pandora_ic_img)
                                        .setCenterCrop()
                                        .into(imageView)
                                    return@setImgLoader
                                }
                                val documentFile = source.document?.documentFile ?: return@setImgLoader
                                ImageLoader.create(context)
                                    .loadUri(documentFile.uri)
                                    .setPlaceholder(R.drawable.pandora_ic_file)
                                    .setError(R.drawable.pandora_ic_file)
                                    .setCenterInside()
                                    .into(imageView)
                            }
                            .setOnFilePickerListener { list ->
                                var str = ""
                                for (item in list) {
                                    str += "${item.name.ifEmpty { item.url }}\n\n"
                                }
                                mBinding.resultTv.text = str
                            }
                            .open(getContext())
                    }
                }
        }

        // 挑选手机相册
        mBinding.pickerPhoneAlbumBtn.setOnClickListener {
            PickerManager.pickPhoneAlbum()
                .setMaxCount(mMaxCount)
                .setNeedPreview(mBinding.itemPreviewSwitch.isChecked)
                .setNeedBottomInfo(false)
                .setPickerUIConfig(mConfig)
                .setNeedCamera(mBinding.showCameraSwitch.isChecked, Environment.DIRECTORY_DCIM)
                .setAuthority(BuildConfig.FILE_AUTHORITY)
                .setImgLoader { context, source, imageView ->
                    ImageLoader.create(context)
                        .loadUri(source.documentFile.uri)
                        .setPlaceholder(R.drawable.pandora_ic_img)
                        .setError(R.drawable.pandora_ic_img)
                        .setCenterCrop()
                        .into(imageView)
                }
                .setPreviewView(object :ImagePreviewAgent<DocumentWrapper>(){
                    override fun displayImag(context: Context, source: DocumentWrapper, imageView: ImageView, position: Int) {
                        ImageLoader.create(context)
                            .loadUri(source.documentFile.uri)
                            .setPlaceholder(R.drawable.pandora_ic_img)
                            .setError(R.drawable.pandora_ic_img)
                            .setFitCenter()
                            .into(imageView)
                    }
                })
                .setOnLifecycleObserver(mLifecycleObserver)
                .setOnFilePickerListener{
                    var str = ""
                    for (dw in it) {
                        str += "${dw.documentFile.name}\n\n"
                    }
                    mBinding.resultTv.text = str
                }
                .open(getContext())
        }

        // 挑选手机相册、音频和视频
        mBinding.pickerPhoneMediaBtn.setOnClickListener {
            mBinding.itemPreviewSwitch.isChecked = false
            PickerManager.pickPhoneAssemble(PickerManager.PICK_PHONE_ALBUM, PickerManager.PICK_PHONE_VIDEO, PickerManager.PICK_PHONE_AUDIO)
                .setMaxCount(mMaxCount)
                .setNeedPreview(mBinding.itemPreviewSwitch.isChecked)
                .setPickerUIConfig(mConfig)
                .setNeedCamera(mBinding.showCameraSwitch.isChecked, Environment.DIRECTORY_DCIM)
                .setAuthority(BuildConfig.FILE_AUTHORITY)
                .setImgLoader { context, source, imageView ->
                    ImageLoader.create(context)
                        .loadUri(source.documentFile.uri)
                        .setPlaceholder(R.drawable.pandora_ic_file)
                        .setError(R.drawable.pandora_ic_audio)
                        .setCenterCrop()
                        .into(imageView)
                }
                .setOnFilePickerListener{
                    var str = ""
                    for (dw in it) {
                        str += "${dw.documentFile.name}\n\n"
                    }
                    mBinding.resultTv.text = str
                }
                .open(getContext())
        }

        // 挑选手机里的Word/Excel/PPT/PDF/APK
        mBinding.pickerPhoneOfficeBtn.setOnClickListener {
            mBinding.showCameraSwitch.isChecked = false
            mBinding.itemPreviewSwitch.isChecked = false
            PickerManager.pickPhoneBySuffix(".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".pdf", ".apk")
                .setMaxCount(mMaxCount)
                .setNeedPreview(mBinding.itemPreviewSwitch.isChecked)
                .setPickerUIConfig(mConfig)
                .setNeedCamera(mBinding.showCameraSwitch.isChecked)
                .setAuthority(BuildConfig.FILE_AUTHORITY)
                .setImgLoader { context, source, imageView ->
                    val iconId = if (source.iconId == 0) R.drawable.pandora_ic_file else source.iconId
                    ImageLoader.create(context)
                        .loadResId(iconId)
                        .setCenterInside()
                        .into(imageView)
                }
                .setOnFileClickListener { context, source ->
                    if (source.fileName.getFileSuffix().isApkSuffix()){
                        installApk(source.file.absolutePath, BuildConfig.FILE_AUTHORITY)
                        return@setOnFileClickListener
                    }
                    openFileByOther(context, source)
                }
                .setOnFilePickerListener{
                    var str = ""
                    for (dw in it) {
                        str += "${dw.documentFile.name}\n\n"
                    }
                    mBinding.resultTv.text = str
                }
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

    private fun getResIdList(): List<Int> {
        val list = ArrayList<Int>()
        list.add(R.drawable.ic_brand)
        list.add(R.drawable.ic_logo)
        list.add(R.drawable.bg_old_trafford)
        list.add(R.drawable.bg_pokemon)
        list.add(R.drawable.bg_splash)
        list.add(R.drawable.ic_gif)
        list.add(R.drawable.ic_webp)
        list.add(R.drawable.ic_regret)
        return list
    }

    /** 获取自定义数据体 */
    private fun getCustomList(): List<CustomPickBean> {
        val list = ArrayList<CustomPickBean>()
        val files = getAllFiles(".txt")
        files.forEachIndexed { index, documentWrapper ->
            list.add(CustomPickBean("", Constant.IMG_URLS[index % Constant.IMG_URLS.size], CustomPickBean.URL_IMG_TYPE, null))
            list.add(CustomPickBean(documentWrapper.fileName, "", CustomPickBean.DOCUMENT_TYPE, documentWrapper))
        }
        return list
    }

    /** 用第三方APP打开文件 */
    private fun openFileByOther(context: Context, source: DocumentWrapper) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setDataAndType(source.documentFile.uri, source.documentFile.type)
        context.startActivity(intent)
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
            .setItemBgColor(R.color.black)
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

    private val mLifecycleObserver = object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            PrintLog.e("testtag", "DefaultLifecycleObserver onDestroy")
        }

        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            PrintLog.e("testtag", "DefaultLifecycleObserver onCreate")
        }

        override fun onPause(owner: LifecycleOwner) {
            super.onPause(owner)
            PrintLog.v("testtag", "DefaultLifecycleObserver onPause")
        }

        override fun onResume(owner: LifecycleOwner) {
            super.onResume(owner)
            PrintLog.v("testtag", "DefaultLifecycleObserver onResume")
        }

        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            PrintLog.d("testtag", "DefaultLifecycleObserver onStart")
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            PrintLog.d("testtag", "DefaultLifecycleObserver onStop")
        }
    }
}