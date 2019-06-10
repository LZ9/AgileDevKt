package com.lodz.android.corekt.utils

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import com.lodz.android.corekt.anko.getColorCompat

/**
 * 颜色帮助类
 * Created by zhouL on 2018/7/2.
 */
object ColorUtils {

    /** 设置颜色资源[color]的透明度[alpha] */
    @ColorInt
    @JvmStatic
    fun getColorAlphaRes(context: Context, @ColorRes color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int = getColorAlphaInt(context.getColorCompat(color), alpha)

    /** 设置颜色[color]的透明度[alpha] */
    @ColorInt
    @JvmStatic
    fun getColorAlphaInt(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
        // 透明度最大值
        val max = 255

        var newAlpha = alpha
        if (alpha < 0f) {
            newAlpha = 0f
        }
        if (alpha > 1f) {
            newAlpha = 1f
        }

        var colorHex = Integer.toHexString(color)
        val length = colorHex.length
        if (length == 7) {
            colorHex = "0$colorHex"
        }

        if (length == 6) {
            colorHex = "ff$colorHex"
        }

        if (length < 6) {
            for (i in 0 until (6 - length)) {
                colorHex = "0$colorHex"
            }
            colorHex = "ff$colorHex"
        }

        val alphaInt = newAlpha * max// 获取alpha对应的十进制数值
        var hex = Integer.toHexString(alphaInt.toInt())//把十进制转为16进制
        if (hex.length == 1) {
            hex = "0$hex"
        }

        if (colorHex.length == 8) {
            return Color.parseColor("#" + hex + colorHex.substring(2, colorHex.length))
        }
        return color
    }
}