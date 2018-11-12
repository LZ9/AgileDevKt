package com.lodz.android.componentkt.widget.base

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.*
import com.lodz.android.componentkt.R
import com.lodz.android.componentkt.base.application.BaseApplication
import com.lodz.android.componentkt.base.application.config.BaseLayoutConfig
import com.lodz.android.componentkt.base.application.config.NoDataLayoutConfig
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.px2sp

/**
 * 无数据控件
 * Created by zhouL on 2018/6/27.
 */
class NoDataLayout : LinearLayout {

    /** 加载页配置 */
    private var mConfig = NoDataLayoutConfig()

    /** 根布局  */
    private val mRootView by bindView<LinearLayout>(R.id.root_view)
    /** 无数据图片  */
    private val mNoDataImg by bindView<ImageView>(R.id.no_data_img)
    /** 无数据提示语  */
    private val mNoDataTv by bindView<TextView>(R.id.no_data_tv)

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (BaseApplication.get() != null) {
            mConfig = BaseApplication.get()!!.getBaseLayoutConfig().getNoDataLayoutConfig()
        }
        LayoutInflater.from(context).inflate(R.layout.componentkt_view_no_data, this)
        configLayout(attrs)
    }

    private fun configLayout(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.NoDataLayout)
        }

        // 布局方向
        val orientation: Int = typedArray?.getInt(R.styleable.NoDataLayout_contentOrientation, mConfig.orientation) ?: mConfig.orientation
        setLayoutOrientation(orientation)

        // 是否需要提示语
        val isNeedTips: Boolean = typedArray?.getBoolean(R.styleable.NoDataLayout_contentOrientation, mConfig.isNeedTips) ?: mConfig.isNeedTips
        needTips(isNeedTips)

        // 是否需要图片
        val isNeedImg: Boolean = typedArray?.getBoolean(R.styleable.NoDataLayout_isNeedImg, mConfig.isNeedImg) ?: mConfig.isNeedImg
        needImg(isNeedImg)

        // 无数据图片
        val src: Drawable? = typedArray?.getDrawable(R.styleable.NoDataLayout_src)
        if (src != null) {
            setImg(src)
        } else if (mConfig.drawableResId != 0) {
            setImg(mConfig.drawableResId)
        } else {
            setImg(R.drawable.componentkt_ic_no_data)
        }

        // 默认提示语
        val defaultTips = if (mConfig.tips.isEmpty()) context.getString(R.string.componentkt_no_data) else mConfig.tips
        val attrsTips: String = typedArray?.getString(R.styleable.NoDataLayout_tips) ?: defaultTips
        setTips(if (attrsTips.isEmpty()) defaultTips else attrsTips)

        // 提示语颜色
        val tipsColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.NoDataLayout_tipsColor)
        if (tipsColor != null) {
            setTipsTextColor(tipsColor)
        } else if (mConfig.textColor != 0) {
            setTipsTextColor(mConfig.textColor)
        }

        // 提示语大小
        val tipsSize: Int = typedArray?.getDimensionPixelSize(R.styleable.NoDataLayout_tipsSize, 0) ?: 0
        if (tipsSize != 0) {
            setTipsTextSize(px2sp(tipsSize))
        } else if (mConfig.textSize != 0) {
            setTipsTextSize(mConfig.textSize.toFloat())
        }

        // 设置加载页背景
        val drawableBackground: Drawable? = typedArray?.getDrawable(R.styleable.NoDataLayout_contentBackground)
        if (drawableBackground != null) {
            background = drawableBackground
        } else if (mConfig.backgroundColor != 0) {
            setBackgroundColor(context.getColorCompat(mConfig.backgroundColor))
        } else {
            setBackgroundColor(context.getColorCompat(android.R.color.white))
        }

        if (typedArray != null) {
            typedArray.recycle()
        }
    }

    /** 是否需要[isNeed]提示图片 */
    fun needImg(isNeed: Boolean) {
        mNoDataImg.visibility = if(isNeed) View.VISIBLE else View.GONE
    }

    /** 是否需要[isNeed]提示文字 */
    fun needTips(isNeed: Boolean) {
        mNoDataTv.visibility = if(isNeed) View.VISIBLE else View.GONE
    }

    /** 设置无数据图片资源[drawableResId] */
    fun setImg(@DrawableRes drawableResId: Int) {
        mNoDataImg.setImageResource(drawableResId)
    }

    /** 设置无数据图片[drawable] */
    fun setImg(drawable: Drawable) {
        mNoDataImg.setImageDrawable(drawable)
    }

    /** 设置提示文字[str] */
    fun setTips(str: String) {
        mNoDataTv.text = str
    }

    /** 设置提示文字资源[strResId] */
    fun setTips(@StringRes strResId: Int) {
        mNoDataTv.text = context.getString(strResId)
    }


    /** 设置文字颜色资源[colorRes] */
    fun setTipsTextColor(@ColorRes colorRes: Int) {
        mNoDataTv.setTextColor(context.getColorCompat(colorRes))
    }

    /** 设置文字颜色[color] */
    fun setTipsTextColorInt(@ColorInt color: Int) {
        mNoDataTv.setTextColor(color)
    }

    /** 设置文字颜色[colorStateList] */
    fun setTipsTextColor(colorStateList: ColorStateList) {
        mNoDataTv.setTextColor(colorStateList)
    }

    /** 设置文字大小[sp] */
    fun setTipsTextSize(sp: Float) {
        mNoDataTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置加载页面的布局方向[orientation] */
    fun setLayoutOrientation(@BaseLayoutConfig.OrientationType orientation: Int) {
        if (orientation == LinearLayout.HORIZONTAL || orientation == LinearLayout.VERTICAL) {
            mRootView.orientation = orientation
        }
    }
}