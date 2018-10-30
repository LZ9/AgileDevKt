package com.lodz.android.corekt.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.app.NotificationCompat
import java.util.*

/**
 * 通知帮助类
 * Created by zhouL on 2018/7/12.
 */
class NotificationUtils private constructor(context: Context) {

    /** 通知管理 */
    private val mNotificationManager: NotificationManager

    companion object {
        /** 创建通知管理 */
        fun create(context: Context): NotificationUtils = NotificationUtils(context)
    }

    init {
        mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    /** 发送标志为[id]的通知[notification] */
    fun send(id: Int, notification: Notification) {
        mNotificationManager.notify(id, notification)
    }

    /** 发送随机id的通知[notification] */
    fun send(notification: Notification) {
        mNotificationManager.notify(getRandomId(), notification)
    }

    /** 创建一个通知通道[channel] */
    fun createNotificationChannel(channel: NotificationChannel): NotificationUtils {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(channel)
        }
        return this
    }

    /** 创建多个通知通道[channels] */
    fun createNotificationChannels(channels: List<NotificationChannel>): NotificationUtils {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannels(channels)
        }
        return this
    }

    /** 创建一个通知通道组[group] */
    fun createNotificationChannelGroup(group: NotificationChannelGroup): NotificationUtils {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannelGroup(group)
        }
        return this
    }

    /** 创建多个通知通道组[groups] */
    fun createNotificationChannelGroups(groups: List<NotificationChannelGroup>): NotificationUtils {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannelGroups(groups)
        }
        return this
    }

    private fun getRandomId() = Random().nextInt(999998) + 1

    /** 如何创建一个NotificationChannel */
    private fun notificationChannelBuild() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val group = NotificationChannelGroup("g0001", "测试分组")

            val channelId = "c00001"
            val channelName = "测试通道"
            val level = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, channelName, level)
            channel.enableLights(true) //开启指示灯，如果设备有的话
            channel.lightColor = Color.RED //设置指示灯颜色
            channel.description = "通道描述" // 通道描述
            channel.enableVibration(true) // 开启震动
            channel.vibrationPattern = longArrayOf(100, 200, 400, 300, 100) // 设置震动频率
            channel.group = group.id
            channel.canBypassDnd() // 检测是否绕过免打扰模式
            channel.setBypassDnd(true) // 设置绕过免打扰模式
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            channel.canShowBadge() // 检测是否显示角标
            channel.setShowBadge(true) // 设置是否显示角标
        }
    }

    private fun notificationBuild(context: Context, bitmap: Bitmap, @DrawableRes drawableId: Int, @LayoutRes layoutId: Int) {
        val builder = NotificationCompat.Builder(context, "123456")// 获取构造器，channelId在O（26）才有需要适配

        //---------------------- 常用 ---------------------
        builder.setTicker("状态栏显示的提示") // 在5.0以上不显示Ticker属性信息
        builder.setContentTitle("内容标题") // 通知栏通知的标题
        builder.setContentText("内容文本信息") // 通知栏通知的详细内容（只有一行）
        builder.setAutoCancel(true) // 设置为true，点击该条通知会自动删除，false时只能通过滑动来删除（一般都是true）
        builder.setSmallIcon(drawableId)//通知上面的小图标（必传）

        //---------------------- 不常用 ---------------------
        builder.setLargeIcon(bitmap)// 通知消息上的大图标（可传可不传）
        builder.setColor(Color.RED)//这边设置颜色，可以给5.0及以上版本smallIcon设置背景色（基本不使用）
        builder.setWhen(System.currentTimeMillis())// 设置该条通知时间（基本不使用）
        builder.setOngoing(false)//设置是否为一个正在进行中的通知，这一类型的通知将无法删除（基本不使用）

        //---------------------- 设置意图 ---------------------
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com"))
        val pIntent = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), intent, PendingIntent.FLAG_CANCEL_CURRENT)
        builder.setContentIntent(pIntent)// 将意图设置到通知上
        builder.setFullScreenIntent(pIntent, true) // 横幅通知（有的系统会直接打开PendingIntent）推荐使用setContentIntent()

        //---------------------- 提示音和优先级 ---------------------
        // NotificationCompat.DEFAULT_SOUND/NotificationCompat.DEFAULT_VIBRATE/NotificationCompat.DEFAULT_LIGHTS
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)//通知默认的声音 震动 呼吸灯
        // NOTIFI_PRIORITY_MAX, NOTIFI_PRIORITY_HIGH, NOTIFI_PRIORITY_DEFAULT, NOTIFI_PRIORITY_LOW, NOTIFI_PRIORITY_MIN
        builder.priority = NotificationCompat.PRIORITY_MAX//设置优先级，级别高的排在前面

        //---------------------- 进度条 ---------------------
        builder.setProgress(100, 10, true)// 设置进度条setProgress(0, 0, false)为移除进度条

        //---------------------- 设置大文本样式 ---------------------
        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle("大文本的标题")// 给样式设置大文本的标题
        bigTextStyle.bigText("这里是点击通知后要显示的正文，正文很多字正文很多字正文很多字") // 给样式设置大文本内容
        bigTextStyle.setSummaryText("末尾只一行的文字内容")//总结，可以不设置
        builder.setStyle(bigTextStyle)// 将样式添加到通知

        //---------------------- 多行大文本样式 ---------------------
        val inboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.setBigContentTitle("大文本的标题")// 给样式设置大文本的标题
        inboxStyle.addLine("啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦")//设置每行内容
        inboxStyle.addLine("哈哈哈哈哈")
        inboxStyle.addLine("嘻嘻嘻嘻")
        inboxStyle.setSummaryText("末尾只一行的文字内容")//总结，可以不设置
        builder.setStyle(inboxStyle)

        //---------------------- 大图文本样式 ---------------------
        val bigPictureStyle = NotificationCompat.BigPictureStyle()
        bigPictureStyle.setBigContentTitle("大文本的标题")// 给样式设置大文本的标题
        bigPictureStyle.setSummaryText("末尾只一行的文字内容")//大文本的内容（只有一行）
        bigPictureStyle.bigPicture(bitmap)//设置大图
        builder.setStyle(bigPictureStyle)

        //---------------------- 自定义内容样式 ---------------------
        val R_id_title = 1 //模拟layout里面的资源id
        val R_id_img = 2 //模拟layout里面的资源id
        val remoteViews = RemoteViews(context.packageName, layoutId)
        remoteViews.setTextViewText(R_id_title, "自定义文本")//设置对应id的内容
        remoteViews.setImageViewResource(R_id_img, drawableId)
        builder.setContent(remoteViews)

        val notification = builder.build() //构建通知
    }
}