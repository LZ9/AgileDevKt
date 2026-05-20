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
import com.lodz.android.pandora.widget.base.compose.error.ErrorPage
import com.lodz.android.pandora.widget.base.compose.error.ErrorPageOption
import com.lodz.android.pandora.widget.base.compose.error.ErrorPageOptionBuilder
import com.lodz.android.pandora.widget.base.compose.error.errorPageOptionCreate
import com.lodz.android.pandora.widget.base.compose.error.errorPageOptionUpdate
import com.lodz.android.pandora.widget.base.compose.loading.LoadingPage
import com.lodz.android.pandora.widget.base.compose.loading.LoadingPageOption
import com.lodz.android.pandora.widget.base.compose.loading.LoadingPageOptionBuilder
import com.lodz.android.pandora.widget.base.compose.loading.loadingPageOptionCreate
import com.lodz.android.pandora.widget.base.compose.loading.loadingPageOptionUpdate
import com.lodz.android.pandora.widget.base.compose.nodata.NoDataPage
import com.lodz.android.pandora.widget.base.compose.nodata.NoDataPageOption
import com.lodz.android.pandora.widget.base.compose.nodata.NoDataPageOptionBuilder
import com.lodz.android.pandora.widget.base.compose.nodata.noDataPageOptionCreate
import com.lodz.android.pandora.widget.base.compose.nodata.noDataPageOptionUpdate
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
    /** 无数据页配置项 */
    private var mPdrNoDataPageOption by mutableStateOf(NoDataPageOption())
    /** 错误页配置项 */
    private var mPdrErrorPageOption by mutableStateOf(ErrorPageOption())

    /** 是否显示标题栏 */
    private var isShowTitleBar by mutableStateOf(true)
    /** 页面状态 */
    private var mPdrPageState by mutableStateOf<BaseContentState>(BaseContentState.Loading)
    /** 是否显示标题栏 */
    private var mPdrErrorThrowable by mutableStateOf<Throwable?>(null)

    typealias AreaContent = @Composable BoxScope.() -> Unit

    override fun beforeSetContent() {
        super.beforeSetContent()
        mPdrTitleBarOption = getContext().titleBarOptionCreate { }
        mPdrLoadingPageOption = getContext().loadingPageOptionCreate {  }
        mPdrNoDataPageOption = getContext().noDataPageOptionCreate {  }
        mPdrErrorPageOption = getContext().errorPageOptionCreate {  }
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
                ErrorPage(
                    modifier = Modifier.fillMaxSize(),
                    option = mPdrErrorPageOption,
                    t = mPdrErrorThrowable,
                    onReloadClick = { onClickReload() }
                )
            },

            noData = {
                NoDataPage(
                    modifier = Modifier.fillMaxSize(),
                    option = mPdrNoDataPageOption
                )
            }
        ) { innerPadding ->
            ContentUI(innerPadding)
        }
    }

    @Composable
    protected abstract fun ContentUI(innerPadding: PaddingValues)

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
        mPdrErrorThrowable = t
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

    /** 更新标题栏配置项参数 */
    protected fun updateTitleBar(block: TitleBarOptionBuilder.() -> Unit) {
        mPdrTitleBarOption = mPdrTitleBarOption.titleBarOptionUpdate(block)
    }

    /** 更新加载页配置项参数 */
    protected fun updateLoadingPage(block: LoadingPageOptionBuilder.() -> Unit) {
        mPdrLoadingPageOption = mPdrLoadingPageOption.loadingPageOptionUpdate(block)
    }

    /** 更新无数据页配置项参数 */
    protected fun updateNoDataPage(block: NoDataPageOptionBuilder.() -> Unit) {
        mPdrNoDataPageOption = mPdrNoDataPageOption.noDataPageOptionUpdate(block)
    }

    /** 更新错误页配置项参数 */
    protected fun updateErrorPage(block: ErrorPageOptionBuilder.() -> Unit) {
        mPdrErrorPageOption = mPdrErrorPageOption.errorPageOptionUpdate(block)
    }
}