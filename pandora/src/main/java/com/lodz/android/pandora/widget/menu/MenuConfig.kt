package com.lodz.android.pandora.widget.menu

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.StateListDrawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.utils.SelectorUtils
import com.lodz.android.pandora.R

/**
 * 菜单按钮配置
 * @author zhouL
 * @date 2019/2/25
 */
open class MenuConfig {

    /** 图标状态Drawable */
     var iconDrawableState: StateListDrawable? = null
    /** 图片大小 */
     var iconSizeDp: Int = 0

    /** 菜单类型 */
     var type = 0

    /** 菜单文字 */
     var text = ""
    /** 菜单文字大小 */
     var textSizeSp = 0f
    /** 文字状态Drawable */
     var textColorState: ColorStateList? = null
    /** 文字和图片间距 */
     var drawablePaddingPx = 0

    /** 数字 */
     var num = 0
    /** 数字大小 */
     var numTextSizeSp = 0f
    /** 数字颜色 */
    @ColorInt
     var numTextColor = 0
    /** 数字背景 */
    @DrawableRes
     var numTextBackgroundDrawableResId = 0
    /** 数字距中心点的旋转角度 */
     var numTextAngle = 0
    /** 数字距中心点的距离 */
     var numTextRadiusPx = 0
    /** 数字背景大小 */
     var numTextBgSizeDp = 0

    /** 提醒点显隐 */
    var pointVisibility = View.GONE
    /** 提醒点颜色（默认红色） */
    @DrawableRes
    var pointBackgroundDrawableResId = R.drawable.pandora_bg_ea413c_circle
    /** 提醒点距中心点的旋转角度 */
    var pointAngle = 0
    /** 提醒点距中心点的距离 */
    var pointRadiusPx = 0

    /** 角标图片显隐 */
    var badgeImgVisibility = View.GONE
    /** 角标图片 */
    @DrawableRes
    var badgeImgResId = 0
    /** 角标图片 */
    var badgeImgWidthPx = ViewGroup.LayoutParams.WRAP_CONTENT
    /** 角标图片 */
    var badgeImgHeightPx = ViewGroup.LayoutParams.WRAP_CONTENT
    /** 提醒点距中心点的旋转角度 */
    var badgeImgAngle = 0
    /** 提醒点距中心点的距离 */
    var badgeImgRadiusPx = 0

    /** 指示器宽度 */
    var indicatorWidthPx = ViewGroup.LayoutParams.MATCH_PARENT
    /** 指示器高度 */
    var indicatorHeightPx = 0
    /** 使用指示器 */
    var isUseIndicator = false
    /** 指示器左间距 */
    var indicatorMarginStartPx = 0
    /** 指示器右间距 */
    var indicatorMarginEndPx = 0
    /** 指示器上间距 */
    var indicatorMarginTopPx = 0
    /** 指示器下间距 */
    var indicatorMarginBottomPx = 0
    /** 指示器颜色 */
    @ColorInt
    var indicatorColor = Color.BLUE

    /** 设置图标资源Id，[context]上下文，[normal]正常图标，[selected]选中图标 */
    fun setIconResId(context: Context, @DrawableRes normal: Int, @DrawableRes selected: Int) {
        iconDrawableState = SelectorUtils.createBgSelectedDrawable(context, normal, selected)
    }

    /** 设置菜单文字颜色，[normal]正常颜色，[selected]选中颜色 */
    fun setTextColor(@ColorInt normal: Int, @ColorInt selected: Int) {
        textColorState = SelectorUtils.createTxSelectedColor(normal, selected)
    }

    /** 设置菜单文字颜色，[context]上下文，[normal]正常颜色，[selected]选中颜色 */
    fun setTextColor(context: Context, @ColorRes normal: Int, @ColorRes selected: Int) {
        textColorState = SelectorUtils.createTxSelectedColor(context, normal, selected)
    }

    /** 设置数字颜色[color] */
    fun setNumTextColor(context: Context, @ColorRes color: Int) {
        numTextColor = context.getColorCompat(color)
    }

    /** 设置指示器颜色[color] */
    fun setIndicatorColor(context: Context, @ColorRes color: Int) {
        indicatorColor = context.getColorCompat(color)
    }

}