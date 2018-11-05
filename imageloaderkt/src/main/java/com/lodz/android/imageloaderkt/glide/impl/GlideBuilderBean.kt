package com.lodz.android.imageloaderkt.glide.impl

import android.graphics.Color
import androidx.annotation.AnimRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.transition.ViewPropertyTransition
import com.lodz.android.imageloaderkt.R
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Glide构建类
 * Created by zhouL on 2018/7/11.
 */
class GlideBuilderBean {

    /** 加载路径 */
    var path: Any? = null
    /** 加载图的资源id */
    @DrawableRes
    var placeholderResId = R.drawable.imageloaderkt_ic_launcher
    /** 失败图的资源id */
    @DrawableRes
    var errorResId = R.drawable.imageloaderkt_ic_launcher
    /** 控件宽度 */
    var widthPx = 0
    /** 控件高度  */
    var heightPx = 0
    /** 使用高斯模糊 */
    var useBlur = false
    /** 高斯模糊率（0-25） */
    var blurRadius = 5
    /** 是否使用圆形图片 */
    var useCircle = false
    /** 是否使用圆角 */
    var useRoundCorner = false
    /** 圆角半径 */
    var roundCornerRadius = 10
    /** 保存到内存 */
    var saveToMemoryCache = true
    /** 磁盘缓存策略 */
    var diskCacheStrategy = DiskCacheStrategy.AUTOMATIC
    /** 居中裁切 */
    var centerCrop = false
    /** 居中自适应 */
    var fitCenter = false
    /** 设置内部居中 */
    var centerInside = false
    /** 直接显示图片不使用动画 */
    var dontAnimate = false
    /** 渐变显示 */
    var crossFade = false
    /** 动画资源id */
    @AnimRes
    var animResId = -1
    /** 动画编辑器 */
    var animator: ViewPropertyTransition.Animator? = null
    /** 使用覆盖颜色 */
    var useFilterColor = false
    /** 覆盖颜色 */
    @ColorInt
    var filterColor = Color.TRANSPARENT
    /** 圆角图片的Margin */
    var roundedCornersMargin = 0
    /** 圆角位置参数 */
    var cornerType = RoundedCornersTransformation.CornerType.ALL
    /** 使用灰度化 */
    var useGrayscale = false
    /** 使用正方形图 */
    var useCropSquare = false
    /** 使用蒙板 */
    var useMask = false
    /** 默认蒙板图片资源id */
    @DrawableRes
    var maskResId = R.drawable.imageloaderkt_mask_starfish
    /** 是否显示视频第一帧 */
    var isVideo = false
    /** 图片请求监听器 */
    var requestListener: RequestListener<*>? = null
}