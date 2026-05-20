package com.lodz.android.pandora.widget.base.compose.nodata

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
import com.lodz.android.pandora.base.application.config.NoDataLayoutConfig
import com.lodz.android.pandora.widget.base.compose.base.Orientation

/**
 * 无数据页配置选项构建器
 * @author zhouL
 * @date 2026/5/20
 */
class NoDataPageOptionBuilder(option: NoDataPageOption? = null)  {

    /** 需要提示图片 */
    var isNeedImg: Boolean? = option?.isNeedImg

    /** 无数据图片 */
    @DrawableRes
    var imgResId: Int? = option?.imgResId

    /** 无数据图片大小 */
    var imgSize: Dp = option?.imgSize ?: 200.dp

    /** 无数据图片缩放策略 */
    var imgScale: ContentScale = option?.imgScale ?: ContentScale.Fit

    /** 无数据图片对齐方式 */
    var imgAlignment: Alignment = option?.imgAlignment ?: Alignment.Center

    /** 是否需要提示文字 */
    var isNeedTips: Boolean? = option?.isNeedTips

    /** 提示文字 */
    var tips: String = option?.tips ?: ""

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


    /** 构建LoadingPageOption，上下文[context] */
    fun build(context: Context): NoDataPageOption {
        // 获取Application里面的标题栏全局配置参数
        val config = BaseApplication.get()?.getBaseLayoutConfig()?.getNoDataLayoutConfig() ?: NoDataLayoutConfig()

        return NoDataPageOption(
            isNeedImg = isNeedImg ?: config.isNeedImg,

            imgResId = imgResId ?: config.drawableResId,

            imgSize = imgSize,

            imgScale = imgScale,

            imgAlignment = imgAlignment,

            isNeedTips = isNeedTips ?: config.isNeedTips,

            tips = tips,

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

    /** 更新LoadingPageOption */
    fun update(): NoDataPageOption {
        return NoDataPageOption(
            isNeedImg = isNeedImg ?: true,

            imgResId = imgResId,

            imgSize = imgSize,

            imgScale = imgScale,

            imgAlignment = imgAlignment,

            isNeedTips = isNeedTips ?: true,

            tips = tips,

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

/** 创建NoDataPageOption对象 */
fun Context.noDataPageOptionCreate(block: NoDataPageOptionBuilder.() -> Unit): NoDataPageOption = NoDataPageOptionBuilder().apply(block).build(this)

/** 更新NoDataPageOption对象 */
fun NoDataPageOption.noDataPageOptionUpdate(block: NoDataPageOptionBuilder.() -> Unit): NoDataPageOption = NoDataPageOptionBuilder(this).apply(block).update()