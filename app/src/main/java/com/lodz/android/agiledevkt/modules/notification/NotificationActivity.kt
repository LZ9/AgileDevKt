package com.lodz.android.agiledevkt.modules.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.config.Constant
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.utils.NotificationUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 通知测试类
 * Created by zhouL on 2018/7/13.
 */
class NotificationActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, NotificationActivity::class.java)
            context.startActivity(intent)
        }
    }



    /** 基本通知 */
    private val mBbaseBtn by bindView<Button>(R.id.base_btn)
    /** 带意图通知 */
    private val mIntentBtn by bindView<Button>(R.id.intent_btn)
    /** 带进度条 */
    private val mProgressBtn by bindView<Button>(R.id.progress_btn)
    /** 单行大文本样式 */
    private val mSingleTxtBtn by bindView<Button>(R.id.single_txt_btn)
    /** 多行大文本样式 */
    private val mMultiTxtBtn by bindView<Button>(R.id.multi_txt_btn)
    /** 大图文本样式 */
    private val mLargeImgBtn by bindView<Button>(R.id.large_img_btn)
    /** 自定义内容样式 */
    private val mCustomBtn by bindView<Button>(R.id.custom_btn)
    /** 跳转详情页通知 */
    private val mMsgBtn by bindView<Button>(R.id.msg_btn)


    override fun getLayoutId() = R.layout.activity_notification

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 基本通知
        mBbaseBtn.setOnClickListener {
            showBaseNotify()
        }

        // 带意图通知
        mIntentBtn.setOnClickListener {
            showIntentNotify()
        }

        // 带进度条
        mProgressBtn.setOnClickListener {
            startProgressNotify()
        }

        // 单行大文本样式
        mSingleTxtBtn.setOnClickListener {
            showSingleTxtNotify()
        }

        // 多行大文本样式
        mMultiTxtBtn.setOnClickListener {
            showMultiTxtNotify()
        }

        // 大图文本样式
        mLargeImgBtn.setOnClickListener {
            showLargeImgNotify()
        }

        // 自定义内容样式
        mCustomBtn.setOnClickListener {
            showCustomNotify()
        }

        mMsgBtn.setOnClickListener {
            goMsgDetail()
        }
    }

    /** 显示基础通知 */
    private fun showBaseNotify() {
        val builder = NotificationCompat.Builder(getContext(), Constant.NOTIFI_CHANNEL_MAIN_ID)// 获取构造器
        builder.setTicker("震惊！拉玛西亚竟然是这个意思") // 通知栏显示的文字
        builder.setContentTitle("拉玛西亚由来") // 通知栏通知的标题
        builder.setContentText("拉玛西亚足球学校始建于1979年，位于巴塞罗那阿里斯蒂德斯大街左侧。当时的球场非常简陋，都是人工草皮，巴萨四个年龄梯队的使用这片拥挤的训练空间。") // 通知栏通知的详细内容（只有一行）
        builder.setAutoCancel(true)// 设置为true，点击该条通知会自动删除，false时只能通过滑动来删除（一般都是true）
        builder.setSmallIcon(R.mipmap.ic_launcher)//通知上面的小图标（必传）
        builder.setDefaults(NotificationCompat.DEFAULT_ALL) //通知默认的声音 震动 呼吸灯
        builder.priority = NotificationCompat.PRIORITY_DEFAULT//设置优先级，级别高的排在前面
        NotificationUtils.create(getContext()).send(builder.build())
    }

    /** 显示带意图的通知 */
    private fun showIntentNotify() {
        val builder = NotificationCompat.Builder(getContext(), Constant.NOTIFI_CHANNEL_MAIN_ID)// 获取构造器
        builder.setTicker("希尔斯堡惨案真相")// 通知栏显示的文字
        builder.setContentTitle("希尔斯堡惨案")// 通知栏通知的标题
        builder.setContentText("因在场警官的谎言、媒体的恶意报道与政府的失公处理，迄今还没有人为希尔斯堡惨案负起应有的责任。")// 通知栏通知的详细内容（只有一行）
        builder.setAutoCancel(true)// 设置为true，点击该条通知会自动删除，false时只能通过滑动来删除（一般都是true）
        builder.setSmallIcon(R.mipmap.ic_launcher)//通知上面的小图标（必传）
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)//通知默认的声音 震动 呼吸灯
        builder.priority = NotificationCompat.PRIORITY_DEFAULT//设置优先级，级别高的排在前面

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com"))
//        val intent = Intent(getContext(), MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(getContext(), UUID.randomUUID().hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT)//创建一个意图
        builder.setContentIntent(pIntent)// 将意图设置到通知上

        NotificationUtils.create(getContext()).send(builder.build())
    }

    /** 开始进度通知 */
    private fun startProgressNotify() {
        Observable.interval(0, 200, TimeUnit.MILLISECONDS)
                .subscribe(object : BaseObserver<Long>() {
                    override fun onBaseNext(times: Long) {
                        if (times > 100) {
                            showProgressNotify(0, true)
                            dispose()
                            return
                        }
                        showProgressNotify(times.toInt(), false)
                    }

                    override fun onBaseError(e: Throwable) {

                    }
                })
    }

    /** 显示带进度[progress]的通知，[isComplete]是否完成 */
    private fun showProgressNotify(progress: Int, isComplete: Boolean) {
        val builder = NotificationCompat.Builder(getContext(), if (isComplete) Constant.NOTIFI_CHANNEL_MAIN_ID else Constant.NOTIFI_CHANNEL_DOWNLOAD_ID)
        builder.setTicker(if (isComplete) "下载完成" else "下载支付宝")// 通知栏显示的文字
        builder.setContentTitle("支付宝V8.8.12")// 通知栏通知的标题
        builder.setContentText(if (isComplete) "支付宝下载完成" else "正在为您下载支付宝")// 通知栏通知的详细内容（只有一行）
        builder.setAutoCancel(true)// 设置为true，点击该条通知会自动删除，false时只能通过滑动来删除（一般都是true）
        builder.setSmallIcon(R.mipmap.ic_launcher)//通知上面的小图标（必传）
        if (isComplete) {
            builder.setProgress(0, 0, false)
            builder.setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)//通知默认的声音 震动 呼吸灯
            builder.priority = NotificationCompat.PRIORITY_DEFAULT//设置优先级，级别高的排在前面
            builder.setVibrate(longArrayOf(0))
            builder.setSound(null)
        } else {
            builder.setProgress(100, progress, false)
        }
        NotificationUtils.create(getContext()).send(1112, builder.build())
    }

    /** 显示单行大文本样式通知 */
    private fun showSingleTxtNotify() {
        val builder = NotificationCompat.Builder(getContext(), Constant.NOTIFI_CHANNEL_MAIN_ID)// 获取构造器
        builder.setTicker("桑切斯加盟梦剧场")// 通知栏显示的文字
        builder.setContentTitle("官宣！智利C罗加盟曼联")// 通知栏通知的标题
        builder.setContentText("北京时间1月23日，桑切斯加盟曼联，身披7号球衣。")// 通知栏通知的详细内容（只有一行）
        builder.setAutoCancel(true)// 设置为true，点击该条通知会自动删除，false时只能通过滑动来删除（一般都是true）
        builder.setSmallIcon(R.mipmap.ic_launcher)//通知上面的小图标（必传）
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)//通知默认的声音 震动 呼吸灯
        builder.priority = NotificationCompat.PRIORITY_DEFAULT//设置优先级，级别高的排在前面

        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle("互换！曼联官方宣布签下桑切斯 姆希塔良加盟阿森纳")// 给样式设置大文本的标题
        bigTextStyle.bigText("穆里尼奥表示：“桑切斯是世界上最好的进攻球员之一，他会进一步增强我们年轻而充满天赋的攻击线，他会带来他的野心、动力和个人品质，能让全队变得更好，让球迷为俱乐部感到骄傲。”")// 给样式设置大文本内容（几行都可以）
        bigTextStyle.setSummaryText("ESPN")//总结，可以不设置
        builder.setStyle(bigTextStyle)// 将样式添加到通知

        NotificationUtils.create(getContext()).send(builder.build())
    }

    /** 显示多行大文本样式通知 */
    private fun showMultiTxtNotify() {
        val builder = NotificationCompat.Builder(getContext(), Constant.NOTIFI_CHANNEL_MAIN_ID)// 获取构造器
        builder.setTicker("你懂鬼畜吗")// 通知栏显示的文字
        builder.setContentTitle("带你了解鬼畜全明星")// 通知栏通知的标题
        builder.setContentText("鬼畜是人类进步的阶梯，你今天进步了吗")// 通知栏通知的详细内容（只有一行）
        builder.setAutoCancel(true)// 设置为true，点击该条通知会自动删除，false时只能通过滑动来删除（一般都是true）
        builder.setSmallIcon(R.mipmap.ic_launcher)//通知上面的小图标（必传）
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)//通知默认的声音 震动 呼吸灯
        builder.priority = NotificationCompat.PRIORITY_DEFAULT//设置优先级，级别高的排在前面

        val inboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.setBigContentTitle("鬼畜常用语")// 给样式设置大文本的标题
        inboxStyle.addLine("* 蕉迟但到")//设置每行内容
        inboxStyle.addLine("* 乖乖站好")
        inboxStyle.addLine("* 我从未见过有如此厚颜无耻之人")
        inboxStyle.addLine("* 新日暮里")
        inboxStyle.setSummaryText("Bilibili动画")//总结，可以不设置
        builder.setStyle(inboxStyle)

        NotificationUtils.create(getContext()).send(builder.build())
    }

    /** 大图文本样式 */
    private fun showLargeImgNotify() {
        val builder = NotificationCompat.Builder(getContext(), Constant.NOTIFI_CHANNEL_MAIN_ID)// 获取构造器
        builder.setTicker("阿森纳の梗")// 通知栏显示的文字
        builder.setContentTitle("阿森纳与4的不解之缘")// 通知栏通知的标题
        builder.setContentText("争4狂魔，没4找4")// 通知栏通知的详细内容（只有一行）
        builder.setAutoCancel(true)// 设置为true，点击该条通知会自动删除，false时只能通过滑动来删除（一般都是true）
        builder.setSmallIcon(R.mipmap.ic_launcher)//通知上面的小图标（必传）
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)//通知默认的声音 震动 呼吸灯
        builder.priority = NotificationCompat.PRIORITY_DEFAULT//设置优先级，级别高的排在前面

        val bigPictureStyle = NotificationCompat.BigPictureStyle()
        bigPictureStyle.setBigContentTitle("温格的无奈")// 给样式设置大文本的标题
        bigPictureStyle.setSummaryText("忠义法 纳私利 范雄心 宋公明")//大文本的内容（只有一行）
        bigPictureStyle.bigPicture(BitmapFactory.decodeResource(resources, R.drawable.ic_regret))//设置大图
        builder.setStyle(bigPictureStyle)

        NotificationUtils.create(getContext()).send(builder.build())
    }

    /** 自定义内容样式 */
    private fun showCustomNotify() {
        val builder = NotificationCompat.Builder(getContext(), Constant.NOTIFI_CHANNEL_MAIN_ID)// 获取构造器
        builder.setTicker("电影头文字D即将上映")// 通知栏显示的文字
        builder.setAutoCancel(true)// 设置为true，点击该条通知会自动删除，false时只能通过滑动来删除（一般都是true）
        builder.setSmallIcon(R.mipmap.ic_launcher)//通知上面的小图标（必传）
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)//通知默认的声音 震动 呼吸灯
        builder.priority = NotificationCompat.PRIORITY_DEFAULT//设置优先级，级别高的排在前面

        val remoteViews = RemoteViews(packageName, R.layout.notify_remote_notification)
        remoteViews.setImageViewResource(R.id.remoteview_icon, R.drawable.ic_regret)
        remoteViews.setTextViewText(R.id.remoteview_title, "头文字D首映")//设置对应id的标题
        remoteViews.setTextViewText(R.id.remoteview_msg, "周杰伦，铃木杏，陈冠希，黄秋生，余文乐，陈小春等主演悉数参加首映式")//设置对应id的内容
        builder.setContent(remoteViews)

        NotificationUtils.create(getContext()).send(builder.build())
    }

    /** 跳转消息详情 */
    private fun goMsgDetail(){
        val money = Random().nextInt(999999) + 1
        val builder = NotificationCompat.Builder(getContext(), Constant.NOTIFI_CHANNEL_MAIN_ID)// 获取构造器
        builder.setTicker("您收到一条消息")// 通知栏显示的文字
        builder.setContentTitle("您有一笔支付宝收款已到账")// 通知栏通知的标题
        builder.setContentText("支付宝到账${money}元")// 通知栏通知的详细内容（只有一行）
        builder.setAutoCancel(true)// 设置为true，点击该条通知会自动删除，false时只能通过滑动来删除（一般都是true）
        builder.setSmallIcon(R.mipmap.ic_launcher)//通知上面的小图标（必传）
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)//通知默认的声音 震动 呼吸灯
        builder.priority = NotificationCompat.PRIORITY_DEFAULT//设置优先级，级别高的排在前面
        builder.setContentIntent(MsgDetailActivity.startPendingIntent(getContext(), "支付宝到账${money}元"))// 将意图设置到通知上
        NotificationUtils.create(getContext()).send(builder.build())
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

}