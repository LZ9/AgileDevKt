package com.lodz.android.agiledevkt.modules.service

import android.content.Context
import android.content.Intent

/**
 * 总线服务接口
 * Created by zhouL on 2018/11/15.
 */
interface ServiceContract {

    fun onCreate(context: Context)

    fun onStartCommand(intent: Intent?, flags: Int, startId: Int)

    fun onDestroy()
}