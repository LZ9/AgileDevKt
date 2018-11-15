package com.lodz.android.agiledevkt.modules.service.impl

import android.content.Context
import android.content.Intent
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.lodz.android.agiledevkt.App
import com.lodz.android.agiledevkt.modules.location.LocationTestActivity
import com.lodz.android.agiledevkt.modules.location.LocationUpdateEvent
import com.lodz.android.agiledevkt.modules.service.ServiceContract
import com.lodz.android.corekt.network.NetworkManager
import org.greenrobot.eventbus.EventBus

/**
 * 高德定位服务
 * Created by zhouL on 2018/11/15.
 */
class AMapLocationServiceImpl : ServiceContract {

    /** 超时时间  */
    private val TIME_OUT = 30 * 1000L
    /** 高德定位成功  */
    private val REQUEST_LOCATION_OK = 0

    /** 上下文  */
    private lateinit var mContext: Context
    /** 高德定位客户端  */
    private var mAMapLocationClient: AMapLocationClient? = null

    override fun onCreate(context: Context) {
        mContext = context
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) {
        if (intent != null) {
            val type = intent.getIntExtra(LocationTestActivity.EXTRA_TYPE, -1)
            if (type != LocationTestActivity.LOCATION_AMAP) {
                return
            }
        }

        if (mAMapLocationClient != null) {// 说明已经注册过了定位监听
            return
        }

        mAMapLocationClient = AMapLocationClient(mContext.applicationContext)
        mAMapLocationClient!!.setLocationOption(getLocationOption())
        mAMapLocationClient!!.setLocationListener(mAMapLocationListener)
        mAMapLocationClient!!.startLocation()
    }

    private fun getLocationOption(): AMapLocationClientOption {
        val option = AMapLocationClientOption()
        option.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        option.isGpsFirst = true//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        option.httpTimeOut = TIME_OUT//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        option.interval = LocationTestActivity.LOCATION_INTERVAL_TIME//可选，设置定位间隔。默认为2秒
        option.isNeedAddress = true//可选，设置是否返回逆地理地址信息。默认是true
        option.isOnceLocation = false//可选，设置是否单次定位。默认是false
        option.isOnceLocationLatest = false//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP)//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        option.isSensorEnable = false//可选，设置是否使用传感器。默认是false
        option.isWifiScan = true//可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        option.isLocationCacheEnable = true//可选，设置是否使用缓存定位，默认为true
        option.geoLanguage = AMapLocationClientOption.GeoLanguage.ZH//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return option
    }

    private val mAMapLocationListener = object : AMapLocationListener {
        override fun onLocationChanged(location: AMapLocation?) {
            if (null == location) {
                EventBus.getDefault().post(LocationUpdateEvent(false, "", "", "", "", "", "", "定位失败，定位结果为null"))
                return
            }
            if (location.getErrorCode() != REQUEST_LOCATION_OK) {
                val log = "定位失败，类型：${location.errorCode}，原因：${location.locationDetail}"
                EventBus.getDefault().post(LocationUpdateEvent(false, "", "", "", "", "", "", log))
                return
            }

            val longitude = location.longitude.toString() // 经度
            val latitude = location.latitude.toString() // 纬度
            val info = NetworkManager.get().getOperatorInfo(App.get())
            val mcc = if (info == null) "" else info.mcc
            val mnc = if (info == null) "" else info.mnc
            val lac = if (info == null) "" else info.lac
            val cid = if (info == null) "" else info.cid
            val log = location.toStr()

            EventBus.getDefault().post(LocationUpdateEvent(true, longitude, latitude, mcc, mnc, lac, cid, log))
        }
    }

    override fun onDestroy() {
        if (mAMapLocationClient != null) {
            mAMapLocationClient!!.stopLocation()
            mAMapLocationClient!!.unRegisterLocationListener(mAMapLocationListener)
            mAMapLocationClient!!.onDestroy()
            mAMapLocationClient = null
        }
    }
}