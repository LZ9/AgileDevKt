package com.lodz.android.pandora.widget.base.compose

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

/**
 * TitleBar配置选项
 * @author zhouL
 * @date 2026/5/12
 */
@Immutable
data class TitleBarOption(
    /** 是否需要显示返回按钮 */
    val isNeedBackBtn: Boolean = true,
    /** 替换默认的返回按钮资源图片 */
    val backBtnDrawable: Drawable? = null,
    /** 返回按钮文字 */
    val backBtnText: String = "",
    /** 返回按钮文字颜色 */
    val backBtnTextColor: Color? = null,
    /** 返回按钮文字大小（单位sp） */
    val backBtnTextSize: TextUnit? = null,
    /** 标题文字 */
    val titleText: String = "",
    /** 标题文字颜色 */
    val titleTextColor: Color? = null,
    /** 标题文字大小（单位sp） */
    val titleTextSize: TextUnit? = null,
    /** 是否显示分割线 */
    val isShowDivideLine: Boolean = false,
    /** 分割线颜色 */
    val divideLineColor: Color? = null,
    /** 分割线高度（单位dp） */
    val divideLineHeight: Dp = 1.dp,
    /** 背景资源图片 */
    val backgroundResId: Int? = null,
    /** 背景资源图片缩放策略 */
    val backgroundScale: ContentScale = ContentScale.Inside,
    /** 背景资源图片对齐方式 */
    val backgroundAlignment: Alignment = Alignment.Center,
    /** 背景颜色 */
    val backgroundColor: Color? = null,
    /** 是否需要阴影 */
    val isNeedElevation: Boolean = true,
    /** 阴影的值 */
    val elevationVale: Float = 12f,

    // TODO: 2026/5/13 还有左右侧扩展区、返回按钮回调
)