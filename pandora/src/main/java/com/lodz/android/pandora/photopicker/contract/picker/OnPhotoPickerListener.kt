package com.lodz.android.pandora.photopicker.contract.picker

import com.lodz.android.corekt.album.PicInfo

/**
 * 图片选择监听器
 * Created by zhouL on 2018/12/13.
 */
fun interface OnPhotoPickerListener {

    /** 照片选中回调，照片列表[photos] */
    fun onPickerSelected(photos: List<PicInfo>)
}