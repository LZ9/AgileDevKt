package com.lodz.android.pandora.widget.base.compose.nodata

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lodz.android.pandora.R
import com.lodz.android.pandora.widget.base.compose.base.Orientation


/**
 * 无数据页
 * @author zhouL
 * @date 2026/5/20
 */
@Composable
fun NoDataPage(
    modifier: Modifier = Modifier,
    option: NoDataPageOption = NoDataPageOption()
) {
    val modifierBg = modifier.let {
        var modifier = it
        if (!option.backgroundOverlay) { // 是否由外部设置颜色
            if (option.backgroundColor != null) {
                modifier = modifier.background(option.backgroundColor)
            }
            if (option.backgroundResId != null) {
                modifier = modifier.paint(
                    painter = painterResource(option.backgroundResId),
                    contentScale = option.backgroundScale,
                    alignment = option.backgroundAlignment
                )
            }
        }
        modifier
    }

    if (option.orientation == Orientation.Vertical) {
        Column(
            modifier = modifierBg,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) { NoDataContainer(option) }

    } else {
        Row(
            modifier = modifierBg,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) { NoDataContainer(option) }
    }
}

@Composable
private fun NoDataContainer(option: NoDataPageOption) {

    if (option.isNeedImg) {
        Image(
            painter = painterResource(if (option.imgResId != null && option.imgResId != 0) option.imgResId else R.drawable.pandora_ic_no_data),
            contentScale = option.imgScale,
            alignment = option.imgAlignment,
            contentDescription = stringResource(R.string.pandora_app_img_name),
            modifier = Modifier.size(option.imgSize).padding(10.dp)
        )
    }

    if (option.isNeedTips) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = option.tips.ifEmpty { stringResource(R.string.pandora_no_data) },
            fontSize = option.textSize ?: 15.sp,
            color = option.textColor ?: Color.DarkGray
        )
    }
}


@Preview(device = Devices.NEXUS_6, showSystemUi = true, locale = "zh")
@Composable
fun UIPreviewVertical() {
    val context = LocalContext.current

    NoDataPage(
        modifier = Modifier.fillMaxSize(),
        option = context.noDataPageOptionCreate {
            orientation = Orientation.Vertical
        }
    )
}

@Preview(device = Devices.NEXUS_6, showSystemUi = true, locale = "zh")
@Composable
fun UIPreviewHorizontal() {
    val context = LocalContext.current

    NoDataPage(
        modifier = Modifier.fillMaxSize(),
        option = context.noDataPageOptionCreate {
            orientation = Orientation.Horizontal
        }
    )
}