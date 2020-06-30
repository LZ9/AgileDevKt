package com.lodz.android.pandora.photopicker.picker

import android.view.View
import com.lodz.android.corekt.album.PicInfo
import com.lodz.android.pandora.photopicker.contract.OnImgLoader
import com.lodz.android.pandora.photopicker.contract.picker.OnPhotoPickerListener
import com.lodz.android.pandora.photopicker.preview.AbsImageView

/**
 * 图片数据
 * Created by zhouL on 2018/12/18.
 */
internal class PickerBean<V : View> {

    /** 资源列表 */
    var sourceList: List<PicInfo>? = null
    /** 图片加载接口 */
    var imgLoader: OnImgLoader<PicInfo>? = null
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
    /** 图片控件 */
    var imgView: AbsImageView<V, PicInfo>? = null

    fun clear() {
        sourceList = null
        imgLoader = null
        imgView = null
        photoPickerListener = null
    }
}