package com.lodz.android.agiledevkt.modules.koin.coffee.heater


/**
 * 电子加热器
 * @author zhouL
 * @date 2020/12/9
 */
open class ElectricHeater : Heater {

    private var isHot = false

    override fun on() {
        isHot = true
//        PrintLog.v(KoinManager.KOIN_TAG, "ElectricHeater 打开电子加热器")
    }

    override fun off() {
        isHot = false
//        PrintLog.v(KoinManager.KOIN_TAG, "ElectricHeater 关闭电子加热器")
    }

    override fun isHot(): Boolean = isHot
}