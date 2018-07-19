package com.lodz.android.corekt.anko

import java.text.DecimalFormat

/**
 * 数字格式化扩展类
 * Created by zhouL on 2018/7/18.
 */

object AnkoNumFormat{
    /** 保留1位小数  */
    const val TYPE_ONE_DECIMAL = "0.0"
    /** 保留2位小数  */
    const val TYPE_TWO_DECIMAL = "0.00"
    /** 保留3位小数  */
    const val TYPE_THREE_DECIMAL = "0.000"
    /** 保留4位小数  */
    const val TYPE_FOUR_DECIMAL = "0.0000"
    /** 保留5位小数  */
    const val TYPE_FIVE_DECIMAL = "0.00000"
}

/** 格式化[formatType]数字[data]（默认两位小数） */
fun Double.format(formatType: String = AnkoNumFormat.TYPE_TWO_DECIMAL): String {
    try {
        val format = DecimalFormat(formatType)
        return format.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/** 格式化[formatType]数字[data]（默认两位小数） */
fun Float.format(formatType: String = AnkoNumFormat.TYPE_TWO_DECIMAL): String {
    try {
        val format = DecimalFormat(formatType)
        return format.format(this.toDouble())
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}