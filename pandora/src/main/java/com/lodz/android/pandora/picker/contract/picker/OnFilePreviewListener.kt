package com.lodz.android.pandora.picker.contract.picker

import android.content.Context
import com.lodz.android.pandora.picker.file.PickerUIConfig

/**
 * 文件预览监听器
 * Created by zhouL on 2018/12/13.
 */
fun interface OnFilePreviewListener<T : Any> {
    /** 文件预览回调，上下文[context]，预览文件[files]，UI配置[uiConfig] */
    fun onPickerSelected(context: Context, files: List<T>, uiConfig: PickerUIConfig)
}