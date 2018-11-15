package com.lodz.android.agiledevkt.modules.service.impl

import android.content.Context
import android.content.Intent
import com.lodz.android.agiledevkt.modules.location.LocationTestActivity
import com.lodz.android.agiledevkt.modules.service.ServiceContract

/**
 * 高德定位服务
 * Created by zhouL on 2018/11/15.
 */
class AMapLocationServiceImpl : ServiceContract {
    override fun onCreate(context: Context) {


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) {
        if (intent != null){
            val type = intent.getIntExtra(LocationTestActivity.EXTRA_TYPE, -1)
            if (type != LocationTestActivity.LOCATION_AMAP){
                return
            }
        }

    }

    override fun onDestroy() {

    }
}