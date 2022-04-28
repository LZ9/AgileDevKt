package com.lodz.android.pandora.picker.take

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
            context.toastShort(R.string.pandora_picker_take_listener_unset)
            return
        }
        val path = Environment.getExternalStoragePublicDirectory(takeBean.publicDirectoryName)?.absolutePath ?: ""
        if (path.isEmpty()) {// 校验公共目录路径
            context.toastShort(R.string.pandora_picker_public_directory_empty)
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && takeBean.authority.isEmpty()) {//当前系统是7.0以上且没有配置FileProvider
            context.toastShort(R.string.pandora_picker_authority_empty)
            return
        }
        TakePhotoActivity.start(context, takeBean, flags)
    }

}