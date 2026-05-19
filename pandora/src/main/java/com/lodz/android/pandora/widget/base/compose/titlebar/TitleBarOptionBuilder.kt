package com.lodz.android.pandora.widget.base.compose.titlebar

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
class TitleBarOptionBuilder(option: TitleBarOption? = null) {

    /** 是否保留状态栏间距 */
    var isPaddingStatusBar: Boolean = option?.isPaddingStatusBar ?: true

    /** 是否需要返回按钮 */
    var isNeedBackBtn: Boolean? = option?.isNeedBackBtn

    /** 返回按钮资源图片 */
    @DrawableRes
    var backBtnResId: Int? = option?.backBtnResId

    /** 返回按钮按下资源图片 */
    @DrawableRes
    var backBtnSelResId: Int? = option?.backBtnSelResId

    /** 返回按钮文字 */
    var backBtnText: String? = option?.backBtnText

    /** 返回按钮文字颜色 */
    var backBtnTextColor: Color? = option?.backBtnTextColor

    /** 返回按钮文字大小（单位sp） */
    var backBtnTextSize: TextUnit? = option?.backBtnTextSize

    /** 标题文字 */
    var titleText: String = option?.titleText ?: ""

    /** 标题文字颜色 */
    var titleTextColor: Color? = option?.titleTextColor

    /** 标题文字大小（单位sp） */
    var titleTextSize: TextUnit? = option?.titleTextSize

    /** 是否显示分割线 */
    var isShowDivideLine: Boolean? = option?.isShowDivideLine

    /** 分割线背景色 */
    var divideLineColor: Color? = option?.divideLineColor

    /** 分割线高度 */
    var divideLineHeight: Dp? = option?.divideLineHeight

    /** 背景资源图片 */
    @DrawableRes
    var backgroundResId: Int? = option?.backgroundResId

    /** 背景资源图片缩放策略 */
    var backgroundScale: ContentScale = option?.backgroundScale ?: ContentScale.Inside

    /** 背景资源图片对齐方式 */
    var backgroundAlignment: Alignment = option?.backgroundAlignment ?: Alignment.Center

    /** 背景颜色 */
    var backgroundColor: Color? = option?.backgroundColor

    /** 是否由用户在外部自定义背景色 */
    var backgroundOverlay: Boolean = option?.backgroundOverlay ?: false

    /** 是否需要阴影 */
    var isNeedElevation: Boolean? = option?.isNeedElevation

    /** 阴影取值 */
    var elevationVale: Float? = option?.elevationVale


    /** 构建TitleBarOption，上下文[context] */
    fun build(context: Context): TitleBarOption {
        // 获取Application里面的标题栏全局配置参数
        val config = BaseApplication.get()?.getBaseLayoutConfig()?.getTitleBarLayoutConfig() ?: TitleBarLayoutConfig()

        return TitleBarOption(
            isPaddingStatusBar = isPaddingStatusBar,

            isNeedBackBtn = isNeedBackBtn ?: config.isNeedBackBtn,

            backBtnResId = backBtnResId ?: if (config.backBtnResId != 0) config.backBtnResId else null,

            backBtnSelResId = backBtnSelResId ?: if (config.backBtnResId != 0) config.backBtnResId else null,

            backBtnText = backBtnText ?: config.backBtnText,

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

    /** 更新TitleBarOption */
    fun update(): TitleBarOption {
        return TitleBarOption(
            isPaddingStatusBar = isPaddingStatusBar,

            isNeedBackBtn = isNeedBackBtn ?: true,

            backBtnResId = backBtnResId,

            backBtnSelResId = backBtnSelResId,

            backBtnText = backBtnText ?: "",

            backBtnTextColor = backBtnTextColor ,

            backBtnTextSize = backBtnTextSize ,

            titleText = titleText,

            titleTextColor = titleTextColor,

            titleTextSize = titleTextSize,

            isShowDivideLine = isShowDivideLine ?: false,

            divideLineColor = divideLineColor,

            divideLineHeight = divideLineHeight ?: 1.dp,

            backgroundResId = backgroundResId,

            backgroundScale = backgroundScale,

            backgroundAlignment = backgroundAlignment,

            backgroundColor = backgroundColor,

            backgroundOverlay = backgroundOverlay,

            isNeedElevation = isNeedElevation ?: true,

            elevationVale = elevationVale ?: 12f
        )
    }
}

/** 创建TitleBarOption对象 */
fun Context.titleBarOptionCreate(block: TitleBarOptionBuilder.() -> Unit): TitleBarOption = TitleBarOptionBuilder().apply(block).build(this)

/** 更新TitleBarOption对象 */
fun TitleBarOption.titleBarOptionUpdate(block: TitleBarOptionBuilder.() -> Unit): TitleBarOption = TitleBarOptionBuilder(this).apply(block).update()