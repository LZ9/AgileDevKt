package com.lodz.android.componentkt.photopicker.take

import android.content.Context
import android.os.Build
import android.os.Environment
import com.lodz.android.componentkt.R
import com.lodz.android.corekt.utils.toastShort
import java.io.File

/**
 * 拍照管理类
 * Created by zhouL on 2018/12/25.
 */
class TakePhotoManager internal constructor(private val takeBean: TakeBean) {

    companion object {
        fun create(): TakeBuilder = TakeBuilder()
    }


    /** 拍照，上下文[context]，是否立即返回结果[isImmediately]（默认true） */
    fun take(context: Context) {
        if (takeBean.previewLoader == null && !takeBean.isImmediately) {//没有立即返回需要校验预览图片加载器
            context.toastShort(R.string.componentkt_preview_loader_unset)
            return
        }
        if (takeBean.photoPickerListener == null) {// 校验图片选中回调监听
            context.toastShort(R.string.componentkt_photo_selected_listener_unset)
            return
        }
        if (takeBean.cameraSavePath.isEmpty()) {// 校验拍照保存地址
            takeBean.cameraSavePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
        }
        if (!takeBean.cameraSavePath.endsWith(File.separator)) {//补全地址
            takeBean.cameraSavePath += File.separator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && takeBean.authority.isEmpty()) {//当前系统是7.0以上且没有配置FileProvider
            context.toastShort(R.string.componentkt_photo_authority_empty)
            return
        }
        TakePhotoActivity.start(context, takeBean)
    }

}