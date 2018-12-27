package com.lodz.android.pandora.widget.rv.horizontal

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * 解决横向滑动冲突RecyclerView
 * Created by zhouL on 2018/11/21.
 */
open class HorRecyclerView : RecyclerView {

    /** 按下时的X和Y */
    private var mTouchX: Float = 0f
    private var mTouchY: Float = 0f
    /** 偏移量差值，当X轴的滑动偏移量差值大于这个值时，默认由内部来处理这个滑动事件 */
    private var mOffset = 20

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        if (e == null) {
            return super.onInterceptTouchEvent(e)
        }
        val action = e.action
        if (action == MotionEvent.ACTION_DOWN) {
            mTouchX = e.x
            mTouchY = e.y
        }
        if (action == MotionEvent.ACTION_MOVE) {
            val dx = e.x - mTouchX
            val dy = e.y - mTouchY
            if (Math.abs(dx) > mOffset) {
                parent.requestDisallowInterceptTouchEvent(true)
            } else {
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return super.onInterceptTouchEvent(e)
    }

    /** 设置横向偏移量差值[offset]（默认20） */
    fun setHorOffset(offset: Int) {
        this.mOffset = offset
    }
}