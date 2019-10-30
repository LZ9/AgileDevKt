package com.lodz.android.pandora.widget.search

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

/**
 * 推荐数据接口
 * @author zhouL
 * @date 2019/10/25
 */
interface RecomdData {

    /** item背景 */
    @DrawableRes
    fun getItemBackgroundDrawableRes(): Int = 0

    /** item背景 */
    @ColorInt
    fun getItemBackgroundColor(): Int = 0

    /** item高度 */
    fun getItemHigthPx(): Int = 0

    /** 左侧图标资源 */
    @DrawableRes
    fun getIconDrawableRes(): Int = 0

    /** 左侧图标显隐 */
    fun getIconVisibility(): Int = View.VISIBLE

    /** 关键字 */
    fun getTitleText(): String

    /** 关键字颜色 */
    @ColorInt
    fun getTitleTextColor(): Int = 0

    /** 关键字大小（单位SP） */
    fun getTitleTextSize(): Float = 0f

    /** 描述文字 */
    fun getDescText(): String = ""

    /** 描述文字颜色 */
    @ColorInt
    fun getDescTextColor(): Int = 0

    /** 描述文字大小（单位SP） */
    fun getDescTextSize(): Float = 0f

    /** 提示文字 */
    fun getTipsText(): String = ""

    /** 提示文字颜色 */
    @ColorInt
    fun getTipsTextColor(): Int = 0

    /** 提示文字大小（单位SP） */
    fun getTipsTextSize(): Float = 0f

    /** 标题标签 */
    fun getTitleTagText(): String = ""

    /** 标题标签文字颜色 */
    @ColorInt
    fun getTitleTagTextColor(): Int = 0

    /** 标题标签文字大小（单位SP） */
    fun getTitleTagTextSize(): Float = 0f

    /** 标题标签背景 */
    @DrawableRes
    fun getTitleTagTextBackground(): Int = 0

    /** 第一标签 */
    fun getFirstTagText(): String = ""

    /** 第一标签文字颜色 */
    @ColorInt
    fun geFirstTagTextColor(): Int = 0

    /** 第一标签文字大小（单位SP） */
    fun getFirstTagTextSize(): Float = 0f

    /** 第一标签背景 */
    @DrawableRes
    fun getFirstTagTextBackground(): Int = 0

    /** 第二标签 */
    fun getSecondTagText(): String = ""

    /** 第二标签文字颜色 */
    @ColorInt
    fun getSecondTagTextColor(): Int = 0

    /** 第二标签文字大小（单位SP） */
    fun getSecondTagTextSize(): Float = 0f

    /** 第二标签背景 */
    @DrawableRes
    fun getSecondTagTextBackground(): Int = 0

    /** 分割线颜色 */
    @ColorInt
    fun getDivideLineColor(): Int = 0

    /** 分割线显隐 */
    fun getDivideLineVisibility(): Int = View.VISIBLE
}