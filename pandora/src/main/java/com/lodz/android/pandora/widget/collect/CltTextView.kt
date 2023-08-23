package com.lodz.android.pandora.widget.collect

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.px2sp
import com.lodz.android.pandora.R

/**
 * 采集TextView
 * @author zhouL
 * @date 2019/3/13
 */
class CltTextView : FrameLayout {

    /** 必填图标 */
    private val mPdrRequiredImg by bindView<ImageView>(R.id.pdr_required_img)
    /** 标题控件 */
    private val mPdrTitleTv by bindView<TextView>(R.id.pdr_title_tv)
    /** 内容控件 */
    private val mPdrContentTv by bindView<TextView>(R.id.pdr_content_tv)
    /** 单位控件 */
    private val mPdrUnitTv by bindView<TextView>(R.id.pdr_unit_tv)
    /** 跳转按钮 */
    private val mPdrJumpBtn by bindView<TextView>(R.id.pdr_jump_btn)

    /** 内容标记 */
    private var mPdrContentTag = ""

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        findViews()
        configLayout(attrs)
    }

    private fun findViews() {
        LayoutInflater.from(context).inflate(R.layout.pandora_view_clt_text_view, this)
    }

    private fun configLayout(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.CltTextView)
        }

        // 设置必填图片是否显示
        val visibility = typedArray?.getInt(R.styleable.CltTextView_requiredVisibility, 0) ?: 0
        when (visibility) {
            1 -> setRequiredVisibility(View.INVISIBLE)
            2 -> setRequiredVisibility(View.GONE)
            else -> setRequiredVisibility(View.VISIBLE)
        }

        // 设置必填图片
        val src: Drawable? = typedArray?.getDrawable(R.styleable.CltTextView_requiredSrc)
        if (src != null) {
            setRequiredImg(src)
        }
        // 设置标题文字
        val titleText: String? = typedArray?.getString(R.styleable.CltTextView_titleText)
        if (titleText != null) {
            setTitleText(titleText)
        }
        // 设置标题文字颜色
        val titleTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltTextView_titleTextColor)
        if (titleTextColor != null) {
            setTitleTextColor(titleTextColor)
        }
        // 设置标题文字大小
        val titleTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.CltTextView_titleTextSize, 0) ?: 0
        if (titleTextSize > 0) {
            setTitleTextSize(px2sp(titleTextSize.toFloat()))
        }
        // 设置标题控件宽度
        val titleWidth: Int = typedArray?.getDimensionPixelSize(R.styleable.CltTextView_titleWidth, 0)
                ?: 0
        if (titleWidth > 0) {
            setTitleWidth(titleWidth)
        }
        // 设置标题背景
        val titleBackground: Drawable? = typedArray?.getDrawable(R.styleable.CltTextView_titleBackground)
        if (titleBackground != null) {
            setTitleBackground(titleBackground)
        }
        // 设置内容文字
        val contentText: String? = typedArray?.getString(R.styleable.CltTextView_contentText)
        if (contentText != null) {
            setContentText(contentText)
        }
        // 设置内容文字颜色
        val contentTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltTextView_contentTextColor)
        if (contentTextColor != null) {
            setContentTextColor(contentTextColor)
        }
        // 设置内容文字大小
        val contentTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.CltTextView_contentTextSize, 0) ?: 0
        if (contentTextSize > 0) {
            setContentTextSize(px2sp(contentTextSize.toFloat()))
        }
        // 设置内容提示语
        val contentHint: String? = typedArray?.getString(R.styleable.CltTextView_contentHint)
        if (contentHint != null) {
            setContentHint(contentHint)
        }
        // 设置内容提示语颜色
        val contentHintColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltTextView_contentHintColor)
        if (contentHintColor != null) {
            setContentHintColor(contentHintColor)
        }
        // 设置内容右侧图标
        val contentDrawableEnd: Drawable? = typedArray?.getDrawable(R.styleable.CltTextView_contentDrawableEnd)
        if (contentDrawableEnd != null) {
            setContentDrawableEnd(contentDrawableEnd)
        }
        // 设置内容右侧图标
        val contentDrawableStart: Drawable? = typedArray?.getDrawable(R.styleable.CltTextView_contentDrawableStart)
        if (contentDrawableStart != null) {
            setContentDrawableStart(contentDrawableStart)
        }
        // 设置内容图标间距
        val contentDrawablePadding: Int = typedArray?.getDimensionPixelSize(R.styleable.CltTextView_contentDrawablePadding, 0)
                ?: 0
        if (contentDrawablePadding > 0) {
            setContentDrawablePadding(contentDrawablePadding)
        }
        // 设置内容标记
        val contentTag: String? = typedArray?.getString(R.styleable.CltTextView_contentTag)
        if (contentTag != null) {
            setContentTag(contentTag)
        }
        // 设置加载页背景
        val contentBackground: Drawable? = typedArray?.getDrawable(R.styleable.CltTextView_contentBackground)
        if (contentBackground != null) {
            setContentBackground(contentBackground)
        }
        // 设置内容文字位置
        val gravity = typedArray?.getInt(R.styleable.CltTextView_contentGravity, 0) ?: 0
        when (gravity) {
            1 -> setContentGravity(Gravity.CENTER)
            2 -> setContentGravity(Gravity.START)
            3 -> setContentGravity(Gravity.END )
            4 -> setContentGravity(Gravity.START or Gravity.CENTER_VERTICAL)
            5 -> setContentGravity(Gravity.END or Gravity.CENTER_VERTICAL)
            else -> setContentGravity(Gravity.CENTER_VERTICAL)
        }
        // 设置单位文字
        val unitText: String? = typedArray?.getString(R.styleable.CltTextView_unitText)
        if (unitText != null) {
            setNeedUnit(true)
            setUnitText(unitText)
        } else {
            setNeedUnit(false)
        }
        // 设置单位文字颜色
        val unitTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltTextView_unitTextColor)
        if (unitTextColor != null) {
            setUnitTextColor(unitTextColor)
        }
        // 设置单位文字大小
        val unitTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.CltTextView_unitTextSize, 0) ?: 0
        if (unitTextSize > 0) {
            setUnitTextSize(px2sp(unitTextSize.toFloat()))
        }
        // 设置跳转按钮文字
        val jumpText: String? = typedArray?.getString(R.styleable.CltTextView_jumpText)
        if (jumpText != null) {
            setNeedJumpBtn(true)
            setJumpBtnText(jumpText)
        } else {
            setNeedJumpBtn(false)
        }
        // 设置跳转按钮文字颜色
        val jumpTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltTextView_jumpTextColor)
        if (jumpTextColor != null) {
            setJumpBtnTextColor(jumpTextColor)
        }
        // 设置跳转按钮文字大小
        val jumpTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.CltTextView_jumpTextSize, 0) ?: 0
        if (jumpTextSize > 0) {
            setJumpBtnTextSize(px2sp(jumpTextSize.toFloat()))
        }
        // 设置内容右侧图标
        val jumpBackground: Drawable? = typedArray?.getDrawable(R.styleable.CltTextView_jumpBackground)
        if (jumpBackground != null) {
            setJumpBackground(jumpBackground)
        }
        // 设置是否只读
        setReadOnly(typedArray?.getBoolean(R.styleable.CltTextView_isReadOnly, false) ?: false)
        typedArray?.recycle()
    }

    /** 设置必填图片资源[resId] */
    fun setRequiredImg(@DrawableRes resId: Int) {
        mPdrRequiredImg.setImageResource(resId)
    }

    /** 设置必填图片[bitmap] */
    fun setRequiredImg(bitmap: Bitmap) {
        mPdrRequiredImg.setImageBitmap(bitmap)
    }

    /** 设置必填图片[drawable] */
    fun setRequiredImg(drawable: Drawable) {
        mPdrRequiredImg.setImageDrawable(drawable)
    }

    /** 设置必填图片显隐[visibility] */
    fun setRequiredVisibility(visibility: Int) {
        mPdrRequiredImg.visibility = visibility
    }

    /** 获取必填图片是否显示 */
    fun isRequired(): Boolean = mPdrRequiredImg.visibility == View.VISIBLE

    /** 设置标题文字[title] */
    fun setTitleText(title: String) {
        mPdrTitleTv.text = title
    }

    /** 设置标题文字资源[resId] */
    fun setTitleText(@StringRes resId: Int) {
        mPdrTitleTv.setText(resId)
    }

    /** 获取标题文字 */
    fun getTitleText(): String = mPdrTitleTv.text.toString()

    /** 设置标题文字颜色[color] */
    fun setTitleTextColor(@ColorInt color: Int) {
        mPdrTitleTv.setTextColor(color)
    }

    /** 设置标题文字颜色[color] */
    fun setTitleTextColorRes(@ColorRes color: Int) {
        mPdrTitleTv.setTextColor(getColorCompat(color))
    }

    /** 设置标题文字颜色[color] */
    fun setTitleTextColor(color: ColorStateList) {
        mPdrTitleTv.setTextColor(color)
    }

    /** 设置标题文字大小[sp] */
    fun setTitleTextSize(sp: Float) {
        mPdrTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置标题控件宽度 */
    fun setTitleWidth(px: Int) {
        mPdrTitleTv.layoutParams.width = px
    }

    /** 设置标题背景[resId] */
    fun setTitleBackgroundRes(@DrawableRes resId: Int) {
        mPdrTitleTv.setBackgroundResource(resId)
    }

    /** 设置标题背景[color] */
    fun setTitleBackgroundColor(@ColorInt color: Int) {
        mPdrTitleTv.setBackgroundColor(color)
    }

    /** 设置标题背景[color] */
    fun setTitleBackgroundColorRes(@ColorRes color: Int) {
        mPdrTitleTv.setBackgroundColor(getColorCompat(color))
    }

    /** 设置标题背景[drawable] */
    fun setTitleBackground(drawable: Drawable) {
        mPdrTitleTv.background = drawable
    }

    /** 设置内容文字[content] */
    fun setContentText(content: String) {
        mPdrContentTv.text = content
    }

    /** 设置内容文字资源[resId] */
    fun setContentText(@StringRes resId: Int) {
        mPdrContentTv.setText(resId)
    }

    /** 获取内容文字 */
    fun getContentText(): String = mPdrContentTv.text.toString()

    /** 设置内容标记[tag] */
    fun setContentTag(tag: String) {
        mPdrContentTag = tag
    }

    /** 获取内容标记 */
    fun getContentTag(): String = mPdrContentTag

    /** 设置内容文字颜色[color] */
    fun setContentTextColor(@ColorInt color: Int) {
        mPdrContentTv.setTextColor(color)
    }

    /** 设置内容文字颜色[color] */
    fun setContentTextColorRes(@ColorRes color: Int) {
        mPdrContentTv.setTextColor(getColorCompat(color))
    }

    /** 设置内容文字颜色[color] */
    fun setContentTextColor(color: ColorStateList) {
        mPdrContentTv.setTextColor(color)
    }

    /** 设置内容文字大小[sp] */
    fun setContentTextSize(sp: Float) {
        mPdrContentTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置内容提示语资源[resId] */
    fun setContentHint(@StringRes resId: Int) {
        mPdrContentTv.setHint(resId)
    }

    /** 设置内容提示语[hint] */
    fun setContentHint(hint: String) {
        mPdrContentTv.hint = hint
    }

    /** 获取内容提示语 */
    fun getContentHint(): String = mPdrContentTv.hint.toString()

    /** 设置内容提示语颜色[color] */
    fun setContentHintColor(@ColorInt color: Int) {
        mPdrContentTv.setHintTextColor(color)
    }

    /** 设置内容提示语颜色[color] */
    fun setContentHintColorRes(@ColorRes color: Int) {
        mPdrContentTv.setHintTextColor(getColorCompat(color))
    }

    /** 设置内容提示语颜色[color] */
    fun setContentHintColor(color: ColorStateList) {
        mPdrContentTv.setHintTextColor(color)
    }

    /** 设置内容右侧图标资源[resId] */
    fun setContentDrawableEnd(@DrawableRes resId: Int) {
        mPdrContentTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0)
    }

    /** 设置内容右侧图标[drawable] */
    fun setContentDrawableEnd(drawable: Drawable) {
        mPdrContentTv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }

    /** 设置内容左侧图标资源[resId] */
    fun setContentDrawableStart(@DrawableRes resId: Int) {
        mPdrContentTv.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0)
    }

    /** 设置内容左侧图标[drawable] */
    fun setContentDrawableStart(drawable: Drawable) {
        mPdrContentTv.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }

    /** 设置内容图标间距[pad] */
    fun setContentDrawablePadding(pad: Int) {
        mPdrContentTv.compoundDrawablePadding = pad
    }

    /** 设置内容点击监听器[listener] */
    fun setOnContentClickListener(listener: ((view: View) -> Unit)?) {
        mPdrContentTv.setOnClickListener(listener)
    }

    /** 设置内容点击监听器[listener] */
    fun setOnContentClickListener(listener: View.OnClickListener?) {
        mPdrContentTv.setOnClickListener(listener)
    }

    /** 设置内容背景[drawable] */
    fun setContentBackground(drawable: Drawable) {
        mPdrContentTv.background = drawable
    }

    /** 设置内容背景[color] */
    fun setContentBackground(@ColorInt color: Int) {
        mPdrContentTv.setBackgroundColor(color)
    }

    /** 设置内容背景[resId] */
    fun setContentBackgroundRes(@DrawableRes resId: Int) {
        mPdrContentTv.setBackgroundResource(resId)
    }

    /** 设置内容文字位置 */
    fun setContentGravity(gravity: Int) {
        mPdrContentTv.gravity = gravity
    }

    /** 设置是否需要单位[isNeed] */
    fun setNeedUnit(isNeed: Boolean) {
        mPdrUnitTv.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 是否显示单位 */
    fun isShowUnit(): Boolean = mPdrUnitTv.visibility == View.VISIBLE

    /** 设置单位文字[unit] */
    fun setUnitText(unit: String) {
        mPdrUnitTv.text = unit
    }

    /** 设置单位文字资源[resId] */
    fun setUnitText(@StringRes resId: Int) {
        mPdrUnitTv.setText(resId)
    }

    /** 获取单位文字 */
    fun getUnitText(): String = mPdrUnitTv.text.toString()

    /** 设置单位文字颜色[color] */
    fun setUnitTextColor(@ColorInt color: Int) {
        mPdrUnitTv.setTextColor(color)
    }

    /** 设置单位文字颜色[color] */
    fun setUnitTextColorRes(@ColorRes color: Int) {
        mPdrUnitTv.setTextColor(getColorCompat(color))
    }

    /** 设置单位文字颜色[color] */
    fun setUnitTextColor(color: ColorStateList) {
        mPdrUnitTv.setTextColor(color)
    }

    /** 设置单位文字大小[sp] */
    fun setUnitTextSize(sp: Float) {
        mPdrUnitTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置是否需要跳转按钮[isNeed] */
    fun setNeedJumpBtn(isNeed: Boolean) {
        mPdrJumpBtn.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 是否显示跳转按钮 */
    fun isShowJumpBtn(): Boolean = mPdrJumpBtn.visibility == View.VISIBLE

    /** 设置跳转按钮文字[unit] */
    fun setJumpBtnText(unit: String) {
        mPdrJumpBtn.text = unit
    }

    /** 设置跳转按钮文字资源[resId] */
    fun setJumpBtnText(@StringRes resId: Int) {
        mPdrJumpBtn.setText(resId)
    }

    /** 获取跳转按钮文字 */
    fun getJumpBtnText(): String = mPdrJumpBtn.text.toString()

    /** 设置跳转按钮文字颜色[color] */
    fun setJumpBtnTextColor(@ColorInt color: Int) {
        mPdrJumpBtn.setTextColor(color)
    }

    /** 设置跳转按钮文字颜色[color] */
    fun setJumpBtnTextColorRes(@ColorRes color: Int) {
        mPdrJumpBtn.setTextColor(getColorCompat(color))
    }

    /** 设置跳转按钮文字颜色[color] */
    fun setJumpBtnTextColor(color: ColorStateList) {
        mPdrJumpBtn.setTextColor(color)
    }

    /** 设置跳转按钮文字大小[sp] */
    fun setJumpBtnTextSize(sp: Float) {
        mPdrJumpBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置跳转按钮背景[resId] */
    fun setJumpBackgroundRes(@DrawableRes resId: Int) {
        mPdrJumpBtn.setBackgroundResource(resId)
    }

    /** 设置跳转按钮背景[color] */
    fun setJumpBackgroundColor(@ColorInt color: Int) {
        mPdrJumpBtn.setBackgroundColor(color)
    }

    /** 设置跳转按钮背景[color] */
    fun setJumpBackgroundColorRes(@ColorRes color: Int) {
        mPdrJumpBtn.setBackgroundColor(getColorCompat(color))
    }

    /** 设置跳转按钮背景[drawable] */
    fun setJumpBackground(drawable: Drawable) {
        mPdrJumpBtn.background = drawable
    }

    /** 设置跳转按钮监听器[listener] */
    fun setOnJumpClickListener(listener: ((view: View) -> Unit)?) {
        mPdrJumpBtn.setOnClickListener(listener)
    }

    /** 设置跳转按钮监听器[listener] */
    fun setOnJumpClickListener(listener: View.OnClickListener?) {
        mPdrJumpBtn.setOnClickListener(listener)
    }

    /** 设置是否只读[isReadOnly] */
    fun setReadOnly(isReadOnly: Boolean) {
        mPdrContentTv.isEnabled = !isReadOnly
        mPdrJumpBtn.isEnabled = !isReadOnly
    }

    /** 是否只读 */
    fun isReadOnly(): Boolean = !mPdrContentTv.isEnabled && !mPdrJumpBtn.isEnabled
}