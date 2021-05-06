package com.lodz.android.agiledevkt.modules.koin.coffee

import com.lodz.android.agiledevkt.modules.koin.coffee.heater.Heater
import com.lodz.android.agiledevkt.modules.koin.coffee.pump.Pump
import com.lodz.android.corekt.log.PrintLog

/**
 * 咖啡机
 * @author zhouL
 * @date 2020/12/9
 */
class CoffeeMaker(private val pump: Pump, private val heater: Heater) {

    /** 煮咖啡 */
    fun brew() {
        heater.on()
        val isSuucess =
            if (heater.isHot()) {
//                PrintLog.i(KoinManager.KOIN_TAG, "CoffeeMaker 加热器已打开")
                pump.pump()
                true
            } else {
//                PrintLog.e(KoinManager.KOIN_TAG, "CoffeeMaker 加热器已关闭，告警")
                false
            }
        if (isSuucess) {
//            PrintLog.i(KoinManager.KOIN_TAG, "CoffeeMaker 完成咖啡 ")
        }
        heater.off()
    }
}