package com.lodz.android.corekt.network

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import android.os.ext.SdkExtensions
import android.telephony.CellLocation
import android.telephony.TelephonyManager
import android.telephony.cdma.CdmaCellLocation
import android.telephony.gsm.GsmCellLocation
import androidx.annotation.RequiresPermission

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

    /** 网络监听器 */
    private val mNetworkListeners = ArrayList<ConnectivityManager.NetworkCallback>()
    /** 7.0及以上网络状态管理器 */
    private var mConnectivityManager: ConnectivityManager? = null
    /** 网络是否可用 */
    private var isNetworkAvailable = false
    /** 网络功能 */
    private var mNetworkCapabilities: NetworkCapabilities? = null

    /** 注册网络监听广播 */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun init(context: Context, request: NetworkRequest? = null) {
        mConnectivityManager = context.getSystemService(ConnectivityManager::class.java)
        if (request != null) {
            mConnectivityManager?.registerNetworkCallback(request, mNetworkCallback)
        } else {
            mConnectivityManager?.registerDefaultNetworkCallback(mNetworkCallback)
        }
    }

    /** 解除网络监听广播 */
    fun release() {
        mConnectivityManager?.unregisterNetworkCallback(mNetworkCallback)
    }

    /** 网络是否可用 */
    fun isNetworkAvailable(): Boolean = mNetworkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false

    /** 是否在wifi下 */
    fun isWifi(): Boolean = mNetworkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false

    /** 获取当前网络功能对象 */
    fun getNetworkCapabilities(): NetworkCapabilities? = mNetworkCapabilities

    /** 获取运营商代号（0：未知 1：移动 2：联通 3：电信） */
    @OperatorInfo.OperatorType
    fun getSimOperator(context: Context): Int {
        val telephonyManager = context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val opeator = telephonyManager.simOperator
        if (opeator.isNullOrEmpty()) {
            return OperatorInfo.OPERATOR_UNKNOWN
        }
        if (opeator == "46000" || opeator == "46002" || opeator == "46007") {// 中国移动
            return OperatorInfo.OPERATOR_CMCC
        } else if (opeator == "46001") {// 中国联通
            return OperatorInfo.OPERATOR_CUCC
        } else if (opeator == "46003" || opeator == "46011") {// 中国电信
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
            if (info.type == OperatorInfo.OPERATOR_CTCC && location is CdmaCellLocation) {
                val cdma: CdmaCellLocation = location
                info.cid = cdma.baseStationId.toString()
                info.lac = cdma.networkId.toString()
                info.mcc = telephonyManager.networkOperator.substring(0, 3)
                info.mnc = telephonyManager.networkOperator
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


    /** 添加网络监听器[listener] */
    fun addNetworkListener(listener: ConnectivityManager.NetworkCallback) {
        mNetworkListeners.add(listener)
    }

    /** 删除网络监听器[listener] */
    fun removeNetworkListener(listener: ConnectivityManager.NetworkCallback) {
        mNetworkListeners.remove(listener)
    }

    /** 清除所有监听器 */
    fun clearNetworkListener() {
        mNetworkListeners.clear()
    }


    val mNetworkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            isNetworkAvailable = true
            notifyOnAvailable(network)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            isNetworkAvailable = false
            notifyOnLost(network)
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            mNetworkCapabilities = networkCapabilities
            isNetworkAvailable = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            notifyOnCapabilitiesChanged(network, networkCapabilities)
        }

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            super.onBlockedStatusChanged(network, blocked)
            notifyOnBlockedStatusChanged(network, blocked)
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties)
            notifyOnLinkPropertiesChanged(network, linkProperties)
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            notifyOnLosing(network, maxMsToLive)
        }

        override fun onReserved(networkCapabilities: NetworkCapabilities) {
            super.onReserved(networkCapabilities)
            notifyOnReserved(networkCapabilities)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            notifyOnUnavailable()
        }

    }

    private fun notifyOnAvailable(network: Network) {
        val iterator = mNetworkListeners.iterator()
        while (iterator.hasNext()) {
            val listener: ConnectivityManager.NetworkCallback = iterator.next()
            listener.onAvailable(network)
        }
    }

    private fun notifyOnLost(network: Network) {
        val iterator = mNetworkListeners.iterator()
        while (iterator.hasNext()) {
            val listener: ConnectivityManager.NetworkCallback = iterator.next()
            listener.onLost(network)
        }
    }


    private fun notifyOnCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        val iterator = mNetworkListeners.iterator()
        while (iterator.hasNext()) {
            val listener: ConnectivityManager.NetworkCallback = iterator.next()
            listener.onCapabilitiesChanged(network, networkCapabilities)
        }
    }

    private fun notifyOnBlockedStatusChanged(network: Network, blocked: Boolean) {
        val iterator = mNetworkListeners.iterator()
        while (iterator.hasNext()) {
            val listener: ConnectivityManager.NetworkCallback = iterator.next()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                listener.onBlockedStatusChanged(network, blocked)
            }
        }
    }

    private fun notifyOnLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        val iterator = mNetworkListeners.iterator()
        while (iterator.hasNext()) {
            val listener: ConnectivityManager.NetworkCallback = iterator.next()
            listener.onLinkPropertiesChanged(network, linkProperties)
        }
    }

    private fun notifyOnLosing(network: Network, maxMsToLive: Int) {
        val iterator = mNetworkListeners.iterator()
        while (iterator.hasNext()) {
            val listener: ConnectivityManager.NetworkCallback = iterator.next()
            listener.onLosing(network, maxMsToLive)
        }
    }

    private fun notifyOnReserved(networkCapabilities: NetworkCapabilities) {
        val iterator = mNetworkListeners.iterator()
        while (iterator.hasNext()) {
            val listener: ConnectivityManager.NetworkCallback = iterator.next()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(Build.VERSION_CODES.UPSIDE_DOWN_CAKE) >= 17) {
                listener.onReserved(networkCapabilities)
            }
        }
    }

    private fun notifyOnUnavailable() {
        val iterator = mNetworkListeners.iterator()
        while (iterator.hasNext()) {
            val listener: ConnectivityManager.NetworkCallback = iterator.next()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                listener.onUnavailable()
            }
        }
    }
}
