package com.lodz.android.pandora.picker.preview

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.pandora.picker.preview.vh.AbsPreviewAgent

/**
 * 图片预览构建器
 * Created by zhouL on 2018/12/15.
 */
class PreviewBuilder<T, VH : RecyclerView.ViewHolder> {
    /** 预览数据 */
    private val previewBean = PreviewBean<T, VH>()

    /** 设置默认展示图片的位置[position] */
    fun setPosition(@IntRange(from = 0) position: Int): PreviewBuilder<T, VH> {
        previewBean.showPosition = position
        return this
    }

    /** 设置背景色[color] */
    fun setBackgroundColor(@ColorInt color: Int): PreviewBuilder<T, VH> {
        previewBean.backgroundColor = color
        return this
    }

    /** 设置背景色[color] */
    fun setBackgroundColor(context: Context, @ColorRes color: Int): PreviewBuilder<T, VH> =
        setBackgroundColor(context.getColorCompat(color))

    /** 设置顶部状态栏颜色[color] */
    fun setStatusBarColor(@ColorInt color: Int): PreviewBuilder<T, VH> {
        previewBean.statusBarColor = color
        return this
    }

    /** 设置顶部状态栏颜色[color] */
    fun setStatusBarColor(context: Context, @ColorRes color: Int): PreviewBuilder<T, VH> =
        setStatusBarColor(context.getColorCompat(color))

    /** 设置底部导航栏颜色[color] */
    fun setNavigationBarColor(@ColorInt color: Int): PreviewBuilder<T, VH> {
        previewBean.navigationBarColor = color
        return this
    }

    /** 设置底部导航栏颜色[color] */
    fun setNavigationBarColor(context: Context, @ColorRes color: Int): PreviewBuilder<T, VH> =
        setNavigationBarColor(context.getColorCompat(color))

    /** 设置页码文字颜色[color] */
    fun setPagerTextColor(@ColorInt color: Int): PreviewBuilder<T, VH> {
        previewBean.pagerTextColor = color
        return this
    }

    /** 设置页码文字颜色[color] */
    fun setPagerTextColor(context: Context, @ColorRes color: Int): PreviewBuilder<T, VH> =
        setPagerTextColor(context.getColorCompat(color))

    /** 设置页码文字大小[sp] */
    fun setPagerTextSize(sp: Int): PreviewBuilder<T, VH> {
        previewBean.pagerTextSize = sp
        return this
    }

    /** 设置是否显示[isShow]页码文字 */
    fun setShowPagerText(isShow: Boolean): PreviewBuilder<T, VH> {
        previewBean.isShowPagerText = isShow
        return this
    }

    /** 设置控件[view] */
    fun setImageView(view: AbsPreviewAgent<T, VH>): PreviewBuilder<T, VH> {
        previewBean.view = view
        return this
    }

    /** 设置VP2的动画[pageTransformer] */
    fun setPageTransformer(pageTransformer: ViewPager2.PageTransformer?): PreviewBuilder<T, VH> {
        previewBean.pageTransformer = pageTransformer
        return this
    }

    /** 完成单张图片[source]构建 */
    fun build(source: T): PreviewManager<T, VH> {
        previewBean.sourceList = arrayListOf(source)
        return PreviewManager(previewBean)
    }

    /** 完成图片列表[sourceList]构建 */
    fun build(sourceList: List<T>): PreviewManager<T, VH> {
        previewBean.sourceList = sourceList
        return PreviewManager(previewBean)
    }

    /** 完成图片数组[sourceArray]构建 */
    fun build(sourceArray: Array<T>): PreviewManager<T, VH> {
        previewBean.sourceList = sourceArray.toList()
        return PreviewManager(previewBean)
    }
}