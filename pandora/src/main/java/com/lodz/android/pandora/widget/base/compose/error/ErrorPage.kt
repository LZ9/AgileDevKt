package com.lodz.android.pandora.widget.base.compose.error

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.lodz.android.corekt.network.NetworkManager
import com.lodz.android.pandora.R
import com.lodz.android.pandora.rx.exception.ExceptionFactory
import com.lodz.android.pandora.widget.base.compose.base.Orientation
import kotlin.Throwable
import kotlin.text.ifEmpty

/**
 * 错误页
 * @author zhouL
 * @date 2026/5/20
 */
@Composable
fun ErrorPage(
    modifier: Modifier = Modifier,
    option: ErrorPageOption = ErrorPageOption(),
    t: Throwable? = null,
    onReloadClick: (() -> Unit)? = null,
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

    Box(Modifier.clickable(onClick = { onReloadClick?.invoke() })) {
        if (option.orientation == Orientation.Vertical) {
            Column(
                modifier = modifierBg,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { ErrorContainer(option, t) }

        } else {
            Row(
                modifier = modifierBg,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) { ErrorContainer(option, t) }
        }
    }

}

@Composable
private fun ErrorContainer(option: ErrorPageOption, t: Throwable? = null) {
    val defTips = stringResource(R.string.pandora_load_fail) //默认的一般错误提示语
    val defNetTips = stringResource(R.string.pandora_load_fail_net) //默认的网络错误提示语

    val imgRes = remember(t) {
        if (!NetworkManager.get().isNetworkAvailable() || (t != null && ExceptionFactory.isNetworkError(t))) {
            getImgRes(option.imgNetResId, R.drawable.pandora_ic_network_fail)
        } else {
            getImgRes(option.imgResId, R.drawable.pandora_ic_data_fail)
        }
    }

    val tips = remember(t) {
        if (!NetworkManager.get().isNetworkAvailable() || (t != null && ExceptionFactory.isNetworkError(t))) {
            option.netTips.ifEmpty { defNetTips }
        } else {
            option.tips.ifEmpty { defTips }
        }
    }

    if (option.isNeedImg) {
        Image(
            painter = painterResource(imgRes),
            contentScale = option.imgScale,
            alignment = option.imgAlignment,
            contentDescription = stringResource(R.string.pandora_app_img_name),
            modifier = Modifier
                .size(option.imgSize)
                .padding(10.dp)
        )
    }

    if (option.isNeedTips) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = tips,
            fontSize = option.textSize ?: 15.sp,
            color = option.textColor ?: Color.DarkGray
        )
    }
}

/** 获取资源图片 */
private fun getImgRes(imgRes: Int?, @DrawableRes defRes: Int): Int = if (imgRes != null && imgRes != 0) imgRes else defRes

@Preview(device = Devices.NEXUS_6, showSystemUi = true, locale = "zh")
@Composable
fun UIPreviewVertical() {
    val context = LocalContext.current

    ErrorPage(
        modifier = Modifier.fillMaxSize(),
        option = context.errorPageOptionCreate {
            orientation = Orientation.Vertical
        }
    )
}

@Preview(device = Devices.NEXUS_6, showSystemUi = true, locale = "zh")
@Composable
fun UIPreviewHorizontal() {
    val context = LocalContext.current

    ErrorPage(
        modifier = Modifier.fillMaxSize(),
        option = context.errorPageOptionCreate {
            orientation = Orientation.Horizontal
        }
    )
}