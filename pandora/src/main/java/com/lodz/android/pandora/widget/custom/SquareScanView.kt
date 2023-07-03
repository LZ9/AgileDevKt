package com.lodz.android.pandora.widget.custom

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.lodz.android.corekt.anko.dp2pxRF
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.pandora.R

/**
 * 方形扫描控件
 * @author zhouL
 * @date 2020/11/5
 */
class SquareScanView  : View {

    /** 边框默认横线长度 */
    private val FRAME_LONG_DEFAULT = 80
    /** 边框默认横线宽度 */
    private val FRAME_WIDTH_DEFAULT = 18
    /** 扫描线默认宽度 */
    private val SCAN_LINE_WIDTH_DEFAULT = 10
    /** 扫描线默认刷新间隔 */
    private val SCAN_LINE_DELAY = 10L

    /** 边框颜色 */
    @ColorInt
    private var mFrameColor :Int = Color.GREEN
    /** 边框长度（像素） */
    private var mFrameLongPx = FRAME_LONG_DEFAULT.toFloat()
    /** 边框宽度（像素） */
    private var mFrameStrokeWidthPx = FRAME_WIDTH_DEFAULT.toFloat()
    /** 边框 */
    private val mFramePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** 扫描线颜色 */
    @ColorInt
    private var mScanLineColor :Int = Color.GREEN
    /** 扫描线宽度（像素） */
    private var mScanLineStrokeWidth = SCAN_LINE_WIDTH_DEFAULT.toFloat()
    /** 扫描线高度 */
    private var mScanLineHeight = 0f
    /** 扫描线 */
    private val mGradientPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    /** 扫描线梯度颜色 */
    private var mGradientColors = intArrayOf(
        Color.TRANSPARENT,
        Color.GREEN,
        Color.GREEN,
        Color.GREEN,
        Color.TRANSPARENT
    )
    /** 扫描线刷新间隔 */
    private var mScanDelayMilliseconds: Long = SCAN_LINE_DELAY


    constructor(context: Context?) : super(context){
        init(null)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init(attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes){
        init(attrs)
    }

    private fun init(attrs: AttributeSet?){
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.SquareScanView)
        }

        // 边框颜色
        val frameColor = typedArray?.getColor(R.styleable.SquareScanView_frameColor, Color.GREEN) ?: Color.GREEN
        setFrameColor(frameColor)

        // 边框长度
        val frameLong : Int = typedArray?.getDimensionPixelSize(R.styleable.SquareScanView_frameLong, FRAME_LONG_DEFAULT) ?: FRAME_LONG_DEFAULT
        setFrameLong(frameLong.toFloat())

        // 边框粗细
        val frameStrokeWidth : Int = typedArray?.getDimensionPixelSize(R.styleable.SquareScanView_frameStrokeWidth, FRAME_WIDTH_DEFAULT) ?: FRAME_WIDTH_DEFAULT
        setFrameStrokeWidth(frameStrokeWidth.toFloat())

        // 扫描线颜色
        val scanLineColor = typedArray?.getColor(R.styleable.SquareScanView_scanLineColor, Color.GREEN) ?: Color.GREEN
        setScanLineColor(scanLineColor)

        // 扫描线粗细
        val scanLineStrokeWidth : Int = typedArray?.getDimensionPixelSize(R.styleable.SquareScanView_scanLineStrokeWidth, FRAME_WIDTH_DEFAULT) ?: FRAME_WIDTH_DEFAULT
        setScanLineStrokeWidth(scanLineStrokeWidth.toFloat())

        // 扫描线粗细
        val delay : Int = typedArray?.getInteger(R.styleable.SquareScanView_scanDelayMilliseconds, SCAN_LINE_DELAY.toInt()) ?: SCAN_LINE_DELAY.toInt()
        setScanDelayMilliseconds(delay.toLong())

        typedArray?.recycle()
    }

    /** 设置边框颜色[color] */
    fun setFrameColor(@ColorInt color: Int) {
        mFrameColor = color
        mFramePaint.color = mFrameColor
    }

    /** 设置边框颜色[color] */
    fun setFrameColorRes(@ColorRes color: Int) {
        setFrameColor(getColorCompat(color))
    }

    /** 获取边框颜色 */
    @ColorInt
    fun getFrameColor(): Int = mFrameColor

    /** 设置边框横线长度[dp] */
    fun setFrameLong(dp: Int) {
        setFrameLong(dp2pxRF(dp))
    }

    /** 设置边框横线长度[px] */
    fun setFrameLong(px: Float) {
        mFrameLongPx = px
    }

    /** 获取边框横线长度（像素） */
    fun getFrameLong(): Float = mFrameLongPx

    /** 设置边框宽度[dp] */
    fun setFrameStrokeWidth(dp: Int) {
        setFrameStrokeWidth(dp2pxRF(dp))
    }

    /** 设置边框宽度[px] */
    fun setFrameStrokeWidth(px: Float) {
        mFrameStrokeWidthPx = px
        mFramePaint.strokeWidth = mFrameStrokeWidthPx
    }

    /** 获取边框宽度（像素） */
    fun getFrameStrokeWidth(): Float = mFrameStrokeWidthPx

    /** 设置扫描线颜色[color] */
    fun setScanLineColor(@ColorInt color: Int) {
        mScanLineColor = color
        mGradientPaint.color = mScanLineColor
        mGradientColors = intArrayOf(
            Color.TRANSPARENT,
            mScanLineColor,
            mScanLineColor,
            mScanLineColor,
            Color.TRANSPARENT
        )
    }

    /** 设置扫描线颜色[color] */
    fun setScanLineColorRes(@ColorRes color: Int) {
        setScanLineColor(getColorCompat(color))
    }

    /** 获取扫描线颜色 */
    @ColorInt
    fun getScanLineColor(): Int = mScanLineColor


    /** 设置扫描线宽度[dp] */
    fun setScanLineStrokeWidth(dp: Int) {
        setScanLineStrokeWidth(dp2pxRF(dp))
    }

    /** 设置扫描线宽度[px] */
    fun setScanLineStrokeWidth(px: Float) {
        mScanLineStrokeWidth = px
        mGradientPaint.strokeWidth = mScanLineStrokeWidth
    }

    /** 获取扫描线宽度（像素） */
    fun getScanLineStrokeWidth(): Float = mScanLineStrokeWidth

    /** 设置扫描线刷新间隔[delay] */
    fun setScanDelayMilliseconds(delay: Long) {
        mScanDelayMilliseconds = delay
    }

    /** 获取扫描线刷新间隔 */
    fun getScanDelayMilliseconds(): Long = mScanDelayMilliseconds

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width  = measuredWidth.toFloat()
        val height  = measuredHeight.toFloat()

        // 左上
        canvas.drawLine(0f, 0f, mFrameLongPx, 0f, mFramePaint)
        canvas.drawLine(0f, 0f, 0f, mFrameLongPx, mFramePaint)
        // 左下
        canvas.drawLine(0f, height, 0f, height - mFrameLongPx, mFramePaint)
        canvas.drawLine(0f, height, mFrameLongPx, height, mFramePaint)
        // 右上
        canvas.drawLine(width, 0f, width - mFrameLongPx, 0f, mFramePaint)
        canvas.drawLine(width, 0f, width, mFrameLongPx, mFramePaint)
        // 右下
        canvas.drawLine(width, height, width - mFrameLongPx, height, mFramePaint)
        canvas.drawLine(width, height, width, height - mFrameLongPx, mFramePaint)


        mScanLineHeight += 10
        mGradientPaint.shader = LinearGradient(0f, mScanLineHeight, width, mScanLineHeight, mGradientColors, null, Shader.TileMode.CLAMP)
        canvas.drawLine(0f, mScanLineHeight, width, mScanLineHeight, mGradientPaint)
        if (mScanLineHeight >= height - 10) {
            mScanLineHeight = 0f
        }
        postInvalidateDelayed(mScanDelayMilliseconds)
    }


}