package com.lodz.android.componentkt.widget.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText

/**
 * 外嵌ScrollView可滚动文字
 * Created by zhouL on 2018/12/11.
 */
open class ScrollEditText : AppCompatEditText {

    /** 移动距离临界 */
    private val MOVE_SLOP = 20

    /** 滑动距离的最大边界 */
    private var mOffsetHeight = 0

    /** 是否到顶或者到底的标志 */
    private var mBottomFlag = false
    /** 标记内容是否触发了滚动 */
    private var isCanScroll = false
    private var mLastY = 0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //计算滑动距离的边界(H_content - H_view = H_scroll)
        mOffsetHeight = layout.height + totalPaddingTop + totalPaddingBottom + height
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event != null && event.action == MotionEvent.ACTION_DOWN) {
            //手指按下事件，重置状态
            mBottomFlag = false
            isCanScroll = false
            mLastY = 0f
        }
        return super.dispatchTouchEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //如果是需要拦截，则再拦截，这个方法会在onScrollChanged方法之后再调用一次
        if (!mBottomFlag) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        if (event != null && event.action == MotionEvent.ACTION_MOVE) {
            if (mLastY == 0f) {
                mLastY = event.rawY
            }
            //条件：手指move了一段距离，但是onScrollChanged函数未调用，说明文字无法滚动了，则将触摸处理权交还给ParentView
            if (Math.abs(mLastY - event.rawY) > MOVE_SLOP) {
                if (!isCanScroll) {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onScrollChanged(horiz: Int, vert: Int, oldHoriz: Int, oldVert: Int) {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert)
        isCanScroll = true
        if (vert == mOffsetHeight || vert == 0) {
            //这里将处理权交还给ScrollView
            parent.requestDisallowInterceptTouchEvent(false)
            mBottomFlag = true
        }
    }
}