package com.lodz.android.corekt.network

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.CellLocation
import android.telephony.TelephonyManager
import android.telephony.cdma.CdmaCellLocation
import android.telephony.gsm.GsmCellLocation

/**
 * 网络管理
 * Created by zhouL on 2018/7/3.
 */
class NetworkManager private constructor() {

    companion object {
        private val sInstance = NetworkManager()
        @JvmStatic
        fun get(): NetworkManager = sInstance
    }

    /** 网络广播 */
    private var mReceiver: ConnectBroadcastReceiver? = null
    /** 网络信息 */
    private val mNetInfo = NetInfo()
    /** 网络监听器 */
    private val mNetworkListeners = ArrayList<NetworkListener>()

    /** 注册网络监听广播 */
    fun init(context: Context) {
        try {
            val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            mReceiver = ConnectBroadcastReceiver()
            context.applicationContext.registerReceiver(mReceiver, filter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** 解除网络监听广播 */
    fun release(context: Context) {
        try {
            if (mReceiver != null) {
                context.applicationContext.unregisterReceiver(mReceiver)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** 网络是否可用 */
    fun isNetworkAvailable(): Boolean = mNetInfo.type != NetInfo.NETWORK_TYPE_NONE

    /** 是否在wifi下 */
    fun isWifi(): Boolean = mNetInfo.type == NetInfo.NETWORK_TYPE_WIFI

    /** 获取当前网络类型（未连接/WIFI/4G/3G/2G） */
    @NetInfo.NetType
    fun getNetType(): Int = mNetInfo.type

    /** 获取运营商代号（0：未知 1：移动 2：联通 3：电信） */
    @OperatorInfo.OperatorType
    fun getSimOperator(context: Context): Int {
        val telephonyManager = context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val opeator = telephonyManager.simOperator
        if (opeator.isNullOrEmpty()) {
            return OperatorInfo.OPERATOR_UNKNOWN
        }
        if (opeator.equals("46000") || opeator.equals("46002") || opeator.equals("46007")) {// 中国移动
            return OperatorInfo.OPERATOR_CMCC
        } else if (opeator.equals("46001")) {// 中国联通
            return OperatorInfo.OPERATOR_CUCC
        } else if (opeator.equals("46003")) {// 中国电信
            return OperatorInfo.OPERATOR_CTCC
        }
        // 未知
        return OperatorInfo.OPERATOR_UNKNOWN
    }

    /** 获取运营商（基站）信息 */
    @SuppressLint("MissingPermission")
    fun getOperatorInfo(context: Context): OperatorInfo? {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val info = OperatorInfo()
        info.type = getSimOperator(context)
        if (!info.isSuccess()) {
            return null
        }

        try {
            val location: CellLocation = telephonyManager.cellLocation ?: return null

            // 电信
            if (info.type == OperatorInfo.OPERATOR_CTCC) {
                if (location is CdmaCellLocation) {
                    val cdma: CdmaCellLocation = location
                    info.cid = cdma.baseStationId.toString()
                    info.lac = cdma.networkId.toString()
                    info.mcc = telephonyManager.networkOperator.substring(0, 3)
                    info.mnc = telephonyManager.networkOperator
                }
            }

            // 其他情况
            if (location is GsmCellLocation) {
                val gsm: GsmCellLocation = location
                info.cid = gsm.cid.toString()
                info.lac = gsm.lac.toString()
                info.mcc = telephonyManager.networkOperator.substring(0, 3)
                info.mnc = telephonyManager.networkOperator.substring(3)
                return info
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 更新网络信息 */
    @SuppressLint("MissingPermission")
    internal fun updateNet(manager: ConnectivityManager) {
        mNetInfo.type = NetInfo.NETWORK_TYPE_NONE
        mNetInfo.standard = NetInfo.NETWORK_TYPE_NONE
        var netInfo: NetworkInfo? = null
        try {
            netInfo = manager.activeNetworkInfo
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (netInfo == null || !netInfo.isAvailable) {//网络未连接
            return
        }

        var type = getType(netInfo)
        if (type == ConnectivityManager.TYPE_WIFI) {// wifi下
            mNetInfo.type = NetInfo.NETWORK_TYPE_WIFI
            mNetInfo.standard = NetInfo.NETWORK_TYPE_WIFI
            return
        }

        val subType = getSubType(netInfo)
        type = when (subType) {
            TelephonyManager.NETWORK_TYPE_1xRTT,
            TelephonyManager.NETWORK_TYPE_CDMA,
            TelephonyManager.NETWORK_TYPE_EDGE,
            TelephonyManager.NETWORK_TYPE_GPRS,
            TelephonyManager.NETWORK_TYPE_IDEN,
            TelephonyManager.NETWORK_TYPE_GSM
            -> NetInfo.NETWORK_TYPE_2G
            TelephonyManager.NETWORK_TYPE_EVDO_0,
            TelephonyManager.NETWORK_TYPE_EVDO_A,
            TelephonyManager.NETWORK_TYPE_HSDPA,
            TelephonyManager.NETWORK_TYPE_HSPA,
            TelephonyManager.NETWORK_TYPE_HSUPA,
            TelephonyManager.NETWORK_TYPE_UMTS,
            TelephonyManager.NETWORK_TYPE_EVDO_B,
            TelephonyManager.NETWORK_TYPE_EHRPD,
            TelephonyManager.NETWORK_TYPE_HSPAP,
            TelephonyManager.NETWORK_TYPE_TD_SCDMA,
            TelephonyManager.NETWORK_TYPE_IWLAN
            -> NetInfo.NETWORK_TYPE_3G
            TelephonyManager.NETWORK_TYPE_LTE,
            19
            -> NetInfo.NETWORK_TYPE_4G
            else -> NetInfo.NETWORK_TYPE_UNKNOWN
        }
        mNetInfo.type = type
        mNetInfo.standard = subType
        mNetInfo.extraInfo = netInfo.extraInfo
    }

    /** 从网络信息[info]中获取网络类型 */
    private fun getType(info: NetworkInfo): Int {
        try {
            //这里做类型判断防止一些ROM被修改这个类型不是int型
            val type: Any? = info.type
            if (type is Int) {
                return type.toInt()
            }
            if (type is String) {
                if (type.isNotEmpty()) {
                    return type.toInt()
                }
            }
            if (type is Double) {
                return type.toInt()
            }
            if (type is Float) {
                return type.toInt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return NetInfo.NETWORK_TYPE_UNKNOWN
    }

    /** 从网络信息[info]中获取网络制式 */
    private fun getSubType(info: NetworkInfo): Int {
        try {
            //这里做类型判断防止一些ROM被修改这个类型不是int型
            val type: Any? = info.subtype
            if (type is Int) {
                return type.toInt()
            }
            if (type is String) {
                if (type.isNotEmpty()) {
                    return type.toInt()
                }
            }
            if (type is Double) {
                return type.toInt()
            }
            if (type is Float) {
                return type.toInt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return TelephonyManager.NETWORK_TYPE_UNKNOWN
    }

    /** 添加网络监听器[listener] */
    fun addNetworkListener(listener: NetworkListener) {
        mNetworkListeners.add(listener)
    }

    /** 删除网络监听器[listener] */
    fun removeNetworkListener(listener: NetworkListener) {
        mNetworkListeners.remove(listener)
    }

    /** 清除所有监听器 */
    fun clearNetworkListener() {
        mNetworkListeners.clear()
    }

    /** 通知监听器回调 */
    internal fun notifyNetworkListeners() {
        val iterator = mNetworkListeners.iterator()
        while (iterator.hasNext()) {
            val listener: NetworkListener? = iterator.next()
            if (listener != null) {
                listener.onNetworkStatusChanged(isNetworkAvailable(), mNetInfo)
            } else {
                iterator.remove()
            }
        }
    }

    /** 网络监听器 */
    interface NetworkListener {
        /** 网络状态变化，网络是否可用[isNetworkAvailable]，网络信息[netInfo] */
        fun onNetworkStatusChanged(isNetworkAvailable: Boolean, netInfo: NetInfo)
    }
}
