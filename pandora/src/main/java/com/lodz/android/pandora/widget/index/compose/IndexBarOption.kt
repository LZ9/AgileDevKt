package com.lodz.android.pandora.widget.index.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.lodz.android.pandora.widget.base.compose.base.Orientation

/**
 * 索引栏配置选项
 * @author zhouL
 * @date 2026/5/21
 */
@Immutable
data class IndexBarOption(
    /** 索引文字颜色 */
    val indexTextColor: Color = Color.DarkGray,
    /** 索引文字大小 */
    val indexTextSize: TextUnit = 13.sp,
    /** 索引文字大小 */
    val indexTextWeight: FontWeight = FontWeight.Bold,
    /** 索引栏间距 */
    val padding: PaddingValues? = null,
    /** 索引栏的背景颜色 */
    val backgroundColor: Color = Color.Transparent,
    /** 按下时索引栏的背景颜色 */
    val pressBackgroundColor: Color? = null,
    /** 页面方向布局 */
    val orientation: Orientation = Orientation.Vertical,
)