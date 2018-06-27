package com.lodz.android.componentkt.base.application.config

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.widget.LinearLayout

/**
 * 错误页面配置
 * Created by zhouL on 2018/6/27.
 */
class ErrorLayoutConfig {

    /** 需要提示图片  */
    var isNeedImg = true
    /** 需要提示文字  */
    var isNeedTips = false
    /** 错误图片  */
    @DrawableRes
    var drawableResId = 0
    /** 提示文字  */
    var tips = ""
    /** 文字颜色  */
    @ColorRes
    var textColor = 0
    /** 文字大小  */
    var textSize = 0
    /** 背景色  */
    @ColorRes
    var backgroundColor = 0
    /** 页面方向布局  */
    var orientation = LinearLayout.VERTICAL
        set(value) {
            if (value == LinearLayout.HORIZONTAL || value == LinearLayout.VERTICAL) {
                field = value
            }
        }
}