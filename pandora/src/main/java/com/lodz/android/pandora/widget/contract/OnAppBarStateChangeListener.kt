package com.lodz.android.pandora.widget.contract

import androidx.annotation.IntDef
import com.google.android.material.appbar.AppBarLayout

/**
 * AppBarLayout的滑动偏移监听器
 * Created by zhouL on 2018/9/5.
 */
abstract class OnAppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {

    companion object {
        /** 完全折叠 */
        const val COLLAPSED = 0
        /** 完全展开 */
        const val EXPANDED = 1
        /** 滑动中 */
        const val SCROLLING = 2
    }

    @IntDef(EXPANDED, COLLAPSED, SCROLLING)
    @Retention(AnnotationRetention.SOURCE)
    annotation class OffsetStatus

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (appBarLayout == null) {
            return
        }
        if (verticalOffset == 0) {
            // 张开
            onStateChanged(appBarLayout, EXPANDED, 1.0)
        } else if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange) {
            //收缩
            onStateChanged(appBarLayout, COLLAPSED, 0.0)
        } else {
            val delta = Math.abs(verticalOffset) / appBarLayout.totalScrollRange.toDouble()
            onStateChanged(appBarLayout, SCROLLING, 1.0 - delta)
        }

    }

    /** 控件[appBarLayout]偏移状态[state]回调，偏移参数[delta]范围：0展开 1折叠 0.0-1.0滚动  */
    abstract fun onStateChanged(appBarLayout: AppBarLayout, @OffsetStatus state: Int, delta: Double)
}