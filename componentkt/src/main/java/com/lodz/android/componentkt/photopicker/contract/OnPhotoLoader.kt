package com.lodz.android.componentkt.photopicker.contract

import android.content.Context
import android.widget.ImageView
import java.io.Serializable

/**
 * 预览图片加载接口
 * Created by zhouL on 2018/12/13.
 */
interface OnPhotoLoader<T> : Serializable {

    /** 展示图片，上下文[context]，数据[source]，控件[imageView] */
    fun displayImg(context: Context, source: T, imageView: ImageView)
}