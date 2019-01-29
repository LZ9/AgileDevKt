package com.lodz.android.pandora.photopicker.preview

import android.content.Context
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.lodz.android.pandora.photopicker.contract.OnClickListener
import com.lodz.android.pandora.photopicker.contract.OnImgLoader
import com.lodz.android.pandora.photopicker.contract.OnLongClickListener
import com.lodz.android.pandora.photopicker.contract.preview.OnLargeImgLoader
import com.lodz.android.pandora.photopicker.contract.preview.PreviewController

/**
 * 图片预览构建器
 * Created by zhouL on 2018/12/15.
 */
class PreviewBuilder<T : Any> {
    /** 预览数据 */
    private val previewBean = PreviewBean<T>()

    /** 设置大图片加载器[imgLoader] */
    fun setLargeImgLoader(imgLoader: OnLargeImgLoader<T>): PreviewBuilder<T> {
        previewBean.largeImgLoader = imgLoader
        return this
    }

    /** 设置大图片加载器[imgLoader] */
    fun setLargeImgLoader(imgLoader: (context: Context, source: T, imageView: SubsamplingScaleImageView) -> Unit): PreviewBuilder<T> =
            setLargeImgLoader(object : OnLargeImgLoader<T> {
                override fun displayImg(context: Context, source: T, imageView: SubsamplingScaleImageView) {
                    imgLoader.invoke(context, source, imageView)
                }
            })

    /** 设置图片加载器[imgLoader] */
    fun setImgLoader(imgLoader: OnImgLoader<T>): PreviewBuilder<T> {
        previewBean.imgLoader = imgLoader
        return this
    }

    /** 设置图片加载器[imgLoader] */
    fun setImgLoader(imgLoader: (context: Context, source: T, imageView: ImageView) -> Unit): PreviewBuilder<T> =
            setImgLoader(object : OnImgLoader<T> {
                override fun displayImg(context: Context, source: T, imageView: ImageView) {
                    imgLoader.invoke(context, source, imageView)
                }
            })

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

    /** 设置点击监听[listener] */
    fun setOnClickListener(listener: (context: Context, source: T, position: Int, controller: PreviewController) -> Unit): PreviewBuilder<T> =
            setOnClickListener(object : OnClickListener<T> {
                override fun onClick(context: Context, source: T, position: Int, controller: PreviewController) {
                    listener.invoke(context, source, position, controller)
                }
            })

    /** 设置长按监听[listener] */
    fun setOnLongClickListener(listener: OnLongClickListener<T>): PreviewBuilder<T> {
        previewBean.longClickListener = listener
        return this
    }

    /** 设置长按监听[listener] */
    fun setOnLongClickListener(listener: (context: Context, source: T, position: Int, controller: PreviewController) -> Unit): PreviewBuilder<T> =
            setOnLongClickListener(object : OnLongClickListener<T> {
                override fun onLongClick(context: Context, source: T, position: Int, controller: PreviewController) {
                    listener.invoke(context, source, position, controller)
                }
            })

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