package com.lodz.android.agiledevkt

import android.app.Application
import kotlin.properties.Delegates

/**
 * Application
 * Created by zhouL on 2018/6/20.
 */
class App : Application() {

    companion object {
        private var instance: App by Delegates.notNull()
        fun get() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}