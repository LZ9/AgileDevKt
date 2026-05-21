package com.lodz.android.agiledevkt.compose.effect

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat

/**
 * 敏感权限申请业务
 * @author zhouL
 * @date 2026/5/21
 */
@Composable
fun PermissionRequestEffect(
    permissions: List<String>, // 申请权限列表
    trigger: Int, // 触发器，由外部出发权限申请
    autoRequest: Boolean = true, // 是否自动触发
    onGranted: () -> Unit, // 授权成功
    onDenied: (deniedList: List<String>) -> Unit,// 被拒绝
    onNeverAskAgain: (neverAskList: List<String>) -> Unit // 拒绝并不再询问
) {
    val context = LocalContext.current
    val activity = context as Activity

    var hasRequested by rememberSaveable { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { result ->

        if (result.all { it.value }) {// 全部成功
            onGranted()
            return@rememberLauncherForActivityResult
        }

        val deniedList = mutableListOf<String>()
        val neverAskList = mutableListOf<String>()

        result.forEach { entry ->

            val permission = entry.key
            val granted = entry.value

            if (!granted) {
                val shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                if (shouldShow || !hasRequested) {
                    deniedList.add(permission)
                } else {
                    neverAskList.add(permission)
                }
            }
        }

        if (deniedList.isNotEmpty()) {
            onDenied(deniedList)
        }

        if (neverAskList.isNotEmpty()) {
            onNeverAskAgain(neverAskList)
        }
    }

    LaunchedEffect(trigger, autoRequest) {
        if (autoRequest) {
            hasRequested = true
            launcher.launch(permissions.toTypedArray())
        }
    }
}
