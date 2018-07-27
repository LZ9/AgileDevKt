package com.lodz.android.corekt.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.support.annotation.RequiresPermission
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
    /** 版本  */
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

/** 获取APN名称 */
@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.getApnName(): String {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info: NetworkInfo? = manager.activeNetworkInfo
    if (info == null) {
        return ""
    }
    if (ConnectivityManager.TYPE_MOBILE == info.type && info.extraInfo.isNotEmpty()) {
        return info.extraInfo
    }
    return ""
}

/** 获取手机的IMSI1 */
fun Context.getIMSI1() = getOperatorBySlot("getSubscriberId", 0)

/** Sim卡1是否可用 */
fun Context.isSim1Ready(): Boolean {
    var type = getOperatorBySlot("getSimState", 0)
    if (type.isEmpty()){
        type = getOperatorBySlot("getSimStateGemini", 0)
    }
    return type.isNotEmpty() && type.toInt() == TelephonyManager.SIM_STATE_READY
}

/** 获取手机的IMSI2 */
fun Context.getIMSI2() = getOperatorBySlot("getSubscriberId", 1)

/** Sim卡2是否可用 */
fun Context.isSim2Ready(): Boolean {
    var type = getOperatorBySlot("getSimState", 1)
    if (type.isEmpty()){
        type = getOperatorBySlot("getSimStateGemini", 1)
    }
    return type.isNotEmpty() && type.toInt() == TelephonyManager.SIM_STATE_READY
}

/** sim卡数据连接状态 */
fun Context.getSimDataState(): Int {
    try {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.dataState
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return -1
}

/** sim卡数据是否已连接 */
fun Context.isSimDataConnected() =getSimDataState() == TelephonyManager.DATA_CONNECTED

/** 获取手机的IMEI1 */
fun Context.getIMEI1(): String {
    var imei = getOperatorBySlot("getDeviceId", 0)
    if (imei.isEmpty()) {
        imei = getOperatorBySlot("getDeviceIdGemini", 0)
    }
    return imei
}

/** 获取手机的IMEI2 */
fun Context.getIMEI2(): String {
    var imei = getOperatorBySlot("getDeviceId", 1)
    if (imei.isEmpty()) {
        imei = getOperatorBySlot("getDeviceIdGemini", 1)
    }
    return imei
}

/** 手机是否双卡双待 */
fun Context.isDualSim() = getIMEI2().isNotEmpty()

/** 通过方法名[predictedMethodName]获取对应序号[slotId]下的设备信息 */
private fun Context.getOperatorBySlot(predictedMethodName: String, slotId: Int): String {
    try {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val cls = ReflectUtils.getClassForName(tm.javaClass.name)!!
        val value = ReflectUtils.executeFunction(cls, tm, predictedMethodName, arrayOf(Int::class.java), arrayOf(slotId))
        if (value != null) {
            return value.toString()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}