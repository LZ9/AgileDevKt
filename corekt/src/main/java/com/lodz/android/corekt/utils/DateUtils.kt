package com.lodz.android.corekt.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.annotation.StringDef
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间帮助类
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
    const val TYPE_12 = "yyyyMM"
    const val TYPE_13 = "yyyy-MM"
    const val TYPE_14 = "yyyy"

    @StringDef(TYPE_1, TYPE_2, TYPE_3, TYPE_4, TYPE_5, TYPE_6, TYPE_7, TYPE_8, TYPE_9, TYPE_10, TYPE_11, TYPE_12, TYPE_13, TYPE_14)
    @Retention(AnnotationRetention.SOURCE)
    annotation class FormatType

    /** 格式化[formatType]当前时间 */
    @JvmStatic
    fun getCurrentFormatString(formatType: String): String = getFormatString(formatType, Date(System.currentTimeMillis()))

    /** 格式化[formatType]当前时间 */
    @JvmStatic
    fun getFormatString(formatType: String, date: Date?): String {
        if (date == null) {
            return ""
        }
        try {
            val format = SimpleDateFormat(formatType, Locale.CHINA)
            return format.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /** 将格式化[formatType]后的时间字符串[source]转成Date */
    @JvmStatic
    fun parseFormatDate(formatType: String, source: String): Date? {
        try {
            val format = SimpleDateFormat(formatType, Locale.CHINA)
            return format.parse(source)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 将格式化[formatType]后的时间字符串[source]转成Calendar */
    @JvmStatic
    fun parseFormatToCalendar(formatType: String, source: String): Calendar? {
        val date = parseFormatDate(formatType, source)
        if (date != null) {
            return parseDateToCalendar(date)
        }
        return null
    }

    /** 将[date]转成Calendar */
    @JvmStatic
    fun parseDateToCalendar(date: Date): Calendar {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal
    }

    /** 将日期字符串[source]从旧格式[oldFormatType]改为新格式[newFormatType] */
    @JvmStatic
    fun changeFormatString(oldFormatType: String, newFormatType: String, source: String): String {
        val date = parseFormatDate(oldFormatType, source) ?: return ""
        return getFormatString(newFormatType, date)
    }

    /** 获取格式为[formatType]的日期字符串[source]之后[n]天的日期 */
    @JvmStatic
    fun getAfterDay(formatType: String, source: String, n: Int): String {
        try {
            val cal = Calendar.getInstance()
            cal.time = parseFormatDate(formatType, source) ?: return ""
            cal.set(Calendar.DATE, cal.get(Calendar.DATE) + n)
            return parseFormatCalendar(formatType, cal)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /** 获取格式为[formatType]的日期字符串[source]之前[n]天的日期 */
    @JvmStatic
    fun getBeforeDay(formatType: String, source: String, n: Int): String {
        try {
            val cal = Calendar.getInstance()
            cal.time = parseFormatDate(formatType, source) ?: return ""
            cal.set(Calendar.DATE, cal.get(Calendar.DATE) - n)
            return parseFormatCalendar(formatType, cal)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /** 根据日历[calendar]获取对应格式[formatType]的时间 */
    @JvmStatic
    fun parseFormatCalendar(formatType: String, calendar: Calendar): String {
        try {
            return getFormatString(formatType, Date(calendar.timeInMillis))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /** 显示日期选择器，[context]上下文，[listener]监听器，[calendar]日历对象（默认自动获取） */
    @JvmStatic
    @JvmOverloads
    fun showDatePicker(
        context: Context,
        listener: DatePickerDialog.OnDateSetListener,
        calendar: Calendar = Calendar.getInstance()
    ) {
        val dialog = DatePickerDialog(
            context, listener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }

    /** 显示时间选择器，[context]上下文，[listener]监听器，[calendar]日历对象（默认自动获取），[is24HourView]是否按24小时格式显示界面 */
    @JvmStatic
    @JvmOverloads
    fun showTimePicker(
        context: Context,
        listener: TimePickerDialog.OnTimeSetListener,
        calendar: Calendar = Calendar.getInstance(),
        is24HourView: Boolean = true
    ) {
        val dialog = TimePickerDialog(
            context, listener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE), is24HourView
        )
        dialog.show()
    }

    /** 日期1是否在日期2之前（dateOne < dateTwo） */
    @JvmStatic
    fun isBefore(dateOne: Date, dateTwo: Date): Boolean = dateOne.before(dateTwo)

    /** 日期1是否在日期2之前（dateOne < dateTwo） */
    @JvmStatic
    fun isBefore(formatType: String, sourceOne: String, sourceTwo: String): Boolean {
        val dateOne = parseFormatDate(formatType, sourceOne)
        val dateTwo = parseFormatDate(formatType, sourceTwo)
        return dateOne?.before(dateTwo) ?: false
    }
}




