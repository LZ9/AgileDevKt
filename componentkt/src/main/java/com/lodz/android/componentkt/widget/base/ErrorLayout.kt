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
import com.lodz.android.componentkt.base.application.config.ErrorLayoutConfig
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.px2sp

/**
 * 加载失败控件
 * Created by zhouL on 2018/6/27.
 */
class ErrorLayout : LinearLayout {

    /** 异常界面配置 */
    private var mConfig = ErrorLayoutConfig()

    /** 根布局  */
    private val mRootView by bindView<LinearLayout>(R.id.error_root_layout)
    /** 失败图片  */
    private val mErrorImg by bindView<ImageView>(R.id.error_img)
    /** 提示语  */
    private val mErrorTipsTv by bindView<TextView>(R.id.error_tips_tv)

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
            mConfig = BaseApplication.get()!!.getBaseLayoutConfig().getErrorLayoutConfig()
        }
        LayoutInflater.from(context).inflate(R.layout.componentkt_view_error_layout, this)
        configLayout(attrs)
    }

    private fun configLayout(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.ErrorLayout)
        }

        // 布局方向
        val orientation: Int = typedArray?.getInt(R.styleable.ErrorLayout_contentOrientation, mConfig.orientation)
                ?: mConfig.orientation
        setLayoutOrientation(orientation)

        // 是否需要图片
        val isNeedImg: Boolean = typedArray?.getBoolean(R.styleable.ErrorLayout_isNeedImg, mConfig.isNeedImg)
                ?: mConfig.isNeedImg
        needImg(isNeedImg)

        // 是否需要提示语
        val isNeedTips: Boolean = typedArray?.getBoolean(R.styleable.ErrorLayout_isNeedTips, mConfig.isNeedTips)
                ?: mConfig.isNeedTips
        needTips(isNeedTips)

        val src: Drawable? = typedArray?.getDrawable(R.styleable.ErrorLayout_src)
        if (src != null) {
            setImg(src)
        } else if (mConfig.drawableResId != 0) {
            setImg(mConfig.drawableResId)
        } else {
            setImg(R.drawable.componentkt_ic_data_fail)
        }

        // 默认提示语
        val defaultTips = if (mConfig.tips.isEmpty()) context.getString(R.string.componentkt_load_fail) else mConfig.tips
        val attrsTips: String = typedArray?.getString(R.styleable.ErrorLayout_tips) ?: defaultTips
        setTips(if (attrsTips.isEmpty()) defaultTips else attrsTips)

        // 提示语颜色
        val tipsColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.ErrorLayout_tipsColor)
        if (tipsColor != null) {
            setTipsTextColor(tipsColor)
        } else if (mConfig.textColor != 0) {
            setTipsTextColor(mConfig.textColor)
        }

        // 提示语大小
        val tipsSize: Int = typedArray?.getDimensionPixelSize(R.styleable.ErrorLayout_tipsSize, 0)
                ?: 0
        if (tipsSize != 0) {
            setTipsTextSize(px2sp(tipsSize))
        } else if (mConfig.textSize != 0) {
            setTipsTextSize(mConfig.textSize.toFloat())
        }

        // 设置加载页背景
        val drawableBackground: Drawable? = typedArray?.getDrawable(R.styleable.ErrorLayout_contentBackground)
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
        mErrorImg.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 是否需要[isNeed]提示文字 */
    fun needTips(isNeed: Boolean) {
        mErrorTipsTv.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 设置界面错误图片资源[drawableResId] */
    fun setImg(@DrawableRes drawableResId: Int) {
        mErrorImg.setImageResource(drawableResId)
    }

    /** 设置无数据图片[drawable] */
    fun setImg(drawable: Drawable) {
        mErrorImg.setImageDrawable(drawable)
    }

    /** 设置提示文字[str] */
    fun setTips(str: String) {
        mErrorTipsTv.text = str
    }

    /** 设置提示文字资源[strResId] */
    fun setTips(@StringRes strResId: Int) {
        mErrorTipsTv.text = context.getString(strResId)
    }

    /** 设置文字颜色资源[colorRes] */
    fun setTipsTextColor(@ColorRes colorRes: Int) {
        mErrorTipsTv.setTextColor(context.getColorCompat(colorRes))
    }

    /** 设置文字颜色[color] */
    fun setTipsTextColorInt(@ColorInt color: Int) {
        mErrorTipsTv.setTextColor(color)
    }

    /** 设置文字颜色[colorStateList] */
    fun setTipsTextColor(colorStateList: ColorStateList) {
        mErrorTipsTv.setTextColor(colorStateList)
    }

    /** 设置文字大小[sp] */
    fun setTipsTextSize(sp: Float) {
        mErrorTipsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置重载监听器[listener] */
    fun setReloadListener(listener: (View) -> Unit) {
        mRootView.setOnClickListener(listener)
    }

    /** 设置加载页面的布局方向[orientation] */
    fun setLayoutOrientation(@BaseLayoutConfig.OrientationType orientation: Int) {
        if (orientation == LinearLayout.HORIZONTAL || orientation == LinearLayout.VERTICAL) {
            mRootView.orientation = orientation
        }
    }
}
