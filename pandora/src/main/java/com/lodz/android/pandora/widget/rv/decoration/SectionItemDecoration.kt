package com.lodz.android.pandora.widget.rv.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.array.Groupable

/**
 * 分组标签装饰器（分组标题由item里字符串的第一位截取）
 * 泛型T可以为String或者实现了Groupable的任意类
 * Created by zhouL on 2018/11/22.
 */
open class SectionItemDecoration<T> protected constructor(context: Context) : BaseSectionItemDecoration(context) {

    companion object {
        /** 创建 */
        fun <T> create(context: Context): SectionItemDecoration<T> = SectionItemDecoration(context)
    }

    /** 数据回调 */
    protected var mOnSectionCallback: OnSectionCallback<T>? = null

    /** 设置回调[callback] */
    fun setOnSectionCallback(callback: OnSectionCallback<T>?): SectionItemDecoration<*> {
        mOnSectionCallback = callback
        return this
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (mOnSectionCallback == null) {
            return
        }
        if (!isVerLinearLayout(parent)) {
            return
        }
        val position = parent.getChildAdapterPosition(view)
        if (getItem(position, mOnSectionCallback!!).isEmpty()) {
            return
        }
        outRect.top = if (isFirstGroupItem(position, mOnSectionCallback!!)) mSectionHeightPx else 0// 设置分组高度
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
        if (!isVerLinearLayout(parent)) {
            return
        }
        if (mOnSectionCallback == null) {
            return
        }
        val childCount = parent.childCount
        if (childCount == 0) {
            return
        }

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            if (!isFirstGroupItem(position, mOnSectionCallback!!)) {
                continue
            }

            val top = view.top - mSectionHeightPx
            val bottom = view.top
            drawBgPaint(canvas, left, top, right, bottom)
            drawTextPaint(canvas, getItem(position, mOnSectionCallback!!), left, top, right, bottom)
        }
    }

    /** 该位置[position]的数据是否是分组的第一个数据，数据回调[callback] */
    protected fun isFirstGroupItem(position: Int, callback: OnSectionCallback<T>): Boolean {
        if (position == 0) {
            return true
        }
        val current = getItem(position, callback)
        val previous = getItem(position - 1, callback)
        if (current.isEmpty() || previous.isEmpty()) {
            return false
        }
        return !current.substring(0, 1).equals(previous.substring(0, 1))
    }

    /** 获取对应位置[position]的数据，数据回调[callback] */
    protected fun getItem(position: Int, callback: OnSectionCallback<T>): String {
        if (position < 0) {
            return ""
        }
        val t = callback.getSourceItem(position)
        if (!(t is Groupable) && !(t is String)) {
            return ""
        }
        val item = if (t is Groupable) t.getSortStr() else t as String
        if (item.isEmpty()) {
            return ""
        }
        return item
    }

    override fun drawTextPaint(canvas: Canvas, text: String, left: Int, top: Int, right: Int, bottom: Int) {
        var title = ""
        if (text.isNotEmpty()) {
            title = text.substring(0, 1)
        }
        super.drawTextPaint(canvas, title, left, top, right, bottom)
    }

    interface OnSectionCallback<T> {
        /** 根据位置[position]获取列表内容 */
        fun getSourceItem(position: Int): T
    }
}