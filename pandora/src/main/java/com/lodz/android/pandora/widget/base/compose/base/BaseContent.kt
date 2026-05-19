package com.lodz.android.pandora.widget.base.compose.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lodz.android.pandora.widget.base.compose.loading.LoadingPage
import com.lodz.android.pandora.widget.base.compose.titlebar.TitleBar

/**
 * 基础框架内容组件
 * @author zhouL
 * @date 2026/5/19
 */
@Composable
fun BaseContent(
    pageState: BaseContentState = BaseContentState.Loading,
    titleBar: @Composable (() -> Unit)? = {
        TitleBar()
    },

    loading: @Composable (() -> Unit)? = {
        LoadingPage()
    },

    error: @Composable (() -> Unit)? = {
        LoadingPage()
    },

    noData: @Composable (() -> Unit)? = {
        LoadingPage()
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