package com.lodz.android.pandora.picker.preview

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.lodz.android.pandora.widget.rv.recycler.base.AbsRvAdapter

/**
 * 预览数据
 * Created by zhouL on 2018/12/13.
 */
internal class PreviewBean<T : Any, VH : RecyclerView.ViewHolder> {

    /** 资源列表 */
    var sourceList: List<T>? = null
    /** 默认显示的图片位置 */
    var showPosition = 0
    /** 预览页的背景色 */
    @ColorInt
    var backgroundColor = Color.BLACK
    /** 顶部状态栏颜色 */
    @ColorInt
    var statusBarColor = Color.BLACK
    /** 底部导航栏颜色 */
    @ColorInt
    var navigationBarColor = Color.BLACK
    /** 页码文字颜色 */
    @ColorInt
    var pagerTextColor = Color.DKGRAY
    /** 页码文字大小（单位SP） */
    var pagerTextSize = 16
    /** 是否显示页码文字 */
    var isShowPagerText = true
    /** 适配器 */
    var adapter: AbsRvAdapter<T, VH>? = null
    /** VP2的动画 */
    var pageTransformer: ViewPager2.PageTransformer? = null

    fun clear() {
        sourceList = null
        adapter = null
    }
}