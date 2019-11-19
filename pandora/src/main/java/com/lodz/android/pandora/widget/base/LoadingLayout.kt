package com.lodz.android.pandora.widget.base

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.lodz.android.corekt.anko.*
import com.lodz.android.pandora.R
import com.lodz.android.pandora.base.application.BaseApplication
import com.lodz.android.pandora.base.application.config.BaseLayoutConfig
import com.lodz.android.pandora.base.application.config.LoadingLayoutConfig

/**
 * 加载控件
 * Created by zhouL on 2018/6/27.
 */
class LoadingLayout : LinearLayout {

    /** 加载页配置 */
    private var mPdrConfig = LoadingLayoutConfig()

    /** 根布局 */
    private val mPdrRootView by bindView<LinearLayout>(R.id.pdr_root_view)
    /** 提示语 */
    private val mPdrLoadingTipsTv by bindView<TextView>(R.id.pdr_loading_tips_tv)
    /** 进度条 */
    private lateinit var mPdrLoadingProgressBar: ProgressBar

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
            mPdrConfig = app.getBaseLayoutConfig().getLoadingLayoutConfig()
        }
        findViews()
        configLayout(attrs)
    }

    private fun findViews() {
        LayoutInflater.from(context).inflate(R.layout.pandora_view_loading, this)
        mPdrLoadingProgressBar = findViewById(R.id.pdr_loading_progressbar)
    }

    private fun configLayout(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout)
        }

        // 布局方向
        val orientation: Int = typedArray?.getInt(R.styleable.LoadingLayout_contentOrientation, mPdrConfig.orientation)
                ?: mPdrConfig.orientation
        setLayoutOrientation(orientation)

        // 是否需要提示语
        val isNeedTips: Boolean = typedArray?.getBoolean(R.styleable.LoadingLayout_isNeedTips, mPdrConfig.isNeedTips)
                ?: mPdrConfig.isNeedTips
        needTips(isNeedTips)

        // 默认提示语
        val defaultTips = if (mPdrConfig.tips.isEmpty()) context.getString(R.string.pandora_loading) else mPdrConfig.tips
        val attrsTips: String = typedArray?.getString(R.styleable.LoadingLayout_tips) ?: defaultTips
        setTips(if (attrsTips.isEmpty()) defaultTips else attrsTips)

        // 提示语颜色
        val tipsColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.LoadingLayout_tipsColor)
        if (tipsColor != null) {
            setTipsTextColor(tipsColor)
        } else if (mPdrConfig.textColor != 0) {
            setTipsTextColor(mPdrConfig.textColor)
        }

        // 提示语大小
        val tipsSize: Int = typedArray?.getDimensionPixelSize(R.styleable.LoadingLayout_tipsSize, 0)
                ?: 0
        if (tipsSize != 0) {
            setTipsTextSize(px2spRF(tipsSize))
        } else if (mPdrConfig.textSize != 0) {
            setTipsTextSize(mPdrConfig.textSize.toFloat())
        }

        // 设置ProgressBar模式
        val isIndeterminate: Boolean = typedArray?.getBoolean(R.styleable.LoadingLayout_isIndeterminate, mPdrConfig.isIndeterminate)
                ?: mPdrConfig.isIndeterminate
        mPdrLoadingProgressBar.isIndeterminate = isIndeterminate

        // 是否使用默认加载资源
        val useSysDefDrawable: Boolean = typedArray?.getBoolean(R.styleable.LoadingLayout_useSysDefDrawable, mPdrConfig.useSysDefDrawable)
            ?: mPdrConfig.useSysDefDrawable

        // 设置ProgressBar的Drawable
        val drawable: Drawable? = typedArray?.getDrawable(R.styleable.LoadingLayout_indeterminateDrawable)
        if (drawable != null) {
            mPdrLoadingProgressBar.indeterminateDrawable = drawable
        } else if (mPdrConfig.indeterminateDrawable != 0) {
            mPdrLoadingProgressBar.indeterminateDrawable = getDrawableCompat(mPdrConfig.indeterminateDrawable)
        } else if(!useSysDefDrawable){
            mPdrLoadingProgressBar.indeterminateDrawable = getDrawableCompat(R.drawable.pandora_anims_loading)
        }

        // 设置ProgressBar宽高
        val layoutParams = mPdrLoadingProgressBar.layoutParams
        var pbWidth: Int = typedArray?.getDimensionPixelSize(R.styleable.LoadingLayout_pbWidth, mPdrConfig.pbWidthPx)
                ?: mPdrConfig.pbWidthPx
        var pbHeight: Int = typedArray?.getDimensionPixelSize(R.styleable.LoadingLayout_pbHeight, mPdrConfig.pbHeightPx)
                ?: mPdrConfig.pbHeightPx
        if (pbWidth == 0 && !useSysDefDrawable){
            pbWidth = dp2px(90)
        }
        if (pbHeight == 0 && !useSysDefDrawable){
            pbHeight = dp2px(90)
        }
        if (pbWidth != 0) {
            layoutParams.width = pbWidth
        }
        if (pbHeight != 0) {
            layoutParams.height = pbHeight
        }

        // 设置加载页背景
        val drawableBackground: Drawable? = typedArray?.getDrawable(R.styleable.LoadingLayout_contentBackground)
        when {
            drawableBackground != null -> background = drawableBackground
            mPdrConfig.backgroundColor != 0 -> setBackgroundColor(getColorCompat(mPdrConfig.backgroundColor))
            else -> setBackgroundColor(getColorCompat(android.R.color.white))
        }

        typedArray?.recycle()
    }

    /** 是否需要[isNeed]提示文字 */
    fun needTips(isNeed: Boolean) {
        mPdrLoadingTipsTv.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 设置提示文字[str] */
    fun setTips(str: String) {
        mPdrLoadingTipsTv.text = str
    }

    /** 设置提示文字资源[strResId] */
    fun setTips(@StringRes strResId: Int) {
        mPdrLoadingTipsTv.text = context.getString(strResId)
    }

    /** 设置文字颜色资源[colorRes] */
    fun setTipsTextColor(@ColorRes colorRes: Int) {
        mPdrLoadingTipsTv.setTextColor(getColorCompat(colorRes))
    }

    /** 设置文字颜色[color] */
    fun setTipsTextColorInt(@ColorInt color: Int) {
        mPdrLoadingTipsTv.setTextColor(color)
    }

    /** 设置文字颜色[colorStateList] */
    fun setTipsTextColor(colorStateList: ColorStateList) {
        mPdrLoadingTipsTv.setTextColor(colorStateList)
    }

    /** 设置文字大小[sp] */
    fun setTipsTextSize(sp: Float) {
        mPdrLoadingTipsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置进度条控件[progressBar] */
    fun setProgressBar(progressBar: ProgressBar) {
        mPdrLoadingProgressBar = progressBar
    }

    /** 获取进度条控件 */
    fun getProgressBar(): ProgressBar = mPdrLoadingProgressBar

    /** 设置加载页面的布局方向[orientation] */
    fun setLayoutOrientation(@BaseLayoutConfig.OrientationType orientation: Int) {
        if (orientation == LinearLayout.HORIZONTAL || orientation == LinearLayout.VERTICAL) {
            mPdrRootView.orientation = orientation
        }
    }
}