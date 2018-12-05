package com.lodz.android.componentkt.widget.rv.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getColorCompat

/**
 * 外围分割线装饰器
 * Created by zhouL on 2018/11/22.
 */
class RoundItemDecoration private constructor(context: Context) : BaseItemDecoration(context) {
    companion object {
        /** 创建 */
        fun create(context: Context): RoundItemDecoration = RoundItemDecoration(context)

        /**
         * 创建底部分割线，间距[space]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传使用默认值），背景颜色[bgColor]（不传使用默认值）
         * @param dp
         * @param color （使用默认值传0）
         * @param bgColor （使用默认值传0）
         * @param lrPaddingDp
         */
        fun createBottomDivider(context: Context, @IntRange(from = 1) space: Int, @IntRange(from = 0) lrPaddingDp: Int,
                                @ColorRes color: Int = 0, @ColorRes bgColor: Int = 0): RoundItemDecoration =
                RoundItemDecoration(context).setBottomDividerRes(space, lrPaddingDp, color, bgColor)
    }

    /** 默认分割线颜色  */
    @ColorInt
    private val DEFAULT_DIVIDER_COLOR = Color.GRAY
    /** 默认分割线背景色  */
    @ColorInt
    private val DEFAULT_DIVIDER_BG_COLOR = Color.WHITE

    /** 顶部间距  */
    private var mTopPx = 0
    /** 顶部左右间隔  */
    private var mTopLrPadding = 0
    /** 顶部画笔  */
    private var mTopPaint: Paint? = null
    /** 顶部背景画笔  */
    private var mTopBgPaint: Paint? = null

    /** 底部间距  */
    private var mBottomPx = 0
    /** 底部左右间隔  */
    private var mBottomLrPadding = 0
    /** 底部画笔  */
    private var mBottomPaint: Paint? = null
    /** 底部背景画笔  */
    private var mBottomBgPaint: Paint? = null

    /** 左侧间距  */
    private var mLeftPx = 0
    /** 左侧上下间隔  */
    private var mLeftTbPadding = 0
    /** 左侧画笔  */
    private var mLeftPaint: Paint? = null
    /** 左侧背景画笔  */
    private var mLeftBgPaint: Paint? = null

    /** 右侧间距  */
    private var mRightPx = 0
    /** 右侧上下间隔  */
    private var mRightTbPadding = 0
    /** 右侧画笔  */
    private var mRightPaint: Paint? = null
    /** 右侧背景画笔  */
    private var mRightBgPaint: Paint? = null

    /** 设置顶部分割线，间距[spaceDp]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传使用默认值），背景颜色[bgColor]（不传使用默认值） */
    fun setTopDividerRes(@IntRange(from = 1) spaceDp: Int = 1, @IntRange(from = 0) lrPaddingDp: Int = 0,
                         @ColorRes color: Int = 0, @ColorRes bgColor: Int = 0): RoundItemDecoration =
            setTopDividerInt(spaceDp, lrPaddingDp,
                    if (color != 0) getContext().getColorCompat(color) else DEFAULT_DIVIDER_COLOR,
                    if (bgColor != 0) getContext().getColorCompat(bgColor) else DEFAULT_DIVIDER_BG_COLOR)

    /** 设置顶部分割线，间距[spaceDp]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传默认灰色），背景颜色[bgColor]（不传默认白色） */
    fun setTopDividerInt(@IntRange(from = 1) spaceDp: Int = 1, @IntRange(from = 0) lrPaddingDp: Int = 0,
                         @ColorInt color: Int = DEFAULT_DIVIDER_COLOR, @ColorInt bgColor: Int = DEFAULT_DIVIDER_BG_COLOR): RoundItemDecoration {
        mTopPx = getContext().dp2px(spaceDp).toInt()
        mTopLrPadding = getContext().dp2px(lrPaddingDp).toInt()

        if (mTopPaint == null) {
            mTopPaint = Paint()
        }
        mTopPaint!!.color = color

        if (mTopBgPaint == null) {
            mTopBgPaint = Paint()
        }
        mTopBgPaint!!.color = bgColor
        return this
    }

    /** 设置底部分割线，间距[spaceDp]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传使用默认值），背景颜色[bgColor]（不传使用默认值） */
    fun setBottomDividerRes(@IntRange(from = 1) spaceDp: Int = 1, @IntRange(from = 0) lrPaddingDp: Int = 0,
                            @ColorRes color: Int = 0, @ColorRes bgColor: Int = 0): RoundItemDecoration =
            setBottomDividerInt(spaceDp, lrPaddingDp,
                    if (color != 0) getContext().getColorCompat(color) else DEFAULT_DIVIDER_COLOR,
                    if (bgColor != 0) getContext().getColorCompat(bgColor) else DEFAULT_DIVIDER_BG_COLOR)

    /** 设置底部分割线，间距[spaceDp]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传默认灰色），背景颜色[bgColor]（不传默认白色） */
    fun setBottomDividerInt(@IntRange(from = 1) spaceDp: Int = 1, @IntRange(from = 0) lrPaddingDp: Int = 0,
                            @ColorInt color: Int = DEFAULT_DIVIDER_COLOR, @ColorInt bgColor: Int = DEFAULT_DIVIDER_BG_COLOR): RoundItemDecoration {
        mBottomPx = getContext().dp2px(spaceDp).toInt()
        mBottomLrPadding = getContext().dp2px(lrPaddingDp).toInt()

        if (mBottomPaint == null) {
            mBottomPaint = Paint()
        }
        mBottomPaint!!.color = color

        if (mBottomBgPaint == null) {
            mBottomBgPaint = Paint()
        }
        mBottomBgPaint!!.color = bgColor
        return this
    }

    /** 设置左侧分割线，间距[spaceDp]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传使用默认值），背景颜色[bgColor]（不传使用默认值） */
    fun setLeftDividerRes(@IntRange(from = 1) spaceDp: Int = 1, @IntRange(from = 0) lrPaddingDp: Int = 0,
                          @ColorRes color: Int = 0, @ColorRes bgColor: Int = 0): RoundItemDecoration =
            setLeftDividerInt(spaceDp, lrPaddingDp,
                    if (color != 0) getContext().getColorCompat(color) else DEFAULT_DIVIDER_COLOR,
                    if (bgColor != 0) getContext().getColorCompat(bgColor) else DEFAULT_DIVIDER_BG_COLOR)

    /** 设置左侧分割线，间距[spaceDp]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传默认灰色），背景颜色[bgColor]（不传默认白色） */
    fun setLeftDividerInt(@IntRange(from = 1) spaceDp: Int = 1, @IntRange(from = 0) lrPaddingDp: Int = 0,
                          @ColorInt color: Int = DEFAULT_DIVIDER_COLOR, @ColorInt bgColor: Int = DEFAULT_DIVIDER_BG_COLOR): RoundItemDecoration {
        mLeftPx = getContext().dp2px(spaceDp).toInt()
        mLeftTbPadding = getContext().dp2px(lrPaddingDp).toInt()

        if (mLeftPaint == null) {
            mLeftPaint = Paint()
        }
        mLeftPaint!!.color = color

        if (mLeftBgPaint == null) {
            mLeftBgPaint = Paint()
        }
        mLeftBgPaint!!.color = bgColor
        return this
    }

    /** 设置右侧分割线，间距[spaceDp]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传使用默认值），背景颜色[bgColor]（不传使用默认值） */
    fun setRightDividerRes(@IntRange(from = 1) spaceDp: Int = 1, @IntRange(from = 0) lrPaddingDp: Int = 0,
                           @ColorRes color: Int = 0, @ColorRes bgColor: Int = 0): RoundItemDecoration =
            setRightDividerInt(spaceDp, lrPaddingDp,
                    if (color != 0) getContext().getColorCompat(color) else DEFAULT_DIVIDER_COLOR,
                    if (bgColor != 0) getContext().getColorCompat(bgColor) else DEFAULT_DIVIDER_BG_COLOR)

    /** 设置右侧分割线，间距[spaceDp]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传默认灰色），背景颜色[bgColor]（不传默认白色） */
    fun setRightDividerInt(@IntRange(from = 1) spaceDp: Int = 1, @IntRange(from = 0) lrPaddingDp: Int = 0,
                           @ColorInt color: Int = DEFAULT_DIVIDER_COLOR, @ColorInt bgColor: Int = DEFAULT_DIVIDER_BG_COLOR): RoundItemDecoration {
        mRightPx = getContext().dp2px(spaceDp).toInt()
        mRightTbPadding = getContext().dp2px(lrPaddingDp).toInt()

        if (mRightPaint == null) {
            mRightPaint = Paint()
        }
        mRightPaint!!.color = color

        if (mRightBgPaint == null) {
            mRightBgPaint = Paint()
        }
        mRightBgPaint!!.color = bgColor
        return this
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (mTopPx > 0) {
            outRect.top = mTopPx
        }
        if (mBottomPx > 0) {
            outRect.bottom = mBottomPx
        }
        if (mLeftPx > 0) {
            outRect.left = mLeftPx
        }
        if (mRightPx > 0) {
            outRect.right = mRightPx
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mTopPx <= 0 && mBottomPx <= 0 && mLeftPx <= 0 && mRightPx <= 0) {
            return
        }
        val childCount = parent.childCount
        if (childCount == 0) {
            return
        }
        if (mTopPx > 0) {
            drawTopDivider(canvas, parent, childCount, mTopPx, mTopLrPadding, mTopPaint, mTopBgPaint)
        }
        if (mBottomPx > 0) {
            drawBottomDivider(canvas, parent, childCount, mBottomPx, mBottomLrPadding, mBottomPaint, mBottomBgPaint)
        }
        if (mLeftPx > 0) {
            drawLeftDivider(canvas, parent, childCount, mLeftPx, mLeftTbPadding, mLeftPaint, mLeftBgPaint)
        }
        if (mRightPx > 0) {
            drawRightDivider(canvas, parent, childCount, mRightPx, mRightTbPadding, mRightPaint, mRightBgPaint)
        }
    }

    /** 绘制顶部分割线，画布[canvas]，父控件[parent]，子项数量[childCount]，间距[px]，间隔[padding]，画笔[paint]，背景画笔[bgPaint] */
    private fun drawTopDivider(canvas: Canvas, parent: RecyclerView, childCount: Int, px: Int, padding: Int, paint: Paint?, bgPaint: Paint?) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val top = view.top - px
            val bottom = view.top
            if (bgPaint != null) {
                canvas.drawRect(checkValue(left).toFloat(), checkValue(top).toFloat(), checkValue(right).toFloat(), checkValue(bottom).toFloat(), bgPaint)
            }
            if (paint != null) {
                canvas.drawRect(checkValue(left + padding).toFloat(), checkValue(top).toFloat(), checkValue(right - padding).toFloat(), checkValue(bottom).toFloat(), paint)
            }
        }
    }

    /** 绘制底部分割线，画布[canvas]，父控件[parent]，子项数量[childCount]，间距[px]，间隔[padding]，画笔[paint]，背景画笔[bgPaint] */
    private fun drawBottomDivider(canvas: Canvas, parent: RecyclerView, childCount: Int, px: Int, padding: Int, paint: Paint?, bgPaint: Paint?) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val top = view.bottom
            val bottom = view.bottom + px
            if (bgPaint != null) {
                canvas.drawRect(checkValue(left).toFloat(), checkValue(top).toFloat(), checkValue(right).toFloat(), checkValue(bottom).toFloat(), bgPaint)
            }
            if (paint != null) {
                canvas.drawRect(checkValue(left + padding).toFloat(), checkValue(top).toFloat(), checkValue(right - padding).toFloat(), checkValue(bottom).toFloat(), paint)
            }
        }
    }

    /** 绘制左侧分割线，画布[canvas]，父控件[parent]，子项数量[childCount]，间距[px]，间隔[padding]，画笔[paint]，背景画笔[bgPaint] */
    private fun drawLeftDivider(canvas: Canvas, parent: RecyclerView, childCount: Int, px: Int, padding: Int, paint: Paint?, bgPaint: Paint?) {
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val top = view.top
            val bottom = view.bottom
            val left = view.left - px
            val right = view.left
            if (bgPaint != null) {
                canvas.drawRect(checkValue(left).toFloat(), checkValue(top).toFloat(), checkValue(right).toFloat(), checkValue(bottom).toFloat(), bgPaint)
            }
            if (paint != null) {
                canvas.drawRect(checkValue(left).toFloat(), checkValue(top + padding).toFloat(), checkValue(right).toFloat(), checkValue(bottom - padding).toFloat(), paint)
            }
        }
    }

    /** 绘制右侧分割线，画布[canvas]，父控件[parent]，子项数量[childCount]，间距[px]，间隔[padding]，画笔[paint]，背景画笔[bgPaint] */
    private fun drawRightDivider(canvas: Canvas, parent: RecyclerView, childCount: Int, px: Int, padding: Int, paint: Paint?, bgPaint: Paint?) {
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val top = view.top
            val bottom = view.bottom
            val left = view.right
            val right = view.right + px
            if (bgPaint != null) {
                canvas.drawRect(checkValue(left).toFloat(), checkValue(top).toFloat(), checkValue(right).toFloat(), checkValue(bottom).toFloat(), bgPaint)
            }
            if (paint != null) {
                canvas.drawRect(checkValue(left).toFloat(), checkValue(top + padding).toFloat(), checkValue(right).toFloat(), checkValue(bottom - padding).toFloat(), paint)
            }
        }
    }
}