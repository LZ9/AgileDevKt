package com.lodz.android.pandora.widget.base.compose.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.lodz.android.corekt.anko.px2dp
import com.lodz.android.pandora.R
import androidx.compose.ui.res.stringResource

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
    // 上下文
    val context = LocalContext.current


    //    /** 文字颜色 */
    //    val textColor: Color? = null,
    //    /** 文字大小 */
    //    val textSize: TextUnit? = null,
    //    /** 背景资源图片 */
    //    val backgroundResId: Int? = null,
    //    /** 背景资源图片缩放策略 */
    //    val backgroundScale: ContentScale = ContentScale.Inside,
    //    /** 背景资源图片对齐方式 */
    //    val backgroundAlignment: Alignment = Alignment.Center,
    //    /** 背景颜色 */
    //    val backgroundColor: Color? = null,
    //    /** 是否由用户在外部自定义背景色 */
    //    val backgroundOverlay: Boolean = false,
    //    /** 不确定模式 */
    //    val isIndeterminate: Boolean = true,
    //    /** 不确定模式下的资源 */
    //    val indeterminateDrawableResId: Int? = null,
    //    /** 使用系统默认加载资源 */
    //    val useSysDefDrawable: Boolean = true,
    //    /** 页面子元素垂直排列方式 */
    //    val verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    //    /** 页面子元素水平排列方式 */
    //    val horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,


    Column(
        modifier = modifier.fillMaxSize().let{
            var modifier = it
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
            modifier
        },
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {

        CircularProgressIndicator(
            modifier = Modifier.padding(10.dp).let{
                var modifier = it
                if (option.pbWidth != null){
                    modifier = modifier.width(option.pbWidth)
                }
                if (option.pbHeight != null){
                    modifier = modifier.height(option.pbHeight)
                }
                modifier
            }
        )

        if (option.isNeedTips) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = option.tips.ifEmpty { stringResource(R.string.pandora_loading) },
                fontSize = option.textSize ?: 15.sp,
                color = option.textColor ?: Color.DarkGray
            )
        }

    }
}

@Preview(device = Devices.NEXUS_6, showSystemUi = true, locale = "zh")
@Composable
fun UIPreview() {
    val context = LocalContext.current

    LoadingPage(
        option = context.loadingPageOptionCreate {
            pbHeight = 30.dp
            pbWidth = 30.dp


        }
    )
}