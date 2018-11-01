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
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.lodz.android.componentkt.R
import com.lodz.android.componentkt.base.application.BaseApplication
import com.lodz.android.componentkt.base.application.config.BaseLayoutConfig
import com.lodz.android.componentkt.base.application.config.LoadingLayoutConfig
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.getDrawableCompat
import com.lodz.android.corekt.anko.px2sp

/**
 * 加载控件
 * Created by zhouL on 2018/6/27.
 */
class LoadingLayout : LinearLayout {

    /** 加载页配置 */
    private var mConfig = LoadingLayoutConfig()

    /** 根布局  */
    private val mRootView by bindView<LinearLayout>(R.id.root_view)
    /** 提示语  */
    private val mLoadingTipsTv by bindView<TextView>(R.id.loading_tips_tv)
    /** 进度条 */
    private lateinit var mLoadingProgressBar: ProgressBar

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
            mConfig = BaseApplication.get()!!.getBaseLayoutConfig().getLoadingLayoutConfig()
        }
        findViews()
        configLayout(attrs)
    }

    private fun findViews() {
        LayoutInflater.from(context).inflate(R.layout.componentkt_view_loading_layout, this)
        mLoadingProgressBar = findViewById(R.id.loading_progressbar)
    }

    private fun configLayout(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout)
        }

        // 布局方向
        val orientation: Int = typedArray?.getInt(R.styleable.LoadingLayout_contentOrientation, mConfig.orientation) ?: mConfig.orientation
        setLayoutOrientation(orientation)

        // 是否需要提示语
        val isNeedTips: Boolean = typedArray?.getBoolean(R.styleable.LoadingLayout_isNeedTips, mConfig.isNeedTips) ?: mConfig.isNeedTips
        needTips(isNeedTips)

        // 默认提示语
        val defaultTips = if (mConfig.tips.isEmpty()) context.getString(R.string.componentkt_loading) else mConfig.tips
        val attrsTips: String = typedArray?.getString(R.styleable.LoadingLayout_tips) ?: defaultTips
        setTips(if (attrsTips.isEmpty()) defaultTips else attrsTips)

        // 提示语颜色
        val tipsColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.LoadingLayout_tipsColor)
        if (tipsColor != null) {
            setTipsTextColor(tipsColor)
        } else if (mConfig.textColor != 0) {
            setTipsTextColor(mConfig.textColor)
        }

        // 提示语大小
        val tipsSize: Int = typedArray?.getDimensionPixelSize(R.styleable.LoadingLayout_tipsSize, 0) ?: 0
        if (tipsSize != 0) {
            setTipsTextSize(px2sp(tipsSize))
        } else if (mConfig.textSize != 0) {
            setTipsTextSize(mConfig.textSize.toFloat())
        }

        // 设置ProgressBar模式
        val isIndeterminate: Boolean = typedArray?.getBoolean(R.styleable.LoadingLayout_isIndeterminate, mConfig.isIndeterminate) ?: mConfig.isIndeterminate
        mLoadingProgressBar.isIndeterminate = isIndeterminate

        // 设置ProgressBar的Drawable
        val drawable: Drawable? = typedArray?.getDrawable(R.styleable.LoadingLayout_indeterminateDrawable)
        if (drawable != null) {
            mLoadingProgressBar.indeterminateDrawable = drawable
        } else if (mConfig.indeterminateDrawable != 0) {
            mLoadingProgressBar.indeterminateDrawable = context.getDrawableCompat(mConfig.indeterminateDrawable)
        }

        // 设置ProgressBar宽高
        val layoutParams = mLoadingProgressBar.layoutParams
        val pbWidth: Int = typedArray?.getDimensionPixelSize(R.styleable.LoadingLayout_pbWidth, mConfig.pbWidthPx) ?: mConfig.pbWidthPx
        val pbHeight: Int = typedArray?.getDimensionPixelSize(R.styleable.LoadingLayout_pbHeight, mConfig.pbHeightPx) ?: mConfig.pbHeightPx
        if (pbWidth != 0) {
            layoutParams.width = pbWidth
        }
        if (pbHeight != 0) {
            layoutParams.height = pbHeight
        }

        // 设置加载页背景
        val drawableBackground: Drawable? = typedArray?.getDrawable(R.styleable.LoadingLayout_contentBackground)
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

    /** 是否需要[isNeed]提示文字 */
    fun needTips(isNeed: Boolean) {
        mLoadingTipsTv.visibility = if(isNeed) View.VISIBLE else View.GONE
    }

    /** 设置提示文字[str] */
    fun setTips(str: String) {
        mLoadingTipsTv.text = str
    }

    /** 设置提示文字资源[strResId] */
    fun setTips(@StringRes strResId: Int) {
        mLoadingTipsTv.text = context.getString(strResId)
    }

    /** 设置文字颜色资源[colorRes] */
    fun setTipsTextColor(@ColorRes colorRes: Int) {
        mLoadingTipsTv.setTextColor(context.getColorCompat(colorRes))
    }

    /** 设置文字颜色[color] */
    fun setTipsTextColorInt(@ColorInt color: Int) {
        mLoadingTipsTv.setTextColor(color)
    }

    /** 设置文字颜色[colorStateList] */
    fun setTipsTextColor(colorStateList: ColorStateList) {
        mLoadingTipsTv.setTextColor(colorStateList)
    }

    /** 设置文字大小[sp] */
    fun setTipsTextSize(sp: Float) {
        mLoadingTipsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置进度条控件[progressBar] */
    fun setProgressBar(progressBar: ProgressBar) {
        mLoadingProgressBar = progressBar
    }

    /** 获取进度条控件  */
    fun getProgressBar(): ProgressBar = mLoadingProgressBar

    /** 设置加载页面的布局方向[orientation] */
    fun setLayoutOrientation(@BaseLayoutConfig.OrientationType orientation: Int) {
        if (orientation == LinearLayout.HORIZONTAL || orientation == LinearLayout.VERTICAL) {
            mRootView.orientation = orientation
        }
    }
}