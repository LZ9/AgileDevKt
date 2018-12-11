package com.lodz.android.componentkt.widget.custom

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout
import com.google.android.material.tabs.TabLayout
import com.lodz.android.componentkt.R
import com.lodz.android.corekt.anko.px2dp
import com.lodz.android.corekt.utils.ReflectUtils

/**
 * 可设置底线宽度的TabLayout
 * Created by zhouL on 2018/12/3.
 */
open class MmsTabLayout : TabLayout {

    /** 两侧间距 */
    private var mTabMarginPx = 0

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MmsTabLayout)
        if (typedArray == null) {
            return
        }
        mTabMarginPx = typedArray.getDimensionPixelSize(R.styleable.MmsTabLayout_tabMargin, 0)
        typedArray.recycle()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed) {
            if (mTabMarginPx != 0) {
                setTabIndicatorMargin(px2dp(mTabMarginPx))
            }
        }
    }

    /** 设置底线两侧间距[marginDp] */
    fun setTabIndicatorMargin(marginDp: Float) {
        val cls: Class<*>? = ReflectUtils.getClassForName("com.google.android.material.tabs.TabLayout")
        if (cls == null) {
            return
        }
        var layout: LinearLayout? = null
        try {
            layout = ReflectUtils.getFieldValue(cls, this, "slidingTabIndicator") as LinearLayout
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (layout == null) {
            return
        }
        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginDp, Resources.getSystem().displayMetrics)
        for (i in 0 until layout.childCount) {
            val child = layout.getChildAt(i)
            if (child != null && child.layoutParams is LinearLayout.LayoutParams) {
                val params: LinearLayout.LayoutParams = child.layoutParams as LinearLayout.LayoutParams
                params.leftMargin = margin.toInt()
                params.rightMargin = margin.toInt()
                child.layoutParams = params
                child.invalidate()
            }
        }
    }

}