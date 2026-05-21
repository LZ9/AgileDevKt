package com.lodz.android.pandora.widget.index.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
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
    onStart: ((position: Int, indexText: String) -> Unit)? = null, // 开始触摸索引回调
    onEnd: (() -> Unit)? = null  // 结束触摸索引回调
) {

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (option.orientation == Orientation.Vertical) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) { IndexBarContainer(option, indexList) }

        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) { IndexBarContainer(option, indexList) }
        }
    }
}

@Composable
private fun IndexBarContainer(option: IndexBarOption, indexList: List<String>) {
    indexList.forEachIndexed { index, text ->
        Text(
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
                .align(Alignment.CenterEnd)
                .background(Color.Yellow)
                .padding(top = 5.dp, bottom = 5.dp),
            indexList = indexList,
            option = IndexBarOptionCreate {
                orientation = Orientation.Vertical
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
                .align(Alignment.TopCenter)
                .background(Color.Yellow)
                .padding(start = 5.dp, end = 5.dp),
            indexList = indexList,
            option = IndexBarOptionCreate {
                orientation = Orientation.Horizontal
            }
        )
    }


}