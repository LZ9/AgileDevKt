package com.lodz.android.corekt

import com.lodz.android.corekt.anko.AnkoNumFormat
import com.lodz.android.corekt.anko.format
import org.junit.Assert
import org.junit.Test

/**
 * 数字格式化单元测试
 * Created by zhouL on 2018/7/27.
 */
class NumFormatTest {

    private val FORMAT_NUM_ONE = 18.49382716
    private val FORMAT_NUM_TWO = 9.51839461.toFloat()
    private val FORMAT_NUM_THREE = 0.45179283

    @Test
    fun formatTest1() {
        Assert.assertEquals("18.5", FORMAT_NUM_ONE.format(AnkoNumFormat.TYPE_ONE_DECIMAL))
        Assert.assertEquals("18.49", FORMAT_NUM_ONE.format(AnkoNumFormat.TYPE_TWO_DECIMAL))
        Assert.assertEquals("18.494", FORMAT_NUM_ONE.format(AnkoNumFormat.TYPE_THREE_DECIMAL))
        Assert.assertEquals("18.4938", FORMAT_NUM_ONE.format(AnkoNumFormat.TYPE_FOUR_DECIMAL))
        Assert.assertEquals("18.49383", FORMAT_NUM_ONE.format(AnkoNumFormat.TYPE_FIVE_DECIMAL))
    }

    @Test
    fun formatTest2() {
        Assert.assertEquals("9.5", FORMAT_NUM_TWO.format(AnkoNumFormat.TYPE_ONE_DECIMAL))
        Assert.assertEquals("9.52", FORMAT_NUM_TWO.format(AnkoNumFormat.TYPE_TWO_DECIMAL))
        Assert.assertEquals("9.518", FORMAT_NUM_TWO.format(AnkoNumFormat.TYPE_THREE_DECIMAL))
        Assert.assertEquals("9.5184", FORMAT_NUM_TWO.format(AnkoNumFormat.TYPE_FOUR_DECIMAL))
        Assert.assertEquals("9.51839", FORMAT_NUM_TWO.format(AnkoNumFormat.TYPE_FIVE_DECIMAL))
    }

    @Test
    fun formatTest3() {
        Assert.assertEquals("0.5", FORMAT_NUM_THREE.format(AnkoNumFormat.TYPE_ONE_DECIMAL))
        Assert.assertEquals("0.45", FORMAT_NUM_THREE.format(AnkoNumFormat.TYPE_TWO_DECIMAL))
        Assert.assertEquals("0.452", FORMAT_NUM_THREE.format(AnkoNumFormat.TYPE_THREE_DECIMAL))
        Assert.assertEquals("0.4518", FORMAT_NUM_THREE.format(AnkoNumFormat.TYPE_FOUR_DECIMAL))
        Assert.assertEquals("0.45179", FORMAT_NUM_THREE.format(AnkoNumFormat.TYPE_FIVE_DECIMAL))
    }

}