package com.lodz.android.pandora.anko

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.compose.ui.graphics.Color
import com.lodz.android.corekt.anko.getColorCompat

/**
 * Compose相关扩展类
 * @author zhouL
 * @date 2026/5/13
 */
fun Context.getComposeColor(@ColorRes id: Int): Color = Color(this.getColorCompat(id))

fun toComposeColor(@ColorInt color: Int): Color = Color(color)