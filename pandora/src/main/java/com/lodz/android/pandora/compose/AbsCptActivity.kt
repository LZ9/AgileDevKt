package com.lodz.android.pandora.compose

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable

/**
 * 底层抽象ComponentActivity
 * @author zhouL
 * @date 2026/5/9
 */
abstract class AbsCptActivity : ComponentActivity() {

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        startCreate()
        beforeSetContent()
        setContent { RootContentUI() }
        initData()
    }

    protected open fun beforeSetContent() {}

    @Composable
    abstract fun RootContentUI()

    protected open fun startCreate() {}

    protected open fun initData() {}

    protected open fun getContext(): Context = this
}