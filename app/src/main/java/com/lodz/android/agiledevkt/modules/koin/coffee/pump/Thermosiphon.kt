package com.lodz.android.agiledevkt.modules.koin.coffee.pump

import com.lodz.android.agiledevkt.modules.koin.KoinManager
import com.lodz.android.corekt.log.PrintLog

/**
 * 热虹吸式泵
 * @author zhouL
 * @date 2020/12/9
 */
class Thermosiphon : Pump {

    override fun pump() {
        PrintLog.d(KoinManager.KOIN_TAG, "Thermosiphon 开始注水")
    }

}