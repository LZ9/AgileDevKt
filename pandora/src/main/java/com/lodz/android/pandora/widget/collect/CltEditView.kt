package com.lodz.android.pandora.widget.collect

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.text.method.ReplacementTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.px2sp
import com.lodz.android.pandora.R

/**
 * 采集EditText
 * @author zhouL
 * @date 2019/5/9
 */
class CltEditView : FrameLayout {

    companion object {
        /** 输入文本 */
        const val TYPE_TEXT = 0
        /** 输入身份证 */
        const val TYPE_ID_CARD = 1
        /** 输入手机号 */
        const val TYPE_PHONE = 2
        /** 输入整型数字 */
        const val TYPE_NUMBER = 3
        /** 输入小数数字 */
        const val TYPE_NUMBER_DECIMAL = 4
        /** 外国证件 */
        const val TYPE_FOREIGN_CERT = 5
    }

    @IntDef(TYPE_TEXT, TYPE_ID_CARD, TYPE_PHONE, TYPE_NUMBER, TYPE_NUMBER_DECIMAL, TYPE_FOREIGN_CERT)
    @Retention(AnnotationRetention.SOURCE)
    annotation class EditInputType

    /** 必填图标  */
    private val mRequiredImg by bindView<ImageView>(R.id.required_img)
    /** 标题控件  */
    private val mTitleTv by bindView<TextView>(R.id.title_tv)
    /** 内容输入框  */
    private val mContentEdit by bindView<EditText>(R.id.content_edit)
    /** 单位控件  */
    private val mUnitTv by bindView<TextView>(R.id.unit_tv)
    /** 跳转按钮  */
    private val mJumpBtn by bindView<TextView>(R.id.jump_btn)

    /** 当前输入类型 */
    @EditInputType
    private var mInputType = TYPE_TEXT
    /** 文字监听器 */
    private var mTextWatcher: TextWatcher? = null
    /** 内容标记  */
    private var mContentTag = ""

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(null)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(null)
    }

    private fun init(attrs: AttributeSet?) {
        findViews()
        configLayout(attrs)
        setListeners()
    }

    private fun findViews() {
        LayoutInflater.from(context).inflate(R.layout.pandora_view_clt_edit_view, this)
    }

    private fun configLayout(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.CltEditView)
        }

        // 设置必填图片是否显示
        val visibility = typedArray?.getInt(R.styleable.CltEditView_requiredVisibility, 0) ?: 0
        if (visibility == 1) {
            setRequiredVisibility(View.INVISIBLE)
        } else if (visibility == 2) {
            setRequiredVisibility(View.GONE)
        } else {
            setRequiredVisibility(View.VISIBLE)
        }
        // 设置必填图片
        val src: Drawable? = typedArray?.getDrawable(R.styleable.CltEditView_requiredSrc)
        if (src != null) {
            setRequiredImg(src)
        }
        // 设置标题文字
        val titleText: String? = typedArray?.getString(R.styleable.CltEditView_titleText)
        if (titleText != null) {
            setTitleText(titleText)
        }
        // 设置标题文字颜色
        val titleTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltEditView_titleTextColor)
        if (titleTextColor != null) {
            setTitleTextColor(titleTextColor)
        }
        // 设置标题文字大小
        val titleTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.CltEditView_titleTextSize, 0)
                ?: 0
        if (titleTextSize > 0) {
            setTitleTextSize(px2sp(titleTextSize))
        }
        // 设置标题控件宽度
        val titleWidth: Int = typedArray?.getDimensionPixelSize(R.styleable.CltEditView_titleWidth, 0)
                ?: 0
        if (titleWidth > 0) {
            setTitleWidth(titleWidth)
        }
        // 设置标题背景
        val titleBackground: Drawable? = typedArray?.getDrawable(R.styleable.CltEditView_titleBackground)
        if (titleBackground != null) {
            setTitleBackground(titleBackground)
        }
        // 设置内容文字
        val contentText: String? = typedArray?.getString(R.styleable.CltEditView_contentText)
        if (contentText != null) {
            setContentText(contentText)
        }
        // 设置内容文字颜色
        val contentTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltEditView_contentTextColor)
        if (contentTextColor != null) {
            setContentTextColor(contentTextColor)
        }
        // 设置内容文字大小
        val contentTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.CltEditView_contentTextSize, 0)
                ?: 0
        if (contentTextSize > 0) {
            setContentTextSize(px2sp(contentTextSize))
        }
        // 设置内容提示语
        val contentHint: String? = typedArray?.getString(R.styleable.CltEditView_contentHint)
        if (contentHint != null) {
            setContentHint(contentHint)
        }
        // 设置内容提示语颜色
        val contentHintColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltEditView_contentHintColor)
        if (contentHintColor != null) {
            setContentHintColor(contentHintColor)
        }
        // 设置内容右侧图标
        val contentDrawableEnd: Drawable? = typedArray?.getDrawable(R.styleable.CltEditView_contentDrawableEnd)
        if (contentDrawableEnd != null) {
            setContentDrawableEnd(contentDrawableEnd)
        }
        // 设置内容右侧图标
        val contentDrawableStart: Drawable? = typedArray?.getDrawable(R.styleable.CltEditView_contentDrawableStart)
        if (contentDrawableStart != null) {
            setContentDrawableStart(contentDrawableStart)
        }
        // 设置内容图标间距
        val contentDrawablePadding: Int = typedArray?.getDimensionPixelSize(R.styleable.CltEditView_contentDrawablePadding, 0)
                ?: 0
        if (contentDrawablePadding > 0) {
            setContentDrawablePadding(contentDrawablePadding)
        }
        // 设置内容标记
        val contentTag: String? = typedArray?.getString(R.styleable.CltEditView_contentTag)
        if (contentTag != null) {
            setContentTag(contentTag)
        }
        // 设置加载页背景
        val contentBackground: Drawable? = typedArray?.getDrawable(R.styleable.CltEditView_contentBackground)
        if (contentBackground != null) {
            setContentBackground(contentBackground)
        }
        // 设置单位文字
        val unitText: String? = typedArray?.getString(R.styleable.CltEditView_unitText)
        if (unitText != null) {
            setNeedUnit(true)
            setUnitText(unitText)
        } else {
            setNeedUnit(false)
        }
        // 设置单位文字颜色
        val unitTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltEditView_unitTextColor)
        if (unitTextColor != null) {
            setUnitTextColor(unitTextColor)
        }
        // 设置单位文字大小
        val unitTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.CltEditView_unitTextSize, 0)
                ?: 0
        if (unitTextSize > 0) {
            setUnitTextSize(px2sp(unitTextSize))
        }
        // 设置跳转按钮文字
        val jumpText: String? = typedArray?.getString(R.styleable.CltEditView_jumpText)
        if (jumpText != null) {
            setNeedJumpBtn(true)
            setJumpBtnText(jumpText)
        } else {
            setNeedJumpBtn(false)
        }
        // 设置跳转按钮文字颜色
        val jumpTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltEditView_jumpTextColor)
        if (jumpTextColor != null) {
            setJumpBtnTextColor(jumpTextColor)
        }
        // 设置跳转按钮文字大小
        val jumpTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.CltEditView_jumpTextSize, 0)
                ?: 0
        if (jumpTextSize > 0) {
            setJumpBtnTextSize(px2sp(jumpTextSize))
        }
        // 设置内容右侧图标
        val jumpBackground: Drawable? = typedArray?.getDrawable(R.styleable.CltEditView_jumpBackground)
        if (jumpBackground != null) {
            setJumpBackground(jumpBackground)
        }
        // 设置是否只读
        setReadOnly(typedArray?.getBoolean(R.styleable.CltEditView_isReadOnly, false) ?: false)
        // 设置输入类型
        setEditInputType(typedArray?.getInt(R.styleable.CltEditView_inputType, TYPE_TEXT)
                ?: TYPE_TEXT)
        val max: Int = typedArray?.getInt(R.styleable.CltEditView_maxLength, -1) ?: -1
        if (max >= 0){
            setMaxLength(max)
        }

        if (typedArray != null) {
            typedArray.recycle()
        }
    }

    private fun setListeners() {
        mContentEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                mTextWatcher?.beforeTextChanged(s, start, count, after)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (mInputType == TYPE_NUMBER_DECIMAL) {
                    val index = getContentText().indexOf(".")
                    val lastIndex = getContentText().lastIndexOf(".")
                    if (index == 0) {
                        // 第一位输入点则默认头部加0
                        setContentText("0.")
                        mContentEdit.setSelection(getContentText().length)
                    } else if (index != lastIndex) {
                        // 输入了多个小数点只保留第一个
                        val str = getContentText().substring(0, lastIndex)
                        setContentText(str)
                        mContentEdit.setSelection(str.length)
                    }
                }
                mTextWatcher?.onTextChanged(s, start, before, count)
            }

            override fun afterTextChanged(s: Editable?) {
                mTextWatcher?.afterTextChanged(s)
            }
        })

    }

    /** 设置必填图片资源[resId] */
    fun setRequiredImg(@DrawableRes resId: Int) {
        mRequiredImg.setImageResource(resId)
    }

    /** 设置必填图片[bitmap] */
    fun setRequiredImg(bitmap: Bitmap) {
        mRequiredImg.setImageBitmap(bitmap)
    }

    /** 设置必填图片[drawable] */
    fun setRequiredImg(drawable: Drawable) {
        mRequiredImg.setImageDrawable(drawable)
    }

    /** 设置必填图片显隐[visibility] */
    fun setRequiredVisibility(visibility: Int) {
        mRequiredImg.visibility = visibility
    }

    /** 获取必填图片是否显示 */
    fun isRequired(isShow: Boolean): Boolean = mRequiredImg.visibility == View.VISIBLE

    /** 设置标题文字[title] */
    fun setTitleText(title: String) {
        mTitleTv.setText(title)
    }

    /** 设置标题文字资源[resId] */
    fun setTitleText(@StringRes resId: Int) {
        mTitleTv.setText(resId)
    }

    /** 获取标题文字 */
    fun getTitleText(): String = mTitleTv.text.toString()

    /** 设置标题文字颜色[color] */
    fun setTitleTextColor(@ColorInt color: Int) {
        mTitleTv.setTextColor(color)
    }

    /** 设置标题文字颜色[color] */
    fun setTitleTextColorRes(@ColorRes color: Int) {
        mTitleTv.setTextColor(getColorCompat(color))
    }

    /** 设置标题文字颜色[color] */
    fun setTitleTextColor(color: ColorStateList) {
        mTitleTv.setTextColor(color)
    }

    /** 设置标题文字大小[sp] */
    fun setTitleTextSize(sp: Float) {
        mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置标题控件宽度 */
    fun setTitleWidth(px: Int) {
        mTitleTv.layoutParams.width = px
    }

    /** 设置标题背景[resId] */
    fun setTitleBackgroundRes(@DrawableRes resId: Int) {
        mTitleTv.setBackgroundResource(resId)
    }

    /** 设置标题背景[color] */
    fun setTitleBackgroundColor(@ColorInt color: Int) {
        mTitleTv.setBackgroundColor(color)
    }

    /** 设置标题背景[color] */
    fun setTitleBackgroundColorRes(@ColorRes color: Int) {
        mTitleTv.setBackgroundColor(getColorCompat(color))
    }

    /** 设置标题背景[drawable] */
    fun setTitleBackground(drawable: Drawable) {
        mTitleTv.background = drawable
    }

    /** 设置内容文字[content] */
    fun setContentText(content: String) {
        mContentEdit.setText(content)
    }

    /** 设置内容文字资源[resId] */
    fun setContentText(@StringRes resId: Int) {
        mContentEdit.setText(resId)
    }

    /** 获取内容文字 */
    fun getContentText(): String = mContentEdit.text.toString()

    /** 设置内容标记[tag] */
    fun setContentTag(tag: String) {
        mContentTag = tag
    }

    /** 获取内容标记 */
    fun getContentTag(): String = mContentTag

    /** 设置内容文字颜色[color] */
    fun setContentTextColor(@ColorInt color: Int) {
        mContentEdit.setTextColor(color)
    }

    /** 设置内容文字颜色[color] */
    fun setContentTextColorRes(@ColorRes color: Int) {
        mContentEdit.setTextColor(getColorCompat(color))
    }

    /** 设置内容文字颜色[color] */
    fun setContentTextColor(color: ColorStateList) {
        mContentEdit.setTextColor(color)
    }

    /** 设置内容文字大小[sp] */
    fun setContentTextSize(sp: Float) {
        mContentEdit.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置内容提示语资源[resId] */
    fun setContentHint(@StringRes resId: Int) {
        mContentEdit.setHint(resId)
    }

    /** 设置内容提示语[hint] */
    fun setContentHint(hint: String) {
        mContentEdit.hint = hint
    }

    /** 获取内容提示语 */
    fun getContentHint(): String = mContentEdit.hint.toString()

    /** 设置内容提示语颜色[color] */
    fun setContentHintColor(@ColorInt color: Int) {
        mContentEdit.setHintTextColor(color)
    }

    /** 设置内容提示语颜色[color] */
    fun setContentHintColorRes(@ColorRes color: Int) {
        mContentEdit.setHintTextColor(getColorCompat(color))
    }

    /** 设置内容提示语颜色[color] */
    fun setContentHintColor(color: ColorStateList) {
        mContentEdit.setHintTextColor(color)
    }

    /** 设置内容右侧图标资源[resId] */
    fun setContentDrawableEnd(@DrawableRes resId: Int) {
        mContentEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0)
    }

    /** 设置内容右侧图标[drawable] */
    fun setContentDrawableEnd(drawable: Drawable) {
        mContentEdit.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }

    /** 设置内容左侧图标资源[resId] */
    fun setContentDrawableStart(@DrawableRes resId: Int) {
        mContentEdit.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0)
    }

    /** 设置内容左侧图标[drawable] */
    fun setContentDrawableStart(drawable: Drawable) {
        mContentEdit.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }

    /** 设置内容图标间距[pad] */
    fun setContentDrawablePadding(pad: Int) {
        mContentEdit.compoundDrawablePadding = pad
    }

    /** 设置内容编辑文字监听[watcher] */
    fun setContentTextChangedListener(watcher: TextWatcher?) {
        mTextWatcher = watcher
    }

    /** 设置内容背景[drawable] */
    fun setContentBackground(drawable: Drawable) {
        mContentEdit.background = drawable
    }

    /** 设置内容背景[color] */
    fun setContentBackground(@ColorInt color: Int) {
        mContentEdit.setBackgroundColor(color)
    }

    /** 设置内容背景[resId] */
    fun setContentBackgroundRes(@DrawableRes resId: Int) {
        mContentEdit.setBackgroundResource(resId)
    }

    /** 设置是否需要单位[isNeed] */
    fun setNeedUnit(isNeed: Boolean) {
        mUnitTv.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 是否显示单位 */
    fun isShowUnit(): Boolean = mUnitTv.visibility == View.VISIBLE

    /** 设置单位文字[unit] */
    fun setUnitText(unit: String) {
        mUnitTv.setText(unit)
    }

    /** 设置单位文字资源[resId] */
    fun setUnitText(@StringRes resId: Int) {
        mUnitTv.setText(resId)
    }

    /** 获取单位文字 */
    fun getUnitText(): String = mUnitTv.text.toString()

    /** 设置单位文字颜色[color] */
    fun setUnitTextColor(@ColorInt color: Int) {
        mUnitTv.setTextColor(color)
    }

    /** 设置单位文字颜色[color] */
    fun setUnitTextColorRes(@ColorRes color: Int) {
        mUnitTv.setTextColor(getColorCompat(color))
    }

    /** 设置单位文字颜色[color] */
    fun setUnitTextColor(color: ColorStateList) {
        mUnitTv.setTextColor(color)
    }

    /** 设置单位文字大小[sp] */
    fun setUnitTextSize(sp: Float) {
        mUnitTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置是否需要跳转按钮[isNeed] */
    fun setNeedJumpBtn(isNeed: Boolean) {
        mJumpBtn.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 是否显示跳转按钮 */
    fun isShowJumpBtn(): Boolean = mJumpBtn.visibility == View.VISIBLE

    /** 设置跳转按钮文字[unit] */
    fun setJumpBtnText(unit: String) {
        mJumpBtn.setText(unit)
    }

    /** 设置跳转按钮文字资源[resId] */
    fun setJumpBtnText(@StringRes resId: Int) {
        mJumpBtn.setText(resId)
    }

    /** 获取跳转按钮文字 */
    fun getJumpBtnText(): String = mJumpBtn.text.toString()

    /** 设置跳转按钮文字颜色[color] */
    fun setJumpBtnTextColor(@ColorInt color: Int) {
        mJumpBtn.setTextColor(color)
    }

    /** 设置跳转按钮文字颜色[color] */
    fun setJumpBtnTextColorRes(@ColorRes color: Int) {
        mJumpBtn.setTextColor(getColorCompat(color))
    }

    /** 设置跳转按钮文字颜色[color] */
    fun setJumpBtnTextColor(color: ColorStateList) {
        mJumpBtn.setTextColor(color)
    }

    /** 设置跳转按钮文字大小[sp] */
    fun setJumpBtnTextSize(sp: Float) {
        mJumpBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置跳转按钮背景[resId] */
    fun setJumpBackgroundRes(@DrawableRes resId: Int) {
        mJumpBtn.setBackgroundResource(resId)
    }

    /** 设置跳转按钮背景[color] */
    fun setJumpBackgroundColor(@ColorInt color: Int) {
        mJumpBtn.setBackgroundColor(color)
    }

    /** 设置跳转按钮背景[color] */
    fun setJumpBackgroundColorRes(@ColorRes color: Int) {
        mJumpBtn.setBackgroundColor(getColorCompat(color))
    }

    /** 设置跳转按钮背景[drawable] */
    fun setJumpBackground(drawable: Drawable) {
        mJumpBtn.background = drawable
    }

    /** 设置跳转按钮监听器[listener] */
    fun setOnJumpClickListener(listener: ((view: View) -> Unit)?) {
        mJumpBtn.setOnClickListener(listener)
    }

    /** 设置跳转按钮监听器[listener] */
    fun setOnJumpClickListener(listener: View.OnClickListener?) {
        mJumpBtn.setOnClickListener(listener)
    }

    /** 设置是否只读[isReadOnly] */
    fun setReadOnly(isReadOnly: Boolean) {
        mContentEdit.isEnabled = !isReadOnly
        mJumpBtn.isEnabled = !isReadOnly
    }

    /** 是否只读 */
    fun isReadOnly(): Boolean = !mContentEdit.isEnabled && !mJumpBtn.isEnabled

    /** 设置输入类型[type] */
    fun setEditInputType(@EditInputType type: Int) {
        mInputType = type
        if (type == TYPE_ID_CARD) {
            // 输入身份证号
            mContentEdit.inputType = InputType.TYPE_CLASS_NUMBER
            mContentEdit.keyListener = DigitsKeyListener.getInstance("1234567890xX")
            mContentEdit.transformationMethod = UpperCaseTransformation()
        } else if (type == TYPE_PHONE) {
            // 输入手机号
            mContentEdit.inputType = InputType.TYPE_CLASS_PHONE
        } else if (type == TYPE_NUMBER) {
            // 输入数字
            mContentEdit.inputType = InputType.TYPE_CLASS_NUMBER
        } else if (type == TYPE_NUMBER_DECIMAL) {
            // 输入小数
            mContentEdit.inputType = InputType.TYPE_CLASS_NUMBER
            mContentEdit.keyListener = DigitsKeyListener.getInstance("1234567890.")
        } else if (type == TYPE_NUMBER_DECIMAL) {
            // 输入国外证件号
            mContentEdit.inputType = InputType.TYPE_CLASS_NUMBER
            mContentEdit.keyListener = DigitsKeyListener.getInstance("1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
            mContentEdit.transformationMethod = UpperCaseTransformation()
        }
    }

    /** 设置文本最大长度[max] */
    fun setMaxLength(max: Int) {
        mContentEdit.filters = arrayOf(InputFilter.LengthFilter(max))
    }

    /** 所有字符转大写 */
    private class UpperCaseTransformation : ReplacementTransformationMethod() {
        override fun getOriginal(): CharArray = charArrayOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z')
        override fun getReplacement(): CharArray = charArrayOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
    }

}