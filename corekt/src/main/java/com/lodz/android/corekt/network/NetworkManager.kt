package com.lodz.android.corekt.network

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.ext.SdkExtensions
import android.telephony.TelephonyDisplayInfo
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.lodz.android.corekt.anko.then
import com.lodz.android.corekt.log.PrintLog
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

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

    /** 电话管理器 */
    private var mTelephonyManager: TelephonyManager? = null
    /** 电话显示信息 */
    private var mTelephonyDisplayInfo: TelephonyDisplayInfo? = null

    /** 网络监听器 */
    private val mNetworkCallbacks = ArrayList<ConnectivityManager.NetworkCallback>()
    /** 网络监听器 */
    private val mNetworkListeners = ArrayList<NetworkListener>()

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
        mTelephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mTelephonyManager?.registerTelephonyCallback(context.mainExecutor, mDisplayInfoListener)
        }
    }

    /** 解除网络监听广播 */
    fun release() {
        mConnectivityManager?.unregisterNetworkCallback(mNetworkCallback)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mTelephonyManager?.unregisterTelephonyCallback(mDisplayInfoListener)
        }
    }

    /** 网络是否可用 */
    fun isNetworkAvailable(): Boolean = mNetworkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false

    /** 是否WIFI网络 */
    fun isWifi(): Boolean = mNetworkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false

    /** 是否流量网络 */
    fun isCellular(): Boolean = mNetworkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false

    /** 获取当前网络功能对象 */
    fun getNetworkCapabilities(): NetworkCapabilities? = mNetworkCapabilities

    private val mDisplayInfoListener = @RequiresApi(Build.VERSION_CODES.S) object : TelephonyDisplayInfoListener() {
        override fun onDisplayInfoChanged(telephonyDisplayInfo: TelephonyDisplayInfo) {
            mTelephonyDisplayInfo = telephonyDisplayInfo
        }
    }

    /** 获取数据网络类型名称，例如4G、5G */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun getDataNetworkTypeName(): String {
        val tm = mTelephonyManager ?: return ""
        val dataNetworkType = getDataNetworkTypeName(tm.dataNetworkType)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val displayInfo = mTelephonyDisplayInfo ?: return dataNetworkType
            val networkType = displayInfo.networkType
            val overrideNetworkType = displayInfo.overrideNetworkType
            if (networkType == TelephonyManager.NETWORK_TYPE_LTE || networkType == TelephonyManager.NETWORK_TYPE_NR) {
                val is5G = when (overrideNetworkType) {
                    TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA,
                    TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_ADVANCED -> true
                    else -> displayInfo.networkType == TelephonyManager.NETWORK_TYPE_NR
                }
                return is5G.then { "5G" } ?: "4G"
            }
            return getDataNetworkTypeName(networkType)
        }
        return dataNetworkType
    }

    /** 获取数据网络类型名称 */
    private fun getDataNetworkTypeName(networkType: Int): String {
        return when (networkType) {
            TelephonyManager.NETWORK_TYPE_GPRS,
            TelephonyManager.NETWORK_TYPE_EDGE,
            TelephonyManager.NETWORK_TYPE_GSM -> "2G"

            TelephonyManager.NETWORK_TYPE_UMTS,
            TelephonyManager.NETWORK_TYPE_HSDPA,
            TelephonyManager.NETWORK_TYPE_HSUPA,
            TelephonyManager.NETWORK_TYPE_HSPA,
            TelephonyManager.NETWORK_TYPE_HSPAP,
            TelephonyManager.NETWORK_TYPE_TD_SCDMA -> "3G"

            TelephonyManager.NETWORK_TYPE_LTE -> "4G"
            TelephonyManager.NETWORK_TYPE_NR -> "5G"
            else -> ""
        }
    }

    /** 添加网络监听器[callback] */
    fun addNetworkCallback(callback: ConnectivityManager.NetworkCallback) {
        mNetworkCallbacks.add(callback)
    }

    /** 添加网络监听器[listener] */
    fun addNetworkListener(listener: NetworkListener) {
        mNetworkListeners.add(listener)
    }

    /** 删除网络监听器[callback] */
    fun removeNetworkCallback(callback: ConnectivityManager.NetworkCallback) {
        mNetworkCallbacks.remove(callback)
    }

    /** 删除网络监听器[listener] */
    fun removeNetworkListener(listener: NetworkListener) {
        mNetworkListeners.remove(listener)
    }

    /** 清除所有监听器 */
    fun clearNetworkListener() {
        mNetworkCallbacks.clear()
        mNetworkListeners.clear()
    }

    val mNetworkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            isNetworkAvailable = true
            notifyOnAvailable(network)
            onNetworkStatusChanged(network)
            PrintLog.d("testtag", "onAvailable --- > network : $network")
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            isNetworkAvailable = false
            mNetworkCapabilities = null
            notifyOnLost(network)
            onNetworkStatusChanged(network)
            PrintLog.e("testtag", "onLost --- > network : $network")
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            mNetworkCapabilities = networkCapabilities
            isNetworkAvailable = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            notifyOnCapabilitiesChanged(network, networkCapabilities)
            onNetworkStatusChanged(network)
            PrintLog.i("testtag", "onCapabilitiesChanged --- > network : $network ; networkCapabilities : $networkCapabilities")
        }

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            super.onBlockedStatusChanged(network, blocked)
            notifyOnBlockedStatusChanged(network, blocked)
            PrintLog.w("testtag", "onBlockedStatusChanged --- > network : $network ; blocked : $blocked")
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties)
            notifyOnLinkPropertiesChanged(network, linkProperties)
            PrintLog.v("testtag", "onLinkPropertiesChanged --- > network : $network ; linkProperties : $linkProperties")
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            notifyOnLosing(network, maxMsToLive)
            PrintLog.w("testtag", "onLosing --- > network : $network ; maxMsToLive : $maxMsToLive")
        }

        override fun onReserved(networkCapabilities: NetworkCapabilities) {
            super.onReserved(networkCapabilities)
            notifyOnReserved(networkCapabilities)
            PrintLog.d("testtag", "onReserved --- > networkCapabilities : $networkCapabilities")
        }

        override fun onUnavailable() {
            super.onUnavailable()
            notifyOnUnavailable()
            PrintLog.e("testtag", "notifyOnUnavailable --- > ")
        }

    }

    private fun onNetworkStatusChanged(network: Network) {
        val iterator = mNetworkListeners.iterator()
        while (iterator.hasNext()) {
            val listener: NetworkListener = iterator.next()
            MainScope().launch { listener.onNetworkStatusChanged(isNetworkAvailable, network, mNetworkCapabilities) }
        }
    }

    private fun notifyOnAvailable(network: Network) {
        val iterator = mNetworkCallbacks.iterator()
        while (iterator.hasNext()) {
            val listener: ConnectivityManager.NetworkCallback = iterator.next()
            MainScope().launch { listener.onAvailable(network) }
        }
    }

    private fun notifyOnLost(network: Network) {
        val iterator = mNetworkCallbacks.iterator()
        while (iterator.hasNext()) {
            val listener: ConnectivityManager.NetworkCallback = iterator.next()
            MainScope().launch { listener.onLost(network) }
        }
    }


    private fun notifyOnCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        val iterator = mNetworkCallbacks.iterator()
        while (iterator.hasNext()) {
            val listener: ConnectivityManager.NetworkCallback = iterator.next()
            MainScope().launch { listener.onCapabilitiesChanged(network, networkCapabilities) }
        }
    }

    private fun notifyOnBlockedStatusChanged(network: Network, blocked: Boolean) {
        val iterator = mNetworkCallbacks.iterator()
        while (iterator.hasNext()) {
            val listener: ConnectivityManager.NetworkCallback = iterator.next()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MainScope().launch { listener.onBlockedStatusChanged(network, blocked) }
            }
        }
    }

    private fun notifyOnLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        val iterator = mNetworkCallbacks.iterator()
        while (iterator.hasNext()) {
            val listener: ConnectivityManager.NetworkCallback = iterator.next()
            MainScope().launch { listener.onLinkPropertiesChanged(network, linkProperties) }
        }
    }

    private fun notifyOnLosing(network: Network, maxMsToLive: Int) {
        val iterator = mNetworkCallbacks.iterator()
        while (iterator.hasNext()) {
            val listener: ConnectivityManager.NetworkCallback = iterator.next()
            MainScope().launch { listener.onLosing(network, maxMsToLive) }
        }
    }

    private fun notifyOnReserved(networkCapabilities: NetworkCapabilities) {
        val iterator = mNetworkCallbacks.iterator()
        while (iterator.hasNext()) {
            val listener: ConnectivityManager.NetworkCallback = iterator.next()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(Build.VERSION_CODES.UPSIDE_DOWN_CAKE) >= 17) {
                MainScope().launch { listener.onReserved(networkCapabilities) }
            }
        }
    }

    private fun notifyOnUnavailable() {
        val iterator = mNetworkCallbacks.iterator()
        while (iterator.hasNext()) {
            val listener: ConnectivityManager.NetworkCallback = iterator.next()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                MainScope().launch { listener.onUnavailable() }
            }
        }
    }

    /** 网络监听器 */
    fun interface NetworkListener {
        /** 网络状态变化，网络是否可用[isNetworkAvailable]，网络信息[network]，网络功能[networkCapabilities] */
        fun onNetworkStatusChanged(isNetworkAvailable: Boolean, network: Network, networkCapabilities: NetworkCapabilities?)
    }
}
