package com.lodz.android.agiledevkt.modules.koin.coffee.heater

/**
 * 加热器
 * @author zhouL
 * @date 2020/12/9
 */
interface Heater {

    /** 开 */
    fun on()
    /** 关 */
    fun off()
    /** 是否加热完成 */
    fun isHot(): Boolean
}