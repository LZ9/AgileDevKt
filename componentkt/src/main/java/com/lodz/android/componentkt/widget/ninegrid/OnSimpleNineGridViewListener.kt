package com.lodz.android.componentkt.widget.ninegrid

import android.content.Context
import android.widget.ImageView

/**
 * 简单实现的九宫格接口
 * Created by zhouL on 2018/12/25.
 */
interface OnSimpleNineGridViewListener {

    /** 展示九宫格图片，上下文[context]，数据[data]，控件[imageView] */
    fun onDisplayNineGridImg(context: Context, data: String, imageView: ImageView)

    /** 展示预览器图片，上下文[context]，数据[data]，控件[imageView] */
    fun onDisplayPreviewImg(context: Context, data: String, imageView: ImageView)

    /** 展示选择器图片，上下文[context]，数据[data]，控件[imageView] */
    fun onDisplayPickerImg(context: Context, data: String, imageView: ImageView)
}