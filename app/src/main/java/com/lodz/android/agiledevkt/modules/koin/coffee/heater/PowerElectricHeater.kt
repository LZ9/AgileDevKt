package com.lodz.android.agiledevkt.modules.koin.coffee.heater

import com.lodz.android.agiledevkt.modules.koin.KoinManager
import com.lodz.android.corekt.log.PrintLog

/**
 * 功率电子加热器
 * @author zhouL
 * @date 2020/12/9
 */
class PowerElectricHeater(val power: Int) :ElectricHeater(){

    override fun on() {
        super.on()
        PrintLog.v(KoinManager.KOIN_TAG, "PowerElectricHeater 功率：${power}瓦")
    }

}