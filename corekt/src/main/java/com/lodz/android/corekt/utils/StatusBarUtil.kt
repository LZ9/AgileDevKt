package com.lodz.android.corekt.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.IntRange
import android.support.design.widget.CoordinatorLayout
import android.support.v4.widget.DrawerLayout
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import com.lodz.android.corekt.R

/**
 * 状态栏帮助类
 * Created by zhouL on 2018/8/29.
 */
object StatusBarUtil {

    private val DEFAULT_STATUS_BAR_ALPHA = 112
    private val FAKE_STATUS_BAR_VIEW_ID = R.id.statusbarutil_fake_status_bar_view
    private val FAKE_TRANSLUCENT_VIEW_ID = R.id.statusbarutil_translucent_view
    private val TAG_KEY_HAVE_SET_OFFSET = -123

    /** 设置状态栏颜色[color]，透明度[alpha] */
    fun setColor(activity: Activity, @ColorInt color: Int, @IntRange(from = 0, to = 255) alpha: Int = DEFAULT_STATUS_BAR_ALPHA) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = calculateStatusColor(color, alpha)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val decorView = activity.window.decorView
            val fakeStatusBarView: View? = decorView.findViewById<View>(FAKE_STATUS_BAR_VIEW_ID)
            if (fakeStatusBarView != null) {
                fakeStatusBarView.visibility = View.VISIBLE
                fakeStatusBarView.setBackgroundColor(calculateStatusColor(color, alpha))
            } else {
                if (decorView is ViewGroup) {
                    decorView.addView(createStatusBarView(activity, color, alpha))
                }
            }
            setRootView(activity)
        }
    }

    /** 为滑动返回界面设置状态栏颜色[color]，透明度[alpha] */
    fun setColorForSwipeBack(activity: Activity, @ColorInt color: Int, @IntRange(from = 0, to = 255) alpha: Int = DEFAULT_STATUS_BAR_ALPHA) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
            val rootView = contentView.getChildAt(0)
            val statusBarHeight = getStatusBarHeight(activity)
            if (rootView != null && rootView is CoordinatorLayout) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    rootView.fitsSystemWindows = false
                    contentView.setBackgroundColor(calculateStatusColor(color, alpha))
                    val isNeedRequestLayout = contentView.paddingTop < statusBarHeight
                    if (isNeedRequestLayout) {
                        contentView.setPadding(0, statusBarHeight, 0, 0)
                        rootView.post(Runnable {
                            rootView.requestLayout()
                        })
                    }
                } else {
                    rootView.setStatusBarBackgroundColor(calculateStatusColor(color, alpha))
                }
            } else {
                contentView.setPadding(0, statusBarHeight, 0, 0)
                contentView.setBackgroundColor(calculateStatusColor(color, alpha))
            }
            setTransparentForWindow(activity)
        }
    }

    /** 设置状态栏纯色[color] 不加半透明效果 */
    fun setColorNoTranslucent(activity: Activity, @ColorInt color: Int) {
        setColor(activity, color, 0)
    }

    /** 设置状态栏透明度[alpha]，默认半透明 */
    fun setTranslucent(activity: Activity, @IntRange(from = 0, to = 255) alpha: Int = DEFAULT_STATUS_BAR_ALPHA) {
        setTransparent(activity)
        addTranslucentView(activity, alpha)
    }

    /** 针对根布局是 CoordinatorLayout, 设置状态栏透明度[alpha]，默认半透明 */
    fun setTranslucentForCoordinatorLayout(activity: Activity, @IntRange(from = 0, to = 255) alpha: Int = DEFAULT_STATUS_BAR_ALPHA) {
        transparentStatusBar(activity)
        addTranslucentView(activity, alpha)
    }

    /** 设置状态栏全透明 */
    fun setTransparent(activity: Activity) {
        transparentStatusBar(activity)
        setRootView(activity)
    }

    /** 为DrawerLayout 布局设置状态栏颜色[color] */
    fun setColorNoTranslucentForDrawerLayout(activity: Activity, drawerLayout: DrawerLayout, @ColorInt color: Int) {
        setColorForDrawerLayout(activity, drawerLayout, color, 0)
    }

    /** 为DrawerLayout 布局设置状态栏颜色[color]和透明度[alpha] */
    fun setColorForDrawerLayout(activity: Activity, drawerLayout: DrawerLayout, @ColorInt color: Int, @IntRange(from = 0, to = 255) alpha: Int = DEFAULT_STATUS_BAR_ALPHA) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = Color.TRANSPARENT
        } else {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        // 生成一个状态栏大小的矩形
        // 添加 statusBarView 到布局中
        val contentLayout = drawerLayout.getChildAt(0) as ViewGroup
        val fakeStatusBarView = contentLayout.findViewById<View>(FAKE_STATUS_BAR_VIEW_ID)
        if (fakeStatusBarView != null) {
            fakeStatusBarView.visibility = View.VISIBLE
            fakeStatusBarView.setBackgroundColor(color)
        } else {
            contentLayout.addView(createStatusBarView(activity, color), 0)
        }
        // 内容布局不是 LinearLayout 时,设置padding top
        if (!(contentLayout is LinearLayout) && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1).setPadding(contentLayout.paddingLeft, getStatusBarHeight(activity) + contentLayout.paddingTop,
                    contentLayout.paddingRight, contentLayout.paddingBottom)
        }
        // 设置属性
        setDrawerLayoutProperty(drawerLayout, contentLayout)
        addTranslucentView(activity, alpha)
    }

    /** 为 DrawerLayout 布局设置状态栏透明度[alpha] */
    fun setTranslucentForDrawerLayout(activity: Activity, drawerLayout: DrawerLayout, @IntRange(from = 0, to = 255) alpha: Int = DEFAULT_STATUS_BAR_ALPHA) {
        setTransparentForDrawerLayout(activity, drawerLayout)
        addTranslucentView(activity, alpha)
    }

    /** 为 DrawerLayout 布局设置状态栏透明 */
    fun setTransparentForDrawerLayout(activity: Activity, drawerLayout: DrawerLayout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = Color.TRANSPARENT
        } else {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        val contentLayout = drawerLayout.getChildAt(0) as ViewGroup
        // 内容布局不是 LinearLayout 时,设置padding top
        if (!(contentLayout is LinearLayout) && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0)
        }
        // 设置属性
        setDrawerLayoutProperty(drawerLayout, contentLayout)
    }

    /** 为头部是 ImageView 的界面设置状态栏全透明，[needOffsetView]是需要向下偏移的View */
    fun setTransparentForImageView(activity: Activity, needOffsetView: View) {
        setTranslucentForImageView(activity, needOffsetView, 0)
    }

    /** 为头部是 ImageView 的界面设置状态栏透明度为[alpha]，[needOffsetView]是需要向下偏移的View */
    fun setTranslucentForImageView(activity: Activity, needOffsetView: View, @IntRange(from = 0, to = 255) alpha: Int = DEFAULT_STATUS_BAR_ALPHA) {
        setTransparentForWindow(activity)
        addTranslucentView(activity, alpha)
        val haveSetOffset = needOffsetView.getTag(TAG_KEY_HAVE_SET_OFFSET)
        if (haveSetOffset != null && haveSetOffset is Boolean && haveSetOffset) {
            return
        }
        val layoutParams = needOffsetView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin + getStatusBarHeight(activity),
                layoutParams.rightMargin, layoutParams.bottomMargin)
        needOffsetView.setTag(TAG_KEY_HAVE_SET_OFFSET, true)
    }

    /** 为 fragment 头部是 ImageView 的设置状态栏全透明，[needOffsetView]是需要向下偏移的View */
    fun setTransparentForImageViewInFragment(activity: Activity, needOffsetView: View) {
        setTranslucentForImageViewInFragment(activity, needOffsetView, 0)
    }

    /** 为 fragment 头部是 ImageView 的设置状态栏透明度[alpha]，[needOffsetView]是需要向下偏移的View */
    fun setTranslucentForImageViewInFragment(activity: Activity, needOffsetView: View, @IntRange(from = 0, to = 255) alpha: Int = DEFAULT_STATUS_BAR_ALPHA) {
        setTranslucentForImageView(activity, needOffsetView, alpha)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            clearPreviousSetting(activity)
        }
    }

    /** 获取状态栏高度 */
    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    /** 设置[activity]的根布局参数 */
    private fun setRootView(activity: Activity) {
        val parent = activity.findViewById<ViewGroup>(android.R.id.content)

        for (i in 0 until parent.childCount) {
            val childView = parent.getChildAt(i)
            if (childView is ViewGroup) {
                childView.fitsSystemWindows = true
                childView.clipToPadding = true
            }
        }
    }

    /** 生成一个和状态栏大小相同的颜色为[color]的矩形条 */
    private fun createStatusBarView(activity: Activity, @ColorInt color: Int) = createStatusBarView(activity, color, 0)

    /** 生成一个和状态栏大小相同的颜色为[color]透明度为[alpha]的矩形条 */
    private fun createStatusBarView(activity: Activity, @ColorInt color: Int, @IntRange(from = 0, to = 255) alpha: Int): View {
        // 绘制一个和状态栏一样高的矩形
        val statusBarView = View(activity)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity))
        statusBarView.layoutParams = params
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha))
        statusBarView.id = FAKE_STATUS_BAR_VIEW_ID
        return statusBarView
    }

    /** 颜色[color]，透明度[alpha] */
    @ColorInt
    private fun calculateStatusColor(@ColorInt color: Int, @IntRange(from = 0, to = 255) alpha: Int): Int {
        if (alpha == 0) {
            return color
        }

        val a = 1 - alpha / 255f
        var red = color shr 16 and 0xff
        var green = color shr 8 and 0xff
        var blue = color and 0xff
        red = (red * a + 0.5).toInt()
        green = (green * a + 0.5).toInt()
        blue = (blue * a + 0.5).toInt()
        return (0xff shl 24) or (red shl 16) or (green shl 8) or blue
    }

    /** 设置[drawerLayout]属性，[drawerLayoutContentLayout]为DrawerLayout的内容布局 */
    private fun setDrawerLayoutProperty(drawerLayout: DrawerLayout, drawerLayoutContentLayout: ViewGroup) {
        val drawer = drawerLayout.getChildAt(1)
        drawerLayout.fitsSystemWindows = false
        drawer.fitsSystemWindows = false
        drawerLayoutContentLayout.fitsSystemWindows = false
        drawerLayoutContentLayout.clipToPadding = true
    }

    /** 设置透明 */
    private fun setTransparentForWindow(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = Color.TRANSPARENT
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    /** 使状态栏透明 */
    private fun transparentStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            activity.window.statusBarColor = Color.TRANSPARENT
        } else {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    /** 添加透明度为[alpha]的矩形条 */
    private fun addTranslucentView(activity: Activity, @IntRange(from = 0, to = 255) alpha: Int) {
        val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
        val fakeTranslucentView = contentView.findViewById<View>(FAKE_TRANSLUCENT_VIEW_ID)
        if (fakeTranslucentView != null) {
            fakeTranslucentView.visibility = View.VISIBLE
            fakeTranslucentView.setBackgroundColor(Color.argb(alpha, 0, 0, 0))
        } else {
            contentView.addView(createTranslucentStatusBarView(activity, alpha))
        }
    }

    /** 创建半透明[alpha]矩形 */
    private fun createTranslucentStatusBarView(activity: Activity, @IntRange(from = 0, to = 255) alpha: Int): View {
        // 绘制一个和状态栏一样高的矩形
        val statusBarView = View(activity)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity))
        statusBarView.layoutParams = params
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0))
        statusBarView.id = FAKE_TRANSLUCENT_VIEW_ID
        return statusBarView
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun clearPreviousSetting(activity: Activity) {
        val decorView = activity.window.decorView as ViewGroup
        val fakeStatusBarView = decorView.findViewById<View>(FAKE_STATUS_BAR_VIEW_ID)
        if (fakeStatusBarView != null) {
            decorView.removeView(fakeStatusBarView)
            val rootView = activity.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
            rootView.setPadding(0, 0, 0, 0)
        }
    }
}







