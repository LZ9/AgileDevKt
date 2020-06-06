package com.lodz.android.imageloaderkt.glide.impl

import android.app.Notification
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.RemoteViews
import com.bumptech.glide.request.target.NotificationTarget

/**
 * NotificationTarget修复类
 * 修复关闭activity时调用onLoadCleared会再次通知的BUG
 * @author zhouL
 * @date 2020/6/6
 */
open class NotificationTargetFix : NotificationTarget {

    constructor(
        context: Context?,
        viewId: Int,
        remoteViews: RemoteViews?,
        notification: Notification?,
        notificationId: Int
    ) : super(context, viewId, remoteViews, notification, notificationId)

    constructor(
        context: Context?,
        viewId: Int,
        remoteViews: RemoteViews?,
        notification: Notification?,
        notificationId: Int,
        notificationTag: String?
    ) : super(context, viewId, remoteViews, notification, notificationId, notificationTag)

    constructor(
        context: Context?,
        width: Int,
        height: Int,
        viewId: Int,
        remoteViews: RemoteViews?,
        notification: Notification?,
        notificationId: Int,
        notificationTag: String?
    ) : super(
        context,
        width,
        height,
        viewId,
        remoteViews,
        notification,
        notificationId,
        notificationTag
    )

    override fun onLoadCleared(placeholder: Drawable?) {
    }
}