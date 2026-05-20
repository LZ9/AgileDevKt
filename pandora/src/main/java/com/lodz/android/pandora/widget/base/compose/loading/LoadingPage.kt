package com.lodz.android.pandora.widget.base.compose.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lodz.android.pandora.R
import androidx.compose.ui.res.stringResource
import com.lodz.android.pandora.widget.base.compose.base.Orientation
import kotlin.text.ifEmpty
import com.lodz.android.pandora.widget.progress.compose.AnimationProgress


/**
 * 加载页
 * @author zhouL
 * @date 2026/5/19
 */
@Composable
fun LoadingPage(
    modifier: Modifier = Modifier,
    option: LoadingPageOption = LoadingPageOption()
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
        ) { LoadingContainer(option) }

    } else {
        Row(
            modifier = modifierBg,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) { LoadingContainer(option) }
    }
}

@Composable
private fun LoadingContainer(option: LoadingPageOption) {
    val modifier = Modifier.padding(10.dp).let {
        var modifier = it
        val w = if (option.pbWidth == 0.dp && !option.useSysDefDrawable) 90.dp else option.pbWidth
        val h = if (option.pbHeight == 0.dp && !option.useSysDefDrawable) 90.dp else option.pbHeight

        if (w != 0.dp) {
            modifier = modifier.width(w)
        }
        if (h != 0.dp) {
            modifier = modifier.height(h)
        }
        modifier
    }

    if (!option.useSysDefDrawable) { // 不使用系统动画
        if (option.indeterminateDrawable != null) { // 使用帧动画
            AnimationProgress(modifier, option.indeterminateDrawable)
        } else if (option.indeterminateDrawableResId != null) { // 使用资源动画
            AnimationProgress(modifier, option.indeterminateDrawableResId)
        }
    } else {
        if (option.isIndeterminate) {
            CircularProgressIndicator(
                modifier = modifier
            )
        } else {
            CircularProgressIndicator(
                modifier = modifier,
                progress = { option.progress ?: 0f }
            )
        }
    }

    if (option.isNeedTips) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = option.tips.ifEmpty { stringResource(R.string.pandora_loading) },
            fontSize = option.textSize ?: 15.sp,
            color = option.textColor ?: Color.DarkGray
        )
    }
}

@Preview(device = Devices.NEXUS_6, showSystemUi = true, locale = "zh")
@Composable
fun UIPreviewVertical() {
    val context = LocalContext.current

    LoadingPage(
        modifier = Modifier.fillMaxSize(),
        option = context.loadingPageOptionCreate {
            orientation = Orientation.Vertical
        }
    )
}

@Preview(device = Devices.NEXUS_6, showSystemUi = true, locale = "zh")
@Composable
fun UIPreviewHorizontal() {
    val context = LocalContext.current

    LoadingPage(
        modifier = Modifier.fillMaxSize(),
        option = context.loadingPageOptionCreate {
            orientation = Orientation.Horizontal
        }
    )
}