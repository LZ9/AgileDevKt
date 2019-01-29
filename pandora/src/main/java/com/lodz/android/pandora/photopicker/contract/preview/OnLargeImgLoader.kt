package com.lodz.android.pandora.photopicker.contract.preview

import android.content.Context
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

/**
 * 大图片加载接口
 * Created by zhouL on 2018/12/13.
 */
interface OnLargeImgLoader<in T : Any> {

    /** 展示图片，上下文[context]，数据[source]，控件[imageView] */
    fun displayImg(context: Context, source: T, imageView: SubsamplingScaleImageView)
}