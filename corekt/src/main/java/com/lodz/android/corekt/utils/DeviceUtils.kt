package com.lodz.android.corekt.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.support.annotation.StringDef
import android.telephony.TelephonyManager
import java.util.*
import kotlin.collections.HashMap

/**
 * 设备帮助类
 * Created by zhouL on 2018/7/2.
 */
object DeviceUtils {

    /** 品牌  */
    const val BRAND = "BRAND"
    /** 型号  */
    const val MODEL = "MODEL"
    /** 模板  */
    const val BOARD = "BOARD"
    /** CPU1  */
    const val CPU_ABI = "CPU_ABI"
    /** CPU2  */
    const val CPU_ABI2 = "CPU_ABI2"
    /** 制造商  */
    const val MANUFACTURER = "MANUFACTURER"
    /** 产品  */
    const val PRODUCT = "PRODUCT"
    /** 设备  */
    const val DEVICE = "DEVICE"

    @StringDef(BRAND, MODEL, BOARD, CPU_ABI, CPU_ABI2, MANUFACTURER, PRODUCT, DEVICE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class DeviceKey

    /** 获取手机的UA信息 */
    fun getUserAgent(): String {
        try {
            return System.getProperty("http.agent")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /** 获取语言 */
    fun getLanguage() = Locale.getDefault().language ?: ""

    /** 获取国家 */
    fun getCountry() = Locale.getDefault().country ?: ""

    /** 获取设备信息 */
    fun getDeviceInfo(): Map<String, String> {
        val infos = HashMap<String, String>()
        try {
            val fields = Build::class.java.declaredFields
            for (field in fields) {
                field.isAccessible = true
                infos.put(field.name, field.get(null).toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return infos
    }

    /** 获取设备[key]对应的值 */
    fun getDeviceValue(key: String): String {
        if (key.isEmpty()) {
            return ""
        }
        try {
            return getDeviceInfo().get(key) ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}

/** 获取手机的IMSI */
@SuppressLint("MissingPermission", "HardwareIds")
fun Context.getIMSI(): String {
    try {
        val tm: TelephonyManager? = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (tm == null) {
            return ""
        }
        return tm.subscriberId ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/** 获取手机的IMEI */
@SuppressLint("MissingPermission", "HardwareIds")
fun Context.getIMEI(): String {
    try {
        val tm: TelephonyManager? = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (tm == null) {
            return ""
        }
        var imei: String? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) tm.imei else tm.deviceId
        if (imei.isNullOrEmpty()) {
            imei = tm.deviceId
        }
        return imei ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/** 获取APN */
@SuppressLint("MissingPermission")
fun Context.getAPN(): String {
    val manager: ConnectivityManager? = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (manager == null) {
        return ""
    }
    val info: NetworkInfo? = manager.activeNetworkInfo
    if (info == null) {
        return ""
    }
    if (ConnectivityManager.TYPE_WIFI == info.type) {
        return info.typeName ?: "WIFI"
    }
    return info.extraInfo ?: "MOBILE"
}