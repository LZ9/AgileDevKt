package com.lodz.android.pandora.picker.take

import androidx.annotation.ColorRes
import com.lodz.android.pandora.picker.contract.OnImgLoader
import com.lodz.android.pandora.picker.contract.take.OnPhotoTakeListener

/**
 * 拍照数据
 * Created by zhouL on 2018/12/25.
 */
internal class TakeBean {

    /** 预览图加载接口 */
    var imgLoader: OnImgLoader<String>? = null
    /** 照片回调接口 */
    var photoTakeListener: OnPhotoTakeListener? = null
    /** 拍照保存地址 */
    var cameraSavePath = ""
    /** 7.0的FileProvider名字 */
    var authority = ""
    /** 是否立即返回结果 */
    var isImmediately = true
    /** 顶部状态栏颜色 */
    @ColorRes
    var statusBarColor = android.R.color.black
    /** 底部导航栏颜色 */
    @ColorRes
    var navigationBarColor = android.R.color.black
    /** 预览页背景色 */
    @ColorRes
    var previewBgColor = android.R.color.black

    fun clear() {
        imgLoader = null
        photoTakeListener = null
    }
}