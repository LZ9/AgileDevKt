package com.lodz.android.pandora.widget.base.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Offset.Companion
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.lodz.android.corekt.anko.px2dp

/**
 *   
 * @author zhouL
 * @date 2026/5/9
 */
@Composable
fun TitleBar(modifier: Modifier = Modifier, option: TitleBarOption = TitleBarOption()) {
    val context = LocalContext.current
    Column(
        modifier = modifier.let{
            var modifier = it
            if (option.isNeedElevation) {
                modifier = modifier.dropShadow(
                    shape = RectangleShape,
                    shadow = Shadow(
                        radius = context.px2dp(option.elevationVale).dp, // 模糊半径
                        color = Color.Black.copy(alpha = 0.25f),
                        offset = DpOffset(0.dp, context.px2dp(option.elevationVale).dp) // 只向下偏移
                    )
                )
            }
            modifier
        },
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .let {
                    var modifier = it
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
                    modifier
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "返回按钮",
                modifier = Modifier.width(80.dp),
                textAlign = TextAlign.Center
            )
            Text(
                "测试标题",
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                "右侧扩展",
                modifier = Modifier.width(80.dp),
                textAlign = TextAlign.Center
            )
        }

        /** 分割线 */
        if (option.isShowDivideLine) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = option.divideLineHeight,
                color = option.divideLineColor ?: Color.LightGray
            )
        }
    }


    //  Column(
    //    if (backText.isNotEmpty()) {
    //         Text(
    //            text = backText,
    //            modifier = Modifier
    //                .padding(horizontal = 10.dp, vertical = 10.dp)
    //                .widthIn(max = 90.dp),
    //            color = backTextColor,
    //            fontSize = backTextSize.sp,
    //            maxLines = 1,
    //            overflow = TextOverflow.Ellipsis
    //                            )
    //                        }
    //                    }
    //                }
    //            }
    //
    //            /**
    //             * 中间标题
    //             */
    //            Row(
    //                modifier = Modifier.align(Alignment.Center),
    //                verticalAlignment = Alignment.CenterVertically,
    //                horizontalArrangement = Arrangement.Center
    //            ) {
    //
    //                Text(
    //                    text = title,
    //                    modifier = Modifier
    //                        .width(200.dp)
    //                        .alpha(titleAlpha),
    //                    color = titleColor,
    //                    fontSize = titleSize.sp,
    //                    textAlign = TextAlign.Center,
    //                    maxLines = 1,
    //                    overflow = TextOverflow.Ellipsis
    //                )
    //            }
    //
    //            /**
    //             * 右侧扩展区域
    //             */
    //            Row(
    //                modifier = Modifier
    //                    .align(Alignment.CenterEnd)
    //                    .fillMaxHeight()
    //                    .padding(horizontal = 8.dp),
    //                verticalAlignment = Alignment.CenterVertically,
    //                horizontalArrangement = Arrangement.Center,
    //                content = expandContent
    //            )
    //        }
    //
    //        /**
    //         * 分割线
    //         */
    //        if (showDivider) {
    //            HorizontalDivider(
    //                modifier = Modifier.fillMaxWidth(),
    //                thickness = dividerHeight,
    //                color = dividerColor
    //            )
    //        }
    //    }
}

@Preview(device = Devices.NEXUS_7, showSystemUi = true)
@Composable
fun UIPreview() {

    TitleBar(
        option = LocalContext.current.titleBarOption {
            isShowDivideLine = true
            backgroundColor = Color.White

        }
    )
}