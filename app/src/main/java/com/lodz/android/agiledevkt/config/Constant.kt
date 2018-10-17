package com.lodz.android.agiledevkt.config

import android.support.annotation.StringDef

/**
 * 常量
 * Created by zhouL on 2018/10/17.
 */
object Constant {
    /** 通知组id  */
    const val NOTIFI_GROUP_ID = "g0001"
    /** 主频道id  */
    const val NOTIFI_CHANNEL_MAIN_ID = "c0001"
    /** 下载频道id  */
    const val NOTIFI_CHANNEL_DOWNLOAD_ID = "c0002"
    /** 服务频道id  */
    const val NOTIFI_CHANNEL_SERVICE_ID = "c0003"

    @StringDef(NOTIFI_GROUP_ID, NOTIFI_CHANNEL_MAIN_ID, NOTIFI_CHANNEL_DOWNLOAD_ID, NOTIFI_CHANNEL_SERVICE_ID)
    @Retention(AnnotationRetention.SOURCE)
    annotation class NotifyChannelId

}