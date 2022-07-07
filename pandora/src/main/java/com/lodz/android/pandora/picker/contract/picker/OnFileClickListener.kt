package com.lodz.android.pandora.picker.contract.picker

import android.content.Context

/**
 * 文件点击监听器
 * Created by zhouL on 2018/12/13.
 */
fun interface OnFileClickListener<T> {
    /** 文件点击回调，上下文[context]，文件[source] */
    fun onItemClick(context: Context, source: T)
}