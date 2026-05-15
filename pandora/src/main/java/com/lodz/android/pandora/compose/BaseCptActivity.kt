package com.lodz.android.pandora.compose

import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.lodz.android.pandora.widget.base.compose.TitleBar
import com.lodz.android.pandora.widget.base.compose.TitleBarOption
import com.lodz.android.pandora.widget.base.compose.TitleBarOptionBuilder
import com.lodz.android.pandora.widget.base.compose.titleBarOption


/**
 * ComponentActivity基类
 * @author zhouL
 * @date 2026/5/9
 */
abstract class BaseCptActivity : AbsCptActivity() {

    private var titleBarOption by mutableStateOf(TitleBarOption())

    override fun beforeSetContent() {
        super.beforeSetContent()
        titleBarOption = getContext().titleBarOption {  }
    }

    @Composable
    override fun RootContentUI() {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TitleBar(
                    modifier = Modifier.fillMaxWidth(),
                    option = titleBarOption,
                    onBackBtnClick = {
                        finish()
                    }
                )
            }
        ) { innerPadding ->
            ContentUI(innerPadding)
        }
    }

    @Composable
    abstract fun ContentUI(innerPadding: PaddingValues)

    protected fun updateTitleBar(block: TitleBarOption.() -> TitleBarOption) {
        titleBarOption = titleBarOption.block()
    }

    protected fun update(block: TitleBarOptionBuilder.() -> Unit) {
        titleBarOption = TitleBarOptionBuilder().apply(block).update(titleBarOption)
    }
}