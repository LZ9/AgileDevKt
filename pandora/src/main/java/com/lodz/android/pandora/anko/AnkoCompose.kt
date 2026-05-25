package com.lodz.android.pandora.anko

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.lodz.android.corekt.anko.getColorCompat

/**
 * Compose相关扩展类
 * @author zhouL
 * @date 2026/5/13
 */

/** 通过ColorRes获取Compose的颜色 */
fun Context.getComposeColor(@ColorRes id: Int): Color = Color(this.getColorCompat(id))

/** 将ColorInt转为Compose的颜色 */
fun toComposeColor(@ColorInt color: Int): Color = Color(color)

/** 导航到目标页面[target]，并关闭当前页面[current] */
fun NavController.start(current: String, target: String, finish: Boolean = true) {
    this.navigate(target) {
        popUpTo(current) {
            inclusive = finish
        }
    }
}

/** 兼容使用Composable里的LocalContext.current来获取资源文字 */
fun Context.getStringCompat(@StringRes id: Int): String = this.getString(id)