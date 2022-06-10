package com.lodz.android.corekt.utils

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.drawerlayout.widget.DrawerLayout
import com.lodz.android.corekt.R
import com.lodz.android.corekt.anko.getStatusBarHeight

/**
 * 状态栏帮助类
 * Created by zhouL on 2018/8/29.
 */
object StatusBarUtil {

    private val FAKE_STATUS_BAR_VIEW_ID = R.id.statusbarutil_fake_status_bar_view
    private val FAKE_TRANSLUCENT_VIEW_ID = R.id.statusbarutil_translucent_view
    private const val TAG_KEY_HAVE_SET_OFFSET = -123
    private const val DEFAULT_ALPHA = 0.45f

    /** 获取状态栏颜色 */
    @ColorInt
    @JvmStatic
    fun getColor(window: Window): Int = window.statusBarColor

    /** 设置状态栏颜色[color]和透明度[alpha]（默认不透明） */
    @JvmStatic
    @JvmOverloads
    fun setColor(window: Window, @ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ColorUtils.getColorAlphaInt(color, alpha)
    }

    /** 获取导航栏颜色 */
    @ColorInt
    @JvmStatic
    fun getNavigationBarColor(window: Window): Int = window.navigationBarColor

    /** 设置导航栏颜色[color] */
    @JvmStatic
    fun setNavigationBarColor(window: Window, @ColorInt color: Int) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.navigationBarColor = color
    }

    /** 设置状态栏全透明 */
    @JvmStatic
    fun setTransparentFully(window: Window) {
        setTransparent(window, 0.0f)
    }

    /** 设置状态栏透明度[alpha]（默认45%） */
    @JvmStatic
    @JvmOverloads
    fun setTransparent(window: Window, @FloatRange(from = 0.0, to = 1.0) alpha: Float = DEFAULT_ALPHA) {
        val color = getColor(window)
        setColor(window, color, alpha)
    }

    /** 为需要向下偏移的[needOffsetView]的界面设置状态栏全透明 */
    @JvmStatic
    fun setTransparentFullyForOffsetView(activity: Activity, needOffsetView: View) {
        setTransparentForOffsetView(activity, needOffsetView, 0.0f)
    }

    /** 为需要向下偏移的[needOffsetView]的界面设置状态栏透明度为[alpha]（默认45%），颜色为[colorBg]（默认黑色） */
    @JvmStatic
    @JvmOverloads
    fun setTransparentForOffsetView(activity: Activity, needOffsetView: View, @FloatRange(from = 0.0, to = 1.0) alpha: Float = DEFAULT_ALPHA, @ColorInt colorBg: Int = Color.BLACK) {
        configStatusBarTransparent(activity.window)
        addStatusBarView(activity, alpha, colorBg)
        configOffsetView(needOffsetView)
    }

    /** 为侧滑栏[drawerLayout]布局设置状态栏全透明，需要向下偏移的控件为[needOffsetView]（不需要传null） */
    @JvmStatic
    fun setTransparentFullyForDrawerLayout(activity: Activity, drawerLayout: DrawerLayout, needOffsetView: View) {
        setTransparentForDrawerLayout(activity, drawerLayout, needOffsetView, 0.0f)
    }

    /** 为侧滑栏[drawerLayout]布局设置状态栏透明度[alpha]（默认45%），需要向下偏移的控件为[needOffsetView]（不需要传null），状态栏颜色为[colorBg]（默认黑色） */
    @JvmStatic
    @JvmOverloads
    fun setTransparentForDrawerLayout(activity: Activity, drawerLayout: DrawerLayout, needOffsetView: View, @FloatRange(from = 0.0, to = 1.0) alpha: Float = DEFAULT_ALPHA, @ColorInt colorBg: Int = Color.BLACK) {
        val drawer = drawerLayout.getChildAt(1)
        if (drawer == null || drawer !is ViewGroup) {
            return
        }

        setTransparentForOffsetView(activity, needOffsetView, alpha, colorBg)
        drawerLayout.fitsSystemWindows = false
        drawer.fitsSystemWindows = false
    }

    /** 配置状态栏为透明 */
    private fun configStatusBarTransparent(window: Window) {
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    /** 配置需要偏移的控件[needOffsetView] */
    private fun configOffsetView(needOffsetView: View) {
        val haveSetOffset = needOffsetView.getTag(TAG_KEY_HAVE_SET_OFFSET)
        if (haveSetOffset != null && haveSetOffset is Boolean && haveSetOffset) {
            return
        }
        val layoutParams = needOffsetView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(
            layoutParams.leftMargin,
            layoutParams.topMargin + needOffsetView.context.getStatusBarHeight(),
            layoutParams.rightMargin,
            layoutParams.bottomMargin
        )
        needOffsetView.setTag(TAG_KEY_HAVE_SET_OFFSET, true)
    }

    /** 添加透明度为[alpha]，颜色为[colorBg]的矩形条 */
    private fun addStatusBarView(activity: Activity, @FloatRange(from = 0.0, to = 1.0) alpha: Float, @ColorInt colorBg: Int) {
        val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
        val view = contentView.findViewById<View>(FAKE_TRANSLUCENT_VIEW_ID)
        if (view != null) {
            view.visibility = View.VISIBLE
            view.setBackgroundColor(ColorUtils.getColorAlphaInt(colorBg, alpha))
            return
        }
        val statusBarView = View(activity)
        statusBarView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, activity.getStatusBarHeight())
        statusBarView.setBackgroundColor(ColorUtils.getColorAlphaInt(colorBg, alpha))
        statusBarView.id = FAKE_TRANSLUCENT_VIEW_ID
        contentView.addView(statusBarView)
    }
}







