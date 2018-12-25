package com.lodz.android.componentkt.photopicker.take

import androidx.annotation.ColorRes
import com.lodz.android.componentkt.photopicker.contract.OnPhotoLoader
import com.lodz.android.componentkt.photopicker.contract.picker.OnPhotoPickerListener

/**
 * 拍照数据
 * Created by zhouL on 2018/12/25.
 */
internal class TakeBean {

    /** 预览图加载接口 */
    var previewLoader: OnPhotoLoader<String>? = null
    /** 照片回调接口 */
    var photoPickerListener: OnPhotoPickerListener? = null
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
        previewLoader = null
        photoPickerListener = null
    }
}