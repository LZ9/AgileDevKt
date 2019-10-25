package com.lodz.android.pandora.widget.search

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

/**
 * 推荐数据接口
 * @author zhouL
 * @date 2019/10/25
 */
interface RecomdData {

    /** 左侧图标资源 */
    @DrawableRes
    fun getIconDrawableRes(): Int

    /** 关键字 */
    fun getTitleText(): String

    /** 关键字颜色 */
    @ColorInt
    fun getTitleTextColor(): Int

    /** 关键字大小（单位SP） */
    fun getTitleTextSize(): Float

    /** 描述文字 */
    fun getDescText(): String

    /** 描述文字颜色 */
    @ColorInt
    fun getDescTextColor(): Int

    /** 描述文字大小（单位SP） */
    fun getDescTextSize(): Float

    /** 提示文字 */
    fun getTipsText(): String

    /** 提示文字颜色 */
    @ColorInt
    fun getTipsTextColor(): Int

    /** 提示文字大小（单位SP） */
    fun getTipsTextSize(): Float

    /** 标题标签 */
    fun getTitleTagText(): String

    /** 标题标签文字颜色 */
    @ColorInt
    fun getTitleTagTextColor(): Int

    /** 标题标签文字大小（单位SP） */
    fun getTitleTagTextSize(): Float

    /** 标题标签背景 */
    @DrawableRes
    fun getTitleTagTextBackground(): Int

    /** 第一标签 */
    fun getFirstTagText(): String

    /** 第一标签文字颜色 */
    @ColorInt
    fun geFirstTagTextColor(): Int

    /** 第一标签文字大小（单位SP） */
    fun getFirstTagTextSize(): Float

    /** 第一标签背景 */
    @DrawableRes
    fun getFirstTagTextBackground(): Int

    /** 第二标签 */
    fun getSecondTagText(): String

    /** 第二标签文字颜色 */
    @ColorInt
    fun geSecondTagTextColor(): Int

    /** 第二标签文字大小（单位SP） */
    fun getSecondTagTextSize(): Float

    /** 第二标签背景 */
    @DrawableRes
    fun getSecondTagTextBackground(): Int

}