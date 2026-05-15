package com.lodz.android.pandora.widget.base.compose

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lodz.android.pandora.anko.getComposeColor
import com.lodz.android.pandora.base.application.BaseApplication
import com.lodz.android.pandora.base.application.config.TitleBarLayoutConfig

/**
 * TitleBar配置选项构建器
 * @author zhouL
 * @date 2026/5/12
 */
class TitleBarOptionBuilder {

    /** 是否保留状态栏间距 */
    var isPaddingStatusBar: Boolean = true

    /** 是否需要返回按钮 */
    var isNeedBackBtn: Boolean? = null

    /** 返回按钮资源图片 */
    @DrawableRes
    var backBtnResId: Int? = null

    /** 返回按钮按下资源图片 */
    @DrawableRes
    var backBtnSelResId: Int? = null

    /** 返回按钮文字 */
    var backText: String? = null

    /** 返回按钮文字颜色 */
    var backBtnTextColor: Color? = null

    /** 返回按钮文字大小（单位sp） */
    var backBtnTextSize: TextUnit? = null

    /** 标题文字 */
    var titleText: String = ""

    /** 标题文字颜色 */
    var titleTextColor: Color? = null

    /** 标题文字大小（单位sp） */
    var titleTextSize: TextUnit? = null

    /** 是否显示分割线 */
    var isShowDivideLine: Boolean? = null

    /** 分割线背景色 */
    var divideLineColor: Color? = null

    /** 分割线高度 */
    var divideLineHeight: Dp? = null

    /** 背景资源图片 */
    @DrawableRes
    var backgroundResId: Int? = null

    /** 背景资源图片缩放策略 */
    var backgroundScale: ContentScale = ContentScale.Inside

    /** 背景资源图片对齐方式 */
    var backgroundAlignment: Alignment = Alignment.Center

    /** 背景颜色 */
    var backgroundColor: Color? = null

    /** 是否由用户在外部自定义背景色 */
    var backgroundOverlay: Boolean = false

    /** 是否需要阴影 */
    var isNeedElevation: Boolean? = null

    /** 阴影取值 */
    var elevationVale: Float? = null


    fun build(context: Context): TitleBarOption {
        // 获取Application里面的标题栏全局配置参数
        val config = BaseApplication.get()?.getBaseLayoutConfig()?.getTitleBarLayoutConfig() ?: TitleBarLayoutConfig()

        return TitleBarOption(
            isPaddingStatusBar = isPaddingStatusBar,

            isNeedBackBtn = isNeedBackBtn ?: config.isNeedBackBtn,

            backBtnResId = backBtnResId ?: if (config.backBtnResId != 0) config.backBtnResId else null,

            backBtnSelResId = backBtnSelResId ?: if (config.backBtnResId != 0) config.backBtnResId else null,

            backBtnText = backText ?: config.backBtnText,

            backBtnTextColor = backBtnTextColor ?: if (config.backBtnTextColor != 0) context.getComposeColor(config.backBtnTextColor) else null,

            backBtnTextSize = backBtnTextSize ?: if (config.backBtnTextSize != 0) config.backBtnTextSize.sp else null,

            titleText = titleText,

            titleTextColor = titleTextColor ?: if (config.titleTextColor != 0) context.getComposeColor(config.titleTextColor) else null,

            titleTextSize = titleTextSize ?: if (config.titleTextSize != 0) config.titleTextSize.sp else null,

            isShowDivideLine = isShowDivideLine ?: config.isShowDivideLine,

            divideLineColor = divideLineColor ?: if (config.divideLineColor != 0) context.getComposeColor(config.divideLineColor) else null,

            divideLineHeight = divideLineHeight ?: if (config.divideLineHeightDp != 0) config.divideLineHeightDp.dp else 1.dp,

            backgroundResId = backgroundResId ?: if (config.backgroundResId != 0) config.backgroundResId else null,

            backgroundScale = backgroundScale,

            backgroundAlignment = backgroundAlignment,

            backgroundColor = backgroundColor ?: if (config.backgroundColor != 0) context.getComposeColor(config.backgroundColor) else null,

            backgroundOverlay = backgroundOverlay,

            isNeedElevation = isNeedElevation ?: config.isNeedElevation,

            elevationVale = elevationVale ?: config.elevationVale

            )
    }

    fun update(option: TitleBarOption): TitleBarOption {
        return option.copy(
            titleText = titleText
        )
    }
}

fun Context.titleBarOption(block: TitleBarOptionBuilder.() -> Unit): TitleBarOption = TitleBarOptionBuilder().apply(block).build(this)