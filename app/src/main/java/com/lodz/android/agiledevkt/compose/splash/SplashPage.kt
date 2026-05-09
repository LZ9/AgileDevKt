package com.lodz.android.agiledevkt.compose.splash

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.lodz.android.agiledevkt.compose.theme.AgileDevKtTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Devices

/**
 * Compose启动页
 * @author zhouL
 * @date 2026/4/28
 */
@Composable
fun SplashPage() {
    var flag by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Hello Android !"
        )

        Button(onClick = {flag = !flag}) {
            Text(text = "$flag")
        }

    }

}

@Preview(device = Devices.NEXUS_7, uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true)
@Preview(device = Devices.NEXUS_7, uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun UIPreview() {
    AgileDevKtTheme {
        SplashPage()
    }
}