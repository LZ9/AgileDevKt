package com.lodz.android.corekt.utils

import android.support.annotation.StringDef
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间格式化帮助类
 * Created by zhouL on 2018/7/2.
 */
object DateUtils {
    const val TYPE_1 = "HH:mm"
    const val TYPE_2 = "yyyy-MM-dd HH:mm:ss"
    const val TYPE_3 = "yyyyMMddHHmmssSSS"
    const val TYPE_4 = "yyyyMMddHHmmss"
    const val TYPE_5 = "yyyyMMdd"
    const val TYPE_6 = "yyyy-MM-dd"
    const val TYPE_7 = "yyyy-MM-dd-HH-mm-ss"
    const val TYPE_8 = "HH:mm:ss"
    const val TYPE_9 = "yyyy-MM-dd HH-mm-ss"
    const val TYPE_10 = "yyyy-MM-dd HH:mm:ss:SSS"
    const val TYPE_11 = "yyyy-MM-dd HH:mm"

    @StringDef(TYPE_1, TYPE_2, TYPE_3, TYPE_4, TYPE_5, TYPE_6, TYPE_7, TYPE_8, TYPE_9, TYPE_10, TYPE_11)
    @Retention(AnnotationRetention.SOURCE)
    annotation class FormatType

    /** 格式化[formatType]当前时间 */
    fun getCurrentFormatString(formatType: String) = getFormatString(formatType, Date(System.currentTimeMillis()))

    /** 格式化[formatType]当前时间 */
    fun getFormatString(formatType: String, date: Date): String {
        try {
            val format = SimpleDateFormat(formatType, Locale.CHINA)
            return format.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /** 将格式化[formatType]后的时间字符串[source]转成Date */
    fun parseFormatDate(formatType: String, source: String): Date? {
        try {
            val format = SimpleDateFormat(formatType, Locale.CHINA)
            return format.parse(source)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 将日期字符串[source]从旧格式[oldFormatType]改为新格式[newFormatType] */
    fun changeFormatString(oldFormatType: String, newFormatType: String, source: String): String {
        val date = parseFormatDate(oldFormatType, source)
        if (date == null) {
            return ""
        }
        return getFormatString(newFormatType, date)
    }

    /** 获取格式为[formatType]的日期字符串[source]之后[n]天的日期 */
    fun getAfterDay(formatType: String, source: String, n: Int): String {
        try {
            val cal = Calendar.getInstance()
            cal.time = parseFormatDate(formatType, source)
            cal.set(Calendar.DATE, cal.get(Calendar.DATE) + n)
            return parseFormatCalendar(formatType, cal)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /** 获取格式为[formatType]的日期字符串[source]之前[n]天的日期 */
    fun getBeforeDay(formatType: String, source: String, n: Int): String {
        try {
            val cal = Calendar.getInstance()
            cal.time = parseFormatDate(formatType, source)
            cal.set(Calendar.DATE, cal.get(Calendar.DATE) - n)
            return parseFormatCalendar(formatType, cal)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /** 根据日历[calendar]获取对应格式[formatType]的时间 */
    fun parseFormatCalendar(formatType: String, calendar: Calendar): String {
        try {
            return DateFormat.format(formatType, calendar).toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}




