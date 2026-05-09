package com.lodz.android.agiledevkt.compose.splash

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lodz.android.agiledevkt.compose.theme.AgileDevKtTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * Compose启动页
 * @author zhouL
 * @date 2026/4/28
 */
@Composable
fun SplashPage(name: String, modifier: Modifier = Modifier) {
    var flag by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )

        Button(onClick = {flag = !flag}) {
            Text(text = "$flag")
        }

    }

}

@Preview(showBackground = true)
@Composable
fun UIPreview() {
    AgileDevKtTheme {
        SplashPage("Android")
    }
}