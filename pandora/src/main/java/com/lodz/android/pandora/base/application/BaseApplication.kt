package com.lodz.android.pandora.base.application

import android.app.Application
import android.os.Bundle
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.lodz.android.pandora.base.application.config.BaseLayoutConfig
import com.lodz.android.pandora.event.ActivityFinishEvent
import org.greenrobot.eventbus.EventBus

/**
 * 基类Application
 * Created by zhouL on 2018/6/25.
 */
abstract class BaseApplication : Application() {

    companion object {
        const val ERROR_TAG = "error_tag"
        private var sInstance: BaseApplication? = null
        @JvmStatic
        fun get(): BaseApplication? = sInstance
    }

    /** 基础控件配置 */
    private lateinit var mPdrBaseLayoutConfig: BaseLayoutConfig
    /** 保存回收前数据的Bundle */
    private var mPdrRestoreMap = HashMap<String, Bundle?>()

    private val mObjectMapper = ObjectMapper()

    private var mWatermarkContent = ""

    final override fun onCreate() {
        super.onCreate()
        sInstance = this
        mPdrBaseLayoutConfig = BaseLayoutConfig()
        configJackson(mObjectMapper)
        onStartCreate()
    }

    /** 配置Jackson */
    protected open fun configJackson(objectMapper: ObjectMapper) {
        // 忽略对象中未映射的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        //忽略大小写
//        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
    }

    fun getJacksonObjectMapper() = mObjectMapper

    /** 配置水印内容 */
    fun setWatermarkContent(content: String) {
        mWatermarkContent = content
    }

    /** 获取水印内容 */
    fun getWatermarkContent() = mWatermarkContent

    /** 获取基础控件配置 */
    fun getBaseLayoutConfig(): BaseLayoutConfig = mPdrBaseLayoutConfig

    /** 初始化回调 */
    abstract fun onStartCreate()

    /** 关闭所有Activity */
    fun finishActivities() {
        EventBus.getDefault().post(ActivityFinishEvent())// 发送关闭事件
    }

    /** 退出app */
    fun exit() {
        finishActivities()
        onExit()
//        System.exit(0)// 退出整个应用
    }

    /** 退出回调 */
    abstract fun onExit()

    /** 当APP在后台被回收时可以调用该方法保存关键数据 */
    fun putSaveInstanceState(key: String, bundle: Bundle) {
        mPdrRestoreMap[key] = bundle
    }

    /** 当APP被回收后从后台回到前台时调用该方法获取保存的关键数据 */
    fun getSaveInstanceState(key: String): Bundle? = mPdrRestoreMap[key]

}