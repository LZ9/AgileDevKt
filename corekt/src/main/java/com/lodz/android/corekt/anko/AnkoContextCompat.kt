package com.lodz.android.corekt.anko

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat

/**
 * 上下文兼容扩展类
 * Created by zhouL on 2018/6/27.
 */

@ColorInt
fun Context.getColorCompat(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable? = ContextCompat.getDrawable(this, id)





