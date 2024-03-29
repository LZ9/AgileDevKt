package com.lodz.android.pandora.widget.base

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.px2sp
import com.lodz.android.pandora.R
import com.lodz.android.pandora.base.application.BaseApplication
import com.lodz.android.pandora.base.application.config.BaseLayoutConfig
import com.lodz.android.pandora.base.application.config.NoDataLayoutConfig

/**
 * 无数据控件
 * Created by zhouL on 2018/6/27.
 */
class NoDataLayout : LinearLayout {

    /** 加载页配置 */
    private var mPdrConfig = NoDataLayoutConfig()

    /** 根布局 */
    private val mPdrRootView by bindView<LinearLayout>(R.id.pdr_root_view)
    /** 无数据图片 */
    private val mPdrNoDataImg by bindView<ImageView>(R.id.pdr_no_data_img)
    /** 无数据提示语 */
    private val mPdrNoDataTv by bindView<TextView>(R.id.pdr_no_data_tv)

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
        val app = BaseApplication.get()
        if (app != null) {
            mPdrConfig = app.getBaseLayoutConfig().getNoDataLayoutConfig()
        }
        LayoutInflater.from(context).inflate(R.layout.pandora_view_no_data, this)
        configLayout(attrs)
    }

    private fun configLayout(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.NoDataLayout)
        }

        // 布局方向
        val orientation: Int = typedArray?.getInt(R.styleable.NoDataLayout_contentOrientation, mPdrConfig.orientation)
                ?: mPdrConfig.orientation
        setLayoutOrientation(orientation)

        // 是否需要提示语
        val isNeedTips: Boolean = typedArray?.getBoolean(R.styleable.NoDataLayout_isNeedTips, mPdrConfig.isNeedTips)
                ?: mPdrConfig.isNeedTips
        needTips(isNeedTips)

        // 是否需要图片
        val isNeedImg: Boolean = typedArray?.getBoolean(R.styleable.NoDataLayout_isNeedImg, mPdrConfig.isNeedImg)
                ?: mPdrConfig.isNeedImg
        needImg(isNeedImg)

        // 无数据图片
        val src: Drawable? = typedArray?.getDrawable(R.styleable.NoDataLayout_src)
        when {
            src != null -> setImg(src)
            mPdrConfig.drawableResId != 0 -> setImg(mPdrConfig.drawableResId)
            else -> setImg(R.drawable.pandora_ic_no_data)
        }

        // 默认提示语
        val defaultTips = if (mPdrConfig.tips.isEmpty()) context.getString(R.string.pandora_no_data) else mPdrConfig.tips
        val attrsTips: String = typedArray?.getString(R.styleable.NoDataLayout_tips) ?: defaultTips
        setTips(if (attrsTips.isEmpty()) defaultTips else attrsTips)

        // 提示语颜色
        val tipsColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.NoDataLayout_tipsColor)
        if (tipsColor != null) {
            setTipsTextColor(tipsColor)
        } else if (mPdrConfig.textColor != 0) {
            setTipsTextColor(mPdrConfig.textColor)
        }

        // 提示语大小
        val tipsSize: Int = typedArray?.getDimensionPixelSize(R.styleable.NoDataLayout_tipsSize, 0) ?: 0
        if (tipsSize != 0) {
            setTipsTextSize(px2sp(tipsSize.toFloat()))
        } else if (mPdrConfig.textSize != 0) {
            setTipsTextSize(mPdrConfig.textSize.toFloat())
        }

        // 设置加载页背景
        val drawableBackground: Drawable? = typedArray?.getDrawable(R.styleable.NoDataLayout_contentBackground)
        when {
            drawableBackground != null -> background = drawableBackground
            mPdrConfig.backgroundColor != 0 -> setBackgroundColor(getColorCompat(mPdrConfig.backgroundColor))
            else -> setBackgroundColor(getColorCompat(android.R.color.white))
        }

        typedArray?.recycle()
    }

    /** 是否需要[isNeed]提示图片 */
    fun needImg(isNeed: Boolean) {
        mPdrNoDataImg.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 是否需要[isNeed]提示文字 */
    fun needTips(isNeed: Boolean) {
        mPdrNoDataTv.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 设置无数据图片资源[drawableResId] */
    fun setImg(@DrawableRes drawableResId: Int) {
        mPdrNoDataImg.setImageResource(drawableResId)
    }

    /** 设置无数据图片[drawable] */
    fun setImg(drawable: Drawable) {
        mPdrNoDataImg.setImageDrawable(drawable)
    }

    /** 设置提示文字[str] */
    fun setTips(str: String) {
        mPdrNoDataTv.text = str
    }

    /** 设置提示文字资源[strResId] */
    fun setTips(@StringRes strResId: Int) {
        mPdrNoDataTv.text = context.getString(strResId)
    }

    /** 设置文字颜色资源[colorRes] */
    fun setTipsTextColor(@ColorRes colorRes: Int) {
        mPdrNoDataTv.setTextColor(getColorCompat(colorRes))
    }

    /** 设置文字颜色[color] */
    fun setTipsTextColorInt(@ColorInt color: Int) {
        mPdrNoDataTv.setTextColor(color)
    }

    /** 设置文字颜色[colorStateList] */
    fun setTipsTextColor(colorStateList: ColorStateList) {
        mPdrNoDataTv.setTextColor(colorStateList)
    }

    /** 设置文字大小[sp] */
    fun setTipsTextSize(sp: Float) {
        mPdrNoDataTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置加载页面的布局方向[orientation] */
    fun setLayoutOrientation(@BaseLayoutConfig.OrientationType orientation: Int) {
        if (orientation == LinearLayout.HORIZONTAL || orientation == LinearLayout.VERTICAL) {
            mPdrRootView.orientation = orientation
        }
    }
}