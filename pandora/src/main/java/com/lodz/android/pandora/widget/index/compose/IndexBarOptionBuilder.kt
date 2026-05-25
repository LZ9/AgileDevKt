package com.lodz.android.pandora.widget.index.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.lodz.android.pandora.widget.base.compose.base.Orientation

/**
 * 索引栏配置选项构建器
 * @author zhouL
 * @date 2026/5/21
 */
class IndexBarOptionBuilder(option: IndexBarOption? = null)  {

    /** 索引文字颜色 */
    var indexTextColor: Color = option?.indexTextColor ?: Color.DarkGray
    /** 索引文字大小 */
    var indexTextSize: TextUnit = option?.indexTextSize ?: 13.sp
    /** 索引文字大小 */
    var indexTextWeight: FontWeight = option?.indexTextWeight ?: FontWeight.Bold
    /** 索引栏间距 */
    var padding: PaddingValues? = option?.padding
    /** 索引栏的背景颜色 */
    var backgroundColor: Color = option?.backgroundColor ?: Color.Transparent
    /** 按下时索引栏的背景颜色 */
    var pressBackgroundColor: Color? = option?.pressBackgroundColor
    /** 页面方向布局 */
    var orientation: Orientation = option?.orientation ?: Orientation.Vertical

    /** 构建IndexBarOption，上下文[context] */
    fun build(): IndexBarOption {
        return IndexBarOption(
            indexTextColor = indexTextColor,

            indexTextSize = indexTextSize,

            indexTextWeight = indexTextWeight,

            padding = padding,

            backgroundColor = backgroundColor,

            pressBackgroundColor = pressBackgroundColor,

            orientation = orientation,
        )
    }

    /** 更新IndexBarOption */
    fun update(): IndexBarOption {
        return IndexBarOption(
            indexTextColor = indexTextColor,

            indexTextSize = indexTextSize,

            indexTextWeight = indexTextWeight,

            padding = padding,

            backgroundColor = backgroundColor,

            pressBackgroundColor = pressBackgroundColor,

            orientation = orientation,
        )
    }
}

/** 创建IndexBarOption对象 */
fun IndexBarOptionCreate(block: IndexBarOptionBuilder.() -> Unit): IndexBarOption = IndexBarOptionBuilder().apply(block).build()

/** 更新IndexBarOption对象 */
fun IndexBarOption.IndexBarOptionUpdate(block: IndexBarOptionBuilder.() -> Unit): IndexBarOption = IndexBarOptionBuilder(this).apply(block).update()