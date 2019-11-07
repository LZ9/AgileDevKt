package com.lodz.android.pandora.widget.custom

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout
import com.google.android.material.tabs.TabLayout
import com.lodz.android.corekt.anko.px2dpRF
import com.lodz.android.corekt.utils.ReflectUtils
import com.lodz.android.pandora.R

/**
 * 可设置底线宽度的TabLayout
 * Created by zhouL on 2018/12/3.
 */
open class MmsTabLayout : TabLayout {

    /** 两侧间距 */
    private var mPdrTabMarginPx = 0

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
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.MmsTabLayout)
        }
        mPdrTabMarginPx = typedArray?.getDimensionPixelSize(R.styleable.MmsTabLayout_tabMargin, 0) ?: 0
        typedArray?.recycle()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed) {
            if (mPdrTabMarginPx != 0) {
                setTabIndicatorMargin(px2dpRF(mPdrTabMarginPx))
            }
        }
    }

    /** 设置底线两侧间距[marginDp] */
    fun setTabIndicatorMargin(marginDp: Float) {
        val layout = ReflectUtils.getFieldValue<TabLayout>(this, "slidingTabIndicator") as? LinearLayout ?: return
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