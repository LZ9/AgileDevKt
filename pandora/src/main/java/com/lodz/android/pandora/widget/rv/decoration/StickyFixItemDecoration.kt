package com.lodz.android.pandora.widget.rv.decoration

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView

/**
 * 固定数据的粘黏标签装饰器
 * Created by zhouL on 2018/11/22.
 */
class StickyFixItemDecoration<T> private constructor(context: Context, sections: List<String>, sources: List<List<T>>) : SectionFixItemDecoration<T>(context, sections, sources) {

    companion object {
        /** 创建，分组标题列表[sections]，各组数据列表集[sources] */
        @JvmStatic
        fun <T> create(context: Context, sections: List<String>, sources: List<List<T>>): StickyFixItemDecoration<T> = StickyFixItemDecoration(context, sections, sources)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
        if (!isVerLinearLayout(parent)) {
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

            val top = 0
            val bottom = mPdrSectionHeightPx

            val sectionTop = Math.max(top, view.top - mPdrSectionHeightPx)// 顶部section上坐标，取top和viewtop最大值
            val sectionBottom = Math.max(bottom, view.top)// 顶部section下坐标，取bottom和viewbottom的最大值

            if (isFirstGroupItem(position) && isLastGroupItem(position) && view.bottom <= mPdrSectionHeightPx) {//分组内只有1条数据
                drawBgPaint(canvas, left, top, right, view.bottom)
                drawTextPaint(canvas, getSectionText(position), left, top, right, view.bottom)
                continue
            }

            if (isFirstGroupItem(position)) {// 分组的第一个数据，绘制分组样式
                drawBgPaint(canvas, left, sectionTop, right, sectionBottom)
                drawTextPaint(canvas, getSectionText(position), left, sectionTop, right, sectionBottom)
                continue
            }

            if (isLastGroupItem(position) && view.bottom <= mPdrSectionHeightPx) {// 分组的最后一个数据并且已经到达顶部，绘制过度动画样式
                drawBgPaint(canvas, left, top, right, view.bottom)
                drawTextPaint(canvas, getSectionText(position), left, top, right, view.bottom)
                continue
            }

            if (i == 0) {// 第一个view属于某个分组内的中间数据，则在顶部绘制固定section
                drawBgPaint(canvas, left, sectionTop, right, sectionBottom)
                drawTextPaint(canvas, getSectionText(position), left, sectionTop, right, sectionBottom)
            }
        }
    }

    /** 该位置[position]数据是否是分组的最后一个数据 */
    private fun isLastGroupItem(position: Int): Boolean {
        var size = 0
        for (list in mPdrSources) {
            size += list.size
            if (position + 1 == size) {
                return true
            }
            if (position < size) {
                return false
            }
        }
        return false
    }
}