package com.lodz.android.componentkt.base.application

import android.app.Application
import android.os.Bundle
import com.lodz.android.componentkt.base.application.config.BaseLayoutConfig
import com.lodz.android.componentkt.event.ActivityFinishEvent
import org.greenrobot.eventbus.EventBus

/**
 * 基类Application
 * Created by zhouL on 2018/6/25.
 */
abstract class BaseApplication : Application() {

    companion object {
        private var sInstance: BaseApplication? = null
        fun get() = sInstance
    }

    /** 基础控件配置 */
    private lateinit var mBaseLayoutConfig: BaseLayoutConfig
    /** 保存回收前数据的Bundle */
    private var mRestoreBundle: Bundle? = null

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        mBaseLayoutConfig = BaseLayoutConfig()
        onStartCreate()
    }

    /** 获取基础控件配置 */
    fun getBaseLayoutConfig() = mBaseLayoutConfig

    /** 初始化回调 */
    abstract fun onStartCreate()

    /** 关闭所有Activity */
    fun finishActivities() {
        EventBus.getDefault().post(ActivityFinishEvent())// 发送关闭事件
    }

    /** 退出app  */
    fun exit() {
        finishActivities()
        onExit()
//        System.exit(0)// 退出整个应用
    }

    /** 退出回调 */
    abstract fun onExit()

    /** 当APP在后台被回收时可以调用该方法保存关键数据 */
    fun putSaveInstanceState(bundle: Bundle) {
        mRestoreBundle = bundle
    }

    /** 当APP被回收后从后台回到前台时调用该方法获取保存的关键数据 */
    fun getSaveInstanceState(): Bundle? = mRestoreBundle

}