package com.lodz.android.pandora.photopicker.contract.picker

/**
 * 图片选择监听器
 * Created by zhouL on 2018/12/13.
 */
interface OnPhotoPickerListener {

    /** 照片选中回调，照片列表[photos] */
    fun onPickerSelected(photos: List<String>)
}