package com.lodz.android.corekt.anko

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

/**
 * Drawable扩展类
 * Created by zhouL on 2018/7/2.
 */

/** 用颜色资源[color]创建Drawable */
fun Context.createColorDrawable(@ColorRes color: Int) = ColorDrawable(getColorCompat(color))

/** 用颜色[color]创建Drawable */
fun Context.createColorIntDrawable(@ColorInt color: Int) = ColorDrawable(color)

/** 用[bitmap]创建Drawable */
fun Context.createBitmapDrawable(bitmap: Bitmap) = BitmapDrawable(resources, bitmap)