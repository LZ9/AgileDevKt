package com.lodz.android.agiledevkt.modules.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.lodz.android.agiledevkt.App
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.config.Constant
import com.lodz.android.agiledevkt.modules.service.impl.AMapLocationServiceImpl
import com.lodz.android.agiledevkt.modules.service.impl.GpsLocationServiceImpl
import com.lodz.android.agiledevkt.modules.service.impl.TencentLocationServiceImpl

/**
 * 服务总线
 * Created by zhouL on 2018/11/15.
 */
class BusService :Service(){

    /** 定位服务通知id */
    private val SERVICE_NOTIFY_ID = 777777
    /** 服务队列 */
    private val mServiceList = ArrayList<ServiceContract>()

    override fun onCreate() {
        super.onCreate()
        if (mServiceList.size == 0) {
            mServiceList.add(GpsLocationServiceImpl())
            mServiceList.add(TencentLocationServiceImpl())
            mServiceList.add(AMapLocationServiceImpl())
        }
        for (contract in mServiceList) {
            contract.onCreate(this)
        }
    }

    /** 获取通知 */
    private fun getNotification(): Notification {
        val title = App.get().getString(R.string.location_foreground_service_title)
        val content = App.get().getString(R.string.location_foreground_service_tips)

        val builder = NotificationCompat.Builder(App.get(), Constant.NOTIFI_CHANNEL_SERVICE_ID)
        builder.setTicker(title)// 通知栏显示的文字
        builder.setContentTitle(title)// 通知栏通知的标题
        builder.setContentText(content)// 通知栏通知的详细内容（只有一行）
        builder.setAutoCancel(false)// 设置为true，点击该条通知会自动删除，false时只能通过滑动来删除（一般都是true）
        builder.setSmallIcon(R.mipmap.ic_launcher)//通知上面的小图标（必传）
        builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS)//通知默认的声音 震动 呼吸灯
        builder.priority = NotificationCompat.PRIORITY_MAX//设置优先级，级别高的排在前面
        return builder.build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(SERVICE_NOTIFY_ID, getNotification())// 启动前台通知
        if (mServiceList.size > 0) {
            for (contract in mServiceList) {
                contract.onStartCommand(intent, flags, startId)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        if (mServiceList.size > 0) {
            for (contract in mServiceList) {
                contract.onDestroy()
            }
            mServiceList.clear()
        }
        super.onDestroy()
    }
}