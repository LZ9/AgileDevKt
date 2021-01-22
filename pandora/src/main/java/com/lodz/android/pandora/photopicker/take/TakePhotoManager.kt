package com.lodz.android.pandora.photopicker.take

import android.content.Context
import android.os.Build
import android.os.Environment
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.R
import java.io.File

/**
 * 拍照管理类
 * Created by zhouL on 2018/12/25.
 */
class TakePhotoManager internal constructor(private val takeBean: TakeBean) {

    companion object {
        @JvmStatic
        fun create(): TakeBuilder = TakeBuilder()
    }

    /** 拍照，上下文[context]，Intent启动标记[flags] */
    @JvmOverloads
    fun take(context: Context, flags: List<Int>? = null) {
        if (takeBean.imgLoader == null && !takeBean.isImmediately) {//没有立即返回需要校验预览图片加载器
            context.toastShort(R.string.pandora_preview_loader_unset)
            return
        }
        if (takeBean.photoTakeListener == null) {// 校验图片选中回调监听
            context.toastShort(R.string.pandora_photo_take_listener_unset)
            return
        }
        if (takeBean.cameraSavePath.isEmpty()) {// 校验拍照保存地址
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                takeBean.cameraSavePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath ?: ""
            }
            if (takeBean.cameraSavePath.isEmpty()){
                takeBean.cameraSavePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
            }
        }
        if (!takeBean.cameraSavePath.endsWith(File.separator)) {//补全地址
            takeBean.cameraSavePath += File.separator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && takeBean.authority.isEmpty()) {//当前系统是7.0以上且没有配置FileProvider
            context.toastShort(R.string.pandora_photo_authority_empty)
            return
        }
        TakePhotoActivity.start(context, takeBean, flags)
    }

}