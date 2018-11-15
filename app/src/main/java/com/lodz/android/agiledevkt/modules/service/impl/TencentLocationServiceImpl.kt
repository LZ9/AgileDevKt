package com.lodz.android.agiledevkt.modules.service.impl

import android.content.Context
import android.content.Intent
import com.lodz.android.agiledevkt.App
import com.lodz.android.agiledevkt.modules.location.LocationTestActivity
import com.lodz.android.agiledevkt.modules.location.LocationUpdateEvent
import com.lodz.android.agiledevkt.modules.service.ServiceContract
import com.lodz.android.corekt.network.NetworkManager
import com.tencent.map.geolocation.TencentLocation
import com.tencent.map.geolocation.TencentLocationListener
import com.tencent.map.geolocation.TencentLocationManager
import com.tencent.map.geolocation.TencentLocationRequest
import org.greenrobot.eventbus.EventBus

/**
 * 腾讯定位服务
 * Created by zhouL on 2018/11/15.
 */
class TencentLocationServiceImpl : ServiceContract {

    /** 注册腾讯定位成功 */
    private val REQUEST_LOCATION_OK = 0

    /** 上下文 */
    private lateinit var mContext: Context
    /** 监听器回调 */
    private var mTencentLocationListener: TencentLocationListener? = null

    override fun onCreate(context: Context) {
        mContext = context
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) {
        if (intent != null) {
            val type = intent.getIntExtra(LocationTestActivity.EXTRA_TYPE, -1)
            if (type != LocationTestActivity.LOCATION_TENCENT) {
                return
            }
        }

        if (mTencentLocationListener != null) {// 说明已经注册过了定位监听
            return
        }

        mTencentLocationListener = object : TencentLocationListener {
            override fun onLocationChanged(location: TencentLocation?, type: Int, reason: String?) {
                if (type == TencentLocation.ERROR_OK && location != null) {// 定位成功

                    val longitude = location.longitude.toString() // 经度
                    val latitude = location.latitude.toString() // 纬度
                    val info = NetworkManager.get().getOperatorInfo(App.get())
                    val mcc = if (info == null) "" else info.mcc
                    val mnc = if (info == null) "" else info.mnc
                    val lac = if (info == null) "" else info.lac
                    val cid = if (info == null) "" else info.cid

                    val bundle = location.extra
                    val log = if (bundle == null) "" else bundle.getString("resp_json")

                    EventBus.getDefault().post(LocationUpdateEvent(true, longitude, latitude, mcc, mnc, lac, cid, log))
                }else if (type == TencentLocation.ERROR_UNKNOWN) {// android8.1需要打开定位开关才能定位到
                    val log = "定位失败，如果您是Android8.1以上版本，请打开GPS定位开关，失败类型：$type，原因：$reason"
                    EventBus.getDefault().post(LocationUpdateEvent(false, "", "", "", "", "", "", log))
                }else{// 定位失败
                    val log = "定位失败，失败类型：$type，原因：$reason"
                    EventBus.getDefault().post(LocationUpdateEvent(false, "", "", "", "", "", "", log))
                }
            }

            override fun onStatusUpdate(name: String?, status: Int, desc: String?) {
                val log = "定位状态变化，名称：$name，状态：$status，原因：$desc"
                EventBus.getDefault().post(LocationUpdateEvent(false, "", "", "", "", "", "", log))
            }
        }

        val request = TencentLocationRequest.create()
        request.interval = LocationTestActivity.LOCATION_INTERVAL_TIME
        // 数据级别
        // TencentLocationRequest.REQUEST_LEVEL_GEO 包含经纬度
        // TencentLocationRequest.REQUEST_LEVEL_NAME 包含经纬度, 位置名称, 位置地址
        // TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA 包含经纬度，位置所处的中国大陆行政区划
        // TencentLocationRequest.REQUEST_LEVEL_POI 包含经纬度，位置所处的中国大陆行政区划及周边POI列表
        request.requestLevel = TencentLocationRequest.REQUEST_LEVEL_NAME

        val locationManager = TencentLocationManager.getInstance(mContext)
        val result = locationManager.requestLocationUpdates(request, mTencentLocationListener)
        if (result != REQUEST_LOCATION_OK) {
            EventBus.getDefault().post(LocationUpdateEvent(false, "", "", "", "", "", "", "腾讯定位注册失败"))
            return
        }
        EventBus.getDefault().post(LocationUpdateEvent(false, "", "", "", "", "", "", "腾讯定位注册成功"))
    }

    override fun onDestroy() {
        if (mTencentLocationListener != null) {
            TencentLocationManager.getInstance(mContext).removeUpdates(mTencentLocationListener)
            mTencentLocationListener = null
        }
    }
}