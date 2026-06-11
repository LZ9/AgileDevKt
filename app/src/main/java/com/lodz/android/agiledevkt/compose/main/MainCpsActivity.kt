package com.lodz.android.agiledevkt.compose.main

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import com.lodz.android.agiledevkt.compose.navigation.AppNavHost
import com.lodz.android.agiledevkt.compose.theme.AgileDevKtTheme
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.base.activity.AbsCompose

/**
 * 主页（Compose实现）
 * @author zhouL
 * @date 2026/5/9
 */
@AbsCompose
class MainCpsActivity : AbsActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainCpsActivity::class.java)
            context.startActivity(intent)
        }
    }

    @Composable
    override fun RootContentUI() {
        AgileDevKtTheme {
            AppNavHost()
        }
    }
}