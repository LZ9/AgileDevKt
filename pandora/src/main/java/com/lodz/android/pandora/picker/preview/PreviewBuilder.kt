package com.lodz.android.pandora.picker.preview

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.viewpager2.widget.ViewPager2

/**
 * 图片预览构建器
 * Created by zhouL on 2018/12/15.
 */
class PreviewBuilder<V : View, T : Any> {
    /** 预览数据 */
    private val previewBean = PreviewBean<V, T>()

    /** 设置默认展示图片的位置[position] */
    fun setPosition(@IntRange(from = 0) position: Int): PreviewBuilder<V, T> {
        previewBean.showPosition = position
        return this
    }

    /** 设置背景色[color] */
    fun setBackgroundColor(@ColorRes color: Int): PreviewBuilder<V, T> {
        previewBean.backgroundColor = color
        return this
    }

    /** 设置顶部状态栏颜色[color] */
    fun setStatusBarColor(@ColorRes color: Int): PreviewBuilder<V, T> {
        previewBean.statusBarColor = color
        return this
    }

    /** 设置底部导航栏颜色[color] */
    fun setNavigationBarColor(@ColorRes color: Int): PreviewBuilder<V, T> {
        previewBean.navigationBarColor = color
        return this
    }

    /** 设置页码文字颜色[color] */
    fun setPagerTextColor(@ColorRes color: Int): PreviewBuilder<V, T> {
        previewBean.pagerTextColor = color
        return this
    }

    /** 设置页码文字大小[sp] */
    fun setPagerTextSize(sp: Int): PreviewBuilder<V, T> {
        previewBean.pagerTextSize = sp
        return this
    }

    /** 设置是否显示[isShow]页码文字 */
    fun setShowPagerText(isShow: Boolean): PreviewBuilder<V, T> {
        previewBean.isShowPagerText = isShow
        return this
    }

    /** 设置图片控件[view] */
    fun setImageView(view: AbsImageView<V, T>): PreviewBuilder<V, T> {
        previewBean.imgView = view
        return this
    }

    /** 设置VP2的动画[pageTransformer] */
    fun setPageTransformer(pageTransformer: ViewPager2.PageTransformer?): PreviewBuilder<V, T> {
        previewBean.pageTransformer = pageTransformer
        return this
    }

    /** 完成单张图片[source]构建 */
    fun build(source: T): PreviewManager<V, T> {
        previewBean.sourceList = arrayListOf(source)
        return PreviewManager(previewBean)
    }

    /** 完成图片列表[sourceList]构建 */
    fun build(sourceList: List<T>): PreviewManager<V, T> {
        previewBean.sourceList = sourceList
        return PreviewManager(previewBean)
    }

    /** 完成图片数组[sourceArray]构建 */
    fun build(sourceArray: Array<T>): PreviewManager<V, T> {
        previewBean.sourceList = sourceArray.toList()
        return PreviewManager(previewBean)
    }
}