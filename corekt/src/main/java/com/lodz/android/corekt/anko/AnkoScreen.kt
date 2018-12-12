package com.lodz.android.corekt.anko

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment

/**
 * 屏幕信息扩展类
 * Created by zhouL on 2018/6/29.
 */

/** 获得屏幕宽度 */
fun Context.getScreenWidth(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager

    val dm = DisplayMetrics()
    wm.defaultDisplay.getMetrics(dm)
    return dm.widthPixels
}

/** 获得屏幕高度 */
fun Context.getScreenHeight(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager

    val dm = DisplayMetrics()
    wm.defaultDisplay.getMetrics(dm)
    return dm.heightPixels
}

/** 获得屏幕宽度 */
fun Fragment.getScreenWidth(): Int = requireContext().getScreenWidth()

/** 获得屏幕高度 */
fun Fragment.getScreenHeight(): Int = requireContext().getScreenHeight()

/** 获得屏幕宽度 */
fun View.getScreenWidth(): Int = context.getScreenWidth()

/** 获得屏幕高度 */
fun View.getScreenHeight(): Int = context.getScreenHeight()

/** 判断是否存在NavigationBar */
fun Context.hasNavigationBar(window: Window): Boolean {
    val point = Point()
    window.windowManager.defaultDisplay.getRealSize(point)
    val realHeight = point.y//获取真实屏幕高度
    if (getScreenHeight() < realHeight) {//可用屏幕高度小于真实屏幕高度
        return true
    }
    return false
}

/** 获取虚拟按键高度 */
fun Context.getNavigationBarHeight(window: Window): Int {
    val id = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (id != 0 && hasNavigationBar(window)) {
        return resources.getDimensionPixelSize(id)
    }
    return 0
}

/** 判断是否存在NavigationBar */
fun Activity.hasNavigationBar(): Boolean = hasNavigationBar(window)

/** 判断是否存在NavigationBar */
fun Fragment.hasNavigationBar(): Boolean = requireActivity().hasNavigationBar()

/** 获取虚拟按键高度 */
fun Fragment.getNavigationBarHeight(): Int = requireActivity().getNavigationBarHeight()

/** 获取虚拟按键高度 */
fun Activity.getNavigationBarHeight(): Int = getNavigationBarHeight(window)

/** 获取状态栏高度 */
fun Context.getStatusBarHeight(): Int {
    val id = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (id != 0) {
        return resources.getDimensionPixelSize(id)
    }
    return 0
}

/** 获取状态栏高度 */
fun Fragment.getStatusBarHeight(): Int = requireContext().getStatusBarHeight()

