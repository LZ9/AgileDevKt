@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.lodz.android.corekt.anko

import android.annotation.SuppressLint
import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.content.FileProvider
import com.lodz.android.corekt.utils.FileUtils
import java.io.File


/**
 * Intent扩展方法
 * Created by zhouL on 2018/6/26.
 */

/** 安装apk文件 */
@JvmOverloads
fun Context.installApk(apkPath: String, authority: String, newTask: Boolean = true) {
    val file: File? = FileUtils.create(apkPath)
    if (file == null || !FileUtils.isFileExists(file)) {
        throw IllegalArgumentException("file no exists")
    }

    if (authority.isEmpty()) {
        throw IllegalArgumentException("authority is null")
    }

    // 系统大于8.0且没有“安装未知应用”权限，则跳转到设置页面
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (!packageManager.canRequestPackageInstalls()) {
            //跳转至“安装未知应用”权限界面，引导用户开启权限
            startActivity(Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:$packageName")))
            return
        }
    }

    val intent = Intent(Intent.ACTION_VIEW)
    if (newTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val apkUri = FileProvider.getUriForFile(this, authority, file)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
    } else {
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
    }
    startActivity(intent)
}

/** 卸载包名为[packageName]的应用 */
fun Context.uninstallApp(packageName: String) {
    if (packageName.isEmpty()) {
        return
    }
    startActivity(Intent(Intent.ACTION_DELETE, Uri.parse("package:$packageName")))
}

/** 通过LaunchIntent打开包名为[packageName]的应用 */
@JvmOverloads
fun Context.openAppByLaunch(packageName: String, newTask: Boolean = true) {
    if (packageName.isEmpty()) {
        throw IllegalArgumentException("packageName is null")
    }
    val intent: Intent = packageManager.getLaunchIntentForPackage(packageName) ?: throw ActivityNotFoundException("no found packageName")
    if (newTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

/** 通过android.intent.action.MAIN来打开包名为[packageName]的应用 */
@JvmOverloads
fun Context.openAppByActionMain(packageName: String, newTask: Boolean = true) {
    if (packageName.isEmpty()) {
        throw IllegalArgumentException("packageName is null")
    }

    var mainActivityName: String? = ""// 启动页的路径
    val intent = Intent(Intent.ACTION_MAIN)
    for (resolve in packageManager.queryIntentActivities(intent, 0)) {
        val info: ActivityInfo = resolve.activityInfo ?: continue
        if (packageName.equals(info.packageName)) {
            mainActivityName = info.name
            break
        }
    }

    if (mainActivityName.isNullOrEmpty()) {
        throw ActivityNotFoundException("no found packageName")
    }

    intent.component = ComponentName(packageName, mainActivityName)
    if (newTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

/** 打开应用设置界面 */
fun Context.goAppDetailSetting() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:$packageName")
    startActivity(intent)
}

/** 跳转到日期设置页面 */
fun Context.goDateSetting() {
    startActivity(Intent(Settings.ACTION_DATE_SETTINGS))
}

/** 跳转到定位设置页 */
fun Context.goLocationSetting() {
    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
}

/** 跳转到WIFI设置页 */
fun Context.goWifiSetting() {
    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
}

/** 跳转到数据流量设置页 */
fun Context.goDataRoamingSetting() {
    startActivity(Intent(Settings.ACTION_DATA_ROAMING_SETTINGS))
}

/** 跳转到密码设置页 */
fun Context.goSetPswdSetting() {
    startActivity(Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD))
}

/** 跳转到蓝牙设置页 */
fun Context.goBluetoothSetting() {
    startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
}

/** 打开浏览器访问[url] */
@JvmOverloads
fun Context.browse(url: String, newTask: Boolean = true) {
    if (url.isEmpty()) {
        throw IllegalArgumentException("url is null")
    }
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    if (newTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

/** 分享标题为[subject]，内容为[text]的文字，应用选择框提示[tips] */
@JvmOverloads
fun Context.share(text: String, subject: String = "", tips: String = "") {
    val intent = Intent(android.content.Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject)// 主题
    intent.putExtra(android.content.Intent.EXTRA_TEXT, text)// 内容
    var title: String? = null// 应用选择框提示
    if (tips.isNotEmpty()) {
        title = tips
    }
    startActivity(Intent.createChooser(intent, title))
}

/** 发送标题为[subject]，内容为[text]的邮件到[email]邮箱 */
@JvmOverloads
fun Context.email(email: String, subject: String = "", text: String = "") {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))//邮箱
    if (subject.isNotEmpty())
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)//标题
    if (text.isNotEmpty())
        intent.putExtra(Intent.EXTRA_TEXT, text)// 内容
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}

/** 拨打号码[number] */
@SuppressLint("MissingPermission")
fun Context.makeCall(number: String) {
    startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$number")))
}

/** 发送短信[text]到号码[number] */
@JvmOverloads
fun Context.sendSMS(number: String, text: String = "") {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$number"))
    intent.putExtra("sms_body", text)
    startActivity(intent)
}

/** 拍照，存储地址[savePath]，FileProvider名称[authority]，请求码[requestCode] */
fun Activity.takePhoto(savePath: String, authority: String, requestCode: Int): Boolean {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (intent.resolveActivity(packageManager) == null) {
        return false
    }
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, authority, File(savePath)))
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(File(savePath)))
        }
        startActivityForResult(intent, requestCode)
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
    return true
}
