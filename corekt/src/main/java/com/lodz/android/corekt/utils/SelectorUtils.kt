package com.lodz.android.corekt.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import com.lodz.android.corekt.anko.createColorDrawable
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.getDrawableCompat

/**
 * Selector状态帮助类
 * Created by zhouL on 2018/7/19.
 */
object SelectorUtils {

    /** 创建按压背景，正常背景[normal]，按压背景[pressed] */
    fun createBgPressedDrawable(context: Context, @DrawableRes normal: Int, @DrawableRes pressed: Int): StateListDrawable = createBgSelectorDrawable(context, normal, pressed)

    /** 创建按压和不可用背景，正常背景[normal]，按压背景[pressed]，不可用背景[unable] */
    fun createBgPressedUnableDrawable(context: Context, @DrawableRes normal: Int, @DrawableRes pressed: Int, @DrawableRes unable: Int): StateListDrawable = createBgSelectorDrawable(context, normal, pressed, unable)

    /** 创建选中背景，正常背景[normal]，选中背景[selected] */
    fun createBgSelectedDrawable(context: Context, @DrawableRes normal: Int, @DrawableRes selected: Int): StateListDrawable = createBgSelectorDrawable(context, normal, selected = selected)

    /** 创建按压背景，正常背景[normal]，按压背景[pressed] */
    fun createBgPressedDrawable(normal: Drawable, pressed: Drawable): StateListDrawable = createBgSelectorDrawable(normal, pressed)

    /** 创建按压和不可用背景，正常背景[normal]，按压背景[pressed]，不可用背景[unable] */
    fun createBgPressedUnableDrawable(normal: Drawable, pressed: Drawable, unable: Drawable): StateListDrawable = createBgSelectorDrawable(normal, pressed, unable)

    /** 创建选中背景，正常背景[normal]，选中背景[selected] */
    fun createBgSelectedDrawable(normal: Drawable, selected: Drawable): StateListDrawable = createBgSelectorDrawable(normal, selected = selected)

    /** 创建按压背景，正常背景[normal]，按压背景[pressed] */
    fun createBgPressedColor(context: Context, @ColorRes normal: Int, @ColorRes pressed: Int): StateListDrawable = createBgSelectorColor(context, normal, pressed)

    /** 创建按压和不可用背景，正常背景[normal]，按压背景[pressed]，不可用背景[unable] */
    fun createBgPressedUnableColor(context: Context, @ColorRes normal: Int, @ColorRes pressed: Int, @ColorRes unable: Int): StateListDrawable = createBgSelectorColor(context, normal, pressed, unable)

    /** 创建选中背景，正常背景[normal]，选中背景[selected] */
    fun createBgSelectedColor(context: Context, @ColorRes normal: Int, @ColorRes selected: Int): StateListDrawable = createBgSelectorColor(context, normal, selected = selected)

    /** 创建背景选择器，正常背景[normal]，按压背景[pressed]，不可用背景[unable]，选中背景[selected]，获取焦点背景[focused] */
    fun createBgSelectorDrawable(context: Context, @DrawableRes normal: Int = 0, @DrawableRes pressed: Int = 0, @DrawableRes unable: Int = 0,
                                 @DrawableRes selected: Int = 0, @DrawableRes focused: Int = 0): StateListDrawable = createBgSelectorDrawable(
            if (normal != 0) context.getDrawableCompat(normal) else null,
            if (pressed != 0) context.getDrawableCompat(pressed) else null,
            if (unable != 0) context.getDrawableCompat(unable) else null,
            if (selected != 0) context.getDrawableCompat(selected) else null,
            if (focused != 0) context.getDrawableCompat(focused) else null
    )

    /** 创建背景选择器，正常背景[normal]，按压背景[pressed]，不可用背景[unable]，选中背景[selected]，获取焦点背景[focused] */
    fun createBgSelectorColor(context: Context, @ColorRes normal: Int = 0, @ColorRes pressed: Int = 0, @ColorRes unable: Int = 0,
                              @ColorRes selected: Int = 0, @ColorRes focused: Int = 0): StateListDrawable = createBgSelectorDrawable(
            if (normal != 0) context.createColorDrawable(normal) else null,
            if (pressed != 0) context.createColorDrawable(pressed) else null,
            if (unable != 0) context.createColorDrawable(unable) else null,
            if (selected != 0) context.createColorDrawable(selected) else null,
            if (focused != 0) context.createColorDrawable(focused) else null
    )

    /** 创建背景选择器，正常背景[normal]，按压背景[pressed]，不可用背景[unable]，选中背景[selected]，获取焦点背景[focused] */
    fun createBgSelectorDrawable(normal: Drawable? = null, pressed: Drawable? = null, unable: Drawable? = null,
                                 selected: Drawable? = null, focused: Drawable? = null): StateListDrawable {
        val drawable = StateListDrawable()
        if (pressed != null) {
            drawable.addState(intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled), pressed)
        }
        if (selected != null) {
            drawable.addState(intArrayOf(android.R.attr.state_selected, android.R.attr.state_enabled), selected)
        }
        if (focused != null) {
            drawable.addState(intArrayOf(android.R.attr.state_focused, android.R.attr.state_enabled), focused)
        }
        if (normal != null) {
            drawable.addState(intArrayOf(android.R.attr.state_enabled), normal)
        }
        if (unable != null) {
            drawable.addState(intArrayOf(), unable)
        }
        return drawable
    }

    /** 创建按压文字颜色，正常颜色[normal]，按压颜色[pressed] */
    fun createTxPressedColor(@ColorInt normal: Int, @ColorInt pressed: Int): ColorStateList = createTxSelectorColor(normal, pressed)

    /** 创建按压和不可用文字颜色，正常颜色[normal]，按压颜色[pressed]，不可用颜色[unable] */
    fun createTxPressedUnableColor(@ColorInt normal: Int, @ColorInt pressed: Int, @ColorInt unable: Int): ColorStateList = createTxSelectorColor(normal, pressed, unable)

    /** 创建选中文字颜色，正常颜色[normal]，选中颜色[selected] */
    fun createTxSelectedColor(@ColorInt normal: Int, @ColorInt selected: Int): ColorStateList = createTxSelectorColor(normal, selected = selected)

    /** 创建文字颜色选择器，正常颜色[normal]，按压颜色[pressed] */
    fun createTxPressedColor(context: Context, @ColorRes normal: Int, @ColorRes pressed: Int): ColorStateList = createTxSelectorColor(context, normal, pressed)

    /** 创建文字颜色选择器，正常颜色[normal]，按压颜色[pressed]，不可用颜色[unable] */
    fun createTxPressedUnableColor(context: Context, @ColorRes normal: Int, @ColorRes pressed: Int, @ColorRes unable: Int): ColorStateList = createTxSelectorColor(context, normal, pressed, unable)

    /** 创建文字颜色选择器，正常颜色[normal]，选中颜色[selected] */
    fun createTxSelectedColor(context: Context, @ColorRes normal: Int, @ColorRes selected: Int): ColorStateList = createTxSelectorColor(context, normal, selected = selected)

    /** 创建文字颜色选择器，正常颜色[normal]，按压颜色[pressed]，不可用颜色[unable]，选中颜色[selected]，获取焦点颜色[focused] */
    fun createTxSelectorColor(context: Context, @ColorRes normal: Int = 0, @ColorRes pressed: Int = 0, @ColorRes unable: Int = 0,
                              @ColorRes selected: Int = 0, @ColorRes focused: Int = 0): ColorStateList = createTxSelectorColor(
            if (normal != 0) context.getColorCompat(normal) else 0,
            if (pressed != 0) context.getColorCompat(pressed) else 0,
            if (unable != 0) context.getColorCompat(unable) else 0,
            if (selected != 0) context.getColorCompat(selected) else 0,
            if (focused != 0) context.getColorCompat(focused) else 0
    )


    /** 创建文字颜色选择器，正常颜色[normal]，按压颜色[pressed]，不可用颜色[unable]，选中颜色[selected]，获取焦点颜色[focused] */
    fun createTxSelectorColor(@ColorInt normal: Int = 0, @ColorInt pressed: Int = 0, @ColorInt unable: Int = 0,
                              @ColorInt selected: Int = 0, @ColorInt focused: Int = 0): ColorStateList {
        val colors = intArrayOf(pressed, selected, focused, normal, unable)
        val states: Array<IntArray> = Array(colors.size) { intArrayOf() }
        states[0] = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
        states[1] = intArrayOf(android.R.attr.state_selected, android.R.attr.state_enabled)
        states[2] = intArrayOf(android.R.attr.state_focused, android.R.attr.state_enabled)
        states[3] = intArrayOf(android.R.attr.state_enabled)
        states[4] = intArrayOf()
        return ColorStateList(states, colors)
    }

}