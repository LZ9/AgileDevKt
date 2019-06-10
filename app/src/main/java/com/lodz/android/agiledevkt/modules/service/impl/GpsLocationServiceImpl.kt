package com.lodz.android.agiledevkt.modules.service.impl

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.lodz.android.agiledevkt.App
import com.lodz.android.agiledevkt.modules.location.LocationTestActivity
import com.lodz.android.agiledevkt.modules.location.LocationUpdateEvent
import com.lodz.android.agiledevkt.modules.service.ServiceContract
import com.lodz.android.corekt.network.NetworkManager
import org.greenrobot.eventbus.EventBus

/**
 * 原生GPS定位服务
 * Created by zhouL on 2018/11/15.
 */
class GpsLocationServiceImpl : ServiceContract {

    private lateinit var mContext:Context

    private var mLocationManager: LocationManager? = null

    override fun onCreate(context: Context) {
        mContext = context
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) {
        if (intent != null){
            val type = intent.getIntExtra(LocationTestActivity.EXTRA_TYPE, -1)
            if (type != LocationTestActivity.LOCATION_GPS){
                return
            }
        }

        if (mLocationManager != null) {
            return
        }

        mLocationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (mLocationManager == null) {
            return
        }

        if (!mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)){// 用户没有开GPS定位
            return
        }
        startLocation(mLocationManager!!)
    }


    @SuppressLint("MissingPermission")
    private fun startLocation(locationManager: LocationManager) {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LocationTestActivity.LOCATION_INTERVAL_TIME, 0f, mLocationListener)
    }

    private val mLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            if (location == null) {
                EventBus.getDefault().post(LocationUpdateEvent(false, "", "", "", "", "", "", "定位失败，定位结果为null"))
                return
            }

            val longitude = location.longitude.toString() // 经度
            val latitude = location.latitude.toString() // 纬度
            val info = NetworkManager.get().getOperatorInfo(App.get())
            val mcc = info?.mcc ?: ""
            val mnc = info?.mnc ?: ""
            val lac = info?.lac ?: ""
            val cid = info?.cid ?: ""
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

    override fun onDestroy() {
        if (mLocationManager != null) {
            mLocationManager!!.removeUpdates(mLocationListener)//销毁时先移除监听器
        }
        mLocationManager = null
    }
}