package com.lodz.android.corekt

import com.lodz.android.corekt.utils.DateUtils
import org.junit.Assert
import org.junit.Test

/**
 * 时间格式化帮助类单元测试
 * Created by zhouL on 2018/7/26.
 */
class DateUtilsTest {

    private val CURRENT_DATE = "20180726"
    private val AFTER_CURRENT_DATE = "20180801"
    private val BEFORE_CURRENT_DATE = "20180720"

    @Test
    fun getCurrentFormatStringTest() {
        Assert.assertEquals(CURRENT_DATE, DateUtils.getCurrentFormatString(DateUtils.TYPE_5))
    }

    @Test
    fun changeFormatStringTest() {
        val source = CURRENT_DATE.substring(0, 4) + "-" + CURRENT_DATE.substring(4, 6) + "-" + CURRENT_DATE.substring(6, 8)
        Assert.assertEquals(CURRENT_DATE, DateUtils.changeFormatString(DateUtils.TYPE_6, DateUtils.TYPE_5, source))
    }

    @Test
    fun getAfterDayTest() {
        Assert.assertEquals(AFTER_CURRENT_DATE, DateUtils.getAfterDay(DateUtils.TYPE_5, CURRENT_DATE, 6))
    }

    @Test
    fun getBeforeDayTest(){
        Assert.assertEquals(BEFORE_CURRENT_DATE, DateUtils.getBeforeDay(DateUtils.TYPE_5, CURRENT_DATE, 6))
    }
}