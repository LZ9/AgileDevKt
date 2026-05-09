package com.lodz.android.pandora.widget.base.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 *   
 * @author zhouL
 * @date 2026/5/9
 */
@Composable
fun TitleBarCps() {

    Column(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .windowInsetsPadding(WindowInsets.statusBars),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Text(
            "测试标题",
            modifier = Modifier.weight(1f)
                .background(Color.Yellow),
            textAlign = TextAlign.Center
        )

        Box(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Red)
        )
    }


}

@Preview(device = Devices.NEXUS_7, showSystemUi = true)
@Composable
fun UIPreview() {
    TitleBarCps()
}