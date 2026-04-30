package com.lodz.android.pandora.compose

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.ui.tooling.preview.Preview

/**
 * ComponentActivity基类
 * @author zhouL
 * @date 2026/4/28
 */
abstract class BaseComponentActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        startCreate()
        setContent{}
        afterSetContent()
        setListeners()
        initData()
        endCreate()

    }

    protected open fun startCreate() {}

    abstract fun createContent(parent: CompositionContext? = null, content: @Composable () -> Unit)

    protected open fun afterSetContent() {}

    protected open fun setListeners() {}

    protected open fun initData() {}

    protected open fun endCreate() {}

    protected open fun getContext(): Context = this

    @Preview(showBackground = true)
    @Composable
    abstract fun UIPreview()
}