package com.lodz.android.pandora.photopicker.contract

import android.content.Context
import android.widget.ImageView

/**
 * 预览图片加载接口
 * Created by zhouL on 2018/12/13.
 */
fun interface OnImgLoader<in T : Any> {

    /** 展示图片，上下文[context]，数据[source]，控件[imageView] */
    fun displayImg(context: Context, source: T, imageView: ImageView)
}