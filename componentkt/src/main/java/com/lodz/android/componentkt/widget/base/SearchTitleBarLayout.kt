package com.lodz.android.componentkt.widget.base

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.*
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.lodz.android.componentkt.R
import com.lodz.android.componentkt.base.application.BaseApplication
import com.lodz.android.componentkt.base.application.config.TitleBarLayoutConfig
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.px2dp
import com.lodz.android.corekt.anko.px2sp

/**
 * 搜索标题栏
 * Created by zhouL on 2018/7/18.
 */
class SearchTitleBarLayout : FrameLayout {

    /** 标题栏配置 */
    private var mConfig = TitleBarLayoutConfig()

    /** 返回按钮布局 */
    private val mBackLayout: LinearLayout by lazy {
        findViewById(R.id.back_layout) as LinearLayout
    }
    /** 返回按钮 */
    private val mBackBtn: TextView by lazy {
        findViewById(R.id.back_btn) as TextView
    }
    /** 输入框布局 */
    private val mInputLayout: ViewGroup by lazy {
        findViewById(R.id.input_layout) as ViewGroup
    }
    /** 输入框 */
    private val mInputEdit: EditText by lazy {
        findViewById(R.id.input_edit) as EditText
    }
    /** 扩展区清空按钮布局 */
    private val mClearBtn: ImageView by lazy {
        findViewById(R.id.clear_btn) as ImageView
    }
    /** 竖线 */
    private val mVerticalLineView: View by lazy {
        findViewById(R.id.vertical_line) as View
    }
    /** 返回按钮 */
    private val mSearchBtn: ImageView by lazy {
        findViewById(R.id.search_btn) as ImageView
    }
    /** 分割线 */
    private val mDivideLineView: View by lazy {
        findViewById(R.id.divide_line) as View
    }

    /** 是否需要清空按钮 */
    private var isNeedCleanBtn = true
    /** 输入框监听 */
    private var mTextWatcher: TextWatcher? = null
    /** 清空按钮点击回调 */
    private var mCleanClickListener: ((View) -> Unit)? = null

    constructor(context: Context) : super(context) {
        initialization(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialization(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialization(attrs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initialization(attrs)
    }

    fun initialization(attrs: AttributeSet?) {
        if (BaseApplication.get() != null) {
            mConfig = BaseApplication.get()!!.getBaseLayoutConfig().getTitleBarLayoutConfig()
        }
        LayoutInflater.from(context).inflate(R.layout.componentkt_view_search_title, this)
        configLayout(attrs)
        setListeners()
    }

    private fun configLayout(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchTitleBarLayout)
        }

        // 返回按钮
        needBackButton(typedArray?.getBoolean(R.styleable.SearchTitleBarLayout_isNeedBackBtn, mConfig.isNeedBackBtn)
                ?: mConfig.isNeedBackBtn)

        // 返回按钮图标
        val backDrawable: Drawable? = typedArray?.getDrawable(R.styleable.SearchTitleBarLayout_backDrawable)
        if (backDrawable != null) {
            mBackBtn.setCompoundDrawablesWithIntrinsicBounds(backDrawable, null, null, null)
        } else if (mConfig.backBtnResId != 0) {
            mBackBtn.setCompoundDrawablesWithIntrinsicBounds(mConfig.backBtnResId, 0, 0, 0)
        }

        // 返回按钮文字
        val backText: String = typedArray?.getString(R.styleable.SearchTitleBarLayout_backText)
                ?: ""
        if (backText.isNotEmpty()) {
            setBackBtnName(backText)
        } else if (mConfig.backBtnText.isNotEmpty()) {
            setBackBtnName(mConfig.backBtnText)
        }

        // 返回按钮文字颜色
        val backTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.SearchTitleBarLayout_backTextColor)
        if (backTextColor != null) {
            setBackBtnTextColor(backTextColor)
        } else if (mConfig.backBtnTextColor != 0) {
            setBackBtnTextColor(mConfig.backBtnTextColor)
        }

        // 返回按钮文字大小
        val backTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.SearchTitleBarLayout_backTextSize, 0)
                ?: 0
        if (backTextSize != 0) {
            setBackBtnTextSize(px2sp(backTextSize))
        } else if (mConfig.backBtnTextSize != 0) {
            setBackBtnTextSize(mConfig.backBtnTextSize.toFloat())
        }

        // 是否显示分割线
        val isShowDivideLine: Boolean = typedArray?.getBoolean(R.styleable.SearchTitleBarLayout_isShowDivideLine, mConfig.isShowDivideLine)
                ?: mConfig.isShowDivideLine
        needDivideLine(isShowDivideLine)

        // 分割线背景色
        val divideLineDrawable: Drawable? = typedArray?.getDrawable(R.styleable.SearchTitleBarLayout_divideLineColor)
        if (divideLineDrawable != null) {
            setDivideLineDrawable(divideLineDrawable)
        } else if (mConfig.divideLineColor != 0) {
            setDivideLineColor(mConfig.divideLineColor)
        }

        // 分割线高度
        val divideLineHeight: Int = typedArray?.getDimensionPixelSize(R.styleable.SearchTitleBarLayout_divideLineHeight, 0)
                ?: 0
        if (divideLineHeight > 0) {
            setDivideLineHeight(px2dp(divideLineHeight).toInt())
        } else if (mConfig.divideLineHeightDp > 0) {
            setDivideLineHeight(mConfig.divideLineHeightDp)
        }

        // 标题背景
        val drawableBackground: Drawable? = typedArray?.getDrawable(R.styleable.SearchTitleBarLayout_titleBarBackground)
        if (drawableBackground != null) {
            background = drawableBackground
        } else if (mConfig.backgroundResId != 0) {
            setBackgroundResource(mConfig.backgroundResId)
        } else if (mConfig.backgroundColor != 0) {
            setBackgroundColor(context.getColorCompat(mConfig.backgroundColor))
        } else {
            setBackgroundColor(context.getColorCompat(android.R.color.holo_blue_light))
        }

        // 是否需要阴影
        val isNeedElevation: Boolean = typedArray?.getBoolean(R.styleable.SearchTitleBarLayout_isNeedElevation, mConfig.isNeedElevation)
                ?: mConfig.isNeedElevation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isNeedElevation) {
            val elevationVale: Int = typedArray?.getDimensionPixelSize(R.styleable.SearchTitleBarLayout_elevationVale, 0)
                    ?: 0
            elevation = if (elevationVale != 0) elevationVale.toFloat() else mConfig.elevationVale
        }

        // 是否需要清空按钮
        setNeedCleanBtn(typedArray?.getBoolean(R.styleable.SearchTitleBarLayout_isNeedCleanBtn, true)
                ?: true)

        // 搜索按钮图标
        val searchDrawable: Drawable? = typedArray?.getDrawable(R.styleable.SearchTitleBarLayout_searchDrawable)
        if (searchDrawable != null) {
            setSearchIcon(searchDrawable)
        }

        // 清除按钮图标
        val cleanDrawable: Drawable? = typedArray?.getDrawable(R.styleable.SearchTitleBarLayout_cleanDrawable)
        if (cleanDrawable != null) {
            setCleanIcon(cleanDrawable)
        }

        // 输入框背景
        val inputBackground: Drawable? = typedArray?.getDrawable(R.styleable.SearchTitleBarLayout_inputBackground)
        if (inputBackground != null) {
            setInputBackground(inputBackground)
        }

        // 输入框提示语
        val hintText: String = typedArray?.getString(R.styleable.SearchTitleBarLayout_inputHint)
                ?: ""
        if (hintText.isNotEmpty()) {
            setInputHint(hintText)
        }

        // 输入框提示语颜色
        val hintTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.SearchTitleBarLayout_inputHintTextColor)
        if (hintTextColor != null) {
            setInputHintTextColor(hintTextColor)
        }

        // 输入框内容文字
        val inputText: String = typedArray?.getString(R.styleable.SearchTitleBarLayout_inputText)
                ?: ""
        if (inputText.isNotEmpty()) {
            setInputText(inputText)
        }

        // 输入框内容文字颜色
        val inputTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.SearchTitleBarLayout_inputTextColor)
        if (inputTextColor != null) {
            setInputTextColor(inputTextColor)
        }

        // 标题文字大小
        val inputTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.SearchTitleBarLayout_inputTextSize, 0)
                ?: 0
        if (inputTextSize != 0) {
            setInputTextSize(px2sp(inputTextSize))
        }

        // 是否显示竖线
        setShowVerticalLine(typedArray?.getBoolean(R.styleable.SearchTitleBarLayout_isShowVerticalLine, true)
                ?: true)

        // 竖线背景
        val verticalLineBackground: Drawable? = typedArray?.getDrawable(R.styleable.SearchTitleBarLayout_verticalLineBackground)
        if (verticalLineBackground != null) {
            setVerticalLineBackground(verticalLineBackground)
        }

        if (typedArray != null) {
            typedArray.recycle()
        }
    }

    /** 设置监听器 */
    private fun setListeners() {
        // 输入监听
        mInputEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mTextWatcher?.afterTextChanged(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                mTextWatcher?.beforeTextChanged(s, start, count, after)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mTextWatcher?.onTextChanged(s, start, before, count)
                if (!isNeedCleanBtn) {
                    return
                }
                mClearBtn.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        })

        // 清空按钮
        mClearBtn.setOnClickListener {
            mInputEdit.setText("")
            mCleanClickListener?.invoke(it)
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
    fun setOnBackBtnClickListener(listener: (View) -> Unit) {
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
        mBackBtn.setTextColor(context.getColorCompat(colorRes))
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
        mDivideLineView.setBackgroundColor(context.getColorCompat(colorRes))
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
        layoutParams.height = dp2px(dp).toInt()
        mDivideLineView.layoutParams = layoutParams
    }

    /** 设置搜索按钮监听[listener] */
    fun setOnSearchClickListener(listener: (View) -> Unit) {
        mSearchBtn.setOnClickListener(listener)
    }

    /** 设置搜索图标[drawable] */
    fun setSearchIcon(drawable: Drawable) {
        mSearchBtn.setImageDrawable(drawable)
    }

    /** 设置搜索图标资源[resId] */
    fun setSearchIcon(@DrawableRes resId: Int) {
        mSearchBtn.setImageResource(resId)
    }

    /** 设置是否需要[isNeed]清空按钮 */
    fun setNeedCleanBtn(isNeed: Boolean) {
        isNeedCleanBtn = isNeed
    }

    /** 设置清空图标[drawable] */
    fun setCleanIcon(drawable: Drawable) {
        setNeedCleanBtn(true)
        mClearBtn.setImageDrawable(drawable)
    }

    /** 设置清空图标资源[resId] */
    fun setCleanIcon(@DrawableRes resId: Int) {
        setNeedCleanBtn(true)
        mClearBtn.setImageResource(resId)
    }

    /** 设置清空按钮点击监听[listener] */
    fun setOnCleanClickListener(listener: (View) -> Unit) {
        mCleanClickListener = listener
    }

    /** 设置文本输入监听器[watcher] */
    fun setTextWatcher(watcher: TextWatcher) {
        mTextWatcher = watcher
    }

    /** 获取输入框内容 */
    fun getInputText() = mInputEdit.getText().toString()

    /** 设置输入框背景[drawable] */
    fun setInputBackground(drawable: Drawable) {
        mInputLayout.background = drawable
    }

    /** 设置输入框背景资源[resId] */
    fun setInputBackgroundResource(@DrawableRes resId: Int) {
        mInputLayout.setBackgroundResource(resId)
    }

    /** 设置输入框背景颜色[color] */
    fun setInputBackgroundColor(@ColorInt color: Int) {
        mInputLayout.setBackgroundColor(color)
    }

    /** 设置输入框提示语[hint] */
    fun setInputHint(hint: String) {
        mInputEdit.hint = hint
    }

    /** 设置输入框提示语资源[resId] */
    fun setInputHint(@StringRes resId: Int) {
        mInputEdit.setHint(resId)
    }

    /** 设置输入框提示语颜色资源[colorRes] */
    fun setInputHintTextColor(@ColorRes colorRes: Int) {
        mInputEdit.setHintTextColor(context.getColorCompat(colorRes))
    }

    /** 设置输入框提示语颜色[color] */
    fun setInputHintTextColorInt(@ColorInt color: Int) {
        mInputEdit.setHintTextColor(color)
    }

    /** 设置输入框提示语颜色[colorStateList] */
    fun setInputHintTextColor(colorStateList: ColorStateList) {
        mInputEdit.setHintTextColor(colorStateList)
    }

    /** 设置输入框文字[text] */
    fun setInputText(text: String) {
        mInputEdit.setText(text)
    }

    /** 设置输入框文字资源[resId] */
    fun setInputText(@StringRes resId: Int) {
        mInputEdit.setText(resId)
    }

    /** 设置输入框文字颜色资源[colorRes] */
    fun setInputTextColor(@ColorRes colorRes: Int) {
        mInputEdit.setTextColor(context.getColorCompat(colorRes))
    }

    /** 设置输入框文字颜色[color] */
    fun setInputTextColorInt(@ColorInt color: Int) {
        mInputEdit.setTextColor(color)
    }

    /** 设置输入框文字颜色[colorStateList] */
    fun setInputTextColor(colorStateList: ColorStateList) {
        mInputEdit.setTextColor(colorStateList)
    }

    /** 设置输入框文字大小[sp] */
    fun setInputTextSize(sp: Float) {
        mInputEdit.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 获取输入框控件 */
    fun getInputEdit() = mInputEdit

    /** 是否显示[isShow]竖线 */
    fun setShowVerticalLine(isShow: Boolean) {
        mVerticalLineView.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    /** 设置竖线背景[drawable] */
    fun setVerticalLineBackground(drawable: Drawable) {
        mVerticalLineView.background = drawable
    }

    /** 设置竖线背景资源[resId] */
    fun setVerticalLineBackgroundResource(@DrawableRes resId: Int) {
        mVerticalLineView.setBackgroundResource(resId)
    }

    /** 设置竖线背景[color] */
    fun setVerticalLineBackgroundColor(@ColorInt color: Int) {
        mVerticalLineView.setBackgroundColor(color)
    }
}