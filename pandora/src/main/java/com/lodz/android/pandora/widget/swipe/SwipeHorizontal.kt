package com.lodz.android.pandora.widget.swipe

import android.view.View
import android.view.ViewGroup
import android.widget.OverScroller

/**
 * @author zhouL
 * @date 2022/7/20
 */
abstract class SwipeHorizontal(private val mPdrDirection: Int, private val mPdrMenuView: View) {

    companion object {
        /** 左菜单 */
        const val LEFT_DIRECTION = 1

        /** 右菜单 */
        const val RIGHT_DIRECTION = -1
    }

    protected var mPdrChecker = Checker()

    fun canSwipe(): Boolean {
        if (mPdrMenuView is ViewGroup) {
            return mPdrMenuView.childCount > 0
        }
        return false
    }

    fun isCompleteClose(scrollX: Int): Boolean {
        val i = -getMenuWidth() * getDirection()
        return scrollX == 0 && i != 0
    }

    abstract fun isMenuOpen(scrollX: Int): Boolean

    abstract fun isMenuOpenNotEqual(scrollX: Int): Boolean

    abstract fun autoOpenMenu(scroller: OverScroller, scrollX: Int, duration: Int)

    abstract fun autoCloseMenu(scroller: OverScroller, scrollX: Int, duration: Int)

    abstract fun checkXY(x: Int, y: Int): Checker

    abstract fun isClickOnContentView(contentViewWidth: Int, x: Float): Boolean

    fun getDirection() = mPdrDirection

    fun getMenuView() = mPdrMenuView

    fun getMenuWidth() = mPdrMenuView.width

}