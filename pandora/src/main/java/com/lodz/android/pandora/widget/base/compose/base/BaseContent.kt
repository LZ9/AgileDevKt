package com.lodz.android.pandora.widget.base.compose.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.lodz.android.pandora.widget.base.compose.error.ErrorPage
import com.lodz.android.pandora.widget.base.compose.loading.LoadingPage
import com.lodz.android.pandora.widget.base.compose.nodata.NoDataPage
import com.lodz.android.pandora.widget.base.compose.titlebar.TitleBar
import com.lodz.android.pandora.widget.base.compose.titlebar.titleBarOptionCreate

/**
 * 基础框架内容组件
 * @author zhouL
 * @date 2026/5/19
 */
@Composable
fun BaseContent(
    pageState: BaseContentState = BaseContentState.Loading,
    titleBar: @Composable (() -> Unit)? = {
        TitleBar(
            Modifier
                .fillMaxWidth()
                .background(Color.DarkGray),
            LocalContext.current.titleBarOptionCreate { titleText = "标题" }
        )
    },

    loading: @Composable (() -> Unit)? = {
        LoadingPage(Modifier.fillMaxSize())
    },

    error: @Composable (() -> Unit)? = {
        ErrorPage(Modifier.fillMaxSize())
    },

    noData: @Composable (() -> Unit)? = {
        NoDataPage(Modifier.fillMaxSize())
    },

    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { titleBar?.invoke() }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when (pageState) {
                BaseContentState.Loading -> { loading?.invoke() }

                BaseContentState.Error -> { error?.invoke() }

                BaseContentState.NoData -> { noData?.invoke() }

                BaseContentState.Content -> { content.invoke(innerPadding) }
            }
        }
    }
}