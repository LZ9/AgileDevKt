package com.lodz.android.pandora.widget.index.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.lodz.android.pandora.widget.base.compose.base.Orientation

/**
 * 索引栏
 * @author zhouL
 * @date 2026/5/21
 */
@Composable
fun IndexBar(
    modifier: Modifier = Modifier,
    option: IndexBarOption = IndexBarOption(), // 配置项
    indexList: List<String>, // 索引数据
    onIndexSelected: ((position: Int, indexText: String) -> Unit)? = null,
    onDragEnd: (() -> Unit)? = null
) {

    // 当前触摸选中的索引位置
    var selectedIndex by remember { mutableIntStateOf(-1) }

    // 整个IndexBar尺寸
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    // 是否正在触摸
    var isTouching by remember { mutableStateOf(false) }


    // 计算索引值
    fun calculateIndex(offset: Offset): Int {
        if (indexList.isEmpty()) return -1
        return if (option.orientation == Orientation.Vertical) {
            val itemHeight = containerSize.height.toFloat() / indexList.size
            (offset.y / itemHeight).toInt().coerceIn(0, indexList.lastIndex)
        } else {
            val itemWidth = containerSize.width.toFloat() / indexList.size
            (offset.x / itemWidth).toInt().coerceIn(0, indexList.lastIndex)
        }
    }

    // 回调索引
    fun notifyIndex(offset: Offset) {
        val index = calculateIndex(offset)
        if (index == -1) return
        if (index != selectedIndex) {
            selectedIndex = index
            onIndexSelected?.invoke(index, indexList[index])
        }
    }

    Box(
        modifier = modifier
            .background(if (isTouching) (option.pressBackgroundColor ?: option.backgroundColor) else option.backgroundColor)
            .padding(option.padding ?: PaddingValues.Zero)
            .onSizeChanged {
                containerSize = it
            }
            .pointerInput(Unit) {
                awaitEachGesture {
                    // 按下立即触发
                    val down = awaitFirstDown()
                    isTouching = true
                    notifyIndex(down.position)
                    do {
                        val event = awaitPointerEvent()
                        val change = event.changes.first()
                        // 移动持续触发
                        notifyIndex(change.position)
                    } while (change.pressed)
                    // 抬起
                    selectedIndex = -1
                    isTouching = false
                    onDragEnd?.invoke()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (option.orientation == Orientation.Vertical) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) { IndexBarContainer(option, indexList, selectedIndex) }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) { IndexBarContainer(option, indexList, selectedIndex) }
        }
    }
}

@Composable
private fun IndexBarContainer(option: IndexBarOption, indexList: List<String>, selectedIndex: Int) {
    indexList.forEachIndexed { index, text ->
        val isSelected = index == selectedIndex
        Text(
            modifier = if (option.orientation == Orientation.Vertical) Modifier.fillMaxWidth() else Modifier.fillMaxHeight(),
            text = text,
            color = option.indexTextColor,
            fontSize = option.indexTextSize,
            fontWeight = option.indexTextWeight,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(device = Devices.NEXUS_6, showSystemUi = true, locale = "zh")
@Composable
fun UIPreviewVertical() {
    val context = LocalContext.current
    val indexList = arrayListOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
        "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"
    )

    Box(Modifier.fillMaxSize()) {
        IndexBar(
            modifier = Modifier
                .fillMaxHeight()
                .width(24.dp)
                .align(Alignment.CenterEnd),
            indexList = indexList,
            option = IndexBarOptionCreate {
                orientation = Orientation.Vertical
                padding = PaddingValues(top = 5.dp, bottom = 5.dp)
                backgroundColor = Color.Yellow
            }
        )
    }
}

@Preview(device = Devices.NEXUS_6, showSystemUi = true, locale = "zh")
@Composable
fun UIPreviewHorizontal() {
    val context = LocalContext.current
    val indexList = arrayListOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
        "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"
    )

    Box(Modifier.fillMaxSize()) {
        IndexBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .align(Alignment.TopCenter),
            indexList = indexList,
            option = IndexBarOptionCreate {
                orientation = Orientation.Horizontal
                padding = PaddingValues(start = 5.dp, end = 5.dp)
                backgroundColor = Color.Yellow
            }
        )
    }


}