package com.lodz.android.pandora.compose

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.lodz.android.pandora.widget.base.compose.base.BaseContent
import com.lodz.android.pandora.widget.base.compose.base.BaseContentState
import com.lodz.android.pandora.widget.base.compose.loading.LoadingPage
import com.lodz.android.pandora.widget.base.compose.loading.LoadingPageOption
import com.lodz.android.pandora.widget.base.compose.loading.LoadingPageOptionBuilder
import com.lodz.android.pandora.widget.base.compose.loading.loadingPageOptionCreate
import com.lodz.android.pandora.widget.base.compose.loading.loadingPageOptionUpdate
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

    /** 标题栏配置项 */
    private var mPdrTitleBarOption by mutableStateOf(TitleBarOption())
    /** 加载页配置项 */
    private var mPdrLoadingPageOption by mutableStateOf(LoadingPageOption())



    /** 是否显示标题栏 */
    private var isShowTitleBar by mutableStateOf(true)
    /** 页面状态 */
    private var mPdrPageState by mutableStateOf<BaseContentState>(BaseContentState.Loading)

    typealias AreaContent = @Composable BoxScope.() -> Unit

    override fun beforeSetContent() {
        super.beforeSetContent()
        mPdrTitleBarOption = getContext().titleBarOptionCreate { }
        mPdrLoadingPageOption = getContext().loadingPageOptionCreate {  }
    }

    @Composable
    override fun RootContentUI() {
        BaseContent(
            pageState = mPdrPageState,

            titleBar = {
                if (isShowTitleBar) {
                    TitleBar(
                        modifier = Modifier.fillMaxWidth(),
                        option = mPdrTitleBarOption,
                        onBackBtnClick = { onClickBackBtn() },
                        areaLeft = titleBarAreaLeft(),
                        areaRight = titleBarAreaRight()
                    )
                }
            },

            loading = {
                LoadingPage(
                    modifier = Modifier.fillMaxSize(),
                    option = mPdrLoadingPageOption
                )
            },

            error = {
                LoadingPage()
            },

            noData = {
                LoadingPage()
            }
        ) { innerPadding ->
            ContentUI(innerPadding)
        }
    }

    @Composable
    protected abstract fun ContentUI(innerPadding: PaddingValues)

    protected fun updateTitleBar(block: TitleBarOptionBuilder.() -> Unit) {
        mPdrTitleBarOption = mPdrTitleBarOption.titleBarOptionUpdate(block)
    }

    protected fun updateLoadingPage(block: LoadingPageOptionBuilder.() -> Unit) {
        mPdrLoadingPageOption = mPdrLoadingPageOption.loadingPageOptionUpdate(block)
    }

    /** 点击标题栏的返回按钮 */
    protected open fun onClickBackBtn() {}

    /** 点击错误页面的重试按钮 */
    protected open fun onClickReload() {}

    /** 标题栏左侧区域覆盖 */
    protected open fun titleBarAreaLeft(): AreaContent? = null

    /** 标题栏右侧区域覆盖 */
    protected open fun titleBarAreaRight(): AreaContent? = null

    /** 隐藏TitleBar */
    protected open fun goneTitleBar() {
        isShowTitleBar = false
    }

    /** 显示TitleBar */
    protected open fun showTitleBar() {
        isShowTitleBar = true
    }

    /** 显示无数据页面 */
    protected open fun showStatusNoData() {
        mPdrPageState = BaseContentState.NoData
    }

    /** 显示错误页面 */
    protected open fun showStatusError() {
        showStatusError(null)
    }

    /** 显示错误页面,[t]异常信息 */
    protected open fun showStatusError(t: Throwable?) {
        mPdrPageState = BaseContentState.Error
    }

    /** 显示加载页面 */
    protected open fun showStatusLoading() {
        mPdrPageState = BaseContentState.Loading
    }

    /** 显示内容页面 */
    protected open fun showStatusCompleted() {
        mPdrPageState = BaseContentState.Content
    }
}