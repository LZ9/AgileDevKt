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

    override fun onActivityPaused(activity: Activity?) {}

    override fun onActivityResumed(activity: Activity?) {}

    override fun onActivityStarted(activity: Activity?) {
        mCount++
    }

    override fun onActivityDestroyed(activity: Activity?) {}

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

    override fun onActivityStopped(activity: Activity?) {
        mCount--
        if (mCount < 0) {
            mCount = 0
        }
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}

    /** 是否在后台 */
    fun isBackground(): Boolean = mCount == 0
}