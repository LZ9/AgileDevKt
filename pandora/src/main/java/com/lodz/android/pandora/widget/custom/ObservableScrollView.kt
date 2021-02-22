package com.lodz.android.pandora.widget.custom

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView

/**
 * 可订阅滚动事件的NestedScrollView
 * @author zhouL
 * @date 2020/2/14
 */
class ObservableScrollView : NestedScrollView {

    private var mListener: OnScrollListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        mListener?.onScrollChanged(l, t, oldl, oldt)
    }

    fun setOnScrollListener(listener: OnScrollListener?) {
        mListener = listener
    }

    fun interface OnScrollListener {
        fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int)
    }
}