package com.lodz.android.corekt

import com.lodz.android.corekt.utils.IdCardUtils
import org.junit.Assert
import org.junit.Test

/**
 * 身份证帮助类单元测试
 * Created by zhouL on 2018/7/26.
 */
class IdCardUtilsTest {

    private val ID_CARD_NUM = "513001198604040835"
    private val SEX_CODE = 1
    private val SEX_STR = "男"
    private val BIRTH = "19860404"
    private val AGE = 32
    private val PROVINCE = "四川"


    @Test
    fun validateIdCardTest() {
        Assert.assertEquals(true, IdCardUtils.validateIdCard(ID_CARD_NUM))
    }

    @Test
    fun getValidationCodeTest() {
        val code = ID_CARD_NUM.subSequence(0, ID_CARD_NUM.length - 1).toString()
        val code18 = ID_CARD_NUM.subSequence(ID_CARD_NUM.length - 1, ID_CARD_NUM.length).toString()
        Assert.assertEquals(code18, IdCardUtils.getValidationCode(code))
    }

    @Test
    fun getSexCodeTest() {
        Assert.assertEquals(SEX_CODE, IdCardUtils.getSexCode(ID_CARD_NUM))
        Assert.assertEquals(SEX_STR, IdCardUtils.getSexStr(ID_CARD_NUM))
    }

    @Test
    fun getBirthTest() {
        Assert.assertEquals(BIRTH, IdCardUtils.getBirth(ID_CARD_NUM))
    }

    @Test
    fun getYearTest() {
        val year = BIRTH.subSequence(0, 4).toString()
        Assert.assertEquals(year, IdCardUtils.getYear(ID_CARD_NUM))
    }

    @Test
    fun getMonthTest() {
        val month = BIRTH.subSequence(4, 6).toString()
        Assert.assertEquals(month, IdCardUtils.getMonth(ID_CARD_NUM))
    }

    @Test
    fun getDayTest() {
        val day = BIRTH.subSequence(6, BIRTH.length).toString()
        Assert.assertEquals(day, IdCardUtils.getDay(ID_CARD_NUM))
    }

    @Test
    fun getAgeTest() {
        Assert.assertEquals(AGE, IdCardUtils.getAge(ID_CARD_NUM))
    }

    @Test
    fun getProvinceTest() {
        Assert.assertEquals(PROVINCE, IdCardUtils.getProvince(ID_CARD_NUM))
    }
}