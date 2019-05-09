package com.lodz.android.pandora.widget.menu

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.StateListDrawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.utils.SelectorUtils

/**
 * 菜单按钮配置
 * @author zhouL
 * @date 2019/2/25
 */
class MenuConfig {

    /** 图标状态Drawable */
    private var iconDrawableState: StateListDrawable? = null
    /** 图片大小 */
    private var iconSizeDp: Int = 0
    /** 菜单类型 */
    private var type = 0
    /** 菜单文字 */
    private var text = ""
    /** 菜单文字大小 */
    private var textSize = 0f
    /** 文字状态Drawable */
    private var textColorState: ColorStateList? = null
    /** 文字和图片间距 */
    private var drawablePadding = 0
    /** 数字 */
    private var num = 0
    /** 数字大小 */
    private var numTextSize = 0f
    /** 数字颜色 */
    @ColorInt
    private var numTextColor = 0
    /** 数字背景 */
    private var numBackgroundDrawableResId = 0
    /** 数字上间距 */
    private var numTextMarginTop = 0
    /** 数字右间距 */
    private var numTextMarginEnd = 0
    /** 数字背景大小 */
    private var numTextBgSizeDp = 0

    /** 设置图片大小[dp] */
    fun setIconSize(dp: Int) {
        iconSizeDp = dp
    }

    /** 设置图标资源Id，[context]上下文，[normal]正常图标，[selected]选中图标 */
    fun setIconResId(context: Context, @DrawableRes normal: Int, @DrawableRes selected: Int) {
        iconDrawableState = SelectorUtils.createBgSelectedDrawable(context, normal, selected)
    }

    /** 设置图标状态[drawable] */
    fun setIconDrawableState(drawable: StateListDrawable) {
        iconDrawableState = drawable
    }

    /** 设置菜单类型[type] */
    fun setType(type: Int) {
        this.type = type
    }

    /** 设置菜单文字[text] */
    fun setText(text: String) {
        this.text = text
    }

    /** 设置菜单文字大小[sp] */
    fun setTextSize(sp: Float) {
        textSize = sp
    }

    /** 设置菜单文字颜色，[normal]正常颜色，[selected]选中颜色 */
    fun setTextColor(@ColorInt normal: Int, @ColorInt selected: Int) {
        textColorState = SelectorUtils.createTxSelectedColor(normal, selected)
    }

    /** 设置菜单文字颜色，[context]上下文，[normal]正常颜色，[selected]选中颜色 */
    fun setTextColor(context: Context, @ColorRes normal: Int, @ColorRes selected: Int) {
        textColorState = SelectorUtils.createTxSelectedColor(context, normal, selected)
    }

    /** 设置菜单文字颜色状态[colorState] */
    fun setTextColor(colorState: ColorStateList) {
        textColorState = colorState
    }

    /** 设置文字和图标的间距[px] */
    fun setDrawablePadding(px: Int) {
        drawablePadding = px
    }

    /** 设置数字[value] */
    fun setNum(value: Int) {
        num = value
    }

    /** 设置数字文字大小[sp] */
    fun setNumTextSize(sp: Float) {
        numTextSize = sp
    }

    /** 设置数字颜色[color] */
    fun setNumTextColor(@ColorInt color: Int) {
        numTextColor = color
    }

    /** 设置数字颜色[color] */
    fun setNumTextColor(context: Context, @ColorRes color: Int) {
        numTextColor = context.getColorCompat(color)
    }

    /** 设置数字背景资源id[resId] */
    fun setNumBackground(@DrawableRes resId: Int) {
        numBackgroundDrawableResId = resId
    }

    /** 数字上间距[top] */
    fun setNumTextMarginTop(px: Int) {
        numTextMarginTop = px
    }

    /** 数字右间距[end] */
    fun setNumTextMarginEnd(px: Int) {
        numTextMarginEnd = px
    }

    /** 数字背景大小[end] */
    fun setNumTextBgSizeDp(dp: Int) {
        numTextBgSizeDp = dp
    }

    /** 获取图片大小 */
    internal fun getIconSize(): Int = iconSizeDp

    /** 获取图标状态 */
    internal fun getIconDrawableState(): StateListDrawable? = iconDrawableState

    /** 获取菜单类型 */
    internal fun getType(): Int = type

    /** 获取菜单文字 */
    internal fun getText(): String = text

    /** 获取菜单文字大小 */
    internal fun getTextSize(): Float = textSize

    /** 获取菜单文字颜色状态 */
    internal fun getTextColorState(): ColorStateList? = textColorState

    /** 获取文字和图标的间距 */
    internal fun getDrawablePadding(): Int = drawablePadding

    /** 获取数字 */
    internal fun getNum(): Int = num

    /** 获取数字颜色 */
    internal fun getNumTextColor(): Int = numTextColor

    /** 获取数字背景 */
    internal fun getNumBackgroundDrawableResId(): Int = numBackgroundDrawableResId

    /** 获取数字文字大小 */
    internal fun getNumTextSize(): Float = numTextSize

    /** 获取数字上间距 */
    internal fun getNumTextMarginTop(): Int = numTextMarginTop

    /** 获取数字右间距 */
    internal fun getNumTextMarginEnd(): Int = numTextMarginEnd

    /** 获取数字背景大小 */
    internal fun getNumTextBgSizeDp(): Int = numTextBgSizeDp

}