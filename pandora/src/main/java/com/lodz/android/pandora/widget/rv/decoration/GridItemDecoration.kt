package com.lodz.android.pandora.widget.rv.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getColorCompat

/**
 * 网格分割线装饰器
 * Created by zhouL on 2018/11/22.
 */
class GridItemDecoration private constructor(context: Context) : BaseItemDecoration(context) {

    companion object {
        /** 创建 */
        @JvmStatic
        fun create(context: Context): GridItemDecoration = GridItemDecoration(context)
    }

    /** 外部间距 */
    private var mPdrPx = 0
    /** 顶部画笔 */
    private val mPdrPaint: Paint = Paint()

    init {
        mPdrPaint.color = Color.GRAY
    }

    /** 设置分割线间距[space]（单位dp） */
    fun setDividerSpace(@IntRange(from = 1) space: Int): GridItemDecoration {
        mPdrPx = getContext().dp2px(space)
        return this
    }

    /** 设置分割线颜色[color] */
    fun setDividerRes(@ColorRes color: Int): GridItemDecoration {
        mPdrPaint.color = getContext().getColorCompat(color)
        return this
    }

    /** 设置分割线颜色[color] */
    fun setDividerInt(@ColorInt color: Int): GridItemDecoration {
        mPdrPaint.color = color
        return this
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (mPdrPx <= 0) {
            return
        }

        val layoutManager = parent.layoutManager

        if (layoutManager is StaggeredGridLayoutManager) {// 瀑布流
            setStaggeredGridOffsets(outRect)
            return
        }

        if (layoutManager is GridLayoutManager) {// 网格
            setGridOffsets(outRect, view, parent, layoutManager)
            return
        }

        if (layoutManager is LinearLayoutManager) {// 线性
            setLinearOffsets(outRect, view, parent, layoutManager)
            return
        }
    }

    /** 设置StaggeredGridLayoutManager的边距 */
    private fun setStaggeredGridOffsets(outRect: Rect) {
        outRect.top = mPdrPx
        outRect.bottom = mPdrPx
        outRect.left = mPdrPx
        outRect.right = mPdrPx
    }

    /** 设置LinearLayoutManager的边距 */
    private fun setLinearOffsets(outRect: Rect, view: View, parent: RecyclerView, layoutManager: LinearLayoutManager) {
        val position = parent.getChildAdapterPosition(view)
        if (layoutManager.orientation == RecyclerView.VERTICAL) {//纵向
            outRect.top = if (position == 0) mPdrPx else 0
            outRect.bottom = mPdrPx
            outRect.left = mPdrPx
            outRect.right = mPdrPx
            return
        }

        // 横向
        outRect.top = mPdrPx
        outRect.bottom = mPdrPx
        outRect.left = if (position == 0) mPdrPx else 0
        outRect.right = mPdrPx
    }

    /** 设置GridLayoutManager的边距 */
    private fun setGridOffsets(outRect: Rect, view: View, parent: RecyclerView, layoutManager: GridLayoutManager) {
        val position = parent.getChildAdapterPosition(view)
        val spanCount = layoutManager.spanCount

        if (layoutManager.orientation == RecyclerView.VERTICAL) {//纵向
            if (position == 0) {// 第一个
                outRect.top = mPdrPx
                outRect.bottom = mPdrPx
                outRect.left = mPdrPx
                outRect.right = mPdrPx
                return
            }
            if (position < spanCount) {//第一排
                outRect.top = mPdrPx
                outRect.bottom = mPdrPx
                outRect.left = 0
                outRect.right = mPdrPx
                return
            }
            if (position % spanCount == 0) {//最左侧
                outRect.top = 0
                outRect.bottom = mPdrPx
                outRect.left = mPdrPx
                outRect.right = mPdrPx
                return
            }

            outRect.top = 0
            outRect.bottom = mPdrPx
            outRect.left = 0
            outRect.right = mPdrPx
            return
        }

        // 横向
        parent.setPadding(parent.paddingStart, parent.paddingTop, parent.paddingEnd,
                if (parent.paddingBottom >= mPdrPx) parent.paddingBottom else parent.paddingBottom + mPdrPx)
        if (position < spanCount) {//最左侧
            outRect.top = mPdrPx
            outRect.bottom = 0
            outRect.left = mPdrPx
            outRect.right = mPdrPx
            return
        }
        outRect.top = mPdrPx
        outRect.bottom = 0
        outRect.left = 0
        outRect.right = mPdrPx
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
        if (mPdrPx <= 0) {
            return
        }
        val childCount = parent.childCount
        if (childCount == 0) {
            return
        }
        drawTopDivider(canvas, parent, childCount, mPdrPx, mPdrPaint)
        drawBottomDivider(canvas, parent, childCount, mPdrPx, mPdrPaint)
        drawLeftDivider(canvas, parent, childCount, mPdrPx, mPdrPaint)
        drawRightDivider(canvas, parent, childCount, mPdrPx, mPdrPaint)
    }

    /** 绘制顶部分割线，画布[canvas]，父控件[parent]，子项数量[childCount]，间距[px]，画笔[paint] */
    private fun drawTopDivider(canvas: Canvas, parent: RecyclerView, childCount: Int, px: Int, paint: Paint) {
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val top = view.top - px
            val bottom = view.top
            val left = view.left - px
            val right = view.right + px
            canvas.drawRect(checkValue(left.toFloat()), checkValue(top.toFloat()), checkValue(right.toFloat()), checkValue(bottom.toFloat()), paint)
        }
    }

    /** 绘制底部分割线，画布[canvas]，父控件[parent]，子项数量[childCount]，间距[px]，画笔[paint] */
    private fun drawBottomDivider(canvas: Canvas, parent: RecyclerView, childCount: Int, px: Int, paint: Paint) {
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val top = view.bottom
            val bottom = view.bottom + px
            val left = view.left - px
            val right = view.right + px
            canvas.drawRect(checkValue(left.toFloat()), checkValue(top.toFloat()), checkValue(right.toFloat()), checkValue(bottom.toFloat()), paint)
        }
    }

    /** 绘制左侧分割线，画布[canvas]，父控件[parent]，子项数量[childCount]，间距[px]，画笔[paint] */
    private fun drawLeftDivider(canvas: Canvas, parent: RecyclerView, childCount: Int, px: Int, paint: Paint) {
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val top = view.top
            val bottom = view.bottom
            val left = view.left - px
            val right = view.left
            canvas.drawRect(checkValue(left.toFloat()), checkValue(top.toFloat()), checkValue(right.toFloat()), checkValue(bottom.toFloat()), paint)
        }
    }

    /** 绘制右侧分割线，画布[canvas]，父控件[parent]，子项数量[childCount]，间距[px]，画笔[paint] */
    private fun drawRightDivider(canvas: Canvas, parent: RecyclerView, childCount: Int, px: Int, paint: Paint) {
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val top = view.top
            val bottom = view.bottom
            val left = view.right
            val right = view.right + px
            canvas.drawRect(checkValue(left.toFloat()), checkValue(top.toFloat()), checkValue(right.toFloat()), checkValue(bottom.toFloat()), paint)
        }
    }

}