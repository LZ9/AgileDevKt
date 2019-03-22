package com.lodz.android.corekt.anko

import java.text.DecimalFormat

/**
 * 数字格式化扩展类
 * Created by zhouL on 2018/7/18.
 */

object AnkoNumFormat {
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

/** 数字转中文（到3位百） */
fun Int.toChinese(): String {
    val numCHN = arrayListOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九")
    val numEmptyCHN = arrayListOf("", "一", "二", "三", "四", "五", "六", "七", "八", "九")
    val numStr = this.toString()
    //个
    if (numStr.length == 1) {
        return numCHN[this]
    }
    //十
    if (numStr.length == 2) {
        val ten = numStr.substring(0, 1).toInt()
        val one = numStr.substring(1, 2).toInt()
        if (ten == 1) {
            return "十" + numEmptyCHN[one]
        }
        return numEmptyCHN[ten] + "十" + numEmptyCHN[one]
    }
    //百
    if (numStr.length == 3) {
        if (this == 100) {
            return "一百"
        }
        val hundred = numStr.substring(0, 1).toInt()
        val ten = numStr.substring(1, 2).toInt()
        val one = numStr.substring(2, 3).toInt()
        if (one == 0) {
            return numEmptyCHN[hundred] + "百" + numCHN[ten] + "十"
        }
        if (ten == 0) {
            return numEmptyCHN[hundred] + "百零" + numEmptyCHN[one]
        }
        return numEmptyCHN[hundred] + "百" + numCHN[ten] + "十" + numEmptyCHN[one]
    }
    return ""
}
