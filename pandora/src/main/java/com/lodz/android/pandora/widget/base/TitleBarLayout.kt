package com.lodz.android.pandora.widget.base

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
import android.widget.TextView
import androidx.annotation.*
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
    private var mConfig = TitleBarLayoutConfig()

    /** 返回按钮布局  */
    private val mBackLayout by bindView<LinearLayout>(R.id.back_layout)
    /** 返回按钮  */
    private val mBackBtn by bindView<TextView>(R.id.back_btn)
    /** 标题  */
    private val mTitleTv by bindView<TextView>(R.id.title_tv)
    /** 扩展区布局  */
    private val mExpandLayout by bindView<LinearLayout>(R.id.expand_layout)
    /** 分割线  */
    private val mDivideLineView by bindView<View>(R.id.divide_line_view)

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
        val app = BaseApplication.get()
        if (app != null) {
            mConfig = app.getBaseLayoutConfig().getTitleBarLayoutConfig()
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
        needBackButton(typedArray?.getBoolean(R.styleable.TitleBarLayout_isNeedBackBtn, mConfig.isNeedBackBtn)
                ?: mConfig.isNeedBackBtn)

        // 返回按钮图标
        val backDrawable: Drawable? = typedArray?.getDrawable(R.styleable.TitleBarLayout_backDrawable)
        if (backDrawable != null) {
            mBackBtn.setCompoundDrawablesWithIntrinsicBounds(backDrawable, null, null, null)
        } else if (mConfig.backBtnResId != 0) {
            mBackBtn.setCompoundDrawablesWithIntrinsicBounds(mConfig.backBtnResId, 0, 0, 0)
        }

        // 返回按钮文字
        val backText: String = typedArray?.getString(R.styleable.TitleBarLayout_backText) ?: ""
        if (backText.isNotEmpty()) {
            setBackBtnName(backText)
        } else if (mConfig.backBtnText.isNotEmpty()) {
            setBackBtnName(mConfig.backBtnText)
        }

        // 返回按钮文字颜色
        val backTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.TitleBarLayout_backTextColor)
        if (backTextColor != null) {
            setBackBtnTextColor(backTextColor)
        } else if (mConfig.backBtnTextColor != 0) {
            setBackBtnTextColor(mConfig.backBtnTextColor)
        }

        // 返回按钮文字大小
        val backTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.TitleBarLayout_backTextSize, 0)
                ?: 0
        if (backTextSize != 0) {
            setBackBtnTextSize(px2spRF(backTextSize))
        } else if (mConfig.backBtnTextSize != 0) {
            setBackBtnTextSize(mConfig.backBtnTextSize.toFloat())
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
        } else if (mConfig.titleTextColor != 0) {
            setTitleTextColor(mConfig.titleTextColor)
        }

        // 标题文字大小
        val titleTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.TitleBarLayout_titleTextSize, 0)
                ?: 0
        if (titleTextSize != 0) {
            setTitleTextSize(px2spRF(titleTextSize))
        } else if (mConfig.titleTextSize != 0) {
            setTitleTextSize(mConfig.titleTextSize.toFloat())
        }

        // 是否显示分割线
        val isShowDivideLine: Boolean = typedArray?.getBoolean(R.styleable.TitleBarLayout_isShowDivideLine, mConfig.isShowDivideLine)
                ?: mConfig.isShowDivideLine
        needDivideLine(isShowDivideLine)

        // 分割线背景色
        val divideLineDrawable: Drawable? = typedArray?.getDrawable(R.styleable.TitleBarLayout_divideLineColor)
        if (divideLineDrawable != null) {
            setDivideLineDrawable(divideLineDrawable)
        } else if (mConfig.divideLineColor != 0) {
            setDivideLineColor(mConfig.divideLineColor)
        }

        // 分割线高度
        val divideLineHeight: Int = typedArray?.getDimensionPixelSize(R.styleable.TitleBarLayout_divideLineHeight, 0)
                ?: 0
        if (divideLineHeight > 0) {
            setDivideLineHeight(px2dp(divideLineHeight))
        } else if (mConfig.divideLineHeightDp > 0) {
            setDivideLineHeight(mConfig.divideLineHeightDp)
        }

        // 标题背景
        val drawableBackground: Drawable? = typedArray?.getDrawable(R.styleable.TitleBarLayout_titleBarBackground)
        if (drawableBackground != null) {
            background = drawableBackground
        } else if (mConfig.backgroundResId != 0) {
            setBackgroundResource(mConfig.backgroundResId)
        } else if (mConfig.backgroundColor != 0) {
            setBackgroundColor(getColorCompat(mConfig.backgroundColor))
        } else {
            setBackgroundColor(getColorCompat(android.R.color.holo_blue_light))
        }

        // 是否需要阴影
        val isNeedElevation: Boolean = typedArray?.getBoolean(R.styleable.TitleBarLayout_isNeedElevation, mConfig.isNeedElevation)
                ?: mConfig.isNeedElevation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isNeedElevation) {
            val elevationVale: Int = typedArray?.getDimensionPixelSize(R.styleable.TitleBarLayout_elevationVale, 0)
                    ?: 0
            if (elevationVale != 0) {
                elevation = elevationVale.toFloat()
            } else {
                elevation = mConfig.elevationVale
            }
        }

        // 是否需要右侧扩展区域
        val isNeedExpandView: Boolean = typedArray?.getBoolean(R.styleable.TitleBarLayout_isNeedExpandView, false)
                ?: false
        needExpandView(isNeedExpandView)

        // 加载扩展区布局
        val expandViewId: Int = typedArray?.getResourceId(R.styleable.TitleBarLayout_expandViewId, 0)
                ?: 0
        if (expandViewId > 0) {
            val view: View? = LayoutInflater.from(context).inflate(expandViewId, null)
            if (view != null) {
                addExpandView(view)
            }
        }

        if (typedArray != null) {
            typedArray.recycle()
        }
    }

    /** 是否需要[isNeed]显示返回按钮 */
    fun needBackButton(isNeed: Boolean) {
        mBackLayout.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 设置返回按钮的透明度[alpha] */
    fun setBackButtonAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        mBackLayout.alpha = alpha
    }

    /** 设置返回按钮监听[listener] */
    fun setOnBackBtnClickListener(listener: (view: View) -> Unit) {
        mBackLayout.setOnClickListener(listener)
    }

    /** 替换默认的返回按钮[view] */
    fun replaceBackBtn(view: View) {
        mBackLayout.removeAllViews()
        mBackLayout.addView(view)
    }

    /** 设置返回按钮文字[str] */
    fun setBackBtnName(str: String) {
        mBackBtn.text = str
    }

    /** 设置返回按钮文字资源[strResId] */
    fun setBackBtnName(@StringRes strResId: Int) {
        mBackBtn.text = context.getString(strResId)
    }

    /** 设置返回按钮文字颜色资源[colorRes] */
    fun setBackBtnTextColor(@ColorRes colorRes: Int) {
        mBackBtn.setTextColor(getColorCompat(colorRes))
    }

    /** 设置返回按钮文字颜色[color] */
    fun setBackBtnTextColorInt(@ColorInt color: Int) {
        mBackBtn.setTextColor(color)
    }

    /** 设置返回按钮文字颜色[colorStateList] */
    fun setBackBtnTextColor(colorStateList: ColorStateList) {
        mBackBtn.setTextColor(colorStateList)
    }

    /** 设置返回按钮文字大小[sp] */
    fun setBackBtnTextSize(sp: Float) {
        mBackBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置标题[title] */
    fun setTitleName(title: String) {
        mTitleTv.text = title
    }

    /** 设置标题资源[strResId] */
    fun setTitleName(@StringRes strResId: Int) {
        mTitleTv.text = context.getString(strResId)
    }

    /** 设置标题文字颜色资源[colorRes] */
    fun setTitleTextColor(@ColorRes colorRes: Int) {
        mTitleTv.setTextColor(getColorCompat(colorRes))
    }

    /** 设置标题文字颜色[color] */
    fun setTitleTextColorInt(@ColorInt color: Int) {
        mTitleTv.setTextColor(color)
    }

    /** 设置标题文字颜色资源[colorStateList] */
    fun setTitleTextColor(colorStateList: ColorStateList) {
        mTitleTv.setTextColor(colorStateList)
    }

    /** 设置标题文字大小[sp] */
    fun setTitleTextSize(sp: Float) {
        mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置标题的透明度[alpha] */
    fun setTitleAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        mTitleTv.alpha = alpha
    }

    /** 是否需要[isNeed]右侧扩展区 */
    fun needExpandView(isNeed: Boolean) {
        mExpandLayout.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 添加扩展区域的[view] */
    fun addExpandView(view: View) {
        mExpandLayout.addView(view)
        needExpandView(true)
    }

    /** 获取扩展区域的View */
    fun getExpandView(): View = mExpandLayout


    /** 是否需[isNeed]要分割线 */
    fun needDivideLine(isNeed: Boolean) {
        mDivideLineView.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 隐藏分割线 */
    fun goneDivideLine() {
        mDivideLineView.visibility = View.GONE
    }

    /** 设置分割线颜色资源[colorRes] */
    fun setDivideLineColor(@ColorRes colorRes: Int) {
        mDivideLineView.setBackgroundColor(getColorCompat(colorRes))
    }

    /** 设置分割线颜色[color] */
    fun setDivideLineColorInt(@ColorInt color: Int) {
        mDivideLineView.setBackgroundColor(color)
    }

    /** 设置分割线背景[drawable] */
    fun setDivideLineDrawable(drawable: Drawable) {
        mDivideLineView.setBackground(drawable)
    }

    /** 设置分割线高度[dp] */
    fun setDivideLineHeight(dp: Int) {
        val layoutParams = mDivideLineView.layoutParams
        layoutParams.height = dp2px(dp)
        mDivideLineView.layoutParams = layoutParams
    }
}