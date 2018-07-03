package com.lodz.android.corekt.network

import android.support.annotation.IntDef

/**
 * 手机网络信息
 * Created by zhouL on 2018/7/3.
 */
class NetInfo {

    companion object {
        /** 未连接网络  */
        const val NETWORK_TYPE_NONE = -1
        /** 未知网络（可能是有线）  */
        const val NETWORK_TYPE_UNKNOWN = 0
        /** WIFI  */
        const val NETWORK_TYPE_WIFI = 1
        /** 2G  */
        const val NETWORK_TYPE_2G = 2
        /** 3G  */
        const val NETWORK_TYPE_3G = 3
        /** 4G  */
        const val NETWORK_TYPE_4G = 4
    }

    @IntDef(NETWORK_TYPE_NONE, NETWORK_TYPE_UNKNOWN, NETWORK_TYPE_WIFI, NETWORK_TYPE_2G, NETWORK_TYPE_3G, NETWORK_TYPE_4G)
    @Retention(AnnotationRetention.SOURCE)
    annotation class NetType

    /** 网络类型 */
    @NetType
    var type = NETWORK_TYPE_NONE

    /** 网络制式 */
    var standard = NETWORK_TYPE_NONE

    /** 扩展信息 */
    var extraInfo = ""
}