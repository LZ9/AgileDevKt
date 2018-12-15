package com.lodz.android.componentkt.photopicker.contract

import android.content.Context
import com.lodz.android.componentkt.photopicker.contract.preview.PreviewController

/**
 * 点击事件
 * Created by zhouL on 2018/12/13.
 */
interface OnClickListener<T> {

    /** 点击，上下文[context]，数据[source]，位置[position]，控制器[controller] */
    fun onClick(context: Context, source: T, position: Int, controller: PreviewController)
}