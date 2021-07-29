package com.lodz.android.pandora.photopicker.take

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.view.View
import com.lodz.android.corekt.album.AlbumUtils
import com.lodz.android.corekt.anko.*
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.corekt.utils.FileUtils
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.pandora.R
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.databinding.PandoraActivityTakePhotoBinding
import com.lodz.android.pandora.utils.viewbinding.bindingLayoutLazy
import kotlinx.coroutines.GlobalScope

/**
 * 拍照页面
 * Created by zhouL on 2018/12/20.
 */
internal class TakePhotoActivity : AbsActivity() {

    companion object {
        private var sTakeBean: TakeBean? = null

        internal fun start(context: Context, takeBean: TakeBean, flags: List<Int>?) {
            synchronized(this) {
                if (sTakeBean != null) {
                    return
                }
                sTakeBean = takeBean
                val intent = Intent(context, TakePhotoActivity::class.java)
                flags?.forEach {
                    intent.addFlags(it)
                }
                context.startActivity(intent)
            }
        }
    }

    /** 照相请求码 */
    private val REQUEST_CAMERA = 777

    private val mBinding: PandoraActivityTakePhotoBinding by bindingLayoutLazy(PandoraActivityTakePhotoBinding::inflate)

    /** 拍照数据 */
    private var mPdrTakeBean: TakeBean? = null
    /** 临时文件路径 */
    private var mPdrTempFilePath = ""

    override fun startCreate() {
        super.startCreate()
        mPdrTakeBean = sTakeBean
    }

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun onPressBack(): Boolean {
        handleCameraCancel()
        return true
    }

    override fun setListeners() {
        super.setListeners()

        // 取消按钮
        mBinding.pdrCancelBtn.setOnClickListener {
            handleCameraCancel()
        }

        // 确定按钮
        mBinding.pdrConfirmBtn.setOnClickListener {
            handleConfirm()
        }
    }

    override fun initData() {
        super.initData()
        val bean = mPdrTakeBean ?: return
        mBinding.pdrRootLayout.setBackgroundColor(getColorCompat(bean.previewBgColor))
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
        mPdrTempFilePath = "${bean.cameraSavePath}P_${DateUtils.getCurrentFormatString(DateUtils.TYPE_4)}.jpg"
        if (!FileUtils.createNewFile(mPdrTempFilePath)) {
            toastShort(R.string.pandora_photo_temp_file_fail)
            return
        }
        if (Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(packageManager) == null) {
            toastShort(R.string.pandora_no_camera)
            return
        }
        if (!takePhoto(mPdrTempFilePath, bean.authority, REQUEST_CAMERA)) {
            toastShort(R.string.pandora_photo_temp_file_fail)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != REQUEST_CAMERA) {//不是拍照请求码
            return
        }
        val bean = mPdrTakeBean
        if (bean == null) {//数据为空
            handleCameraCancel()
            return
        }
        if (resultCode != Activity.RESULT_OK) {//拍照不成功
            handleCameraCancel()
            return
        }
        // 拍照成功
        AlbumUtils.notifyScanImageCompat(getContext(), mPdrTempFilePath)// 更新相册
        if (bean.isImmediately) {
            handleConfirm()
            return
        }
        GlobalScope.runOnMainDelay(300) {
            handleCameraSuccess()
        }
    }

    /** 处理确认照片 */
    private fun handleConfirm() {
        mPdrTakeBean?.photoTakeListener?.onTake(mPdrTempFilePath)
        finish()
    }

    /** 处理拍照取消 */
    private fun handleCameraCancel() {
        if (mPdrTempFilePath.isNotEmpty()) {
            FileUtils.delFile(mPdrTempFilePath)// 删除临时文件
        }
        mPdrTakeBean?.photoTakeListener?.onTake("")
        finish()
    }

    /** 处理拍照成功 */
    private fun handleCameraSuccess() {
        mPdrTakeBean?.imgLoader?.displayImg(getContext(), mPdrTempFilePath, mBinding.pdrPhotoImg)
    }

    override fun finish() {
        mPdrTakeBean?.clear()
        mPdrTakeBean = null
        sTakeBean?.clear()
        sTakeBean = null
        mPdrTempFilePath = ""
        super.finish()
    }

}