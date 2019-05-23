package com.lodz.android.pandora.photopicker.take

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.view.ViewGroup
import android.widget.ImageView
import com.lodz.android.corekt.album.AlbumUtils
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.takePhoto
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.corekt.utils.FileUtils
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.corekt.utils.UiHandler
import com.lodz.android.pandora.R
import com.lodz.android.pandora.base.activity.AbsActivity

/**
 * 拍照页面
 * Created by zhouL on 2018/12/20.
 */
internal class TakePhotoActivity : AbsActivity() {

    companion object {
        private var sTakeBean: TakeBean? = null

        internal fun start(context: Context, takeBean: TakeBean) {
            synchronized(this) {
                if (sTakeBean != null) {
                    return
                }
                sTakeBean = takeBean
                val intent = Intent(context, TakePhotoActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    /** 照相请求码 */
    private val REQUEST_CAMERA = 777


    /** 根布局 */
    private val mRootLayout by bindView<ViewGroup>(R.id.root_layout)
    /** 照片图片 */
    private val mPhotoImg by bindView<ImageView>(R.id.photo_img)
    /** 取消按钮 */
    private val mCancelBtn by bindView<ImageView>(R.id.cancel_btn)
    /** 确定按钮 */
    private val mConfirmBtn by bindView<ImageView>(R.id.confirm_btn)

    /** 拍照数据 */
    private var mTakeBean: TakeBean? = null
    /** 临时文件路径 */
    private var mTempFilePath = ""

    override fun startCreate() {
        super.startCreate()
        mTakeBean = sTakeBean
    }

    override fun getAbsLayoutId(): Int = R.layout.pandora_activity_take_photo

    override fun onPressBack(): Boolean {
        handleCameraCancel()
        return true
    }

    override fun setListeners() {
        super.setListeners()

        // 取消按钮
        mCancelBtn.setOnClickListener {
            handleCameraCancel()
        }

        // 确定按钮
        mConfirmBtn.setOnClickListener {
            handleConfirm()
        }
    }

    override fun initData() {
        super.initData()
        val bean = mTakeBean
        if (bean == null) {
            return
        }
        mRootLayout.setBackgroundColor(getColorCompat(bean.previewBgColor))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//设置状态栏和导航栏颜色
            StatusBarUtil.setColor(window, getColorCompat(bean.statusBarColor))
            StatusBarUtil.setNavigationBarColor(window, getColorCompat(bean.navigationBarColor))
        }
        takeCameraPhoto(bean)
    }

    /** 拍照 */
    private fun takeCameraPhoto(bean: TakeBean) {
        if (!FileUtils.isFileExists(bean.cameraSavePath) && !FileUtils.createFolder(bean.cameraSavePath)) {// 文件夹不存在且创建文件夹失败
            toastShort(R.string.pandora_photo_folder_fail)
            return
        }
        mTempFilePath = "${bean.cameraSavePath}P_${DateUtils.getCurrentFormatString(DateUtils.TYPE_4)}.jpg"
        if (!FileUtils.createNewFile(mTempFilePath)) {
            toastShort(R.string.pandora_photo_temp_file_fail)
            return
        }
        if (Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(packageManager) == null) {
            toastShort(R.string.pandora_no_camera)
            return
        }
        if (!takePhoto(mTempFilePath, bean.authority, REQUEST_CAMERA)) {
            toastShort(R.string.pandora_photo_temp_file_fail)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != REQUEST_CAMERA) {//不是拍照请求码
            return
        }
        val bean = mTakeBean
        if (bean == null) {//数据为空
            handleCameraCancel()
            return
        }
        if (resultCode != Activity.RESULT_OK) {//拍照不成功
            handleCameraCancel()
            return
        }
        // 拍照成功
        AlbumUtils.notifyScanImage(getContext(), mTempFilePath)// 更新相册
        if (bean.isImmediately) {
            handleConfirm()
            return
        }
        UiHandler.postDelayed(300){
            handleCameraSuccess()
        }
    }

    /** 处理确认照片 */
    private fun handleConfirm() {
        mTakeBean?.photoTakeListener?.onTake(mTempFilePath)
        finish()
    }

    /** 处理拍照取消 */
    private fun handleCameraCancel() {
        if (mTempFilePath.isNotEmpty()) {
            FileUtils.delFile(mTempFilePath)// 删除临时文件
        }
        finish()
    }

    /** 处理拍照成功 */
    private fun handleCameraSuccess() {
        mTakeBean?.imgLoader?.displayImg(getContext(), mTempFilePath, mPhotoImg)
    }

    override fun finish() {
        mTakeBean?.clear()
        mTakeBean = null
        sTakeBean?.clear()
        sTakeBean = null
        mTempFilePath = ""
        super.finish()
    }

}