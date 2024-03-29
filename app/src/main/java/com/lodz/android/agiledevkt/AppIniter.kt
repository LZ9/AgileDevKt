package com.lodz.android.agiledevkt

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.startup.Initializer
import com.lodz.android.agiledevkt.config.Constant
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.corekt.network.NetworkManager
import com.lodz.android.corekt.utils.NotificationUtils
import com.lodz.android.corekt.utils.SharedPreferencesUtils
import com.lodz.android.imageloaderkt.ImageloaderManager
import com.lodz.android.pandora.utils.acache.ACacheUtils
import java.util.ArrayList

/**
 * APP初始化
 * @author zhouL
 * @date 2020/12/4
 */
class AppIniter : Initializer<Unit> {

    override fun create(context: Context) {
        FileManager.init(context)
        PrintLog.setPrint(BuildConfig.LOG_DEBUG)// 配置日志开关
        NetworkManager.get().init(context)
        initNotificationChannel(context)// 初始化通知通道
        initACache(context)
        initImageLoader(context)
        SharedPreferencesUtils.get().init(context)
//        KoinManager.koinInit(context)
    }

    /** 初始化通知通道 */
    private fun initNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val group = NotificationChannelGroup(Constant.NOTIFI_GROUP_ID, "通知组")
            NotificationUtils.create(context).createNotificationChannelGroup(group)// 设置通知组

            val channels = ArrayList<NotificationChannel>()
            val mainChannel = getMainChannel()
            if (mainChannel != null) {
                channels.add(mainChannel)
            }
            val downloadChannel = getDownloadChannel()
            if (downloadChannel != null) {
                channels.add(downloadChannel)
            }
            val serviceChannel = getServiceChannel()
            if (serviceChannel != null) {
                channels.add(serviceChannel)
            }
            NotificationUtils.create(context).createNotificationChannels(channels)// 设置频道
        }
    }

    /** 获取主通道 */
    private fun getMainChannel(): NotificationChannel? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constant.NOTIFI_CHANNEL_MAIN_ID, "主通知", NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableLights(true)// 开启指示灯，如果设备有的话。
            channel.lightColor = Color.GREEN// 设置指示灯颜色
            channel.description = "应用主通知频道"// 通道描述
            channel.enableVibration(true)// 开启震动
            channel.vibrationPattern = longArrayOf(100, 200, 400, 300, 100)// 设置震动频率
            channel.group = Constant.NOTIFI_GROUP_ID
            channel.canBypassDnd()// 检测是否绕过免打扰模式
            channel.setBypassDnd(true)// 设置绕过免打扰模式
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            channel.canShowBadge()// 检测是否显示角标
            channel.setShowBadge(true)// 设置是否显示角标
            return channel
        }
        return null
    }

    /** 获取下载通道 */
    private fun getDownloadChannel(): NotificationChannel? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constant.NOTIFI_CHANNEL_DOWNLOAD_ID, "下载通知", NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableLights(false)// 开启指示灯，如果设备有的话。
            channel.description = "应用下载通知频道"// 通道描述
            channel.enableVibration(false)// 开启震动
            channel.vibrationPattern = longArrayOf(0)// 设置震动频率
            channel.group = Constant.NOTIFI_GROUP_ID
            channel.setSound(null, null)
            channel.setBypassDnd(false)// 设置绕过免打扰模式
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            channel.setShowBadge(false)// 设置是否显示角标
            return channel
        }
        return null
    }

    /** 获取前台服务通道 */
    private fun getServiceChannel(): NotificationChannel? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constant.NOTIFI_CHANNEL_SERVICE_ID, "服务通知", NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)// 开启指示灯，如果设备有的话。
            channel.lightColor = Color.GREEN// 设置指示灯颜色
            channel.description = "应用服务通知频道"// 通道描述
            channel.enableVibration(true)// 开启震动
            channel.vibrationPattern = longArrayOf(100, 200, 400, 300, 100)// 设置震动频率
            channel.group = Constant.NOTIFI_GROUP_ID
            channel.setBypassDnd(true)// 设置绕过免打扰模式
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.setShowBadge(false)// 设置是否显示角标
            return channel
        }
        return null
    }

    /** 初始化ACache缓存 */
    private fun initACache(context: Context) {
        ACacheUtils.get().newBuilder()
            .setCacheDir(context.cacheDir.absolutePath)// 设置缓存路径，不设置则使用默认路径
            .build(context)// 完成构建
    }

    /** 初始化图片加载库 */
    private fun initImageLoader(context: Context) {
        ImageloaderManager.get().newBuilder()
            .setPlaceholderResId(R.drawable.ic_launcher)//设置默认占位符
            .setErrorResId(R.drawable.ic_launcher)// 设置加载失败图
            .setDirectoryFile(context.cacheDir)// 设置缓存路径
            .setDirectoryName("image_cache")// 缓存文件夹名称
            .build()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}