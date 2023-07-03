package com.lodz.android.pandora.widget.watermark

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.lodz.android.pandora.R
import kotlin.math.sqrt

/**
 * 水印控件
 * @author zhouL
 * @date 2023/6/7
 */
class WatermarkView : View {

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mRect = Rect()

    private var mText = ""

    private var mAttrText = ""

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        configLayout(attrs)
        setPaint()
    }

    private fun configLayout(attrs: AttributeSet?){
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.WatermarkView)
        }
        mAttrText = typedArray?.getString(R.styleable.WatermarkView_watermarkText) ?: ""
        typedArray?.recycle()
    }

    private fun setPaint() {
        mPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18f, context.resources.displayMetrics)
        mPaint.color = Color.BLACK
        mPaint.isAntiAlias = true
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.shader = null
        mPaint.style = Paint.Style.FILL
        mPaint.alpha = (0.1 * 255f).toInt()
        mPaint.isDither = true
        mPaint.isFilterBitmap = true
    }

    /** 设置文字 */
    fun setText(text: String) {
        if (text.isEmpty()) {
            return
        }
        mText = text
        mPaint.getTextBounds(mText, 0, mText.length, mRect) //获取文字长度和宽度
        invalidate()
    }

    /** 设置文字和画笔 */
    fun setText(text: String, paint: Paint) {
        mPaint = paint
        setText(text)
    }

    /** 获取默认画笔配置 */
    fun getPaint() = mPaint

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (mAttrText.isNotEmpty()){
            setText(mAttrText)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mText.isEmpty()){
            return
        }

        val inter: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25f, context.resources.displayMetrics)
        val sideLength: Int = if (width > height) {
            sqrt(2.0 * (width * width)).toInt()
        } else {
            sqrt(2.0 * (height * height)).toInt()
        }

        //先平移，再旋转才不会有空白，使整个图片充满
        if (width > height) {
            canvas.translate(width.toFloat() - sideLength.toFloat() - inter, sideLength - width + inter)
        } else {
            canvas.translate(height.toFloat() - sideLength.toFloat() - inter, sideLength - height + inter)
        }
        //将该文字图片逆时针方向倾斜45度
        canvas.rotate(-45f, width / 2f, height / 2f)

        var i = 0
        while (i <= sideLength) {
            var count = 0
            var j = 0
            while (j <= sideLength) {
                if (count % 2 == 0) {
                    canvas.drawText(mText, i.toFloat(), j.toFloat(), mPaint)
                } else {
                    //偶数行进行错开
                    canvas.drawText(mText, (i + mRect.width() / 2).toFloat(), j.toFloat(), mPaint)
                }
                j = (j.toFloat() + inter + mRect.height().toFloat()).toInt()
                count++
            }
            i = (i.toFloat() + mRect.width().toFloat() + inter).toInt()
        }
    }
}