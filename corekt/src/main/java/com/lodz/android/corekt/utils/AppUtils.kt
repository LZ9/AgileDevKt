package com.lodz.android.corekt.utils

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Looper
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * APP帮助类
 * Created by zhouL on 2018/6/26.
 */
object AppUtils {

    /** 当前是否在主线程（UI线程） */
    fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

    /** 获取随机的UUID */
    fun getUUID(): String = UUID.randomUUID().toString()

}

//----------------------------- Context扩展 ---------------------------------------

/** 获取应用程序名称 */
fun Context.getAppName(): String {
    try {
        return resources.getString(packageManager.getPackageInfo(packageName, 0).applicationInfo.labelRes)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/** 获取客户端版本名称 */
fun Context.getVersionName(): String {
    try {
        return packageManager.getPackageInfo(packageName, 0).versionName
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/** 获取客户端版本号 */
fun Context.getVersionCode(): Int {
    try {
        return packageManager.getPackageInfo(packageName, 0).versionCode
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return -1
}

/** 是否在主进程 */
fun Context.isMainProcess(): Boolean {
    val processName = getProcessName()
    if (processName.isEmpty()) {
        return false
    }
    return !processName.contains(":")
}


/** 获取进程名称 */
fun Context.getProcessName(): String {
    val pid = android.os.Process.myPid()
    val am: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    val runningApps: List<ActivityManager.RunningAppProcessInfo>? = am.runningAppProcesses
    if (runningApps == null) {
        return ""
    }

    for (procInfo in runningApps) {
        if (procInfo.pid == pid) {
            return procInfo.processName
        }
    }
    return ""
}

/** 判断包名为[packageName]的app是否安装 */
fun Context.isPkgInstalled(packageName: String): Boolean {
    if (packageName.isEmpty()) {
        return false
    }
    try {
        val packageInfo: PackageInfo? = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES or PackageManager.GET_SERVICES)
        return packageInfo != null
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

/** 获取已安装的PackageInfo列表 */
fun Context.getInstalledPackages(): List<PackageInfo> {
    val packageInfos: List<PackageInfo>? = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES or PackageManager.GET_SERVICES)
    return if (packageInfos == null) Collections.emptyList() else packageInfos
}

/** 获取包名为[packageName]的PackageInfo */
fun Context.getPackageInfo(packageName: String): PackageInfo? {
    if (packageName.isEmpty()) {
        return null
    }
    try {
        return packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES or PackageManager.GET_SERVICES)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

/** 权限[permission]是否被授予 */
fun Context.isPermissionGranted(permission: String): Boolean {
    try {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

/** 获取MetaData中标签为[key]的值 */
fun Context.getMetaData(key: String): Any? {
    try {
        val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        return appInfo.metaData.get(key)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

/** 判断GPS是否打开 */
fun Context.isGpsOpen(): Boolean {
    val state = Settings.Secure.getInt(contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF)
    return state != Settings.Secure.LOCATION_MODE_OFF
}

/** wifi是否可用 */
fun Context.isWifiEnabled(): Boolean = (applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).isWifiEnabled

/** 设置wifi是否可用[enabled] */
@RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
fun Context.setWifiEnabled(enabled: Boolean) {
    val manager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    if (manager.isWifiEnabled) {
        manager.isWifiEnabled = enabled
    }
}

/** 获取Assets下的文件内容 */
fun Context.getAssetsFileContent(fileName: String): String {
    InputStreamReader(assets.open(fileName), StandardCharsets.UTF_8).use { isr: InputStreamReader ->
        BufferedReader(isr).use { br: BufferedReader ->
            var line: String?
            val builder = StringBuilder()
            while (true) {
                line = br.readLine() ?: break
                builder.append(line)
            }
            return builder.toString()
        }
    }
}