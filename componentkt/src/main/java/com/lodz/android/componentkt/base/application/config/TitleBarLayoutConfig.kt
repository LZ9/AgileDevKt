package com.lodz.android.componentkt.base.application.config

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

/**
 * 标题栏配置
 * Created by zhouL on 2018/6/27.
 */
class TitleBarLayoutConfig {

    /** 是否需要显示返回按钮 */
    var isNeedBackBtn = true
    /** 替换默认的返回按钮资源图片 */
    @DrawableRes
    var backBtnResId = 0
    /** 返回按钮文字 */
    var backBtnText = ""
    /** 返回按钮文字颜色 */
    @ColorRes
    var backBtnTextColor = 0
    /** 返回按钮文字大小（单位sp） */
    var backBtnTextSize = 0
    /** 标题文字颜色 */
    @ColorRes
    var titleTextColor = 0
    /** 标题文字大小（单位sp） */
    var titleTextSize = 0
    /** 是否显示分割线 */
    var isShowDivideLine = false
    /** 分割线颜色 */
    @ColorRes
    var divideLineColor = 0
    /** 分割线高度（单位dp） */
    var divideLineHeightDp = 0
    /** 背景颜色 */
    @ColorRes
    var backgroundColor = 0
    /** 背景资源图片 */
    @DrawableRes
    var backgroundResId = 0
    /** 是否需要阴影 */
    var isNeedElevation = true
    /** 阴影的值 */
    var elevationVale = 12f


}