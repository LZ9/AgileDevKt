package com.lodz.android.agiledevkt.compose.main

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.compose.splash.SplashPage
import com.lodz.android.agiledevkt.compose.theme.AgileDevKtTheme
import com.lodz.android.pandora.compose.BaseCptActivity
import com.lodz.android.pandora.widget.base.compose.base.BaseContent
import com.lodz.android.pandora.widget.base.compose.base.BaseContentState

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
            titleText = getString(R.string.main_title)
        }

        showStatusNoData()
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    @Composable
    override fun RootContentUI() {
        // 如果有统一主题可以在该方法里配置
        AgileDevKtTheme {
            super.RootContentUI()
        }
    }

    @Composable
    override fun ContentUI(innerPadding: PaddingValues) {
        Box(Modifier.fillMaxSize()){
            SplashPage()
        }
    }
}

@Preview(device = Devices.NEXUS_6, showSystemUi = true, locale = "zh")
@Composable
fun UIPreview() {
    val context = LocalContext.current
    AgileDevKtTheme {
        BaseContent(pageState = BaseContentState.Content) {
            SplashPage()
        }

    }

}