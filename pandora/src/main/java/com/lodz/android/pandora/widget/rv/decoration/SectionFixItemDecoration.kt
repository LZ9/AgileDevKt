package com.lodz.android.pandora.widget.rv.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 固定数据的分组标签装饰器
 * Created by zhouL on 2018/11/22.
 */
open class SectionFixItemDecoration<T> protected constructor(context: Context, sections: List<String>, sources: List<List<T>>) : BaseSectionItemDecoration(context) {

    companion object {
        /** 创建，分组标题列表[sections]，各组数据列表集[sources] */
        fun <T> create(context: Context, sections: List<String>, sources: List<List<T>>): SectionFixItemDecoration<T> {
            if (sections.size == 0 || sources.size == 0) {
                throw IllegalArgumentException("sections or sources is can not be empty")
            }
            if (sections.size != sources.size) {
                throw IllegalArgumentException("sections size and sources size must be consistent")
            }
            return SectionFixItemDecoration(context, sections, sources)
        }
    }

    /** 分组标题列表  */
    private val mSections: List<String>
    /** 各组数据列表集  */
    protected val mSources: List<List<T>>

    init {
        mSections = sections
        mSources = sources
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (!isVerLinearLayout(parent)) {
            return
        }
        val position = parent.getChildAdapterPosition(view)
        outRect.top = if (isFirstGroupItem(position)) mSectionHeightPx else 0// 设置分组高度
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
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
            if (!isFirstGroupItem(position)) {
                continue
            }

            val top = view.top - mSectionHeightPx
            val bottom = view.top
            drawBgPaint(canvas, left, top, right, bottom)
            drawTextPaint(canvas, getSectionText(position), left, top, right, bottom)
        }
    }

    /** 该位置[position]数据是否是分组的第一个数据 */
    protected fun isFirstGroupItem(position: Int): Boolean {
        var size = 0
        for (list in mSources) {
            if (position == size) {
                return true
            }
            if (position < size) {
                return false
            }
            size += list.size
        }
        return false
    }

    /** 获取位置[position]对应的分组标题文字 */
    protected fun getSectionText(position: Int): String {
        var index = 0
        var size = 0
        for (list in mSources) {
            if (position >= size && position < size + list.size) {
                return mSections.get(index)
            }
            size += list.size
            index++
        }
        return ""
    }
}