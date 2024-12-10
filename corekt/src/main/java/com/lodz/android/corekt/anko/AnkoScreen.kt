package com.lodz.android.corekt.anko

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.*
import androidx.fragment.app.Fragment
import java.util.*

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

/** 获得屏幕真实高度（屏幕+导航栏） */
fun Context.getRealScreenHeight(window: Window): Int {
    val point = Point()
    window.windowManager.defaultDisplay.getRealSize(point)
    return point.y//获取真实屏幕高度
}

/** 获得屏幕真实高度（屏幕+导航栏） */
fun Activity.getRealScreenHeight(): Int = getRealScreenHeight(window)

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
fun Window.hasNavigationBar(): Boolean {
    if (Build.BRAND.uppercase(Locale.CHINA).contains("XIAOMI")){
        val isFullScreen = Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0) != 0
        return !isFullScreen
    }

    val realHeight = context.getRealScreenHeight(this)//获取真实屏幕高度
    val screenHeight = context.getScreenHeight()
    val statusBarHeight = context.getStatusBarHeight()
    if (screenHeight < realHeight && realHeight - screenHeight != statusBarHeight) {//可用屏幕高度小于真实屏幕高度 && 真实屏幕高度 - 可用屏幕高度 ！= 状态栏高度（兼容全面屏）
        return true
    }
    return false
}

/** 获取虚拟按键高度 */
fun Context.getNavigationBarHeight(): Int {
    try {
        return resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0
}

/** 判断是否存在NavigationBar */
fun Activity.hasNavigationBar(): Boolean = window.hasNavigationBar()

/** 判断是否存在NavigationBar */
fun Fragment.hasNavigationBar(): Boolean = requireActivity().hasNavigationBar()

/** 获取虚拟按键高度 */
fun Fragment.getNavigationBarHeight(): Int = requireActivity().getNavigationBarHeight()

/** 获取虚拟按键高度 */
fun Activity.getNavigationBarHeight(): Int = applicationContext.getNavigationBarHeight()

/** 获取状态栏高度 */
fun Context.getStatusBarHeight(): Int {
    try {
        return resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0
}

/** 获取状态栏高度 */
fun Fragment.getStatusBarHeight(): Int = requireContext().getStatusBarHeight()

/** 屏幕是否亮屏 */
fun Context.isScreenOn(): Boolean {
    val pm: PowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager? ?: return false
    return pm.isInteractive
}