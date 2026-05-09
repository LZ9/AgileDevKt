package com.lodz.android.pandora.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lodz.android.pandora.widget.base.compose.TitleBarCps


/**
 * ComponentActivity基类
 * @author zhouL
 * @date 2026/5/9
 */
abstract class BaseCptActivity : AbsCptActivity() {

    @Composable
    override fun RootContentUI() {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TitleBarCps()
            }
        ) { innerPadding ->
            ContentUI(innerPadding)
        }
    }

    @Composable
    abstract fun ContentUI(innerPadding: PaddingValues)
}