package com.lodz.android.corekt.utils

import java.util.*

/**
 * 身份证帮助类
 * Created by zhouL on 2018/7/17.
 */
object IdCardUtils {
    /** 每位加权因子 */
    private val POWER = arrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)

    /** 验证18位身份证号[idCard]是否合法 */
    fun validateIdCard(idCard: String): Boolean {
        if (idCard.isEmpty()) {
            return false
        }
        if (idCard.length != 18) {
            return false
        }
        val code17 = idCard.substring(0, 17)// 取出前17位
        if (!isNum(code17)) {//前17位存在非数字
            return false
        }
        if (!isProvinceExist(idCard)) {// 判断省份是否存在
            return false
        }
        if (!validateBirth(idCard)) {// 判断日期是否存在
            return false
        }
        val validationCode = getCheckCode18(getPowerSum(converCharToInt(code17.toCharArray())))
        return validationCode.isNotEmpty() && validationCode.equals(idCard.subSequence(17, 18).toString(), true)
    }

    /** 身份证[idCard]的省份是否存在 */
    private fun isProvinceExist(idCard: String) = !getProvinceMap().get(idCard.substring(0, 2)).isNullOrEmpty()

    /** 校验身份证[idCard]的出生日期是否正确 */
    private fun validateBirth(idCard: String): Boolean {
        if (idCard.isEmpty() || idCard.length < 14) {
            return false
        }
        val birth = idCard.substring(6, 14)
        val date = DateUtils.parseFormatDate(DateUtils.TYPE_5, birth)
        if (date == null) {
            return false
        }
        val dateStr = DateUtils.getFormatString(DateUtils.TYPE_5, date)
        return dateStr.equals(birth)
    }

    /** 获取17位待补全的身份证号[idCard]的校验位 */
    fun getValidationCode(idCard: String): String {
        if (idCard.isEmpty()) {
            return ""
        }
        if (idCard.length != 17) {
            return ""
        }
        if (!isNum(idCard)) {//前17位存在非数字
            return ""
        }
        return getCheckCode18(getPowerSum(converCharToInt(idCard.toCharArray())))
    }

    /** 校验文本[str]是否是纯数字 */
    private fun isNum(str: String) = str.isNotEmpty() && str.matches(Regex("^[0-9]*$"))

    /** 将字符数组[array]转换成数字数组 */
    private fun converCharToInt(array: CharArray): IntArray {
        val intArray = IntArray(array.size)
        try {
            for ((i, c) in array.withIndex()) {
                intArray[i] = c.toInt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return intArray
    }

    /** 将身份证的每位和对应位的加权因子相乘之后，再得到和值，[array]身份证前17位数组 */
    private fun getPowerSum(array: IntArray): Int {
        var sum = 0
        if (POWER.size == array.size) {
            for (i in array.indices) {
                for (j in POWER.indices) {
                    if (i == j) {
                        sum += sum + array[i] * POWER[j]
                    }
                }
            }
        }
        return sum
    }

    /** 将power和值[sum]与11取模获得余数进行校验码判断 */
    private fun getCheckCode18(sum: Int) = when (sum % 11) {
        10 -> "2"
        9 -> "3"
        8 -> "4"
        7 -> "5"
        6 -> "6"
        5 -> "7"
        4 -> "8"
        3 -> "9"
        2 -> "x"
        1 -> "0"
        0 -> "1"
        else -> ""
    }

    /** 获取身份证[idCard]的性别代码（0非身份证 1男 2女） */
    fun getSexCode(idCard: String): Int {
        if (!validateIdCard(idCard)) {
            return 0
        }
        val sex = idCard.substring(16, 17).toInt()
        return if (sex % 2 != 0) 1 else 2
    }

    /** 获取身份证[idCard]的性别（男 女） */
    fun getSexStr(idCard: String): String {
        val sexCode = getSexCode(idCard)
        if (sexCode == 1) {
            return "男"
        }
        if (sexCode == 2) {
            return "女"
        }
        return ""
    }

    /** 根据日期格式[dateFormat]从身份证[idCard]获取出生年月 */
    fun getBirth(idCard: String, dateFormat: String): String {
        if (!validateIdCard(idCard)) {
            return ""
        }
        if (dateFormat.isEmpty()) {
            return ""
        }
        val original = idCard.substring(6, 14)
        return if (DateUtils.TYPE_5.equals(dateFormat, true)) original else DateUtils.changeFormatString(DateUtils.TYPE_5, dateFormat, original)
    }

    /** 从身份证[idCard]获取出生年月（格式yyyyMMdd） */
    fun getBirth(idCard: String) = getBirth(idCard, DateUtils.TYPE_5)

    /** 从身份证[idCard]获取年 */
    fun getYear(idCard: String): String {
        if (!validateIdCard(idCard)) {
            return ""
        }
        return idCard.substring(6, 10)
    }

    /** 从身份证[idCard]获取月 */
    fun getMonth(idCard: String): String {
        if (!validateIdCard(idCard)) {
            return ""
        }
        return idCard.substring(10, 12)
    }

    /** 从身份证[idCard]获取日 */
    fun getDay(idCard: String): String {
        if (!validateIdCard(idCard)) {
            return ""
        }
        return idCard.substring(12, 14)
    }

    /** 从身份证[idCard]获取年龄 */
    fun getAge(idCard: String): Int {
        if (!validateIdCard(idCard)) {
            return 0
        }

        val year = Calendar.getInstance().get(Calendar.YEAR)
        return year - getYear(idCard).toInt()
    }

    /** 从身份证[idCard]获取户籍省份 */
    fun getProvince(idCard: String): String {
        if (!validateIdCard(idCard)) {
            return ""
        }
        val provinceNum = idCard.substring(0, 2)
        val map = getProvinceMap()
        return map.get(provinceNum) ?: ""
    }

    /** 获取省份代码 */
    private fun getProvinceMap() = mapOf(
            "11" to "北京",
            "12" to "天津",
            "13" to "河北",
            "14" to "山西",
            "15" to "内蒙古",
            "21" to "辽宁",
            "22" to "吉林",
            "23" to "黑龙江",
            "31" to "上海",
            "32" to "江苏",
            "33" to "浙江",
            "34" to "安徽",
            "35" to "福建",
            "36" to "江西",
            "37" to "山东",
            "41" to "河南",
            "42" to "湖北",
            "43" to "湖南",
            "44" to "广东",
            "45" to "广西",
            "46" to "海南",
            "50" to "重庆",
            "51" to "四川",
            "52" to "贵州",
            "53" to "云南",
            "54" to "西藏",
            "61" to "陕西",
            "62" to "甘肃",
            "63" to "青海",
            "64" to "宁夏",
            "65" to "新疆",
            "71" to "台湾",
            "81" to "香港",
            "82" to "澳门",
            "91" to "国外"
    )

}