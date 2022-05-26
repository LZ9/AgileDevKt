package com.lodz.android.pandora.picker.take

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import com.lodz.android.corekt.anko.*
import com.lodz.android.corekt.media.insertImageForPair
import com.lodz.android.corekt.media.notifyScanImage
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.pandora.R
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.databinding.PandoraActivityTakePhotoBinding
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import java.io.File

/**
 * 拍照页面
 * Created by zhouL on 2018/12/20.
 */
internal class TakePhotoActivity : AbsActivity() {

    companion object {
        private var sTakeBean: TakeBean? = null

        internal fun start(context: Context, takeBean: TakeBean, flags: List<Int>?) {
            synchronized(TakeBean::class.java) {
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

    private val mBinding: PandoraActivityTakePhotoBinding by bindingLayout(PandoraActivityTakePhotoBinding::inflate)

    /** 拍照数据 */
    private val mPdrTakeBean by lazy { sTakeBean }

    /** 图片的地址 */
    private var mPair: Pair<Uri, File>? = null

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
        val bean = mPdrTakeBean
        if (bean == null){
            finish()
            return
        }
        //设置颜色
        mBinding.pdrRootLayout.setBackgroundColor(bean.previewBgColor)
        StatusBarUtil.setColor(window, bean.statusBarColor)
        StatusBarUtil.setNavigationBarColor(window, bean.navigationBarColor)
        takeCameraPhoto(bean)
    }

    /** 拍照 */
    private fun takeCameraPhoto(bean: TakeBean) {
        mPair = insertImageForPair(
            "P_${DateUtils.getCurrentFormatString(DateUtils.TYPE_4)}.jpg",
            bean.authority,
            bean.publicDirectoryName
        )
        if (mPair?.first == null){//校验Uri路径是否获取成功
            toastShort(R.string.pandora_picker_uri_null)
            finish()
            return
        }
        mTakePictureResult.launch(mPair?.first)
    }

    /** 拍照回调 */
    val mTakePictureResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        val uri = mPair?.first
        if (!it || uri == null) {
            handleCameraCancel()
            return@registerForActivityResult
        }
        val bean = mPdrTakeBean
        if (bean == null) {//数据为空
            handleCameraCancel()
            return@registerForActivityResult
        }
        mPair?.second?.notifyScanImage(getContext())
        if (bean.isImmediately) {//立即返回
            handleConfirm()
            return@registerForActivityResult
        }
        showPhoto(DocumentFile.fromSingleUri(getContext(), uri))
    }

    /** 处理确认照片 */
    private fun handleConfirm() {
        val uri = mPair?.first
        val file = if (uri != null) DocumentFile.fromSingleUri(getContext(), uri) else null
        mPdrTakeBean?.photoTakeListener?.onTake(file)
        finish()
    }

    /** 处理拍照取消 */
    private fun handleCameraCancel() {
        val uri = mPair?.first
        if (uri != null) {
            contentResolver.delete(uri, null, null)
        }
        mPdrTakeBean?.photoTakeListener?.onTake(null)
        finish()
    }

    /** 显示照片 */
    private fun showPhoto(file: DocumentFile?) {
        if (file != null){
            mPdrTakeBean?.imgLoader?.displayImg(getContext(), file, mBinding.pdrPhotoImg)
        }
    }

    override fun finish() {
        mPdrTakeBean?.clear()
        sTakeBean?.clear()
        sTakeBean = null
        super.finish()
    }
}