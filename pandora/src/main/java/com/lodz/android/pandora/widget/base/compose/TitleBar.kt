package com.lodz.android.pandora.widget.base.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.lodz.android.corekt.anko.px2dp
import com.lodz.android.pandora.R


/**
 * 标题栏
 * @author zhouL
 * @date 2026/5/9
 */
@Composable
fun TitleBar(
    modifier: Modifier = Modifier,
    option: TitleBarOption = TitleBarOption(),
    onBackBtnClick: (() -> Unit)? = null,
    areaLeft: (@Composable BoxScope.() -> Unit)? = null,
    areaRight: (@Composable BoxScope.() -> Unit)? = null,
) {
    // 上下文
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

    // 是否按下返回按钮
    val isBackBtnPressed by interactionSource.collectIsPressedAsState()
    // 返回按钮图标
    val backBtnIcon = if (isBackBtnPressed) {
        option.backBtnSelResId ?: R.drawable.pandora_ic_back_sel
    } else {
        option.backBtnResId ?: R.drawable.pandora_ic_back
    }

    Column(
        modifier = modifier.let{
            var modifier = it
            if (option.isNeedElevation) { // 配置阴影
                modifier = modifier.dropShadow(
                    shape = RectangleShape,
                    shadow = Shadow(
                        radius = context.px2dp(option.elevationVale).dp, // 模糊半径
                        color = Color.Black.copy(alpha = 0.25f),
                        offset = DpOffset(0.dp, context.px2dp(option.elevationVale).dp) // 只向下偏移
                    )
                )
            }
            if (!option.backgroundOverlay){ // 是否由外部设置颜色
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
            if (option.isPaddingStatusBar){//是否预留状态栏高度
                modifier = modifier.padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            }
            modifier
        },
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            val (left, center, right) = createRefs()

            //标题内容
            Text(
                text = option.titleText,
                modifier = Modifier
                    .width(200.dp)
                    .constrainAs(center) {
                        centerTo(parent)
                    },
                fontSize = option.titleTextSize ?: 18.sp,
                color = option.titleTextColor ?: Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // 左侧返回按钮
            Box(
                modifier = Modifier
                    .constrainAs(left) {
                        start.linkTo(parent.start)
                        end.linkTo(center.start)
                        horizontalBias = 0f // 强制控件贴住最左边
                        width = Dimension.preferredWrapContent
                        centerVerticallyTo(center)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (areaLeft != null) { //支持用户替换左侧布局
                    areaLeft()
                } else {
                    TextButton(
                        onClick = { onBackBtnClick?.invoke() },
                        interactionSource = interactionSource,
                    ) {
                        if (option.isNeedBackBtn) {
                            Icon(
                                painter = painterResource(backBtnIcon),
                                contentDescription = "返回",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Text(
                            option.backBtnText,
                            fontSize = option.backBtnTextSize ?: TextUnit.Unspecified,
                            color = option.backBtnTextColor ?: Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                    }
                }
            }

            // 右侧扩展布局
            Box(
                modifier = Modifier
                    .constrainAs(right) {
                        start.linkTo(center.end)
                        end.linkTo(parent.end)
                        width = Dimension.preferredWrapContent
                        centerVerticallyTo(center)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (areaRight != null) {
                    areaRight()
                }
            }
        }

        // 分割线
        if (option.isShowDivideLine) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = option.divideLineHeight,
                color = option.divideLineColor ?: Color.LightGray
            )
        }
    }
}

@Preview(device = Devices.NEXUS_6, showSystemUi = true)
@Composable
fun UIPreview() {

    TitleBar(
        option = LocalContext.current.titleBarOption {
            titleText = "测试标题"
            backgroundColor = Color.DarkGray
            backText = "返回"
        },
        areaRight = {
            Row() {
                Text(text = "保存", color = Color.White)
                Spacer(Modifier.size(10.dp))
                Text(text = "提交", color = Color.White)
            }
        }
    )
}