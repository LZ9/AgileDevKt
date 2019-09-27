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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.*
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.px2spRF
import com.lodz.android.corekt.network.NetworkManager
import com.lodz.android.pandora.R
import com.lodz.android.pandora.base.application.BaseApplication
import com.lodz.android.pandora.base.application.config.BaseLayoutConfig
import com.lodz.android.pandora.base.application.config.ErrorLayoutConfig
import com.lodz.android.pandora.rx.exception.NetworkException
import java.net.SocketException
import java.net.SocketTimeoutException

/**
 * 加载失败控件
 * Created by zhouL on 2018/6/27.
 */
class ErrorLayout : LinearLayout {

    /** 异常界面配置 */
    private var mConfig = ErrorLayoutConfig()

    /** 根布局 */
    private val mRootView by bindView<LinearLayout>(R.id.error_root_layout)
    /** 失败图片 */
    private val mErrorImg by bindView<ImageView>(R.id.error_img)
    /** 提示语 */
    private val mErrorTipsTv by bindView<TextView>(R.id.error_tips_tv)

    /** 通用异常图标 */
    @DrawableRes
    private var mSrcResId = R.drawable.pandora_ic_data_fail
    private var mSrc: Drawable? = null

    /** 网络异常图标 */
    @DrawableRes
    private var mSrcNetResId = R.drawable.pandora_ic_network_fail
    private var mSrcNet: Drawable? = null

    /** 通用异常提示语 */
    private var mTips = context.getString(R.string.pandora_load_fail)
    /** 网络异常提示语 */
    private var mNetTips = context.getString(R.string.pandora_load_fail_net)

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
            mConfig = app.getBaseLayoutConfig().getErrorLayoutConfig()
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
        when {
            src != null -> setImg(src)
            mConfig.drawableResId != 0 -> setImg(mConfig.drawableResId)
            else -> setImg(R.drawable.pandora_ic_data_fail)
        }

        val srcNet: Drawable? = typedArray?.getDrawable(R.styleable.ErrorLayout_srcNet)
        when {
            srcNet != null -> setNetImg(srcNet)
            mConfig.drawableNetResId != 0 -> setNetImg(mConfig.drawableNetResId)
            else -> setNetImg(R.drawable.pandora_ic_network_fail)
        }

        // 默认提示语
        val defaultTips = if (mConfig.tips.isEmpty()) context.getString(R.string.pandora_load_fail) else mConfig.tips
        val attrsTips: String = typedArray?.getString(R.styleable.ErrorLayout_tips) ?: defaultTips
        setTips(if (attrsTips.isEmpty()) defaultTips else attrsTips)

        // 网络提示语
        val netTips = if (mConfig.netTips.isEmpty()) context.getString(R.string.pandora_load_fail_net) else mConfig.netTips
        val attrsNetTips: String = typedArray?.getString(R.styleable.ErrorLayout_netTips) ?: netTips
        setNetTips(if (attrsNetTips.isEmpty()) netTips else attrsNetTips)

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
            setTipsTextSize(px2spRF(tipsSize))
        } else if (mConfig.textSize != 0) {
            setTipsTextSize(mConfig.textSize.toFloat())
        }

        // 设置加载页背景
        val drawableBackground: Drawable? = typedArray?.getDrawable(R.styleable.ErrorLayout_contentBackground)
        when {
            drawableBackground != null -> background = drawableBackground
            mConfig.backgroundColor != 0 -> setBackgroundColor(getColorCompat(mConfig.backgroundColor))
            else -> setBackgroundColor(getColorCompat(android.R.color.white))
        }

        typedArray?.recycle()
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
        mSrcResId = drawableResId
    }

    /** 设置界面错误图片[drawable] */
    fun setImg(drawable: Drawable) {
        mSrc = drawable
    }

    /** 设置网络错误图片资源[drawableResId] */
    fun setNetImg(@DrawableRes drawableResId: Int) {
        mSrcNetResId = drawableResId
    }

    /** 设置网络错误图片[drawable] */
    fun setNetImg(drawable: Drawable) {
        mSrcNet = drawable
    }

    /** 设置提示文字[str] */
    fun setTips(str: String) {
        mTips = str
    }

    /** 设置提示文字资源[strResId] */
    fun setTips(@StringRes strResId: Int) {
        mTips = context.getString(strResId)
    }

    /** 设置提示文字[str] */
    fun setNetTips(str: String) {
        mNetTips = str
    }

    /** 设置提示文字资源[strResId] */
    fun setNetTips(@StringRes strResId: Int) {
        mNetTips = context.getString(strResId)
    }

    /** 设置文字颜色资源[colorRes] */
    fun setTipsTextColor(@ColorRes colorRes: Int) {
        mErrorTipsTv.setTextColor(getColorCompat(colorRes))
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
    fun setReloadListener(listener: (view: View) -> Unit) {
        mRootView.setOnClickListener(listener)
    }

    /** 设置加载页面的布局方向[orientation] */
    fun setLayoutOrientation(@BaseLayoutConfig.OrientationType orientation: Int) {
        if (orientation == LinearLayout.HORIZONTAL || orientation == LinearLayout.VERTICAL) {
            mRootView.orientation = orientation
        }
    }

    /** 显示数据异常 */
    fun showDataFail(){
        mErrorTipsTv.text = mTips
        if (mSrc != null){
            mErrorImg.setImageDrawable(mSrc)
            return
        }
        mErrorImg.setImageResource(mSrcResId)
    }

    /** 显示网络异常 */
    fun showNetFail(){
        mErrorTipsTv.text = mNetTips
        if (mSrcNet != null){
            mErrorImg.setImageDrawable(mSrcNet)
            return
        }
        mErrorImg.setImageResource(mSrcNetResId)
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
        if (t is NetworkException || t is SocketTimeoutException || t is SocketException) {
            showNetFail()
            return
        }
        showDataFail()
    }
}
