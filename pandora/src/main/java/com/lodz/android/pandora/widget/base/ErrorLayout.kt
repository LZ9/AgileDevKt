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
import com.lodz.android.corekt.network.NetworkManager
import com.lodz.android.pandora.R
import com.lodz.android.pandora.base.application.BaseApplication
import com.lodz.android.pandora.base.application.config.BaseLayoutConfig
import com.lodz.android.pandora.base.application.config.ErrorLayoutConfig
import com.lodz.android.pandora.rx.exception.ExceptionFactory

/**
 * 加载失败控件
 * Created by zhouL on 2018/6/27.
 */
class ErrorLayout : LinearLayout {

    /** 异常界面配置 */
    private var mPdrConfig = ErrorLayoutConfig()

    /** 根布局 */
    private val mPdrRootView by bindView<LinearLayout>(R.id.pdr_error_root_layout)
    /** 失败图片 */
    private val mPdrErrorImg by bindView<ImageView>(R.id.pdr_error_img)
    /** 提示语 */
    private val mPdrErrorTipsTv by bindView<TextView>(R.id.pdr_error_tips_tv)

    /** 通用异常图标 */
    @DrawableRes
    private var mPdrSrcResId = R.drawable.pandora_ic_data_fail
    private var mPdrSrc: Drawable? = null

    /** 网络异常图标 */
    @DrawableRes
    private var mPdrSrcNetResId = R.drawable.pandora_ic_network_fail
    private var mPdrSrcNet: Drawable? = null

    /** 通用异常提示语 */
    private var mPdrTips = context.getString(R.string.pandora_load_fail)
    /** 网络异常提示语 */
    private var mPdrNetTips = context.getString(R.string.pandora_load_fail_net)

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
            mPdrConfig = app.getBaseLayoutConfig().getErrorLayoutConfig()
        }
        LayoutInflater.from(context).inflate(R.layout.pandora_view_error, this)
        configLayout(attrs)
    }

    private fun configLayout(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.ErrorLayout)
        }

        // 布局方向
        val orientation: Int = typedArray?.getInt(R.styleable.ErrorLayout_contentOrientation, mPdrConfig.orientation)
                ?: mPdrConfig.orientation
        setLayoutOrientation(orientation)

        // 是否需要图片
        val isNeedImg: Boolean = typedArray?.getBoolean(R.styleable.ErrorLayout_isNeedImg, mPdrConfig.isNeedImg)
                ?: mPdrConfig.isNeedImg
        needImg(isNeedImg)

        // 是否需要提示语
        val isNeedTips: Boolean = typedArray?.getBoolean(R.styleable.ErrorLayout_isNeedTips, mPdrConfig.isNeedTips)
                ?: mPdrConfig.isNeedTips
        needTips(isNeedTips)

        val src: Drawable? = typedArray?.getDrawable(R.styleable.ErrorLayout_src)
        when {
            src != null -> setImg(src)
            mPdrConfig.drawableResId != 0 -> setImg(mPdrConfig.drawableResId)
            else -> setImg(R.drawable.pandora_ic_data_fail)
        }

        val srcNet: Drawable? = typedArray?.getDrawable(R.styleable.ErrorLayout_srcNet)
        when {
            srcNet != null -> setNetImg(srcNet)
            mPdrConfig.drawableNetResId != 0 -> setNetImg(mPdrConfig.drawableNetResId)
            else -> setNetImg(R.drawable.pandora_ic_network_fail)
        }

        // 默认提示语
        val defaultTips = if (mPdrConfig.tips.isEmpty()) context.getString(R.string.pandora_load_fail) else mPdrConfig.tips
        val attrsTips: String = typedArray?.getString(R.styleable.ErrorLayout_tips) ?: defaultTips
        setTips(if (attrsTips.isEmpty()) defaultTips else attrsTips)

        // 网络提示语
        val netTips = if (mPdrConfig.netTips.isEmpty()) context.getString(R.string.pandora_load_fail_net) else mPdrConfig.netTips
        val attrsNetTips: String = typedArray?.getString(R.styleable.ErrorLayout_netTips) ?: netTips
        setNetTips(if (attrsNetTips.isEmpty()) netTips else attrsNetTips)

        // 提示语颜色
        val tipsColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.ErrorLayout_tipsColor)
        if (tipsColor != null) {
            setTipsTextColor(tipsColor)
        } else if (mPdrConfig.textColor != 0) {
            setTipsTextColor(mPdrConfig.textColor)
        }

        // 提示语大小
        val tipsSize: Int = typedArray?.getDimensionPixelSize(R.styleable.ErrorLayout_tipsSize, 0)?: 0
        if (tipsSize != 0) {
            setTipsTextSize(px2sp(tipsSize.toFloat()))
        } else if (mPdrConfig.textSize != 0) {
            setTipsTextSize(mPdrConfig.textSize.toFloat())
        }

        // 设置加载页背景
        val drawableBackground: Drawable? = typedArray?.getDrawable(R.styleable.ErrorLayout_contentBackground)
        when {
            drawableBackground != null -> background = drawableBackground
            mPdrConfig.backgroundColor != 0 -> setBackgroundColor(getColorCompat(mPdrConfig.backgroundColor))
            else -> setBackgroundColor(getColorCompat(android.R.color.white))
        }

        typedArray?.recycle()
    }

    /** 是否需要[isNeed]提示图片 */
    fun needImg(isNeed: Boolean) {
        mPdrErrorImg.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 是否需要[isNeed]提示文字 */
    fun needTips(isNeed: Boolean) {
        mPdrErrorTipsTv.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 设置界面错误图片资源[drawableResId] */
    fun setImg(@DrawableRes drawableResId: Int) {
        mPdrSrcResId = drawableResId
    }

    /** 设置界面错误图片[drawable] */
    fun setImg(drawable: Drawable) {
        mPdrSrc = drawable
    }

    /** 设置网络错误图片资源[drawableResId] */
    fun setNetImg(@DrawableRes drawableResId: Int) {
        mPdrSrcNetResId = drawableResId
    }

    /** 设置网络错误图片[drawable] */
    fun setNetImg(drawable: Drawable) {
        mPdrSrcNet = drawable
    }

    /** 设置提示文字[str] */
    fun setTips(str: String) {
        mPdrTips = str
    }

    /** 设置提示文字资源[strResId] */
    fun setTips(@StringRes strResId: Int) {
        mPdrTips = context.getString(strResId)
    }

    /** 设置提示文字[str] */
    fun setNetTips(str: String) {
        mPdrNetTips = str
    }

    /** 设置提示文字资源[strResId] */
    fun setNetTips(@StringRes strResId: Int) {
        mPdrNetTips = context.getString(strResId)
    }

    /** 设置文字颜色资源[colorRes] */
    fun setTipsTextColor(@ColorRes colorRes: Int) {
        mPdrErrorTipsTv.setTextColor(getColorCompat(colorRes))
    }

    /** 设置文字颜色[color] */
    fun setTipsTextColorInt(@ColorInt color: Int) {
        mPdrErrorTipsTv.setTextColor(color)
    }

    /** 设置文字颜色[colorStateList] */
    fun setTipsTextColor(colorStateList: ColorStateList) {
        mPdrErrorTipsTv.setTextColor(colorStateList)
    }

    /** 设置文字大小[sp] */
    fun setTipsTextSize(sp: Float) {
        mPdrErrorTipsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置重载监听器[listener] */
    fun setReloadListener(listener: (view: View) -> Unit) {
        mPdrRootView.setOnClickListener(listener)
    }

    /** 设置加载页面的布局方向[orientation] */
    fun setLayoutOrientation(@BaseLayoutConfig.OrientationType orientation: Int) {
        if (orientation == LinearLayout.HORIZONTAL || orientation == LinearLayout.VERTICAL) {
            mPdrRootView.orientation = orientation
        }
    }

    /** 显示数据异常 */
    fun showDataFail(){
        mPdrErrorTipsTv.text = mPdrTips
        if (mPdrSrc != null){
            mPdrErrorImg.setImageDrawable(mPdrSrc)
            return
        }
        mPdrErrorImg.setImageResource(mPdrSrcResId)
    }

    /** 显示网络异常 */
    fun showNetFail(){
        mPdrErrorTipsTv.text = mPdrNetTips
        if (mPdrSrcNet != null){
            mPdrErrorImg.setImageDrawable(mPdrSrcNet)
            return
        }
        mPdrErrorImg.setImageResource(mPdrSrcNetResId)
    }

    /** 自动判断界面显示 */
    @JvmOverloads
    fun showAuto(t: Throwable? = null) {
        if (!NetworkManager.get().isNetworkAvailable()) {
            showNetFail()
            return
        }
        if (t == null) {
            showDataFail()
            return
        }
        if (ExceptionFactory.isNetworkError(t)) {
            showNetFail()
            return
        }
        showDataFail()
    }
}
