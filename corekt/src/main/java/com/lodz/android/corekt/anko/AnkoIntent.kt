@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.lodz.android.corekt.anko

import android.app.admin.DevicePolicyManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.content.FileProvider
import com.lodz.android.corekt.utils.FileUtils
import java.io.File


/**
 * Intent扩展方法
 * Created by zhouL on 2018/6/26.
 */

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_TASK] flag to the [Intent].
 * @return the same intent with the flag applied.
 */
inline fun Intent.clearTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_TOP] flag to the [Intent].
 * @return the same intent with the flag applied.
 */
inline fun Intent.clearTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET] flag to the [Intent].
 * @return the same intent with the flag applied.
 */
inline fun Intent.clearWhenTaskReset(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET) }

/**
 * Add the [Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS] flag to the [Intent].
 * @return the same intent with the flag applied.
 */
inline fun Intent.excludeFromRecents(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS) }

/**
 * Add the [Intent.FLAG_ACTIVITY_MULTIPLE_TASK] flag to the [Intent].
 * @return the same intent with the flag applied.
 */
inline fun Intent.multipleTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NEW_TASK] flag to the [Intent].
 * @return the same intent with the flag applied.
 */
inline fun Intent.newTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NO_ANIMATION] flag to the [Intent].
 * @return the same intent with the flag applied.
 */
inline fun Intent.noAnimation(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NO_HISTORY] flag to the [Intent].
 * @return the same intent with the flag applied.
 */
inline fun Intent.noHistory(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY) }

/**
 * Add the [Intent.FLAG_ACTIVITY_SINGLE_TOP] flag to the [Intent].
 * @return the same intent with the flag applied.
 */
inline fun Intent.singleTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) }


/** 安装akp文件 */
fun Context.installApk(apkPath: String, authority: String, newTask: Boolean = true) {
    val file: File? = FileUtils.create(apkPath)
    if (file == null || !FileUtils.isFileExists(file)) {
        throw IllegalArgumentException("file no exists")
    }

    if (authority.isEmpty()) {
        throw IllegalArgumentException("authority is null")
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
    startActivity(Intent(Intent.ACTION_DELETE, Uri.parse("package:" + packageName)))
}

/** 通过LaunchIntent打开包名为[packageName]的应用 */
fun Context.openAppByLaunch(packageName: String, newTask: Boolean = true) {
    if (packageName.isEmpty()) {
        throw IllegalArgumentException("packageName is null")
    }
    val intent: Intent? = getPackageManager().getLaunchIntentForPackage(packageName)
    if (intent == null) {
        throw ActivityNotFoundException("no found packageName")
    }
    if (newTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

/** 通过android.intent.action.MAIN来打开包名为[packageName]的应用 */
fun Context.openAppByActionMain(packageName: String, newTask: Boolean = true) {
    if (packageName.isEmpty()) {
        throw IllegalArgumentException("packageName is null")
    }

    var mainActivityName: String? = ""// 启动页的路径
    val intent = Intent(Intent.ACTION_MAIN)
    for (resolve in packageManager.queryIntentActivities(intent, 0)) {
        val info: ActivityInfo? = resolve.activityInfo
        if (info == null) {
            continue
        }
        if (packageName.equals(info.packageName)) {
            mainActivityName = info.name
            break
        }
    }

    if (mainActivityName.isNullOrEmpty()) {
        throw ActivityNotFoundException("no found packageName")
    }

    intent.setComponent(ComponentName(packageName, mainActivityName))
    if (newTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

/** 打开应用设置界面 */
fun Context.goAppDetailSetting() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:" + getPackageName())
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

/** 打开浏览器访问[url] */
fun Context.browse(url: String, newTask: Boolean = true) {
    if (url.isEmpty()){
        throw IllegalArgumentException("url is null")
    }
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    if (newTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

/** 分享标题为[subject]，内容为[text]的文字 */
fun Context.share(text: String, subject: String = "") {
    if (text.isEmpty()){
        throw IllegalArgumentException("text is null")
    }

    val intent = Intent(android.content.Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(android.content.Intent.EXTRA_TEXT, text)
    startActivity(Intent.createChooser(intent, null))

    // todo 待完善 

////系统邮件系统的动作为android.content.Intent.ACTION_SEND
//    Intent email = new Intent(android.content.Intent.ACTION_SEND);
//    email.setType("text/plain");
//    emailReciver = new String[]{"pop1030123@163.com", "fulon@163.com"};
//    emailSubject = "你有一条短信";
//    emailBody = sb.toString();


//
////设置邮件默认地址
//    email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);
////设置邮件默认标题
//    email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);
////设置要默认发送的内容
//    email.putExtra(android.content.Intent.EXTRA_TEXT, emailBody);
////调用系统的邮件系统
//    startActivity(Intent.createChooser(email, "请选择邮件发送软件"));
}





