package com.lodz.android.agiledevkt.compose.main

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lodz.android.agiledevkt.compose.splash.SplashPage
import com.lodz.android.pandora.compose.BaseCptActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 主页（Compose实现）
 * @author zhouL
 * @date 2026/5/9
 */
class MainCpsActivity : BaseCptActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainCpsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun initData() {
        super.initData()
        updateTitleBar {
            copy(
                titleText = "测试标题",
                titleTextColor = Color.Red
            )
        }

        MainScope().launch {
            delay(2000)

            updateTitleBar {
                copy(
                    titleText = "测试标题2222",
                )
            }

        }
    }

//    @Composable
//    override fun RootContentUI() {
//        AgileDevKtTheme {
//            super.RootContentUI()
//        }
//    }

    @Composable
    override fun ContentUI(innerPadding: PaddingValues) {
        Box(Modifier.fillMaxSize().padding(innerPadding).background(Color.Green)){
            SplashPage()
        }
    }
}