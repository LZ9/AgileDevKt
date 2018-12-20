package com.lodz.android.componentkt.photopicker.picker

import com.lodz.android.componentkt.photopicker.contract.OnPhotoLoader
import com.lodz.android.componentkt.photopicker.contract.picker.OnPhotoPickerListener

/**
 * 图片数据
 * Created by zhouL on 2018/12/18.
 */
internal class PickerBean {

    /** 资源列表 */
    var sourceList: List<String>? = null
    /** 图片加载接口 */
    var photoLoader: OnPhotoLoader<String>? = null
    /** 预览图加载接口 */
    var previewLoader: OnPhotoLoader<String>? = null
    /** 照片回调接口 */
    var photoPickerListener: OnPhotoPickerListener? = null
    /** 可选最大数量 */
    var maxCount = 9
    /** 是否需要相机功能 */
    var isNeedCamera = false
    /** 是否需要item的预览功能 */
    var isNeedItemPreview = true
    /** 拍照保存地址 */
    var cameraSavePath = ""
    /** UI配置 */
    var pickerUIConfig = PickerUIConfig.createDefault()
    /** 7.0的FileProvider名字 */
    var authority = ""
    /** 是否挑选手机的全部图片 */
    var isPickAllPhoto = true
    /** 是否立即返回结果 */
    var isImmediately = true

    fun clear() {
        sourceList = null
        photoLoader = null
        previewLoader = null
        photoPickerListener = null
    }
}