package com.lodz.android.agiledevkt.compose.splash

import android.Manifest
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.lodz.android.agiledevkt.compose.theme.AgileDevKtTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.compose.effect.PermissionRequestEffect
import com.lodz.android.agiledevkt.compose.navigation.Route
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.anko.start
import kotlinx.coroutines.delay

/**
 * Compose启动页
 * @author zhouL
 * @date 2026/4/28
 */
@Composable
fun SplashPage(nav: NavController? = null) {
    val context = LocalContext.current

    var permissionRetryKey by rememberSaveable { mutableIntStateOf(0) }

    var isAllGranted by remember { mutableStateOf(false) }

    PermissionRequestEffect(

        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) listOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO
        ) else listOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ),

        trigger = permissionRetryKey,

        onGranted = {
            PrintLog.d("testtag", "权限全部通过")
            isAllGranted = true
        },

        onDenied = {
            PrintLog.w("testtag", "权限被拒绝:$it")
            permissionRetryKey++
        },

        onNeverAskAgain = {
            PrintLog.e("testtag", "永久拒绝:$it")
            context.goAppDetailSetting()
        }
    )

    LaunchedEffect(isAllGranted) {
        if (isAllGranted){
            delay(1000)
            nav?.start(Route.Splash, Route.Main)
        }
    }


    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.bg_splash),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                contentDescription = stringResource(R.string.pandora_app_img_name),
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.size(90.dp))

                Text(
                    text = stringResource(R.string.splash_title),
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(device = Devices.PIXEL_9_PRO, showSystemUi = true, locale = "zh")
@Composable
fun UIPreview() {
    val context = LocalContext.current
    AgileDevKtTheme {
        SplashPage()
    }

}