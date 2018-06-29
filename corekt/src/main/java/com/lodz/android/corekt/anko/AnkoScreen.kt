package com.lodz.android.corekt.anko

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager

/**
 * 屏幕信息扩展类
 * Created by zhouL on 2018/6/29.
 */

/** 获得屏幕宽度 */
fun Context.getScreenWidth(): Int {
    val wm: WindowManager? = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    if (wm == null) {
        return 0
    }

    val dm = DisplayMetrics()
    wm.defaultDisplay.getMetrics(dm)
    return dm.widthPixels
}

/** 获得屏幕高度 */
fun Context.getScreenHeight(): Int {
    val wm: WindowManager? = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    if (wm == null) {
        return 0
    }

    val dm = DisplayMetrics()
    wm.defaultDisplay.getMetrics(dm)
    return dm.heightPixels
}

/** 获得屏幕宽度 */
fun Fragment.getScreenWidth() = requireContext().getScreenWidth()
/** 获得屏幕高度 */
fun Fragment.getScreenHeight() = requireContext().getScreenHeight()

/** 获得屏幕宽度 */
fun View.getScreenWidth() = context.getScreenWidth()
/** 获得屏幕高度 */
fun View.getScreenHeight() = context.getScreenHeight()

/** 判断是否存在NavigationBar */
fun Activity.hasNavigationBar(): Boolean {
    val decorViewHeight = window.decorView.height
    val dm = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(dm)
    val useableScreenHeight = dm.heightPixels
    return decorViewHeight != useableScreenHeight
}

/** 获取虚拟按键高度 */
fun Activity.getNavigationBarHeight(): Int {
    val id = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (id != 0 && hasNavigationBar()) {
        return resources.getDimensionPixelSize(id)
    }
    return 0
}

/** 获取状态栏高度 */
fun Context.getStatusBarHeight(): Int {
    val id = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (id != 0) {
        return resources.getDimensionPixelSize(id)
    }
    return 0
}

/** 判断是否存在NavigationBar */
fun Fragment.hasNavigationBar() = requireActivity().hasNavigationBar()
/** 获取虚拟按键高度 */
fun Fragment.getNavigationBarHeight() = requireActivity().getNavigationBarHeight()
/** 获取状态栏高度 */
fun Fragment.getStatusBarHeight() = requireContext().getStatusBarHeight()

