package com.lodz.android.pandora.picker.contract.take

/**
 * 拍照回调
 * Created by zhouL on 2019/1/11.
 */
fun interface OnPhotoTakeListener {

    /** 拍照图片路径[photo]回调 */
    fun onTake(photo: String)
}