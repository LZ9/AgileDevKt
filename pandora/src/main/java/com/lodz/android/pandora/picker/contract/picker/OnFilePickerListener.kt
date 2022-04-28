package com.lodz.android.pandora.picker.contract.picker

/**
 * 文件选择监听器
 * Created by zhouL on 2018/12/13.
 */
fun interface OnFilePickerListener<T : Any> {

    /** 照片选中回调，照片列表[photos] */
    fun onPickerSelected(files: List<T>)
}