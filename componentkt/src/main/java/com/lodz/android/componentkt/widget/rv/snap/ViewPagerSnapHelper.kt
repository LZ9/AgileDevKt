package com.lodz.android.componentkt.widget.rv.snap

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * 支持pager位置回调的PagerSnapHelper
 * Created by zhouL on 2018/11/21.
 */
open class ViewPagerSnapHelper(val mStartPosition: Int) : PagerSnapHelper() {

    /** 监听器 */
    private var mOnPageChangeListener: ((Int) -> Unit)? = null

    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        super.attachToRecyclerView(recyclerView)
        if (recyclerView == null) {
            return
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var lastPosition = mStartPosition
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val layoutManager = recyclerView.layoutManager
                if (layoutManager == null) {
                    return
                }

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val centerView = findSnapView(layoutManager)
                    if (centerView != null) {
                        val position = layoutManager.getPosition(centerView)
                        if (lastPosition == 1 && position == 0) {// 修复用户连续滑动回到第一个item时不会回调RecyclerView.SCROLL_STATE_IDLE状态的BUG
                            if (mOnPageChangeListener != null) {
                                mOnPageChangeListener!!.invoke(position)
                            }
                            lastPosition = position
                        }
                    }
                    return
                }

                val centerView = findSnapView(layoutManager)
                var position = -1
                if (newState == RecyclerView.SCROLL_STATE_SETTLING && centerView != null) {
                    position = layoutManager.getPosition(centerView)//记录滑动中的位置
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE && centerView != null) {
                    val idlePosition = layoutManager.getPosition(centerView)
                    if (position != idlePosition) {//滑动中位置和滑动结束位置不一致时更新位置数据
                        position = idlePosition
                    }
                }

                if (lastPosition != position) {//获取的位置和最近一次的位置不一致时，通知外部变化
                    if (mOnPageChangeListener != null) {
                        mOnPageChangeListener!!.invoke(position)
                    }
                }
                lastPosition = position//更新最近一次的位置
            }
        })
    }

    /** 设置pager变化监听器[listener]，返回pager位置[position] */
    fun setOnPageChangeListener(listener: (Int) -> Unit) {
        mOnPageChangeListener = listener
    }
}