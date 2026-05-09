package com.lodz.android.agiledevkt.compose.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lodz.android.agiledevkt.compose.splash.SplashPage
import com.lodz.android.agiledevkt.compose.theme.AgileDevKtTheme

/**
 *   
 * @author zhouL
 * @date 2026/5/9
 */
class MainCpsActivity : ComponentActivity() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainCpsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgileDevKtTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    SplashPage(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UIPreview() {
    AgileDevKtTheme {
        SplashPage("Android")
    }
}