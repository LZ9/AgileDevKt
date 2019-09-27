package com.lodz.android.agiledevkt

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.widget.LinearLayout
import androidx.multidex.MultiDex
import com.lodz.android.agiledevkt.config.Constant
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.corekt.network.NetworkManager
import com.lodz.android.corekt.threadpool.ThreadPoolManager
import com.lodz.android.corekt.utils.NotificationUtils
import com.lodz.android.corekt.utils.UiHandler
import com.lodz.android.imageloaderkt.ImageloaderManager
import com.lodz.android.pandora.base.application.BaseApplication
import com.lodz.android.pandora.utils.acache.ACacheUtils
import java.util.*

/**
 * Application
 * Created by zhouL on 2018/6/20.
 */
class App : BaseApplication() {

    companion object {
        @JvmStatic
        fun get(): App = BaseApplication.get() as App
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onStartCreate() {
        FileManager.init()
        PrintLog.setPrint(BuildConfig.LOG_DEBUG)// 配置日志开关
        NetworkManager.get().init(this)
        initNotificationChannel()// 初始化通知通道
        configBaseLayout()
        initACache(this)
        initImageLoader()
    }

    /** 配置基类 */
    private fun configBaseLayout() {
        configTitleBarLayout()
        configErrorLayout()
        configLoadingLayout()
        configNoDataLayout()
    }

    /** 配置标题栏 */
    private fun configTitleBarLayout() {
        getBaseLayoutConfig().getTitleBarLayoutConfig().isNeedBackBtn = true
//        getBaseLayoutConfig().getTitleBarLayoutConfig().backBtnResId = R.drawable.pandora_ic_launcher
        getBaseLayoutConfig().getTitleBarLayoutConfig().backgroundColor = R.color.color_00a0e9
//        getBaseLayoutConfig().getTitleBarLayoutConfig().backBtnText = "返回"
//        getBaseLayoutConfig().getTitleBarLayoutConfig().backBtnTextColor = R.color.color_d9d9d9
//        getBaseLayoutConfig().getTitleBarLayoutConfig().backBtnTextSize = 14
        getBaseLayoutConfig().getTitleBarLayoutConfig().titleTextColor = R.color.white
//        getBaseLayoutConfig().getTitleBarLayoutConfig().titleTextSize = 18
//        getBaseLayoutConfig().getTitleBarLayoutConfig().isShowDivideLine = true
//        getBaseLayoutConfig().getTitleBarLayoutConfig().divideLineHeightDp = 10
//        getBaseLayoutConfig().getTitleBarLayoutConfig().divideLineColor = R.color.color_2f6dc9
//        getBaseLayoutConfig().getTitleBarLayoutConfig().isNeedElevation = false
//        getBaseLayoutConfig().getTitleBarLayoutConfig().elevationVale = 23f
    }


    /** 配置无数据 */
    private fun configNoDataLayout() {
        getBaseLayoutConfig().getNoDataLayoutConfig().orientation = LinearLayout.VERTICAL
        getBaseLayoutConfig().getNoDataLayoutConfig().isNeedImg = true
        getBaseLayoutConfig().getNoDataLayoutConfig().isNeedTips = true
//        getBaseLayoutConfig().getNoDataLayoutConfig().drawableResId = R.drawable.pandora_ic_launcher
//        getBaseLayoutConfig().getNoDataLayoutConfig().tips = "没数据飞走了"
//        getBaseLayoutConfig().getNoDataLayoutConfig().textColor = R.color.color_ffa630
//        getBaseLayoutConfig().getNoDataLayoutConfig().textSize = 23
//        getBaseLayoutConfig().getNoDataLayoutConfig().backgroundColor = R.color.color_ea8380

    }

    /** 配置加载页 */
    private fun configLoadingLayout() {
        getBaseLayoutConfig().getLoadingLayoutConfig().orientation = LinearLayout.VERTICAL
        getBaseLayoutConfig().getLoadingLayoutConfig().isNeedTips = true
//        getBaseLayoutConfig().getLoadingLayoutConfig().tips = "正在获取数据"
//        getBaseLayoutConfig().getLoadingLayoutConfig().textColor = R.color.white
//        getBaseLayoutConfig().getLoadingLayoutConfig().textSize = 23
//        getBaseLayoutConfig().getLoadingLayoutConfig().backgroundColor = R.color.color_ff4081
        getBaseLayoutConfig().getLoadingLayoutConfig().isIndeterminate = true
//        getBaseLayoutConfig().getLoadingLayoutConfig().indeterminateDrawable = R.drawable.anims_custom_progress
        getBaseLayoutConfig().getLoadingLayoutConfig().useSysDefDrawable = false
//        getBaseLayoutConfig().getLoadingLayoutConfig().pbWidthPx = dp2px(70)
//        getBaseLayoutConfig().getLoadingLayoutConfig().pbHeightPx = dp2px(70)
    }

    private fun configErrorLayout() {
        getBaseLayoutConfig().getErrorLayoutConfig().orientation = LinearLayout.VERTICAL
        getBaseLayoutConfig().getErrorLayoutConfig().isNeedImg = true
        getBaseLayoutConfig().getErrorLayoutConfig().isNeedTips = true
//        getBaseLayoutConfig().getErrorLayoutConfig().drawableResId = R.drawable.pandora_ic_launcher
//        getBaseLayoutConfig().getErrorLayoutConfig().backgroundColor = R.color.color_ffa630
//        getBaseLayoutConfig().getErrorLayoutConfig().tips = "接口出错啦"
//        getBaseLayoutConfig().getErrorLayoutConfig().textColor = R.color.color_ea413c
//        getBaseLayoutConfig().getErrorLayoutConfig().textSize = 18
    }

    /** 初始化通知通道 */
    private fun initNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val group = NotificationChannelGroup(Constant.NOTIFI_GROUP_ID, "通知组")
            NotificationUtils.create(applicationContext).createNotificationChannelGroup(group)// 设置通知组

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
            NotificationUtils.create(applicationContext).createNotificationChannels(channels)// 设置频道
        }
    }

    /** 获取主通道 */
    private fun getMainChannel(): NotificationChannel? {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
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
    private fun initImageLoader() {
        ImageloaderManager.get().newBuilder()
                .setPlaceholderResId(R.drawable.ic_launcher)//设置默认占位符
                .setErrorResId(R.drawable.ic_launcher)// 设置加载失败图
                .setDirectoryFile(applicationContext.cacheDir)// 设置缓存路径
                .setDirectoryName("image_cache")// 缓存文件夹名称
                .build()
    }

    override fun onExit() {
        // todo 待完善
        UiHandler.destroy()
        ThreadPoolManager.get().releaseAll()
        NetworkManager.get().release(this)
        NetworkManager.get().clearNetworkListener()
    }

}