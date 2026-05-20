package com.lodz.android.pandora.widget.base.compose.nodata

import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.lodz.android.pandora.widget.base.compose.base.Orientation

/**
 * 无数据页配置选项
 * @author zhouL
 * @date 2026/5/20
 */
@Immutable
data class NoDataPageOption(
    /** 需要提示图片 */
    val isNeedImg: Boolean = true,
    /** 无数据图片 */
    val imgResId: Int? = null,
    /** 无数据图片大小 */
    val imgSize: Dp = 200.dp,
    /** 无数据图片缩放策略 */
    val imgScale: ContentScale = ContentScale.Fit,
    /** 无数据图片对齐方式 */
    val imgAlignment: Alignment = Alignment.Center,
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
    /** 页面方向布局 */
    val orientation: Orientation = Orientation.Vertical,
)