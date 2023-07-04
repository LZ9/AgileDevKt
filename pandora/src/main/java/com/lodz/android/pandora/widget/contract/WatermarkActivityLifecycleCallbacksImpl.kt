package com.lodz.android.pandora.widget.contract

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.lodz.android.pandora.widget.watermark.addWatermark

/**
 * 全局Activity水印
 * @author zhouL
 * @date 2023/7/4
 */
class WatermarkActivityLifecycleCallbacksImpl : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        activity.addWatermark()
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}