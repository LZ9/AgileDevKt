package com.lodz.android.agiledevkt.modules.koin

import android.content.Context
import com.lodz.android.agiledevkt.bean.SpotBean
import com.lodz.android.agiledevkt.modules.koin.coffee.CoffeeMaker
import com.lodz.android.agiledevkt.modules.koin.coffee.heater.ElectricHeater
import com.lodz.android.agiledevkt.modules.koin.coffee.heater.Heater
import com.lodz.android.agiledevkt.modules.koin.coffee.heater.PowerElectricHeater
import com.lodz.android.agiledevkt.modules.koin.coffee.pump.Pump
import com.lodz.android.agiledevkt.modules.koin.coffee.pump.Thermosiphon
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Koin初始化类
 * @author zhouL
 * @date 2020/12/9
 */
//object KoinManager {
//
//    const val KOIN_TAG = "koin_tag"
//
//    private val modules = module {
//        single(named("blz")) {
//            SpotBean("白鹭洲", "5")
//        }
//        factory(named("hdl")) { (score: String) ->
//            SpotBean("环岛路", score)
//        }
//
//
//        factory {
//            CoffeeMaker(get(), get())
//        }
//        factory<Pump> {
//            Thermosiphon()
//        }
//        factory<Heater> {
//            ElectricHeater()
//        }
//
//
//        factory(named("high")) {
//            CoffeeMaker(get(), get(named("high")))
//        }
//        factory<Heater>(named("high")) {
//            PowerElectricHeater(3000)
//        }
//
//        factory(named("low")) {
//            CoffeeMaker(get(), get(named("low")))
//        }
//        factory<Heater>(named("low")) {
//            PowerElectricHeater(1000)
//        }
//    }
//
//    fun koinInit(context: Context) {
//        startKoin {
//            androidLogger(Level.DEBUG)
//            androidContext(context)
//            androidFileProperties()
//            modules(listOf(modules))
//        }
//    }
//}