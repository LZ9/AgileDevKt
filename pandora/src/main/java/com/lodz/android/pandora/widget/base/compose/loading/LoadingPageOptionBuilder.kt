package com.lodz.android.pandora.widget.base.compose.loading

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lodz.android.corekt.anko.px2dp
import com.lodz.android.pandora.anko.getComposeColor
import com.lodz.android.pandora.base.application.BaseApplication
import com.lodz.android.pandora.base.application.config.LoadingLayoutConfig
import com.lodz.android.pandora.widget.base.compose.base.Orientation
import kotlin.Boolean


/**
 * 加载页配置选项构建器
 * @author zhouL
 * @date 2026/5/19
 */
class LoadingPageOptionBuilder(option: LoadingPageOption? = null) {

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

    /** 不确定模式 */
    var isIndeterminate: Boolean? = option?.isIndeterminate

    /** 不确定模式下的动画资源 */
    @DrawableRes
    var indeterminateDrawableResId: Int? = option?.indeterminateDrawableResId

    /** 不确定模式下的帧列表 */
    var indeterminateDrawable: List<Pair<Drawable, Int>>? = option?.indeterminateDrawable

    /** 使用系统默认加载资源 */
    var useSysDefDrawable: Boolean? = option?.useSysDefDrawable

    /** 页面方向布局 */
    var orientation: Orientation? = option?.orientation

    /** 进度条高度 */
    var pbHeight: Dp? = option?.pbHeight

    /** 进度条宽度 */
    var pbWidth: Dp? = option?.pbWidth

    /** 进度值 */
    var progress: Float? = option?.progress

    /** 构建LoadingPageOption，上下文[context] */
    fun build(context: Context): LoadingPageOption {
        // 获取Application里面的标题栏全局配置参数
        val config = BaseApplication.get()?.getBaseLayoutConfig()?.getLoadingLayoutConfig() ?: LoadingLayoutConfig()

        return LoadingPageOption(
            isNeedTips = isNeedTips ?: config.isNeedTips,

            tips = tips,

            textColor = textColor ?: if (config.textColor != 0) context.getComposeColor(config.textColor) else null,

            textSize = textSize ?: if (config.textSize != 0) config.textSize.sp else null,

            backgroundResId = backgroundResId,

            backgroundScale = backgroundScale,

            backgroundAlignment = backgroundAlignment,

            backgroundColor = backgroundColor ?: if (config.backgroundColor != 0) context.getComposeColor(config.backgroundColor) else null,

            backgroundOverlay = backgroundOverlay,

            isIndeterminate = isIndeterminate ?: config.isIndeterminate,

            indeterminateDrawableResId = indeterminateDrawableResId ?: if (config.indeterminateDrawable != 0) config.indeterminateDrawable else null,

            indeterminateDrawable = indeterminateDrawable,

            useSysDefDrawable = useSysDefDrawable ?: config.useSysDefDrawable,

            orientation = orientation ?: if (config.orientation == LinearLayout.VERTICAL) Orientation.Vertical else Orientation.Horizontal,

            pbHeight = pbHeight ?: context.px2dp(config.pbHeightPx).dp,

            pbWidth = pbWidth ?: context.px2dp(config.pbWidthPx).dp,

            progress = progress
        )
    }

    /** 更新LoadingPageOption */
    fun update(): LoadingPageOption {
        return LoadingPageOption(
            isNeedTips = isNeedTips ?: true,

            tips = tips,

            textColor = textColor,

            textSize = textSize,

            backgroundResId = backgroundResId,

            backgroundScale = backgroundScale,

            backgroundAlignment = backgroundAlignment,

            backgroundColor = backgroundColor,

            backgroundOverlay = backgroundOverlay,

            isIndeterminate = isIndeterminate ?: true,

            indeterminateDrawableResId = indeterminateDrawableResId,

            indeterminateDrawable = indeterminateDrawable,

            useSysDefDrawable = useSysDefDrawable ?: true,

            orientation = orientation ?: Orientation.Vertical,

            pbHeight = pbHeight ?: 0.dp,

            pbWidth = pbWidth?: 0.dp,

            progress = progress
        )
    }
}

/** 创建LoadingPageOption对象 */
fun Context.loadingPageOptionCreate(block: LoadingPageOptionBuilder.() -> Unit): LoadingPageOption = LoadingPageOptionBuilder().apply(block).build(this)

/** 更新LoadingPageOption对象 */
fun LoadingPageOption.loadingPageOptionUpdate(block: LoadingPageOptionBuilder.() -> Unit): LoadingPageOption = LoadingPageOptionBuilder(this).apply(block).update()