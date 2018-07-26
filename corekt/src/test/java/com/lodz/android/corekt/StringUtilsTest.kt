package com.lodz.android.corekt

import com.lodz.android.corekt.utils.StringUtils
import org.junit.Assert
import org.junit.Test

/**
 * 字符串帮助类单元测试
 * Created by zhouL on 2018/7/26.
 */
class StringUtilsTest {

    private val ORIGINAL_STR = "测试asdqw123@#!$%<>?}*/-{}:"

    private val ORIGINAL_SEPARATOR_STR = "asd,wqe,wrdf,cx,123,^&,"
    private val ORIGINAL_SEPARATOR = ","
    private val ORIGINAL_SEPARATOR_STR_RESULT = listOf("asd", "wqe", "wrdf", "cx", "123", "^&")

    private val ORIGINAL_LIST = listOf("dwe", "123", "&*%&", "@[]<48", "")
    private val ORIGINAL_LIST_SEPARATOR = "-"
    private val ORIGINAL_LIST_RESULT = "dwe-123-&*%&-@[]<48-"

    @Test
    fun utf8Test() {
        val encode = StringUtils.encodeUtf8(ORIGINAL_STR)
        Assert.assertEquals(ORIGINAL_STR, StringUtils.decodeUtf8(encode))
    }

    @Test
    fun getListBySeparatorTest() {
        Assert.assertEquals(ORIGINAL_SEPARATOR_STR_RESULT, StringUtils.getListBySeparator(ORIGINAL_SEPARATOR_STR, ORIGINAL_SEPARATOR))
    }

    @Test
    fun getStringBySeparatorTest() {
        Assert.assertEquals(ORIGINAL_LIST_RESULT, StringUtils.getStringBySeparator(ORIGINAL_LIST, ORIGINAL_LIST_SEPARATOR))
    }
}