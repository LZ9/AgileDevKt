package com.lodz.android.corekt.anko

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.nfc.NfcAdapter
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * 应用扩展类
 * Created by zhouL on 2019/2/1.
 */

/** 获取应用程序名称 */
@JvmOverloads
fun Context.getAppName(packageName: String = getPackageName()): String {
    try {
        val packageInfo: PackageInfo? = packageManager.getPackageInfo(packageName, 0)
        return packageInfo?.applicationInfo?.loadLabel(packageManager)?.toString() ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/** 获取客户端版本名称 */
@JvmOverloads
fun Context.getVersionName(packageName: String = getPackageName()): String {
    try {
        return packageManager.getPackageInfo(packageName, 0).versionName
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/** 获取客户端版本号 */
@JvmOverloads
fun Context.getVersionCode(packageName: String = getPackageName()): Long {
    try {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(packageName, 0).longVersionCode
        } else {
            packageManager.getPackageInfo(packageName, 0).versionCode.toLong()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return -1
}

/** 获取APP的图标 */
fun Context.getAppIcon(packageName: String = getPackageName()): Drawable? {
    try {
        val packageInfo: PackageInfo? = packageManager.getPackageInfo(packageName, 0)
        return packageInfo?.applicationInfo?.loadIcon(packageManager)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
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

    val runningApps: MutableList<ActivityManager.RunningAppProcessInfo> = am.runningAppProcesses ?: return ""

    for (procInfo in runningApps) {
        if (procInfo.pid == pid) {
            return procInfo.processName
        }
    }
    return ""
}

/** 判断包名为[packageName]的app是否安装 */
fun Context.isPkgInstalled(packageName: String): Boolean = getPackageInfo(packageName) != null

/** 获取已安装的PackageInfo列表 */
fun Context.getInstalledPackages(): List<PackageInfo> =
    packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES or PackageManager.GET_SERVICES)

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
    val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
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

/** 手机是否有NFC */
@RequiresPermission(Manifest.permission.NFC)
fun Context.hasNfc(): Boolean = packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)

/** NFC是否打开 */
@RequiresPermission(Manifest.permission.NFC)
fun Context.isNfcOpen(): Boolean {
    val adapter = NfcAdapter.getDefaultAdapter(this) ?: return false
    return adapter.isEnabled
}

/** 获取Assets下文件名为[fileName]的内容 */
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

/** 获堆栈的Activity信息 */
fun Context.getActivityRunningTaskInfo(): ActivityManager.RunningTaskInfo? {
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
    val list = manager?.getRunningTasks(1)
    return list?.get(0)
}

/** 堆栈顶部和底部Activity是否一致 */
fun Context.isTopAndBottomActivityTheSame(): Boolean {
    val info = getActivityRunningTaskInfo()
    val topActivityName = info?.topActivity?.className
    val baseActivityName = info?.baseActivity?.className
    return topActivityName?.equals(baseActivityName) ?: false
}

/** 重启APP */
fun Context.restartApp() {
    val intent = packageManager.getLaunchIntentForPackage(packageName) ?: return
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    startActivity(intent)
}