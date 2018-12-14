package com.lodz.android.componentkt.photopicker.contract.picker

import java.io.Serializable

/**
 * 照片选择监听器
 * Created by zhouL on 2018/12/13.
 */
interface OnPhotoPickerListener : Serializable {

    /** 照片选中回调，照片列表[photos] */
    fun onPickerSelected(photos: List<String>)
}