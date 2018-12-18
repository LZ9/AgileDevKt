package com.lodz.android.componentkt.photopicker.preview

import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import com.lodz.android.componentkt.photopicker.contract.OnClickListener
import com.lodz.android.componentkt.photopicker.contract.OnLongClickListener
import com.lodz.android.componentkt.photopicker.contract.OnPhotoLoader

/**
 * 图片预览构建器
 * Created by zhouL on 2018/12/15.
 */
class PreviewBuilder<T : Any> {
    /** 预览数据 */
    private val previewBean = PreviewBean<T>()

    /** 设置图片加载器[photoLoader] */
    fun setImgLoader(photoLoader: OnPhotoLoader<T>): PreviewBuilder<T> {
        previewBean.photoLoader = photoLoader
        return this
    }

    /** 设置是否可缩放[isScale] */
    fun setScale(isScale: Boolean): PreviewBuilder<T> {
        previewBean.isScale = isScale
        return this
    }

    /** 设置默认展示图片的位置[position] */
    fun setPosition(@IntRange(from = 0) position: Int): PreviewBuilder<T> {
        previewBean.showPosition = position
        return this
    }

    /** 设置背景色[color] */
    fun setBackgroundColor(@ColorRes color: Int): PreviewBuilder<T> {
        previewBean.backgroundColor = color
        return this
    }

    /** 设置顶部状态栏颜色[color] */
    fun setStatusBarColor(@ColorRes color: Int): PreviewBuilder<T> {
        previewBean.statusBarColor = color
        return this
    }

    /** 设置底部导航栏颜色[color] */
    fun setNavigationBarColor(@ColorRes color: Int): PreviewBuilder<T> {
        previewBean.navigationBarColor = color
        return this
    }

    /** 设置页码文字颜色[color] */
    fun setPagerTextColor(@ColorRes color: Int): PreviewBuilder<T> {
        previewBean.pagerTextColor = color
        return this
    }

    /** 设置页码文字大小[sp] */
    fun setPagerTextSize(sp: Int): PreviewBuilder<T> {
        previewBean.pagerTextSize = sp
        return this
    }

    /** 设置是否显示[isShow]页码文字 */
    fun setShowPagerText(isShow: Boolean): PreviewBuilder<T> {
        previewBean.isShowPagerText = isShow
        return this
    }

    /** 设置点击监听[listener] */
    fun setOnClickListener(listener: OnClickListener<T>): PreviewBuilder<T> {
        previewBean.clickListener = listener
        return this
    }

    /** 设置长按监听[listener] */
    fun setOnLongClickListener(listener: OnLongClickListener<T>): PreviewBuilder<T> {
        previewBean.longClickListener = listener
        return this
    }

    /** 完成单张图片[source]构建 */
    fun build(source: T): PreviewManager<T> {
        previewBean.sourceList = arrayListOf(source)
        return PreviewManager(previewBean)
    }

    /** 完成图片列表[sourceList]构建 */
    fun build(sourceList: List<T>): PreviewManager<T> {
        previewBean.sourceList = sourceList
        return PreviewManager(previewBean)
    }

    /** 完成图片数组[sourceArray]构建 */
    fun build(sourceArray: Array<T>): PreviewManager<T> {
        previewBean.sourceList = sourceArray.toList()
        return PreviewManager(previewBean)
    }
}