package com.lodz.android.agiledevkt.modules.koin

import android.content.Context
import com.lodz.android.agiledevkt.bean.SpotBean
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
object KoinManager {

    private val girlModule = module {
        single(named("blz")) {
            SpotBean("白鹭洲", "5")
        }
        factory(named("hdl")) { (score: String) ->
            SpotBean("环岛路", score)
        }
    }

    fun koinInit(context: Context) {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(context)
            androidFileProperties()
            modules(listOf(girlModule))
        }
    }
}