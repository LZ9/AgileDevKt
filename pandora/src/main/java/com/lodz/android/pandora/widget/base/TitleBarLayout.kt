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
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import androidx.annotation.StringRes
import com.lodz.android.corekt.anko.*
import com.lodz.android.pandora.R
import com.lodz.android.pandora.base.application.BaseApplication
import com.lodz.android.pandora.base.application.config.TitleBarLayoutConfig

/**
 * 标题栏布局
 * Created by zhouL on 2018/6/27.
 */
open class TitleBarLayout : LinearLayout {

    /** 标题栏配置 */
    private var mPdrConfig = TitleBarLayoutConfig()

    /** 返回按钮布局 */
    private val mPdrBackLayout by bindView<LinearLayout>(R.id.pdr_back_layout)
    /** 返回按钮 */
    private val mPdrBackBtn by bindView<TextView>(R.id.pdr_back_btn)
    /** 标题 */
    private val mPdrTitleTv by bindView<TextView>(R.id.pdr_title_tv)
    /** 扩展区布局 */
    private val mPdrExpandLayout by bindView<LinearLayout>(R.id.pdr_expand_layout)
    /** 分割线 */
    private val mPdrDivideLineView by bindView<View>(R.id.pdr_divide_line_view)

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
            mPdrConfig = app.getBaseLayoutConfig().getTitleBarLayoutConfig()
        }
        LayoutInflater.from(context).inflate(R.layout.pandora_view_title, this)
        configLayout(attrs)
    }

    private fun configLayout(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBarLayout)
        }

        // 返回按钮
        needBackButton(typedArray?.getBoolean(R.styleable.TitleBarLayout_isNeedBackBtn, mPdrConfig.isNeedBackBtn)
                ?: mPdrConfig.isNeedBackBtn)

        // 返回按钮图标
        val backDrawable: Drawable? = typedArray?.getDrawable(R.styleable.TitleBarLayout_backDrawable)
        if (backDrawable != null) {
            mPdrBackBtn.setCompoundDrawablesWithIntrinsicBounds(backDrawable, null, null, null)
        } else if (mPdrConfig.backBtnResId != 0) {
            mPdrBackBtn.setCompoundDrawablesWithIntrinsicBounds(mPdrConfig.backBtnResId, 0, 0, 0)
        }

        // 返回按钮文字
        val backText: String = typedArray?.getString(R.styleable.TitleBarLayout_backText) ?: ""
        if (backText.isNotEmpty()) {
            setBackBtnName(backText)
        } else if (mPdrConfig.backBtnText.isNotEmpty()) {
            setBackBtnName(mPdrConfig.backBtnText)
        }

        // 返回按钮文字颜色
        val backTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.TitleBarLayout_backTextColor)
        if (backTextColor != null) {
            setBackBtnTextColor(backTextColor)
        } else if (mPdrConfig.backBtnTextColor != 0) {
            setBackBtnTextColor(mPdrConfig.backBtnTextColor)
        }

        // 返回按钮文字大小
        val backTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.TitleBarLayout_backTextSize, 0)
                ?: 0
        if (backTextSize != 0) {
            setBackBtnTextSize(px2spRF(backTextSize))
        } else if (mPdrConfig.backBtnTextSize != 0) {
            setBackBtnTextSize(mPdrConfig.backBtnTextSize.toFloat())
        }

        // 标题文字
        val titleText: String = typedArray?.getString(R.styleable.TitleBarLayout_titleText) ?: ""
        if (titleText.isNotEmpty()) {
            setTitleName(titleText)
        }

        // 标题文字颜色
        val titleTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.TitleBarLayout_titleTextColor)
        if (titleTextColor != null) {
            setTitleTextColor(titleTextColor)
        } else if (mPdrConfig.titleTextColor != 0) {
            setTitleTextColor(mPdrConfig.titleTextColor)
        }

        // 标题文字大小
        val titleTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.TitleBarLayout_titleTextSize, 0)
                ?: 0
        if (titleTextSize != 0) {
            setTitleTextSize(px2spRF(titleTextSize))
        } else if (mPdrConfig.titleTextSize != 0) {
            setTitleTextSize(mPdrConfig.titleTextSize.toFloat())
        }

        // 是否显示分割线
        val isShowDivideLine: Boolean = typedArray?.getBoolean(R.styleable.TitleBarLayout_isShowDivideLine, mPdrConfig.isShowDivideLine)
                ?: mPdrConfig.isShowDivideLine
        needDivideLine(isShowDivideLine)

        // 分割线背景色
        val divideLineDrawable: Drawable? = typedArray?.getDrawable(R.styleable.TitleBarLayout_divideLineColor)
        if (divideLineDrawable != null) {
            setDivideLineDrawable(divideLineDrawable)
        } else if (mPdrConfig.divideLineColor != 0) {
            setDivideLineColor(mPdrConfig.divideLineColor)
        }

        // 分割线高度
        val divideLineHeight: Int = typedArray?.getDimensionPixelSize(R.styleable.TitleBarLayout_divideLineHeight, 0)
                ?: 0
        if (divideLineHeight > 0) {
            setDivideLineHeight(px2dp(divideLineHeight))
        } else if (mPdrConfig.divideLineHeightDp > 0) {
            setDivideLineHeight(mPdrConfig.divideLineHeightDp)
        }

        // 标题背景
        val drawableBackground: Drawable? = typedArray?.getDrawable(R.styleable.TitleBarLayout_titleBarBackground)
        when {
            drawableBackground != null -> background = drawableBackground
            mPdrConfig.backgroundResId != 0 -> setBackgroundResource(mPdrConfig.backgroundResId)
            mPdrConfig.backgroundColor != 0 -> setBackgroundColor(getColorCompat(mPdrConfig.backgroundColor))
            else -> setBackgroundColor(getColorCompat(android.R.color.holo_blue_light))
        }

        // 是否需要阴影
        val isNeedElevation: Boolean = typedArray?.getBoolean(R.styleable.TitleBarLayout_isNeedElevation, mPdrConfig.isNeedElevation)
                ?: mPdrConfig.isNeedElevation
        if (isNeedElevation) {
            val elevationVale: Int = typedArray?.getDimensionPixelSize(R.styleable.TitleBarLayout_elevationVale, 0)
                    ?: 0
            elevation = if (elevationVale != 0) elevationVale.toFloat() else mPdrConfig.elevationVale
        }

        // 加载扩展区布局
        val expandViewId: Int = typedArray?.getResourceId(R.styleable.TitleBarLayout_expandViewId, 0)
                ?: 0
        if (expandViewId > 0) {
            val view: View? = LayoutInflater.from(context).inflate(expandViewId, null)
            if (view != null) {
                addExpandView(view)
            }
        }

        // 是否需要右侧扩展区域
        val isNeedExpandView: Boolean = typedArray?.getBoolean(R.styleable.TitleBarLayout_isNeedExpandView, false)
                ?: false
        needExpandView(isNeedExpandView)

        typedArray?.recycle()
    }

    /** 是否需要[isNeed]显示返回按钮 */
    fun needBackButton(isNeed: Boolean) {
        mPdrBackLayout.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 设置返回按钮的透明度[alpha] */
    fun setBackButtonAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        mPdrBackLayout.alpha = alpha
    }

    /** 设置返回按钮监听[listener] */
    fun setOnBackBtnClickListener(listener: (view: View) -> Unit) {
        mPdrBackLayout.setOnClickListener(listener)
    }

    /** 替换默认的返回按钮[view] */
    fun replaceBackBtn(view: View) {
        mPdrBackLayout.removeAllViews()
        mPdrBackLayout.addView(view)
    }

    /** 设置返回按钮文字[str] */
    fun setBackBtnName(str: String) {
        mPdrBackBtn.text = str
    }

    /** 设置返回按钮文字资源[strResId] */
    fun setBackBtnName(@StringRes strResId: Int) {
        mPdrBackBtn.text = context.getString(strResId)
    }

    /** 设置返回按钮文字颜色资源[colorRes] */
    fun setBackBtnTextColor(@ColorRes colorRes: Int) {
        mPdrBackBtn.setTextColor(getColorCompat(colorRes))
    }

    /** 设置返回按钮文字颜色[color] */
    fun setBackBtnTextColorInt(@ColorInt color: Int) {
        mPdrBackBtn.setTextColor(color)
    }

    /** 设置返回按钮文字颜色[colorStateList] */
    fun setBackBtnTextColor(colorStateList: ColorStateList) {
        mPdrBackBtn.setTextColor(colorStateList)
    }

    /** 设置返回按钮文字大小[sp] */
    fun setBackBtnTextSize(sp: Float) {
        mPdrBackBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置标题[title] */
    fun setTitleName(title: String) {
        mPdrTitleTv.text = title
    }

    /** 设置标题资源[strResId] */
    fun setTitleName(@StringRes strResId: Int) {
        mPdrTitleTv.text = context.getString(strResId)
    }

    /** 设置标题文字颜色资源[colorRes] */
    fun setTitleTextColor(@ColorRes colorRes: Int) {
        mPdrTitleTv.setTextColor(getColorCompat(colorRes))
    }

    /** 设置标题文字颜色[color] */
    fun setTitleTextColorInt(@ColorInt color: Int) {
        mPdrTitleTv.setTextColor(color)
    }

    /** 设置标题文字颜色资源[colorStateList] */
    fun setTitleTextColor(colorStateList: ColorStateList) {
        mPdrTitleTv.setTextColor(colorStateList)
    }

    /** 设置标题文字大小[sp] */
    fun setTitleTextSize(sp: Float) {
        mPdrTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置标题的透明度[alpha] */
    fun setTitleAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        mPdrTitleTv.alpha = alpha
    }

    /** 是否需要[isNeed]右侧扩展区 */
    fun needExpandView(isNeed: Boolean) {
        mPdrExpandLayout.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 添加扩展区域的[view] */
    fun addExpandView(view: View) {
        mPdrExpandLayout.addView(view)
        needExpandView(true)
    }

    /** 获取扩展区域的View */
    fun getExpandView(): View = mPdrExpandLayout


    /** 是否需[isNeed]要分割线 */
    fun needDivideLine(isNeed: Boolean) {
        mPdrDivideLineView.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 隐藏分割线 */
    fun goneDivideLine() {
        mPdrDivideLineView.visibility = View.GONE
    }

    /** 设置分割线颜色资源[colorRes] */
    fun setDivideLineColor(@ColorRes colorRes: Int) {
        mPdrDivideLineView.setBackgroundColor(getColorCompat(colorRes))
    }

    /** 设置分割线颜色[color] */
    fun setDivideLineColorInt(@ColorInt color: Int) {
        mPdrDivideLineView.setBackgroundColor(color)
    }

    /** 设置分割线背景[drawable] */
    fun setDivideLineDrawable(drawable: Drawable) {
        mPdrDivideLineView.background = drawable
    }

    /** 设置分割线高度[dp] */
    fun setDivideLineHeight(dp: Int) {
        val layoutParams = mPdrDivideLineView.layoutParams
        layoutParams.height = dp2px(dp)
        mPdrDivideLineView.layoutParams = layoutParams
    }
}