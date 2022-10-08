package com.lodz.android.pandora.widget.swipe

import android.view.View
import android.widget.OverScroller
import kotlin.math.abs

/**
 * @author zhouL
 * @date 2022/7/25
 */
class SwipeLeftHorizontal(menuView: View) : SwipeHorizontal(LEFT_DIRECTION, menuView) {


    override fun isMenuOpen(scrollX: Int): Boolean {
        val i = -getMenuWidth() * getDirection()
        return scrollX <= i && i != 0
    }

    override fun isMenuOpenNotEqual(scrollX: Int): Boolean = scrollX < -getMenuWidth() * getDirection()

    override fun autoOpenMenu(scroller: OverScroller, scrollX: Int, duration: Int) {
        scroller.startScroll(abs(scrollX), 0, getMenuWidth() - abs(scrollX), 0, duration)
    }

    override fun autoCloseMenu(scroller: OverScroller, scrollX: Int, duration: Int) {
        scroller.startScroll(-abs(scrollX), 0, abs(scrollX), 0, duration)
    }

    override fun checkXY(x: Int, y: Int): Checker {
        mPdrChecker.x = x
        mPdrChecker.y = y
        mPdrChecker.shouldResetSwipe = false
        if (mPdrChecker.x == 0) {
            mPdrChecker.shouldResetSwipe = true
        }
        if (mPdrChecker.x >= 0) {
            mPdrChecker.x = 0
        }
        if (mPdrChecker.x <= -getMenuWidth()) {
            mPdrChecker.x = -getMenuWidth()
        }
        return mPdrChecker
    }

    override fun isClickOnContentView(contentViewWidth: Int, x: Float): Boolean = x > getMenuWidth()

}