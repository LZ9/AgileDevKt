package com.lodz.android.corekt.utils

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.os.bundleOf


/**
 * 手机系统角标帮助类
 * @author zhouL
 * @date 2022/10/17
 */

/** 更新华为系统的APP角标，APP启动页完整包名路径[className]，角标数字[badgeNum] */
fun Context.updateHuaweiOSBadge(className: String, badgeNum: Int) {
//    声明权限
//    <uses - permission android: name = "android.permission.INTERNET" / >
//    <uses - permission android: name = "com.huawei.android.launcher.permission.CHANGE_BADGE " / >
    contentResolver.call(
        Uri.parse("content://com.huawei.android.launcher.settings/badge/"),
        "change_badge",
        null,
        bundleOf(
            "package" to packageName,
            "class" to className,
            "badgenumber" to badgeNum
        )
    )
}

/** 更新小米系统的APP角标，APP启动页完整包名路径[className]，角标数字[badgeNum]，
 * 通知频道[channelId]，通知栏图标[iconResId]，通知栏提示语[contentText] */
fun Context.updateXiaomiOSBadge(
    className: String,
    badgeNum: Int,
    channelId: String,
    @DrawableRes iconResId: Int,
    contentText: String
): Boolean {
    val spCls = ReflectUtils.getClassForName("android.os.SystemProperties") ?: return false
    val versionCode = ReflectUtils.executeFunction(
        spCls,
        spCls,
        "get",
        arrayOf(String::class.java, String::class.java),
        arrayOf("ro.miui.ui.version.code", "-1")
    ) as? Int
    // 例如 MIUI 10 返回8
    if (versionCode == null || versionCode == -1) {//不是miui系统
        return false
    }
    if (versionCode <= 3){// MIUI6之前
        val intent = Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE")
        intent.putExtra("android.intent.extra.update_application_component_name", className)
        intent.putExtra("android.intent.extra.update_application_message_text", badgeNum)
        sendBroadcast(intent)
        return true
    }
    val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Notification.Builder(this, channelId)
    } else {
        Notification.Builder(this)
    }
        .setSmallIcon(iconResId)
        .setContentText(contentText)
        .setNumber(badgeNum)
        .build()
    if (versionCode <= 9) {//MIUI6-MIUI11桌面应用角标适配方法
        val field = notification::class.java.getDeclaredField("extraNotification") ?: return false
        val extraNotification = field.get(notification) ?: return false
        val method = extraNotification::class.java.getDeclaredMethod("setMessageCount", Int::class.java) ?: return false
        method.invoke(extraNotification, badgeNum)
        return true
    }
    //MIUI12及以后
    NotificationUtils.create(this).send(notification)
    return true
}