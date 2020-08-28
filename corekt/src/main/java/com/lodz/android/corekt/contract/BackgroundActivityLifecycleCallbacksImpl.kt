package com.lodz.android.corekt.contract

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * 后台activity数量统计
 * Created by zhouL on 2018/11/8.
 */
class BackgroundActivityLifecycleCallbacksImpl : Application.ActivityLifecycleCallbacks {

    /** 显示的activity数量 */
    private var mCount = 0

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        mCount++
    }

    override fun onActivityResumed(activity: Activity) {}


    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {
        mCount--
        if (mCount < 0) {
            mCount = 0
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    /** 是否在后台 */
    fun isBackground(): Boolean = mCount == 0
}