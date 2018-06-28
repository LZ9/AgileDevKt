package com.lodz.android.componentkt.base.application.config

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.widget.LinearLayout

/**
 * 加载页面配置
 * Created by zhouL on 2018/6/27.
 */
class LoadingLayoutConfig {

    /** 是否需要提示文字  */
    var isNeedTips = true
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
    /** 不确定模式  */
    var isIndeterminate = true
    /** 不确定模式下的资源  */
    @DrawableRes
    var indeterminateDrawable = 0
    /** 页面方向布局  */
    var orientation = LinearLayout.VERTICAL
        set(value) {
            if (value == LinearLayout.HORIZONTAL || value == LinearLayout.VERTICAL) {
                field = value
            }
        }
    /** 进度条高度  */
    var pbHeightPx = 0
    /** 进度条宽度  */
    var pbWidthPx = 0
}