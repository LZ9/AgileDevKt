package com.lodz.android.componentkt.photopicker.preview

import androidx.annotation.ColorRes
import com.lodz.android.componentkt.photopicker.contract.OnClickListener
import com.lodz.android.componentkt.photopicker.contract.OnLongClickListener
import com.lodz.android.componentkt.photopicker.contract.OnPhotoLoader

/**
 * 预览数据
 * Created by zhouL on 2018/12/13.
 */
internal class PreviewBean<T : Any> {

    /** 资源列表 */
    var sourceList: List<T>? = null
    /** 图片加载接口 */
    var photoLoader: OnPhotoLoader<T>? = null
    /** 默认显示的图片位置 */
    var showPosition = 0
    /** 预览页的背景色 */
    @ColorRes
    var backgroundColor = android.R.color.black
    /** 顶部状态栏颜色 */
    @ColorRes
    var statusBarColor = android.R.color.black
    /** 底部导航栏颜色 */
    @ColorRes
    var navigationBarColor = android.R.color.black
    /** 页码文字颜色 */
    @ColorRes
    var pagerTextColor = android.R.color.darker_gray
    /** 页码文字大小（单位SP） */
    var pagerTextSize = 16
    /** 是否显示页码文字 */
    var isShowPagerText = true
    /** 点击事件 */
    var clickListener: OnClickListener<T>? = null
    /** 长按事件事件 */
    var longClickListener: OnLongClickListener<T>? = null
    /** 是否可缩放 */
    var isScale = true

    fun clear() {
        sourceList = null
        photoLoader = null
        clickListener = null
        longClickListener = null
    }
}