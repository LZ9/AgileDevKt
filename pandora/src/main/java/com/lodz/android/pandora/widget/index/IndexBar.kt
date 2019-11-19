package com.lodz.android.pandora.widget.index

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.px2sp
import com.lodz.android.pandora.R

/**
 * 索引标题栏
 * Created by zhouL on 2018/11/22.
 */
class IndexBar : LinearLayout {

    /** 索引文字颜色 */
    @ColorInt
    private var mPdrTextColor = Color.BLACK
    /** 索引文字颜色 */
    private var mPdrTextColorStateList: ColorStateList? = null
    /** 索引文字大小 */
    private var mPdrTextSizeSp = 13
    /** 文字是否粗体 */
    private var isPdrTextBold = true
    /** 按下索引栏的背景色 */
    private var mPdrPressBgDrawable: Drawable? = null
    /** 索引监听器 */
    private var mPdrOnIndexListener: OnIndexListener? = null

    /** 提示控件 */
    private var mPdrHintTextView: TextView? = null

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndexBar)
        }

        mPdrTextColorStateList = typedArray?.getColorStateList(R.styleable.IndexBar_indexTextColor)
        if (typedArray != null) {
            mPdrTextSizeSp = px2sp(typedArray.getDimensionPixelSize(R.styleable.IndexBar_indexTextSize, 13))
            isPdrTextBold = typedArray.getBoolean(R.styleable.IndexBar_isTextBold, true)
        }
        mPdrPressBgDrawable = typedArray?.getDrawable(R.styleable.IndexBar_pressBackgroundColor)

        typedArray?.recycle()
    }

    /** 设置索引监听器[listener] */
    fun setOnIndexListener(listener: OnIndexListener) {
        mPdrOnIndexListener = listener
    }

    /** 设置索引文字颜色[color] */
    fun setIndexTextColorInt(@ColorInt color: Int) {
        mPdrTextColor = color
    }

    /** 设置索引文字颜色[color] */
    fun setIndexTextColorRes(@ColorRes color: Int) {
        mPdrTextColor = getColorCompat(color)
    }

    /** 设置索引文字大小[textSize]（单位sp） */
    fun setIndexTextSize(textSize: Int) {
        mPdrTextSizeSp = textSize
    }

    /** 文字是否粗体[isBold] */
    fun setTextBold(isBold: Boolean) {
        isPdrTextBold = isBold
    }

    /** 设置按下索引栏的背景色[color] */
    fun setPressBgColorInt(@ColorInt color: Int) {
        mPdrPressBgDrawable = ColorDrawable(color)
    }

    /** 设置按下索引栏的背景色[color] */
    fun setPressBgColorRes(@ColorRes color: Int) {
        mPdrPressBgDrawable = ColorDrawable(getColorCompat(color))
    }

    /** 设置提示控件[textView] */
    fun setHintTextView(textView: TextView) {
        mPdrHintTextView = textView
    }

    /** 设置索引数据列表[list] */
    fun setIndexList(list: List<String>) {
        if (list.isEmpty()) {
            return
        }
        addTextView(list)
        requestLayout()
        invalidate()
    }

    /** 添加索引列表[list] */
    private fun addTextView(list: List<String>) {
        for (str in list) {
            val textView = TextView(context)
            textView.text = str
            if (mPdrTextColorStateList != null) {
                textView.setTextColor(mPdrTextColorStateList)
            } else {
                textView.setTextColor(mPdrTextColor)
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mPdrTextSizeSp.toFloat())
            textView.typeface = if (isPdrTextBold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            textView.gravity = Gravity.CENTER
            val layoutParams = LinearLayout.LayoutParams(
                if (orientation == VERTICAL) LinearLayout.LayoutParams.MATCH_PARENT else 0,
                if (orientation == VERTICAL) 0 else LinearLayout.LayoutParams.MATCH_PARENT, 1f
            )
            addView(textView, layoutParams)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return super.onTouchEvent(event)
        }
        if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
            if (mPdrPressBgDrawable != null) {
                background = mPdrPressBgDrawable
            }
            mPdrHintTextView?.visibility = View.VISIBLE
            callbackTouch(event.x, event.y)
        }
        if (event.action == MotionEvent.ACTION_UP) {
            mPdrOnIndexListener?.onEnd()
            mPdrHintTextView?.visibility = View.GONE
            background = ColorDrawable(Color.TRANSPARENT)
        }
        return true
    }

    /** 计算触摸回调，x轴坐标[x]，y轴坐标[y] */
    private fun callbackTouch(x: Float, y: Float) {
        val count = childCount
        for (i in 0 until count) {
            val view = getChildAt(i) as? TextView ?: continue
            if (orientation == VERTICAL) {
                if (y >= view.top && y <= view.bottom) {
                    mPdrHintTextView?.text = view.text
                    mPdrOnIndexListener?.onStart(i, view.text.toString())
                    return
                }
            } else {
                if (x >= view.left && x <= view.right) {
                    mPdrHintTextView?.text = view.text
                    mPdrOnIndexListener?.onStart(i, view.text.toString())
                    return
                }
            }
        }
    }

    interface OnIndexListener {
        /** 开始，索引位置[position]，索引文字[indexText] */
        fun onStart(position: Int, indexText: String)

        /** 结束 */
        fun onEnd()
    }
}