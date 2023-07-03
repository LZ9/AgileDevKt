package com.lodz.android.pandora.widget.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.view.children
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.pandora.R

/**
 * 将布局里的子控件透明显示
 * @author zhouL
 * @date 2020/11/5
 */
open class HoleLayout : FrameLayout {

    companion object{
        /** 不处理 */
        const val NONE = -1
        /** 首个子控件镂空 */
        const val FIRST_CHILD = 0
        /** 圆形镂空 */
        const val CIRCLE = 1
        /** 矩形镂空 */
        const val RECTANGLE = 2
    }

    /** 子控件 */
    private var mChildrenView: View? = null

    /** 遮罩层 */
    private val mMaskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    /** 镂空层 */
    private val mHolePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)

    /** 镂空类型 */
    private var mHoleType: Int = NONE

    /** 镂空圆心X轴坐标 */
    private var mCircleX = 0
    /** 镂空圆心Y轴坐标 */
    private var mCircleY = 0
    /** 镂空圆心半径长度 */
    private var mCircleRadius = 0

    /** 镂空矩形起始X轴坐标 */
    private var mRectangleX = 0
    /** 镂空矩形起始Y轴坐标 */
    private var mRectangleY = 0
    /** 镂空矩形长 */
    private var mRectangleLong = 0
    /** 镂空矩形宽 */
    private var mRectangleWidth = 0

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        configLayout(attrs)
        mHolePaint.color = Color.WHITE
        setBackgroundColor(Color.TRANSPARENT)
    }

    private fun configLayout(attrs: AttributeSet?){
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.HoleLayout)
        }

        val defMaskColor = getColorCompat(R.color.design_bottom_navigation_shadow_color)
        val maskColor = typedArray?.getColor(R.styleable.HoleLayout_maskColor, defMaskColor) ?: defMaskColor
        setMaskColor(maskColor)

        val holeType: Int = typedArray?.getInt(R.styleable.HoleLayout_holeType, NONE) ?: NONE
        setHoleType(holeType)

        val circleX = typedArray?.getDimensionPixelSize(R.styleable.HoleLayout_circleX, 0) ?: 0
        val circleY = typedArray?.getDimensionPixelSize(R.styleable.HoleLayout_circleY, 0) ?: 0
        val circleRadius = typedArray?.getDimensionPixelSize(R.styleable.HoleLayout_circleRadius, 0) ?: 0
        setCirclePosition(circleX, circleY, circleRadius)

        val rectangleX = typedArray?.getDimensionPixelSize(R.styleable.HoleLayout_rectangleX, 0) ?: 0
        val rectangleY = typedArray?.getDimensionPixelSize(R.styleable.HoleLayout_rectangleY, 0) ?: 0
        val rectangleLong = typedArray?.getDimensionPixelSize(R.styleable.HoleLayout_rectangleLong, 0) ?: 0
        val rectangleWidth = typedArray?.getDimensionPixelSize(R.styleable.HoleLayout_rectangleWidth, 0) ?: 0
        setRectanglePosition(rectangleX, rectangleY, rectangleLong, rectangleWidth)

        typedArray?.recycle()
    }

    /** 设置遮罩层颜色[color] */
    fun setHoleType(holeType: Int) {
        mHoleType = holeType
    }

    fun getHoleType(): Int = mHoleType

    /** 设置遮罩层颜色[color] */
    fun setMaskColor(@ColorInt color: Int) {
        mMaskPaint.color = color
    }

    /** 设置遮罩层颜色[color] */
    fun setMaskColorRes(@ColorRes color: Int) {
        setMaskColor(getColorCompat(color))
    }

    /** 获取遮罩层颜色 */
    @ColorInt
    fun getMaskColor(): Int = mMaskPaint.color

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (children.count() > 0) {
            mChildrenView = children.elementAt(0)
        }
    }

    /** 设置镂空的圆心X轴坐标[circleX]单位px,镂空的圆心Y轴坐标[circleY]单位px,镂空的圆半径[circleRadius]单位px */
    fun setCirclePosition(circleX: Int, circleY: Int, circleRadius: Int) {
        mCircleX = circleX
        mCircleY = circleY
        mCircleRadius = circleRadius
    }

    /** 设置镂空矩形起始X轴坐标[rectangleX]单位px,镂空矩形起始Y轴坐标[rectangleY]单位px, 镂空矩形长[rectangleLong]单位px,镂空矩形宽[rectangleWidth]单位px */
    fun setRectanglePosition(rectangleX: Int, rectangleY: Int, rectangleLong: Int, rectangleWidth: Int) {
        mRectangleX = rectangleX
        mRectangleY = rectangleY
        mRectangleLong = rectangleLong
        mRectangleWidth = rectangleWidth
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mHoleType == NONE){
            return
        }

        val width = measuredWidth
        val height = measuredHeight


        val dstCircle = when (mHoleType) {
            CIRCLE -> createDstCircleBitmap(width, height)
            RECTANGLE -> createDstRectangleBitmap(width, height)
            FIRST_CHILD -> createDstChildBitmap(width, height)
            else -> null
        } ?: return

        val srcRect = createMaskSrcBitmap(width, height)
        mPaint.isFilterBitmap = false
        canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        canvas.drawBitmap(dstCircle, 0f, 0f, mPaint)
        mPaint.xfermode = mXfermode
        canvas.drawBitmap(srcRect, 0f, 0f, mPaint)
        mPaint.xfermode = null
        canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
    }

    /** 创建遮罩层形状 */
    private fun createMaskSrcBitmap(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvcs = Canvas(bitmap)
        canvcs.drawRect(RectF(0f, 0f, width.toFloat(), height.toFloat()), mMaskPaint)
        return bitmap
    }

    /** 创建镂空层圆形形状 */
    private fun createDstCircleBitmap(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvcs = Canvas(bitmap)
        canvcs.drawCircle(mCircleX.toFloat(), mCircleY.toFloat(), mCircleRadius.toFloat(), mHolePaint)
        return bitmap
    }

    /** 创建镂空层矩形形状 */
    private fun createDstRectangleBitmap(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvcs = Canvas(bitmap)
        canvcs.drawRect(
            mRectangleX.toFloat(),
            mRectangleY.toFloat(),
            (mRectangleX + mRectangleLong).toFloat(),
            (mRectangleY + mRectangleWidth).toFloat(),
            mHolePaint
        )
        return bitmap
    }

    /** 创建镂空层首个子控件形状 */
    private fun createDstChildBitmap(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvcs = Canvas(bitmap)
        canvcs.drawRect(
            mChildrenView?.left?.toFloat() ?: 0f,
            mChildrenView?.top?.toFloat() ?: 0f,
            mChildrenView?.right?.toFloat() ?: 0f,
            mChildrenView?.bottom?.toFloat() ?: 0f,
            mHolePaint
        )
        return bitmap
    }
}