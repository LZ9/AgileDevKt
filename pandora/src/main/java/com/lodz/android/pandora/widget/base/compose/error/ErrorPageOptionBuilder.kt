package com.lodz.android.pandora.widget.base.compose.error

import android.content.Context
import android.widget.LinearLayout
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
import com.lodz.android.pandora.base.application.config.ErrorLayoutConfig
import com.lodz.android.pandora.widget.base.compose.base.Orientation


/**
 * 错误页配置选项构建器
 * @author zhouL
 * @date 2026/5/20
 */
class ErrorPageOptionBuilder(option: ErrorPageOption? = null) {

    /** 需要提示图片 */
    var isNeedImg: Boolean? = option?.isNeedImg

    /** 错误图片 */
    @DrawableRes
    var imgResId: Int? = option?.imgResId

    /** 网络错误图片 */
    @DrawableRes
    var imgNetResId: Int? = option?.imgNetResId

    /** 错误图片大小 */
    var imgSize: Dp = option?.imgSize ?: 200.dp

    /** 错误图片缩放策略 */
    var imgScale: ContentScale = option?.imgScale ?: ContentScale.Fit

    /** 错误图片对齐方式 */
    var imgAlignment: Alignment = option?.imgAlignment ?: Alignment.Center

    /** 是否需要提示文字 */
    var isNeedTips: Boolean? = option?.isNeedTips

    /** 提示文字 */
    var tips: String = option?.tips ?: ""

    /** 网络异常提示文字 */
    var netTips: String = option?.netTips ?: ""

    /** 文字颜色 */
    var textColor: Color? = option?.textColor

    /** 文字大小 */
    var textSize: TextUnit? = option?.textSize

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

    /** 页面方向布局 */
    var orientation: Orientation? = option?.orientation


    /** 构建ErrorPageOption，上下文[context] */
    fun build(context: Context): ErrorPageOption {
        // 获取Application里面的标题栏全局配置参数
        val config = BaseApplication.get()?.getBaseLayoutConfig()?.getErrorLayoutConfig() ?: ErrorLayoutConfig()

        return ErrorPageOption(
            isNeedImg = isNeedImg ?: config.isNeedImg,

            imgResId = imgResId ?: config.drawableResId,

            imgNetResId = imgNetResId,

            imgSize = imgSize,

            imgScale = imgScale,

            imgAlignment = imgAlignment,

            isNeedTips = isNeedTips ?: config.isNeedTips,

            tips = tips,

            netTips = netTips,

            textColor = textColor ?: if (config.textColor != 0) context.getComposeColor(config.textColor) else null,

            textSize = textSize ?: if (config.textSize != 0) config.textSize.sp else null,

            backgroundResId = backgroundResId,

            backgroundScale = backgroundScale,

            backgroundAlignment = backgroundAlignment,

            backgroundColor = backgroundColor ?: if (config.backgroundColor != 0) context.getComposeColor(config.backgroundColor) else null,

            backgroundOverlay = backgroundOverlay,

            orientation = orientation ?: if (config.orientation == LinearLayout.VERTICAL) Orientation.Vertical else Orientation.Horizontal,
        )
    }

    /** 更新ErrorPageOption */
    fun update(): ErrorPageOption {
        return ErrorPageOption(
            isNeedImg = isNeedImg ?: true,

            imgResId = imgResId,

            imgNetResId = imgNetResId,

            imgSize = imgSize,

            imgScale = imgScale,

            imgAlignment = imgAlignment,

            isNeedTips = isNeedTips ?: true,

            tips = tips,

            netTips = netTips,

            textColor = textColor,

            textSize = textSize,

            backgroundResId = backgroundResId,

            backgroundScale = backgroundScale,

            backgroundAlignment = backgroundAlignment,

            backgroundColor = backgroundColor,

            backgroundOverlay = backgroundOverlay,

            orientation = orientation ?: Orientation.Vertical,
        )
    }
}

/** 创建ErrorPageOption对象 */
fun Context.errorPageOptionCreate(block: ErrorPageOptionBuilder.() -> Unit): ErrorPageOption = ErrorPageOptionBuilder().apply(block).build(this)

/** 更新ErrorPageOption对象 */
fun ErrorPageOption.errorPageOptionUpdate(block: ErrorPageOptionBuilder.() -> Unit): ErrorPageOption = ErrorPageOptionBuilder(this).apply(block).update()