package com.lodz.android.pandora.widget.base.compose.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

/**
 * 加载页配置选项
 * @author zhouL
 * @date 2026/5/19
 */
@Immutable
data class LoadingPageOption(
    /** 是否需要提示文字 */
    val isNeedTips: Boolean = true,
    /** 提示文字 */
    val tips: String = "",
    /** 文字颜色 */
    val textColor: Color? = null,
    /** 文字大小 */
    val textSize: TextUnit? = null,
    /** 背景资源图片 */
    val backgroundResId: Int? = null,
    /** 背景资源图片缩放策略 */
    val backgroundScale: ContentScale = ContentScale.Inside,
    /** 背景资源图片对齐方式 */
    val backgroundAlignment: Alignment = Alignment.Center,
    /** 背景颜色 */
    val backgroundColor: Color? = null,
    /** 是否由用户在外部自定义背景色 */
    val backgroundOverlay: Boolean = false,
    /** 不确定模式 */
    val isIndeterminate: Boolean = true,
    /** 不确定模式下的资源 */
    val indeterminateDrawableResId: Int? = null,
    /** 使用系统默认加载资源 */
    val useSysDefDrawable: Boolean = true,
    /** 页面子元素垂直排列方式 */
    val verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    /** 页面子元素水平排列方式 */
    val horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    /** 进度条高度 */
    val pbHeight: Dp? = null,
    /** 进度条宽度 */
    val pbWidth: Dp? = null,
)