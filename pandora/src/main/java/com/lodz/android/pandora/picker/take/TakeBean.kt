package com.lodz.android.pandora.picker.take

import android.graphics.Color
import android.os.Environment
import androidx.annotation.ColorInt
import androidx.documentfile.provider.DocumentFile
import com.lodz.android.pandora.picker.contract.OnImgLoader
import com.lodz.android.pandora.picker.contract.take.OnPhotoTakeListener

/**
 * 拍照数据
 * Created by zhouL on 2018/12/25.
 */
internal class TakeBean {

    /** 预览图加载接口 */
    var imgLoader: OnImgLoader<DocumentFile>? = null
    /** 照片回调接口 */
    var photoTakeListener: OnPhotoTakeListener? = null
    /** 公共目录名称 */
    var publicDirectoryName: String = Environment.DIRECTORY_PICTURES
    /** 7.0的FileProvider名字 */
    var authority = ""
    /** 是否立即返回结果 */
    var isImmediately = true
    /** 顶部状态栏颜色 */
    @ColorInt
    var statusBarColor = Color.BLACK
    /** 底部导航栏颜色 */
    @ColorInt
    var navigationBarColor = Color.BLACK
    /** 预览页背景色 */
    @ColorInt
    var previewBgColor = Color.BLACK

    fun clear() {
        imgLoader = null
        photoTakeListener = null
    }
}