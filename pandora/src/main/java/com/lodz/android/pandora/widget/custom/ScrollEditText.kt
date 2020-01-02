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
    private var isPdrCanScroll = false
    /** 是否滚动到顶部 */
    private var isPdrScrollTop = true
    /** 是否滚动到底部 */
    private var isPdrScrollBottom = false
    /** 是否滚动过 */
    private var isPdrScrolled = false

    /** 前一次Y坐标的值 */
    private var mPdrLastY = -1f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action ?: return super.onTouchEvent(event)
        if ((action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
            // 手指按下时判断控件文字是否允许滚动，-1检查向上滚动，1检查向下滚动。
            isPdrCanScroll = canScrollVertically(1) || canScrollVertically(-1)
            isPdrScrolled = false// 按下时默认赋值未滚动过
        }
        if ((action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
            if (!isPdrCanScroll){
                return super.onTouchEvent(event)
            }
            if (mPdrLastY == -1f){
                mPdrLastY = event.y
                return super.onTouchEvent(event)
            }
            parent.requestDisallowInterceptTouchEvent(true)
            val difference = event.y - mPdrLastY
            if (difference > 0){// 向下滑动
                if (isPdrScrollTop && !isPdrScrolled){// 已经在顶部且未滚动过
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            if (difference < 0){// 向上滑动
                if (isPdrScrollBottom && !isPdrScrolled){// 已经在底部且未滚动过
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            mPdrLastY = event.y
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
        isPdrScrollTop = vert == 0
        isPdrScrollBottom = !canScrollVertically(vert)
        isPdrScrolled = true
    }
}