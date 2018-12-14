package com.lodz.android.componentkt.photopicker.preview

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import com.lodz.android.componentkt.R
import com.lodz.android.componentkt.photopicker.contract.OnClickListener
import com.lodz.android.componentkt.photopicker.contract.OnLongClickListener
import com.lodz.android.componentkt.photopicker.contract.OnPhotoLoader
import com.lodz.android.corekt.anko.getSize
import com.lodz.android.corekt.utils.ToastUtils

/**
 * 图片预览管理类
 * Created by zhouL on 2018/12/13.
 */
class PreviewManager<T> private constructor(private val previewBean: PreviewBean<T>) {
    companion object {
        fun <T> create(): Builder<T> = Builder()
    }

    /** 预览数据构建类 */
    class Builder<T> {
        /** 预览数据 */
        private val previewBean: PreviewBean<T>

        init {
            previewBean = PreviewBean()
        }

        /** 设置图片加载器[photoLoader] */
        fun setImgLoader(photoLoader: OnPhotoLoader<T>): Builder<T> {
            previewBean.photoLoader = photoLoader
            return this
        }

        /** 设置是否可缩放[isScale] */
        fun setScale(isScale: Boolean): Builder<T> {
            previewBean.isScale = isScale
            return this
        }

        /** 设置默认展示图片的位置[position] */
        fun setPosition(@IntRange(from = 0) position: Int): Builder<T> {
            previewBean.showPosition = position
            return this
        }

        /** 设置背景色[color] */
        fun setBackgroundColor(@ColorRes color: Int): Builder<T> {
            previewBean.backgroundColor = color
            return this
        }

        /** 设置顶部状态栏颜色[color] */
        fun setStatusBarColor(@ColorRes color: Int): Builder<T> {
            previewBean.statusBarColor = color
            return this
        }

        /** 设置底部导航栏颜色[color] */
        fun setNavigationBarColor(@ColorRes color: Int): Builder<T> {
            previewBean.navigationBarColor = color
            return this
        }

        /** 设置页码文字颜色[color] */
        fun setPagerTextColor(@ColorRes color: Int): Builder<T> {
            previewBean.pagerTextColor = color
            return this
        }

        /** 设置页码文字大小[sp] */
        fun setPagerTextSize(sp: Int): Builder<T> {
            previewBean.pagerTextSize = sp
            return this
        }

        /** 设置是否显示[isShow]页码文字 */
        fun setShowPagerText(isShow: Boolean): Builder<T> {
            previewBean.isShowPagerText = isShow
            return this
        }

        /** 设置点击监听[listener] */
        fun setOnClickListener(listener: OnClickListener<T>): Builder<T> {
            previewBean.clickListener = listener
            return this
        }

        /** 设置长按监听[listener] */
        fun setOnLongClickListener(listener: OnLongClickListener<T>): Builder<T> {
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

    /** 打开预览器，上下文[context] */
    fun open(context: Context) {
        if (previewBean.photoLoader == null) {// 校验图片加载器
            ToastUtils.showShort(context, R.string.componentkt_photo_loader_unset)
            return
        }
        if (previewBean.sourceList.isNullOrEmpty()) {// 校验数据列表
            ToastUtils.showShort(context, R.string.componentkt_preview_source_list_empty)
            return
        }
        previewBean.isShowPagerText = previewBean.sourceList.getSize() > 1// 只有一张图片不显示页码
        if ((previewBean.showPosition + 1) > previewBean.sourceList.getSize()) {// 校验默认位置参数
            previewBean.showPosition = 0
        }
        
//        previewBean.sourceList = null
//        previewBean.photoLoader = null
//        previewBean.clickListener = null
//        previewBean.longClickListener = null

        PicturePreviewActivity.start(context, previewBean)
    }

}