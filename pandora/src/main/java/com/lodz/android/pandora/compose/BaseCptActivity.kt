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
import com.lodz.android.pandora.widget.base.compose.titlebar.TitleBar
import com.lodz.android.pandora.widget.base.compose.titlebar.TitleBarOption
import com.lodz.android.pandora.widget.base.compose.titlebar.TitleBarOptionBuilder
import com.lodz.android.pandora.widget.base.compose.titlebar.titleBarOptionCreate
import com.lodz.android.pandora.widget.base.compose.titlebar.titleBarOptionUpdate


/**
 * ComponentActivity基类
 * @author zhouL
 * @date 2026/5/9
 */
abstract class BaseCptActivity : AbsCptActivity() {

    private var mPdrTitleBarOption by mutableStateOf(TitleBarOption())

    override fun beforeSetContent() {
        super.beforeSetContent()
        mPdrTitleBarOption = getContext().titleBarOptionCreate { }
    }

    @Composable
    override fun RootContentUI() {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TitleBar(
                    modifier = Modifier.fillMaxWidth(),
                    option = mPdrTitleBarOption,
                    onBackBtnClick = { onClickBackBtn() },
                    areaLeft = { TitleBarAreaLeft() },
                    areaRight = { TitleBarAreaRight() }
                )
            }
        ) { innerPadding ->
            ContentUI(innerPadding)
        }
    }

    @Composable
    abstract fun ContentUI(innerPadding: PaddingValues)

    protected fun updateTitleBar(block: TitleBarOptionBuilder.() -> Unit) {
        mPdrTitleBarOption = mPdrTitleBarOption.titleBarOptionUpdate(block)
    }

    /** 点击标题栏的返回按钮 */
    protected open fun onClickBackBtn() {}

    /** 点击错误页面的重试按钮 */
    protected open fun onClickReload() {}

    /** 标题栏左侧区域覆盖 */
    @Composable
    protected open fun TitleBarAreaLeft() {}

    /** 标题栏右侧区域覆盖 */
    @Composable
    protected open fun TitleBarAreaRight() {}
}