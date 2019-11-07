package com.lodz.android.pandora.widget.rv.decoration

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView

/**
 * 粘黏标签装饰器（粘黏标签的文字由item里字符串的第一位截取）
 * 泛型T可以为String或者实现了Groupable的任意类
 * Created by zhouL on 2018/11/22.
 */
class StickyItemDecoration<T> private constructor(context: Context) : SectionItemDecoration<T>(context) {

    companion object {
        /** 创建 */
        @JvmStatic
        fun <T> create(context: Context): StickyItemDecoration<T> = StickyItemDecoration(context)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
        if (!isVerLinearLayout(parent)) {
            return
        }
        if (mPdrOnSectionCallback == null) {
            return
        }
        val childCount = parent.childCount
        if (childCount == 0) {
            return
        }

        val itemCount = state.itemCount

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            if (getItem(position, mPdrOnSectionCallback!!).isEmpty()) {
                continue
            }

            val top = 0
            val bottom = mPdrSectionHeightPx

            val sectionTop = Math.max(top, view.top - mPdrSectionHeightPx)// 顶部section上坐标，取top和viewtop最大值
            val sectionBottom = Math.max(bottom, view.top)// 顶部section下坐标，取bottom和viewbottom的最大值

            if (isFirstGroupItem(position, mPdrOnSectionCallback!!)) {// 分组的第一个数据，绘制分组样式
                drawBgPaint(canvas, left, sectionTop, right, sectionBottom)
                drawTextPaint(canvas, getItem(position, mPdrOnSectionCallback!!), left, sectionTop, right, sectionBottom)
                continue
            }

            if (isLastGroupItem(position, itemCount, mPdrOnSectionCallback!!) && view.bottom <= mPdrSectionHeightPx) {// 分组的最后一个数据并且已经到达顶部，绘制过度动画样式
                drawBgPaint(canvas, left, top, right, view.bottom)
                drawTextPaint(canvas, getItem(position, mPdrOnSectionCallback!!), left, top, right, view.bottom)
                continue
            }

            if (i == 0) {// 第一个view属于某个分组内的中间数据，则在顶部绘制固定section
                drawBgPaint(canvas, left, sectionTop, right, sectionBottom)
                drawTextPaint(canvas, getItem(position, mPdrOnSectionCallback!!), left, sectionTop, right, sectionBottom)
            }
        }
    }

    /** 是否是分组的最后一个数据，位置[position]，列表长度[itemCount] */
    private fun isLastGroupItem(position: Int, itemCount: Int, callback: OnSectionCallback<T>): Boolean {
        if (position + 1 >= itemCount) {
            return true
        }
        val current = getItem(position, callback)
        val next = getItem(position + 1, callback)
        if (current.isEmpty() || next.isEmpty()) {
            return false
        }
        return current.substring(0, 1) != next.substring(0, 1)
    }
}