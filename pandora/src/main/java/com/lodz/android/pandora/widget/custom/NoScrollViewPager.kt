package com.lodz.android.pandora.widget.custom

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
    var isPdrScroll = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (isPdrScroll) {
            return super.onTouchEvent(ev)
        }
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (isPdrScroll) {
            return super.onInterceptTouchEvent(ev)
        }
        return false
    }
}