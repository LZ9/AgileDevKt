package com.lodz.android.corekt.anko

import java.text.DecimalFormat

/**
 * 数字格式化扩展类
 * Created by zhouL on 2018/7/18.
 */

object AnkoNumFormat {
    /** 保留1位小数 */
    const val TYPE_ONE_DECIMAL = "0.0"
    /** 保留2位小数 */
    const val TYPE_TWO_DECIMAL = "0.00"
    /** 保留3位小数 */
    const val TYPE_THREE_DECIMAL = "0.000"
    /** 保留4位小数 */
    const val TYPE_FOUR_DECIMAL = "0.0000"
    /** 保留5位小数 */
    const val TYPE_FIVE_DECIMAL = "0.00000"
}

/** 格式化[formatType]数字（默认两位小数） */
@JvmOverloads
fun Double.format(formatType: String = AnkoNumFormat.TYPE_TWO_DECIMAL): String {
    try {
        val format = DecimalFormat(formatType)
        return format.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/** 格式化[formatType]数字（默认两位小数） */
@JvmOverloads
fun Float.format(formatType: String = AnkoNumFormat.TYPE_TWO_DECIMAL): String {
    try {
        val format = DecimalFormat(formatType)
        return format.format(this.toDouble())
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/** 15位以内数字转中文 */
fun Long.toChinese(): String {
    // 中文数字字符数组，用于将 0 - 9 转换为中文数字
    val chineseNumbers = arrayOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九")
    // 中文数位字符数组，支持到 15 位数字转换
    val units = arrayOf("", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千", "兆", "十", "百")
    // 处理输入数字为 0 的特殊情况
    if (this == 0L) {
        return "零"
    }
    var numStr = this.toString()
    if (numStr.length > 15) {
        return "数字超出范围"
    }
    var result = ""
    var zeroFlag = false
    val length = numStr.length
    for (i in 0 until length) {
        val digit = numStr[i].toString().toInt()
        val unitIndex = length - i - 1
        if (digit == 0) {
            zeroFlag = true
            // 处理每 4 位分隔处，避免连续零丢失大数位单位
            if (unitIndex % 4 == 0 && result.isNotEmpty() &&!result.endsWith("零")) {
                result += units[unitIndex]
            }
        } else {
            if (zeroFlag) {
                result += "零"
                zeroFlag = false
            }
            result += chineseNumbers[digit] + units[unitIndex]
        }
    }
    // 去除末尾多余的零
    while (result.endsWith("零")) {
        result = result.dropLast(1)
    }
    // 处理一十开头的情况，简化为十
    if (result.startsWith("一十")) {
        result = result.drop(1)
    }
    // 处理大数位分隔情况，避免多余表述
    result = result.replace("零亿", "亿").replace("零万", "万").replace("亿万", "亿").replace("兆亿", "兆")
    return result
}


/** 15位以内数字转中文 */
fun Int.toChinese(): String = this.toLong().toChinese()
