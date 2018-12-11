package com.lodz.android.componentkt.widget.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * 不能左右滑动的ViewPager
 * Created by zhouL on 2018/12/11.
 */
open class NoScrollViewPager : ViewPager {

    /** 是否可以滑动，默认不行 */
    public var isScroll = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (isScroll) {
            return super.onTouchEvent(ev)
        }
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (isScroll) {
            return super.onInterceptTouchEvent(ev)
        }
        return false
    }
}