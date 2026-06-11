package com.lodz.android.corekt.anko

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import android.telephony.CellLocation
import android.telephony.TelephonyManager
import android.telephony.cdma.CdmaCellLocation
import android.telephony.gsm.GsmCellLocation
import androidx.annotation.RequiresPermission
import androidx.core.telephony.TelephonyManagerCompat
import com.lodz.android.corekt.network.OperatorInfo
import com.lodz.android.corekt.utils.ReflectUtils
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.ArrayList

/**
 * 设备扩展类
 * Created by zhouL on 2019/2/1.
 */

/** 获取APN名称 */
@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.getApnName(): String {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info: NetworkInfo = manager.activeNetworkInfo ?: return ""
    return if (ConnectivityManager.TYPE_MOBILE == info.type && info.extraInfo.isNotEmpty()) info.extraInfo else ""
}

/** 获取IPV4的列表 */
fun Any.getIpv4List(): List<Pair<String, String>> {
    val result = ArrayList<Pair<String, String>>()
    val list = NetworkInterface.getNetworkInterfaces()
    while (list.hasMoreElements()) {
        val element = list.nextElement()
        val addresses = element.inetAddresses
        while (addresses.hasMoreElements()) {
            val address = addresses.nextElement()
            if (address is Inet4Address && !address.isLoopbackAddress) {
                result.add(Pair<String, String>(element.name, address.hostAddress ?: ""))
            }
        }
    }
    return result
}

/** 根据网络名称[networkName]获取IPV4的地址，例如eth0、wlan0、rndis0等等 */
fun Any.getIpv4Address(networkName: String): String {
    val list = getIpv4List()
    for (pair in list) {
        if (pair.first == networkName) {
            return pair.second
        }
    }
    return ""
}

/** 获取手机的IMSI1 */
fun Context.getIMSI1(): String = getOperatorBySlot("getSubscriberId", 0)

/** 获取手机的IMSI2 */
fun Context.getIMSI2(): String = getOperatorBySlot("getSubscriberId", 1)

/** 获取SIM标识符 */
fun Context.getSubscriptionId(): String {
    val manager = getSystemService(Context.TELEPHONY_SERVICE)
    if (manager is TelephonyManager) {
        return TelephonyManagerCompat.getSubscriptionId(manager).toString()
    }
    return ""
}

/** Sim卡1是否可用 */
fun Context.isSim1Ready(): Boolean {
    var type = getOperatorBySlot("getSimState", 0)
    if (type.isEmpty()) {
        type = getOperatorBySlot("getSimStateGemini", 0)
    }
    return type.isNotEmpty() && type.toInt() == TelephonyManager.SIM_STATE_READY
}

/** Sim卡2是否可用 */
fun Context.isSim2Ready(): Boolean {
    var type = getOperatorBySlot("getSimState", 1)
    if (type.isEmpty()) {
        type = getOperatorBySlot("getSimStateGemini", 1)
    }
    return type.isNotEmpty() && type.toInt() == TelephonyManager.SIM_STATE_READY
}

/** sim卡数据连接状态，常见返回值包括TelephonyManager.DATA_DISCONNECTED或TelephonyManager.DATA_CONNECTED */
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
fun Context.isSimDataConnected(): Boolean = getSimDataState() == TelephonyManager.DATA_CONNECTED

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
fun Context.isDualSim(): Boolean = getIMEI2().isNotEmpty()

/** 通过方法名[predictedMethodName]获取对应序号[slotId]下的设备信息 */
private fun Context.getOperatorBySlot(predictedMethodName: String, slotId: Int): String {
    try {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val value = ReflectUtils.executeFunction(tm::class.java, tm, predictedMethodName, arrayOf(Int::class.java), arrayOf(slotId))
        if (value != null) {
            return value.toString()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/** 获取手机Android ID */
@SuppressLint("HardwareIds")
fun Context.getAndroidId(): String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID) ?: ""

/** 获取手机硬件id兼容方法 */
fun Context.getDeviceIdCompat(): String {
    val imei: String = getIMEI1()
    return imei.ifEmpty { getAndroidId() }
}

/** 获取运营商代号 */
fun Context.getSimOperator(): String {
    val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val operator = telephonyManager.simOperator
    return if (operator.isNullOrEmpty()) "" else operator
}


/** 获取运营商类型（0：未知 1：移动 2：联通 3：电信 4：广电） */
@OperatorInfo.OperatorType
fun Context.getSimOperatorType(): Int {
    val operator = getSimOperator()
    return when (operator) {
        "46000", "46002", "46004", "46007", "46008", "46013" -> OperatorInfo.OPERATOR_CMCC// 中国移动
        "46001", "46006", "46009" -> OperatorInfo.OPERATOR_CUCC// 中国联通
        "46003", "46005", "46011" -> OperatorInfo.OPERATOR_CTCC// 中国电信
        "46015" -> OperatorInfo.OPERATOR_CBN// 中国广电
        else -> OperatorInfo.OPERATOR_UNKNOWN
    }
}

/** 获取运营商名称 */
fun Context.getSimOperatorName(): String {
    return when (getSimOperatorType()) {
        OperatorInfo.OPERATOR_CMCC -> "中国移动"
        OperatorInfo.OPERATOR_CUCC -> "联通"
        OperatorInfo.OPERATOR_CTCC -> "中国电信"
        OperatorInfo.OPERATOR_CBN -> "中国广电"
        else -> "未知"
    }
}

/** 获取运营商（基站）信息 */
@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
fun Context.getOperatorInfo(): OperatorInfo? {
    val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val info = OperatorInfo()
    info.type = getSimOperatorType()
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


