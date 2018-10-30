package com.lodz.android.agiledevkt.modules.location

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.lodz.android.agiledevkt.App
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.config.Constant
import com.lodz.android.corekt.network.NetworkManager
import org.greenrobot.eventbus.EventBus

/**
 * 定位服务
 * Created by zhouL on 2018/10/17.
 */
class LocationService : Service() {


    companion object {
        /** 定位服务通知id  */
        const val LOCATION_SERVICE_NOTIFY_ID = 777777
        /** 间隔时间  */
        const val INTERVAL_TIME = 2 * 1000L
    }

    private var mLocationManager: LocationManager? = null

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
        if (mLocationManager != null) {
            return super.onStartCommand(intent, flags, startId)
        }
        startForeground(LocationService.LOCATION_SERVICE_NOTIFY_ID, getNotification())// 启动前台通知

        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (mLocationManager == null) {
            return super.onStartCommand(intent, flags, startId)
        }

        if (!mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {// 用户没有开GPS定位
            return super.onStartCommand(intent, flags, startId)
        }
        startLocation(mLocationManager!!)
        return super.onStartCommand(intent, flags, startId)
    }

    private val mLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            if (location == null) {
                return
            }

            val longitude = location.longitude.toString() // 经度
            val latitude = location.latitude.toString() // 纬度
            val info = NetworkManager.get().getOperatorInfo(App.get())
            val mcc = if (info == null) "" else info.mcc
            val mnc = if (info == null) "" else info.mnc
            val lac = if (info == null) "" else info.lac
            val cid = if (info == null) "" else info.cid
            val log = "更新成功"

            EventBus.getDefault().post(LocationUpdateEvent(true, longitude, latitude, mcc, mnc, lac, cid, log))
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            val log = "onStatusChanged   --->   provider : $provider    status : $status"
            EventBus.getDefault().post(LocationUpdateEvent(false, "", "", "", "", "", "", log))
        }

        override fun onProviderEnabled(provider: String?) {
            val log = "onProviderEnabled   --->   provider : $provider"
            EventBus.getDefault().post(LocationUpdateEvent(false, "", "", "", "", "", "", log))
        }

        override fun onProviderDisabled(provider: String?) {
            val log = "onProviderDisabled   --->   provider : $provider"
            EventBus.getDefault().post(LocationUpdateEvent(false, "", "", "", "", "", "", log))
        }

    }

    @SuppressLint("MissingPermission")
    private fun startLocation(locationManager: LocationManager) {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LocationService.INTERVAL_TIME, 0f, mLocationListener)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        if (mLocationManager != null) {
            mLocationManager!!.removeUpdates(mLocationListener)//销毁时先移除监听器
        }
        mLocationManager = null
        super.onDestroy()
    }
}