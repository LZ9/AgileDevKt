package com.lodz.android.pandora.picker.preview

import android.view.View
import androidx.annotation.ColorRes
import androidx.viewpager2.widget.ViewPager2

/**
 * 预览数据
 * Created by zhouL on 2018/12/13.
 */
internal class PreviewBean<V : View, T : Any> {

    /** 资源列表 */
    var sourceList: List<T>? = null
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
    /** 图片控件 */
    var imgView: AbsImageView<V, T>? = null
    /** VP2的动画 */
    var pageTransformer: ViewPager2.PageTransformer? = null

    fun clear() {
        sourceList = null
        imgView = null
    }
}