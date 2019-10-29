package com.lodz.android.pandora.widget.search

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.anko.*
import com.lodz.android.pandora.R
import com.lodz.android.pandora.base.application.BaseApplication
import com.lodz.android.pandora.base.application.config.TitleBarLayoutConfig
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import java.util.concurrent.TimeUnit

/**
 * 搜索标题栏
 * Created by zhouL on 2018/7/18.
 */
class SearchTitleBarLayout : FrameLayout {

    /** 标题栏配置 */
    private var mConfig = TitleBarLayoutConfig()

    /** 返回按钮布局 */
    private val mBackLayout by bindView<LinearLayout>(R.id.back_layout)
    /** 返回按钮 */
    private val mBackBtn by bindView<TextView>(R.id.back_btn)
    /** 输入框布局 */
    private val mInputLayout by bindView<ViewGroup>(R.id.input_layout)
    /** 输入框 */
    private val mInputEdit by bindView<EditText>(R.id.input_edit)
    /** 扩展区清空按钮布局 */
    private val mClearBtn by bindView<ImageView>(R.id.clear_btn)
    /** 竖线 */
    private val mVerticalLineView by bindView<View>(R.id.vertical_line)
    /** 搜索按钮 */
    private val mSearchBtn by bindView<ImageView>(R.id.search_btn)
    /** 分割线 */
    private val mDivideLineView by bindView<View>(R.id.divide_line)
    /** 搜索文字按钮 */
    private val mSearchTxBtn by bindView<TextView>(R.id.search_tx_btn)
    /** 搜索联想列表 */
    private val mRecyclerView by bindView<RecyclerView>(R.id.recycler_view)
    /** 搜索联想列表适配器 */
    private lateinit var mAdapter: RecomdListAdapter

    /** 是否需要清空按钮 */
    private var isNeedCleanBtn = true
    /** 输入框监听 */
    private var mTextWatcher: TextWatcher? = null
    /** 清空按钮点击回调 */
    private var mCleanClickListener: ((view: View) -> Unit)? = null
    /** 搜索联想监听器 */
    private var mOnSearchRecomdListener: OnSearchRecomdListener? = null
    /** 输入回调间隔时长 */
    private var mInputDuration: Long = 500
    /** 输入回调间隔单位 */
    private var mInputDurationUnit: TimeUnit = TimeUnit.MILLISECONDS
    /** 是否选择推荐项 */
    private var isSelectedRecomItem = false

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val app = BaseApplication.get()
        if (app != null) {
            mConfig = app.getBaseLayoutConfig().getTitleBarLayoutConfig()
        }
        LayoutInflater.from(context).inflate(R.layout.pandora_view_search_title, this)
        configLayout(attrs)
        initRecyclerView()
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
            setBackBtnTextSize(px2spRF(backTextSize))
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
            setDivideLineHeight(px2dp(divideLineHeight))
        } else if (mConfig.divideLineHeightDp > 0) {
            setDivideLineHeight(mConfig.divideLineHeightDp)
        }

        // 标题背景
        val drawableBackground: Drawable? = typedArray?.getDrawable(R.styleable.SearchTitleBarLayout_titleBarBackground)
        when {
            drawableBackground != null -> background = drawableBackground
            mConfig.backgroundResId != 0 -> setBackgroundResource(mConfig.backgroundResId)
            mConfig.backgroundColor != 0 -> setBackgroundColor(getColorCompat(mConfig.backgroundColor))
            else -> setBackgroundColor(getColorCompat(android.R.color.holo_blue_light))
        }

        // 是否需要阴影
        val isNeedElevation: Boolean = typedArray?.getBoolean(R.styleable.SearchTitleBarLayout_isNeedElevation, mConfig.isNeedElevation)
            ?: mConfig.isNeedElevation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isNeedElevation) {
            val elevationVale: Int = typedArray?.getDimensionPixelSize(R.styleable.SearchTitleBarLayout_elevationVale, 0) ?: 0
            elevation = if (elevationVale != 0) elevationVale.toFloat() else mConfig.elevationVale
        }

        // 是否需要清空按钮
        setNeedCleanBtn(typedArray?.getBoolean(R.styleable.SearchTitleBarLayout_isNeedCleanBtn, true) ?: true)

        // 搜索按钮图标
        val searchDrawable: Drawable? = typedArray?.getDrawable(R.styleable.SearchTitleBarLayout_searchDrawable)
        if (searchDrawable != null) {
            setSearchIcon(searchDrawable)
        }

        // 搜索按钮是否显示
        val searchBtnVisibility = typedArray?.getInt(R.styleable.SearchTitleBarLayout_searchBtnVisibility, 0) ?: 0
        when (searchBtnVisibility) {
            1 -> setSearchBtnVisibility(View.INVISIBLE)
            2 -> setSearchBtnVisibility(View.GONE)
            else -> setSearchBtnVisibility(View.VISIBLE)
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
        val hintText: String = typedArray?.getString(R.styleable.SearchTitleBarLayout_inputHint) ?: ""
        if (hintText.isNotEmpty()) {
            setInputHint(hintText)
        }

        // 输入框提示语颜色
        val hintTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.SearchTitleBarLayout_inputHintTextColor)
        if (hintTextColor != null) {
            setInputHintTextColor(hintTextColor)
        }

        // 输入框内容文字
        val inputText: String = typedArray?.getString(R.styleable.SearchTitleBarLayout_inputText) ?: ""
        if (inputText.isNotEmpty()) {
            setInputText(inputText)
        }

        // 输入框内容文字颜色
        val inputTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.SearchTitleBarLayout_inputTextColor)
        if (inputTextColor != null) {
            setInputTextColor(inputTextColor)
        }

        // 标题文字大小
        val inputTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.SearchTitleBarLayout_inputTextSize, 0) ?: 0
        if (inputTextSize != 0) {
            setInputTextSize(px2spRF(inputTextSize))
        }

        // 是否显示竖线
        setShowVerticalLine(typedArray?.getBoolean(R.styleable.SearchTitleBarLayout_isShowVerticalLine, true) ?: true)

        // 竖线背景
        val verticalLineBackground: Drawable? = typedArray?.getDrawable(R.styleable.SearchTitleBarLayout_verticalLineBackground)
        if (verticalLineBackground != null) {
            setVerticalLineBackground(verticalLineBackground)
        }

        // 搜索按钮文字
        val searchText: String = typedArray?.getString(R.styleable.SearchTitleBarLayout_searchText) ?: ""
        if (searchText.isNotEmpty()) {
            setSearchText(searchText)
        }

        // 搜索按钮文字大小
        val searchTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.SearchTitleBarLayout_searchTextSize, 0) ?: 0
        if (searchTextSize != 0) {
            setSearchTextSize(px2spRF(searchTextSize))
        }

        // 搜索按钮文字颜色
        val searchTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.SearchTitleBarLayout_searchTextColor)
        if (searchTextColor != null) {
            setSearchTextColor(searchTextColor)
        }

        // 搜索按钮文字是否显示
        val searchTextVisibility = typedArray?.getInt(R.styleable.SearchTitleBarLayout_searchTextVisibility, 2) ?: 2
        when (searchTextVisibility) {
            1 -> setSearchTextVisibility(View.INVISIBLE)
            2 -> setSearchTextVisibility(View.GONE)
            else -> setSearchTextVisibility(View.VISIBLE)
        }

        // 是否开启推搜索联想列表
        setOpenRecomdList(typedArray?.getBoolean(R.styleable.SearchTitleBarLayout_isOpenRecomdList, false) ?: false)

        typedArray?.recycle()
    }

    private fun initRecyclerView() {
        mAdapter = RecomdListAdapter(getContext())
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        mRecyclerView.layoutManager = layoutManager
        mAdapter.onAttachedToRecyclerView(mRecyclerView)// 如果使用网格布局请设置此方法
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter
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

        // 联想文字点击
        mAdapter.setOnItemClickListener { viewHolder, item, position ->
            isSelectedRecomItem = true
            setInputText(item.getTitleText())
            setRecomListData(null)
            mOnSearchRecomdListener?.onItemClick(viewHolder, item, position)
        }

        // 联想搜索
        RxUtils.textChanges(mInputEdit, mInputDuration, mInputDurationUnit)
            .compose(RxUtils.ioToMainObservable())
            .subscribe(object : BaseObserver<CharSequence>(){
                override fun onBaseNext(any: CharSequence) {
                    if (!isSelectedRecomItem){
                        mOnSearchRecomdListener?.onInputTextChange(any.toString())
                    }
                    isSelectedRecomItem = false
                }

                override fun onBaseError(e: Throwable) {

                }

            })
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
        mDivideLineView.background = drawable
    }

    /** 设置分割线高度[dp] */
    fun setDivideLineHeight(dp: Int) {
        val layoutParams = mDivideLineView.layoutParams
        layoutParams.height = dp2px(dp)
        mDivideLineView.layoutParams = layoutParams
    }

    /** 设置搜索按钮监听[listener] */
    fun setOnSearchClickListener(listener: (View) -> Unit) {
        mSearchBtn.setOnClickListener(listener)
        mSearchTxBtn.setOnClickListener(listener)
    }

    /** 设置搜索图标[drawable] */
    fun setSearchIcon(drawable: Drawable) {
        mSearchBtn.setImageDrawable(drawable)
    }

    /** 设置搜索图标资源[resId] */
    fun setSearchIcon(@DrawableRes resId: Int) {
        mSearchBtn.setImageResource(resId)
    }

    /** 设置搜索按钮显隐[visibility] */
    fun setSearchBtnVisibility(visibility: Int) {
        mSearchBtn.visibility = visibility
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
    fun setOnCleanClickListener(listener: (view: View) -> Unit) {
        mCleanClickListener = listener
    }

    /** 设置文本输入监听器[watcher] */
    fun setTextWatcher(watcher: TextWatcher) {
        mTextWatcher = watcher
    }

    /** 获取输入框内容 */
    fun getInputText(): String = mInputEdit.text.toString()

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
        mInputEdit.setHintTextColor(getColorCompat(colorRes))
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
        mInputEdit.setSelection(text.length)
    }

    /** 设置输入框文字资源[resId] */
    fun setInputText(@StringRes resId: Int) {
        mInputEdit.setText(resId)
        mInputEdit.setSelection(mInputEdit.text.length)
    }

    /** 设置输入框文字颜色资源[colorRes] */
    fun setInputTextColor(@ColorRes colorRes: Int) {
        mInputEdit.setTextColor(getColorCompat(colorRes))
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
    fun getInputEdit(): EditText = mInputEdit

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

    /** 设置搜索按钮文字[text] */
    fun setSearchText(text: String) {
        mSearchTxBtn.text = text
    }

    /** 设置搜索按钮文字资源[resId] */
    fun setSearchText(@StringRes resId: Int) {
        mSearchTxBtn.setText(resId)
    }

    /** 设置搜索按钮文字大小[sp] */
    fun setSearchTextSize(sp: Float) {
        mSearchTxBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    /** 设置搜索按钮文字颜色资源[colorRes] */
    fun setSearchTextColor(@ColorRes colorRes: Int) {
        mSearchTxBtn.setTextColor(getColorCompat(colorRes))
    }

    /** 设置搜索按钮文字颜色[color] */
    fun setSearchTextColorInt(@ColorInt color: Int) {
        mSearchTxBtn.setTextColor(color)
    }

    /** 设置搜索按钮文字颜色[colorStateList] */
    fun setSearchTextColor(colorStateList: ColorStateList) {
        mSearchTxBtn.setTextColor(colorStateList)
    }

    /** 设置搜索按钮文字显隐[visibility] */
    fun setSearchTextVisibility(visibility: Int) {
        mSearchTxBtn.visibility = visibility
    }

    /** 设置是否开启推搜索联想列表[isOpen] */
    fun setOpenRecomdList(isOpen: Boolean) {
        mRecyclerView.visibility = if (isOpen) View.VISIBLE else View.GONE
    }

    /** 设置联想列表数据[datas] */
    fun setRecomListData(datas: List<RecomdData>?) {
        mAdapter.setData(datas?.toMutableList() ?: ArrayList())
        mAdapter.notifyDataSetChanged()
    }

    /** 设置输入回调间隔，间隔时长[duration]，间隔单位[unit] */
    fun setInputDuration(duration: Long, unit: TimeUnit) {
        mInputDuration = duration
        mInputDurationUnit = unit
    }

    /** 设置搜索联想监听器[listener] */
    fun setOnSearchRecomdListener(listener: OnSearchRecomdListener) {
        mOnSearchRecomdListener = listener
    }

}