package com.lodz.android.corekt.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.lodz.android.corekt.anko.createColorDrawable
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.getDrawableCompat

/**
 * Selector状态帮助类
 * Created by zhouL on 2018/7/19.
 */
object SelectorUtils {

    /** 创建按压背景，正常背景[normal]，按压背景[pressed] */
    @JvmStatic
    fun createBgPressedDrawable(context: Context, @DrawableRes normal: Int, @DrawableRes pressed: Int): StateListDrawable =
        createBgSelectorDrawable(context, normal, pressed)

    /** 创建按压和不可用背景，正常背景[normal]，按压背景[pressed]，不可用背景[unable] */
    @JvmStatic
    fun createBgPressedUnableDrawable(context: Context, @DrawableRes normal: Int, @DrawableRes pressed: Int, @DrawableRes unable: Int): StateListDrawable =
        createBgSelectorDrawable(context, normal, pressed, unable)

    /** 创建选中背景，正常背景[normal]，选中背景[selected] */
    @JvmStatic
    fun createBgSelectedDrawable(context: Context, @DrawableRes normal: Int, @DrawableRes selected: Int): StateListDrawable =
        createBgSelectorDrawable(context, normal, selected = selected)

    /** 创建按压背景，正常背景[normal]，按压背景[pressed] */
    @JvmStatic
    fun createBgPressedDrawable(normal: Drawable, pressed: Drawable): StateListDrawable =
        createBgSelectorDrawable(normal, pressed)

    /** 创建按压和不可用背景，正常背景[normal]，按压背景[pressed]，不可用背景[unable] */
    @JvmStatic
    fun createBgPressedUnableDrawable(normal: Drawable, pressed: Drawable, unable: Drawable): StateListDrawable =
        createBgSelectorDrawable(normal, pressed, unable)

    /** 创建选中背景，正常背景[normal]，选中背景[selected] */
    @JvmStatic
    fun createBgSelectedDrawable(normal: Drawable, selected: Drawable): StateListDrawable =
        createBgSelectorDrawable(normal, selected = selected)

    /** 创建按压背景，正常背景[normal]，按压背景[pressed] */
    @JvmStatic
    fun createBgPressedColor(context: Context, @ColorRes normal: Int, @ColorRes pressed: Int): StateListDrawable =
        createBgSelectorColor(context, normal, pressed)

    /** 创建按压和不可用背景，正常背景[normal]，按压背景[pressed]，不可用背景[unable] */
    @JvmStatic
    fun createBgPressedUnableColor(context: Context, @ColorRes normal: Int, @ColorRes pressed: Int, @ColorRes unable: Int): StateListDrawable =
        createBgSelectorColor(context, normal, pressed, unable)

    /** 创建选中背景，正常背景[normal]，选中背景[selected] */
    @JvmStatic
    fun createBgSelectedColor(context: Context, @ColorRes normal: Int, @ColorRes selected: Int): StateListDrawable =
        createBgSelectorColor(context, normal, selected = selected)

    /** 创建背景选择器，正常背景[normal]，按压背景[pressed]，不可用背景[unable]，选中背景[selected]，获取焦点背景[focused] */
    @JvmStatic
    @JvmOverloads
    fun createBgSelectorDrawable(
        context: Context, @DrawableRes normal: Int, @DrawableRes pressed: Int? = null, @DrawableRes unable: Int? = null,
        @DrawableRes selected: Int? = null, @DrawableRes focused: Int? = null
    ): StateListDrawable = createBgSelectorDrawable(
        context.getDrawableCompat(normal)!!,
        if (pressed == null) null else context.getDrawableCompat(pressed),
        if (unable == null) null else context.getDrawableCompat(unable),
        if (selected == null) null else context.getDrawableCompat(selected),
        if (focused == null) null else context.getDrawableCompat(focused)
    )

    /** 创建背景选择器，正常背景[normal]，按压背景[pressed]，不可用背景[unable]，选中背景[selected]，获取焦点背景[focused] */
    @JvmStatic
    @JvmOverloads
    fun createBgSelectorColor(
        context: Context, @ColorRes normal: Int, @ColorRes pressed: Int? = null, @ColorRes unable: Int? = null,
        @ColorRes selected: Int? = null, @ColorRes focused: Int? = null
    ): StateListDrawable = createBgSelectorDrawable(
        context.createColorDrawable(normal),
        if (pressed == null) null else context.createColorDrawable(pressed),
        if (unable == null) null else context.createColorDrawable(unable),
        if (selected == null) null else context.createColorDrawable(selected),
        if (focused == null) null else context.createColorDrawable(focused)
    )

    /** 创建背景选择器，正常背景[normal]，按压背景[pressed]，不可用背景[unable]，选中背景[selected]，获取焦点背景[focused] */
    @JvmStatic
    @JvmOverloads
    fun createBgSelectorDrawable(
        normal: Drawable, pressed: Drawable? = null, unable: Drawable? = null,
        selected: Drawable? = null, focused: Drawable? = null
    ): StateListDrawable {
        val drawable = StateListDrawable()

        //设置默认值
        val presseds = pressed ?: normal
        val unables = unable ?: ColorDrawable(Color.LTGRAY)
        val selecteds = selected ?: normal
        val focuseds = focused ?: normal

        drawable.addState(intArrayOf(android.R.attr.state_pressed, -android.R.attr.state_selected, android.R.attr.state_enabled), presseds)
        drawable.addState(intArrayOf(android.R.attr.state_selected, android.R.attr.state_enabled), selecteds)
        drawable.addState(intArrayOf(android.R.attr.state_focused, android.R.attr.state_enabled), focuseds)
        drawable.addState(intArrayOf(android.R.attr.state_enabled), normal)
        drawable.addState(intArrayOf(), unables)
        return drawable
    }

    /** 创建按压文字颜色，正常颜色[normal]，按压颜色[pressed] */
    @JvmStatic
    fun createTxPressedColor(@ColorInt normal: Int, @ColorInt pressed: Int): ColorStateList =
        createTxSelectorColor(normal, pressed)

    /** 创建按压和不可用文字颜色，正常颜色[normal]，按压颜色[pressed]，不可用颜色[unable] */
    @JvmStatic
    fun createTxPressedUnableColor(@ColorInt normal: Int, @ColorInt pressed: Int, @ColorInt unable: Int): ColorStateList =
        createTxSelectorColor(normal, pressed, unable)

    /** 创建选中文字颜色，正常颜色[normal]，选中颜色[selected] */
    @JvmStatic
    fun createTxSelectedColor(@ColorInt normal: Int, @ColorInt selected: Int): ColorStateList =
        createTxSelectorColor(normal, selected = selected)

    /** 创建文字颜色选择器，正常颜色[normal]，按压颜色[pressed] */
    @JvmStatic
    fun createTxPressedColor(context: Context, @ColorRes normal: Int, @ColorRes pressed: Int): ColorStateList =
        createTxSelectorColor(context, normal, pressed)

    /** 创建文字颜色选择器，正常颜色[normal]，按压颜色[pressed]，不可用颜色[unable] */
    @JvmStatic
    fun createTxPressedUnableColor(context: Context, @ColorRes normal: Int, @ColorRes pressed: Int, @ColorRes unable: Int): ColorStateList =
        createTxSelectorColor(context, normal, pressed, unable)

    /** 创建文字颜色选择器，正常颜色[normal]，选中颜色[selected] */
    @JvmStatic
    fun createTxSelectedColor(context: Context, @ColorRes normal: Int, @ColorRes selected: Int): ColorStateList =
        createTxSelectorColor(context, normal, selected = selected)

    /** 创建文字颜色选择器，正常颜色[normal]，按压颜色[pressed]，不可用颜色[unable]，选中颜色[selected]，获取焦点颜色[focused] */
    @JvmStatic
    @JvmOverloads
    fun createTxSelectorColor(
        context: Context, @ColorRes normal: Int, @ColorRes pressed: Int? = null, @ColorRes unable: Int? = null,
        @ColorRes selected: Int? = null, @ColorRes focused: Int? = null
    ): ColorStateList = createTxSelectorColor(
        context.getColorCompat(normal),
        if (pressed == null) null else context.getColorCompat(pressed),
        if (unable == null) null else context.getColorCompat(unable),
        if (selected == null) null else context.getColorCompat(selected),
        if (focused == null) null else context.getColorCompat(focused)
    )

    /** 创建文字颜色选择器，正常颜色[normal]，按压颜色[pressed]，不可用颜色[unable]，选中颜色[selected]，获取焦点颜色[focused] */
    @JvmStatic
    @JvmOverloads
    fun createTxSelectorColor(
        @ColorInt normal: Int, @ColorInt pressed: Int? = null, @ColorInt unable: Int? = null,
        @ColorInt selected: Int? = null, @ColorInt focused: Int? = null
    ): ColorStateList {
        val presseds = pressed ?: normal
        val unables = unable ?: Color.GRAY
        val selecteds = selected ?: normal
        val focuseds = focused ?: normal

        val colors = intArrayOf(presseds, selecteds, focuseds, normal, unables)
        val states: Array<IntArray> = Array(colors.size) { intArrayOf() }
        states[0] = intArrayOf(android.R.attr.state_pressed, -android.R.attr.state_selected, android.R.attr.state_enabled)
        states[1] = intArrayOf(android.R.attr.state_selected, android.R.attr.state_enabled)
        states[2] = intArrayOf(android.R.attr.state_focused, android.R.attr.state_enabled)
        states[3] = intArrayOf(android.R.attr.state_enabled)
        states[4] = intArrayOf()
        return ColorStateList(states, colors)
    }

}