package com.lodz.android.pandora.utils.transition

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat

/**
 * 动画过度帮助类
 * Created by zhouL on 2018/11/20.
 */
object TransitionHelper {

    /** 使用过度动画跳转，[list]为共享元素列表 */
    @JvmStatic
    fun jumpTransition(activity: Activity, intent: Intent, list: List<Pair<View, String>>) {
        if (list.isEmpty()) {
            activity.startActivity(intent)
        } else {
            activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *list.toTypedArray()).toBundle())
        }
    }

    /** 使用过度动画跳转，共享元素对象[sharedElement]，共享元素名称[sharedElementName] */
    @JvmStatic
    fun jumpTransition(activity: Activity, intent: Intent, sharedElement: View, sharedElementName: String) {
        activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElement, sharedElementName).toBundle())
    }

    /** 关闭页面 */
    @JvmStatic
    fun finish(activity: Activity) {
        activity.finishAfterTransition()
    }

    /** 设置共享元素，控件[view]，共享元素名称[shareElementName] */
    @JvmStatic
    fun setTransition(view: View, shareElementName: String) {
        ViewCompat.setTransitionName(view, shareElementName)
    }

    /** 设置进入的过度时间，[duration]时间长度（毫秒） */
    @JvmStatic
    fun setEnterTransitionDuration(activity: Activity, duration: Long) {
        activity.window.enterTransition.duration = duration
    }

    /** 设置退出的过度时间，[duration]时间长度（毫秒） */
    @JvmStatic
    fun setReturnTransitionDuration(activity: Activity, duration: Long) {
        activity.window.returnTransition.duration = duration
    }
}