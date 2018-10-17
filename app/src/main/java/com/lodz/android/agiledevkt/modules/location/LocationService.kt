package com.lodz.android.agiledevkt.modules.location

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * 定位服务
 * Created by zhouL on 2018/10/17.
 */
class LocationService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}