package com.lodz.android.pandora.widget.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText

/**
 * 外嵌ScrollView可滚动文字的EditText
 * Created by zhouL on 2018/12/11.
 */
open class ScrollEditText : AppCompatEditText {

    /** 是否允许滚动 */
    private var isCanScroll = false
    /** 是否滚动到顶部 */
    private var isScrollTop = true
    /** 是否滚动到底部 */
    private var isScrollBottom = false
    /** 是否滚动过 */
    private var isScrolled = false

    /** 前一次Y坐标的值 */
    private var mLastY = -1f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action ?: return super.onTouchEvent(event)
        if ((action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
            // 手指按下时判断控件文字是否允许滚动，-1检查向上滚动，1检查向下滚动。
            isCanScroll = canScrollVertically(1) || canScrollVertically(-1)
            isScrolled = false// 按下时默认赋值未滚动过
        }
        if ((action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
            if (!isCanScroll){
                return super.onTouchEvent(event)
            }
            if (mLastY == -1f){
                mLastY = event.y
                return super.onTouchEvent(event)
            }
            parent.requestDisallowInterceptTouchEvent(true)
            val difference = event.y - mLastY
            if (difference > 0){// 向下滑动
                if (isScrollTop && !isScrolled){// 已经在顶部且未滚动过
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            if (difference < 0){// 向上滑动
                if (isScrollBottom && !isScrolled){// 已经在底部且未滚动过
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            mLastY = event.y
        }
        if ((action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP
                || (action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_CANCEL) {
            // 手指离开时解除拦截
            parent.requestDisallowInterceptTouchEvent(false)
        }
        return super.onTouchEvent(event)
    }

    override fun onScrollChanged(horiz: Int, vert: Int, oldHoriz: Int, oldVert: Int) {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert)
        isScrollTop = vert == 0
        isScrollBottom = !canScrollVertically(vert)
        isScrolled = true
    }
}