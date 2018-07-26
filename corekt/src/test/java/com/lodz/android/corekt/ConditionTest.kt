package com.lodz.android.corekt

import com.lodz.android.corekt.anko.then
import org.junit.Assert
import org.junit.Test

/**
 * 三目运算符条件判断单元测试
 * Created by zhouL on 2018/7/26.
 */
class ConditionTest {

    @Test
    fun conditionTest1() {
        Assert.assertEquals(2, false.then { 1 } ?: 2)
    }

    @Test
    fun conditionTest2() {
        val i = 5
        Assert.assertEquals(10, (i < 7).then { i + 5 } ?: i-2)
    }

    @Test
    fun conditionTest3() {
        val i = 5
        Assert.assertEquals(13, (i < 6).then { getNum(i + 1) } ?: getNum(i - 1))
    }

    private fun getNum(index: Int) = index + 7
}