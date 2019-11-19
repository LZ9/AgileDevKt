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
        @JvmStatic
        fun create(context: Context): RoundItemDecoration = RoundItemDecoration(context)

        /** 创建底部分割线，间距[space]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传使用默认值），背景颜色[bgColor]（不传使用默认值） */
        @JvmStatic
        @JvmOverloads
        fun createBottomDivider(context: Context, @IntRange(from = 1) space: Int, @IntRange(from = 0) lrPaddingDp: Int,
                                @ColorRes color: Int = 0, @ColorRes bgColor: Int = 0): RoundItemDecoration =
                RoundItemDecoration(context).setBottomDividerRes(space, lrPaddingDp, color, bgColor)
    }

    /** 默认分割线颜色 */
    @ColorInt
    private val DEFAULT_DIVIDER_COLOR = Color.GRAY
    /** 默认分割线背景色 */
    @ColorInt
    private val DEFAULT_DIVIDER_BG_COLOR = Color.WHITE

    /** 顶部间距 */
    private var mPdrTopPx = 0
    /** 顶部左右间隔 */
    private var mPdrTopLrPadding = 0
    /** 顶部画笔 */
    private var mPdrTopPaint: Paint? = null
    /** 顶部背景画笔 */
    private var mPdrTopBgPaint: Paint? = null

    /** 底部间距 */
    private var mPdrBottomPx = 0
    /** 底部左右间隔 */
    private var mPdrBottomLrPadding = 0
    /** 底部画笔 */
    private var mPdrBottomPaint: Paint? = null
    /** 底部背景画笔 */
    private var mPdrBottomBgPaint: Paint? = null

    /** 左侧间距 */
    private var mPdrLeftPx = 0
    /** 左侧上下间隔 */
    private var mPdrLeftTbPadding = 0
    /** 左侧画笔 */
    private var mPdrLeftPaint: Paint? = null
    /** 左侧背景画笔 */
    private var mPdrLeftBgPaint: Paint? = null

    /** 右侧间距 */
    private var mPdrRightPx = 0
    /** 右侧上下间隔 */
    private var mPdrRightTbPadding = 0
    /** 右侧画笔 */
    private var mPdrRightPaint: Paint? = null
    /** 右侧背景画笔 */
    private var mPdrRightBgPaint: Paint? = null

    /** 设置顶部分割线，间距[spaceDp]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传使用默认值），背景颜色[bgColor]（不传使用默认值） */
    @JvmOverloads
    fun setTopDividerRes(@IntRange(from = 1) spaceDp: Int = 1, @IntRange(from = 0) lrPaddingDp: Int = 0,
                         @ColorRes color: Int = 0, @ColorRes bgColor: Int = 0): RoundItemDecoration =
            setTopDividerInt(spaceDp, lrPaddingDp,
                    if (color != 0) getContext().getColorCompat(color) else DEFAULT_DIVIDER_COLOR,
                    if (bgColor != 0) getContext().getColorCompat(bgColor) else DEFAULT_DIVIDER_BG_COLOR)

    /** 设置顶部分割线，间距[spaceDp]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传默认灰色），背景颜色[bgColor]（不传默认白色） */
    @JvmOverloads
    fun setTopDividerInt(@IntRange(from = 1) spaceDp: Int = 1, @IntRange(from = 0) lrPaddingDp: Int = 0,
                         @ColorInt color: Int = DEFAULT_DIVIDER_COLOR, @ColorInt bgColor: Int = DEFAULT_DIVIDER_BG_COLOR): RoundItemDecoration {
        mPdrTopPx = getContext().dp2px(spaceDp)
        mPdrTopLrPadding = getContext().dp2px(lrPaddingDp)

        if (mPdrTopPaint == null) {
            mPdrTopPaint = Paint()
        }
        mPdrTopPaint?.color = color

        if (mPdrTopBgPaint == null) {
            mPdrTopBgPaint = Paint()
        }
        mPdrTopBgPaint?.color = bgColor
        return this
    }

    /** 设置底部分割线，间距[spaceDp]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传使用默认值），背景颜色[bgColor]（不传使用默认值） */
    @JvmOverloads
    fun setBottomDividerRes(@IntRange(from = 1) spaceDp: Int = 1, @IntRange(from = 0) lrPaddingDp: Int = 0,
                            @ColorRes color: Int = 0, @ColorRes bgColor: Int = 0): RoundItemDecoration =
            setBottomDividerInt(spaceDp, lrPaddingDp,
                    if (color != 0) getContext().getColorCompat(color) else DEFAULT_DIVIDER_COLOR,
                    if (bgColor != 0) getContext().getColorCompat(bgColor) else DEFAULT_DIVIDER_BG_COLOR)

    /** 设置底部分割线，间距[spaceDp]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传默认灰色），背景颜色[bgColor]（不传默认白色） */
    @JvmOverloads
    fun setBottomDividerInt(@IntRange(from = 1) spaceDp: Int = 1, @IntRange(from = 0) lrPaddingDp: Int = 0,
                            @ColorInt color: Int = DEFAULT_DIVIDER_COLOR, @ColorInt bgColor: Int = DEFAULT_DIVIDER_BG_COLOR): RoundItemDecoration {
        mPdrBottomPx = getContext().dp2px(spaceDp)
        mPdrBottomLrPadding = getContext().dp2px(lrPaddingDp)

        if (mPdrBottomPaint == null) {
            mPdrBottomPaint = Paint()
        }
        mPdrBottomPaint?.color = color

        if (mPdrBottomBgPaint == null) {
            mPdrBottomBgPaint = Paint()
        }
        mPdrBottomBgPaint?.color = bgColor
        return this
    }

    /** 设置左侧分割线，间距[spaceDp]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传使用默认值），背景颜色[bgColor]（不传使用默认值） */
    @JvmOverloads
    fun setLeftDividerRes(@IntRange(from = 1) spaceDp: Int = 1, @IntRange(from = 0) lrPaddingDp: Int = 0,
                          @ColorRes color: Int = 0, @ColorRes bgColor: Int = 0): RoundItemDecoration =
            setLeftDividerInt(spaceDp, lrPaddingDp,
                    if (color != 0) getContext().getColorCompat(color) else DEFAULT_DIVIDER_COLOR,
                    if (bgColor != 0) getContext().getColorCompat(bgColor) else DEFAULT_DIVIDER_BG_COLOR)

    /** 设置左侧分割线，间距[spaceDp]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传默认灰色），背景颜色[bgColor]（不传默认白色） */
    @JvmOverloads
    fun setLeftDividerInt(@IntRange(from = 1) spaceDp: Int = 1, @IntRange(from = 0) lrPaddingDp: Int = 0,
                          @ColorInt color: Int = DEFAULT_DIVIDER_COLOR, @ColorInt bgColor: Int = DEFAULT_DIVIDER_BG_COLOR): RoundItemDecoration {
        mPdrLeftPx = getContext().dp2px(spaceDp)
        mPdrLeftTbPadding = getContext().dp2px(lrPaddingDp)

        if (mPdrLeftPaint == null) {
            mPdrLeftPaint = Paint()
        }
        mPdrLeftPaint?.color = color

        if (mPdrLeftBgPaint == null) {
            mPdrLeftBgPaint = Paint()
        }
        mPdrLeftBgPaint?.color = bgColor
        return this
    }

    /** 设置右侧分割线，间距[spaceDp]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传使用默认值），背景颜色[bgColor]（不传使用默认值） */
    @JvmOverloads
    fun setRightDividerRes(@IntRange(from = 1) spaceDp: Int = 1, @IntRange(from = 0) lrPaddingDp: Int = 0,
                           @ColorRes color: Int = 0, @ColorRes bgColor: Int = 0): RoundItemDecoration =
            setRightDividerInt(spaceDp, lrPaddingDp,
                    if (color != 0) getContext().getColorCompat(color) else DEFAULT_DIVIDER_COLOR,
                    if (bgColor != 0) getContext().getColorCompat(bgColor) else DEFAULT_DIVIDER_BG_COLOR)

    /** 设置右侧分割线，间距[spaceDp]（单位dp），左右间隔[lrPaddingDp]，分割线颜色[color]（不传默认灰色），背景颜色[bgColor]（不传默认白色） */
    @JvmOverloads
    fun setRightDividerInt(@IntRange(from = 1) spaceDp: Int = 1, @IntRange(from = 0) lrPaddingDp: Int = 0,
                           @ColorInt color: Int = DEFAULT_DIVIDER_COLOR, @ColorInt bgColor: Int = DEFAULT_DIVIDER_BG_COLOR): RoundItemDecoration {
        mPdrRightPx = getContext().dp2px(spaceDp)
        mPdrRightTbPadding = getContext().dp2px(lrPaddingDp)

        if (mPdrRightPaint == null) {
            mPdrRightPaint = Paint()
        }
        mPdrRightPaint?.color = color

        if (mPdrRightBgPaint == null) {
            mPdrRightBgPaint = Paint()
        }
        mPdrRightBgPaint?.color = bgColor
        return this
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (mPdrTopPx > 0) {
            outRect.top = mPdrTopPx
        }
        if (mPdrBottomPx > 0) {
            outRect.bottom = mPdrBottomPx
        }
        if (mPdrLeftPx > 0) {
            outRect.left = mPdrLeftPx
        }
        if (mPdrRightPx > 0) {
            outRect.right = mPdrRightPx
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mPdrTopPx <= 0 && mPdrBottomPx <= 0 && mPdrLeftPx <= 0 && mPdrRightPx <= 0) {
            return
        }
        val childCount = parent.childCount
        if (childCount == 0) {
            return
        }
        if (mPdrTopPx > 0) {
            drawTopDivider(canvas, parent, childCount, mPdrTopPx, mPdrTopLrPadding, mPdrTopPaint, mPdrTopBgPaint)
        }
        if (mPdrBottomPx > 0) {
            drawBottomDivider(canvas, parent, childCount, mPdrBottomPx, mPdrBottomLrPadding, mPdrBottomPaint, mPdrBottomBgPaint)
        }
        if (mPdrLeftPx > 0) {
            drawLeftDivider(canvas, parent, childCount, mPdrLeftPx, mPdrLeftTbPadding, mPdrLeftPaint, mPdrLeftBgPaint)
        }
        if (mPdrRightPx > 0) {
            drawRightDivider(canvas, parent, childCount, mPdrRightPx, mPdrRightTbPadding, mPdrRightPaint, mPdrRightBgPaint)
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