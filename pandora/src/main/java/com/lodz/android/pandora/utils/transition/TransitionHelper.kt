package com.lodz.android.pandora.utils.transition

import android.app.Activity
import android.content.Intent
import android.os.Build
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
    fun jumpTransition(activity: Activity, intent: Intent, list: List<Pair<View, String>>) {
        if (list.size == 0 || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivity(intent)
        } else {
            activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *list.toTypedArray()).toBundle())
        }
    }

    /** 使用过度动画跳转，共享元素对象[sharedElement]，共享元素名称[sharedElementName] */
    fun jumpTransition(activity: Activity, intent: Intent, sharedElement: View, sharedElementName: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivity(intent)
        } else {
            activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElement, sharedElementName).toBundle())
        }
    }

    /** 关闭页面 */
    fun finish(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.finishAfterTransition()
        } else {
            activity.finish()
        }
    }

    /** 设置共享元素，控件[view]，共享元素名称[shareElementName] */
    fun setTransition(view: View, shareElementName: String) {
        ViewCompat.setTransitionName(view, shareElementName)
    }

    /** 设置进入的过度时间，[duration]时间长度（毫秒） */
    fun setEnterTransitionDuration(activity: Activity, duration: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.enterTransition.duration = duration
        }
    }

    /** 设置退出的过度时间，[duration]时间长度（毫秒） */
    fun setReturnTransitionDuration(activity: Activity, duration: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.returnTransition.duration = duration
        }
    }
}