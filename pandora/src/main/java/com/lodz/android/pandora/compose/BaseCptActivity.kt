package com.lodz.android.pandora.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lodz.android.pandora.widget.base.compose.TitleBar
import com.lodz.android.pandora.widget.base.compose.titleBarOption


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
                TitleBar(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                    option = getContext().titleBarOption {
                        backgroundColor = Color.White

                    }
                )
            }
        ) { innerPadding ->
            ContentUI(innerPadding)
        }
    }

    @Composable
    abstract fun ContentUI(innerPadding: PaddingValues)
}