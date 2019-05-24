package com.lodz.android.pandora.widget.collect.radio

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
import androidx.annotation.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.anko.*
import com.lodz.android.pandora.R

/**
 * 采集页单选组
 * @author zhouL
 * @date 2019/5/23
 */
class CltRadioGroup : FrameLayout {

    @IntDef(TYPE_SINGLE, TYPE_MULTIPLE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class RadioType

    companion object {
        /** 单选 */
        const val TYPE_SINGLE = 0
        /** 多选 */
        const val TYPE_MULTIPLE = 1
    }

    /** 必填图标 */
    private val mRequiredImg by bindView<ImageView>(R.id.required_img)
    /** 标题控件 */
    private val mTitleTv by bindView<TextView>(R.id.title_tv)
    /** 单选列表 */
    private val mRadioRv by bindView<RecyclerView>(R.id.radio_rv)
    /** 适配器 */
    private lateinit var mAdapter: RadioGroupAdapter
    /** 布局管理器 */
    private lateinit var mLayoutManager: GridLayoutManager

    /** 单选按钮图片 */
    private var mBtnSrc: Drawable? = null
    /** 单选按钮图片资源id */
    private var mBtnSrcResId: Int = 0
    /** 单选文字颜色 */
    private var mRadioTextColor: ColorStateList? = null
    /** 单选文字大小 */
    private var mRadioTextSizeSp: Float = 0f
    /** 选择类型 */
    private var mRadioType: Int = TYPE_SINGLE
    /** 行数 */
    private var mSpanCount: Int = 1
    /** 文字距离图标的左侧间距 */
    private var mMarginStartDp = 0
    /** 布局方向 */
    private var mRadioGravity = 0
    /** 单选项图片宽高 */
    private var mRadioBtnSquareDp = 0
    /** 是否只读模式 */
    private var isReadOnly = false

    /** 数据 */
    private var mList: MutableList<RadioableWrapper> = ArrayList()
    /** item长按 */
    private var mOnCheckedChangeListener: ((radioable: Radioable, isSelected: Boolean) -> Unit)? = null

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
        initRecyclerView()
        configLayout(attrs)
        setListeners()
    }

    private fun findViews() {
        LayoutInflater.from(context).inflate(R.layout.pandora_view_clt_radio_group, this)
    }

    private fun configLayout(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.CltRadioGroup)
        }

        // 设置必填图片是否显示
        val visibility = typedArray?.getInt(R.styleable.CltRadioGroup_requiredVisibility, 0) ?: 0
        if (visibility == 1) {
            setRequiredVisibility(View.INVISIBLE)
        } else if (visibility == 2) {
            setRequiredVisibility(View.GONE)
        } else {
            setRequiredVisibility(View.VISIBLE)
        }

        // 设置必填图片
        val src: Drawable? = typedArray?.getDrawable(R.styleable.CltRadioGroup_requiredSrc)
        if (src != null) {
            setRequiredImg(src)
        }
        // 设置标题文字
        val titleText: String? = typedArray?.getString(R.styleable.CltRadioGroup_titleText)
        if (titleText != null) {
            setTitleText(titleText)
        }
        // 设置标题文字颜色
        val titleTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltRadioGroup_titleTextColor)
        if (titleTextColor != null) {
            setTitleTextColor(titleTextColor)
        }
        // 设置标题文字大小
        val titleTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.CltRadioGroup_titleTextSize, 0)
                ?: 0
        if (titleTextSize > 0) {
            setTitleTextSize(px2spRF(titleTextSize))
        }
        // 设置标题控件宽度
        val titleWidth: Int = typedArray?.getDimensionPixelSize(R.styleable.CltRadioGroup_titleWidth, 0)
                ?: 0
        if (titleWidth > 0) {
            setTitleWidth(titleWidth)
        }
        // 设置标题背景
        val titleBackground: Drawable? = typedArray?.getDrawable(R.styleable.CltRadioGroup_titleBackground)
        if (titleBackground != null) {
            setTitleBackground(titleBackground)
        }
        // 设置是否只读
        setReadOnly(typedArray?.getBoolean(R.styleable.CltRadioGroup_isReadOnly, false) ?: false)

        // 设置单选按钮图片
        val btnSrc: Int = typedArray?.getResourceId(R.styleable.CltRadioGroup_radioBtnSrc, 0) ?: 0
        if (btnSrc != 0) {
            setRadioBtnSrc(btnSrc)
        }
        // 设置单选项图片宽高
        val radioBtnSquare: Int = typedArray?.getDimensionPixelSize(R.styleable.CltRadioGroup_radioBtnSquare, 0)
                ?: 0
        if (radioBtnSquare > 0) {
            setRadioBtnSquare(px2dp(radioBtnSquare))
        }

        // 设置单选文字颜色
        val radioTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltRadioGroup_radioTextColor)
        if (radioTextColor != null) {
            setRadioTextColor(radioTextColor)
        }
        // 设置单选文字大小
        val radioTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.CltRadioGroup_radioTextSize, 0)
                ?: 0
        if (radioTextSize > 0) {
            setRadioTextSize(px2spRF(radioTextSize))
        }
        // 设置选择类型
        setRadioType(typedArray?.getInt(R.styleable.CltRadioGroup_radioType, TYPE_SINGLE)
                ?: TYPE_SINGLE)
        // 设置行数
        setSpanCount(typedArray?.getInt(R.styleable.CltRadioGroup_spanCount, 1)
                ?: 1)

        // 设置文字距离图标的左侧间距
        val marginStart: Int = typedArray?.getDimensionPixelSize(R.styleable.CltRadioGroup_radioTextMarginStart, 0)
                ?: 0
        if (marginStart > 0) {
            setRadioMarginStart(px2dp(marginStart))
        }

        val gravity = typedArray?.getInt(R.styleable.CltRadioGroup_radioGravity, 0) ?: 0
        if (gravity == 1) {
            setRadioGravity(Gravity.START or Gravity.CENTER_VERTICAL)
        } else if (gravity == 2) {
            setRadioGravity(Gravity.END or Gravity.CENTER_VERTICAL)
        }

        if (typedArray != null) {
            typedArray.recycle()
        }
    }

    private fun initRecyclerView() {
        mLayoutManager = GridLayoutManager(getContext(), mSpanCount)
        mLayoutManager.orientation = RecyclerView.VERTICAL
        mAdapter = RadioGroupAdapter(getContext())
        mRadioRv.layoutManager = mLayoutManager
        mRadioRv.setHasFixedSize(true)
        mRadioRv.isNestedScrollingEnabled = false
        mRadioRv.adapter = mAdapter
    }

    private fun setListeners() {
        mAdapter.setOnItemClickListener { viewHolder, item, position ->
            if (isReadOnly) {
                return@setOnItemClickListener
            }

            if (mRadioType == TYPE_SINGLE) {// 单选
                for (i in mList.indices) {
                    mList.get(i).isSelected = if (item.radioable.getIdTag().equals(mList.get(i).getIdTag())) {
                        if (!mList.get(i).isSelected) {// 原来未选中 现在选中了则回调监听器
                            mOnCheckedChangeListener?.invoke(item, true)
                        }
                        true
                    } else {
                        false
                    }
                }
            }
            if (mRadioType == TYPE_MULTIPLE) {// 多选
                for (i in mList.indices) {
                    if (item.radioable.getIdTag().equals(mList.get(i).getIdTag())) {
                        mList.get(i).isSelected = !mList.get(i).isSelected
                        mOnCheckedChangeListener?.invoke(item, mList.get(i).isSelected)
                    }

                }
            }
            mAdapter.setData(mList)
            mAdapter.notifyDataSetChanged()
        }
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

    /** 设置是否只读[isReadOnly] */
    fun setReadOnly(isReadOnly: Boolean) {
        this.isReadOnly = isReadOnly
    }

    /** 是否只读 */
    fun isReadOnly(): Boolean = isReadOnly

    /** 设置单选按钮图片[src] */
    fun setRadioBtnSrc(src: Drawable) {
        mBtnSrc = src
        mAdapter.setBtnSrc(mBtnSrc)
        mAdapter.notifyDataSetChanged()
    }

    /** 设置单选按钮图片[resId] */
    fun setRadioBtnSrc(@DrawableRes resId: Int) {
        mBtnSrcResId = resId
        mAdapter.setBtnSrcResId(mBtnSrcResId)
        mAdapter.notifyDataSetChanged()
    }

    /** 设置单选按钮图片[bitmap] */
    fun setRadioBtnSrc(bitmap: Bitmap) {
        mBtnSrc = createBitmapDrawable(bitmap)
        mAdapter.setBtnSrc(mBtnSrc)
        mAdapter.notifyDataSetChanged()
    }

    /** 设置单选文字颜色[textColor] */
    fun setRadioTextColor(textColor: ColorStateList) {
        mRadioTextColor = textColor
        mAdapter.setRadioTextColor(mRadioTextColor)
        mAdapter.notifyDataSetChanged()
    }

    /** 设置单选文字颜色[color] */
    fun setRadioTextColor(@ColorInt color: Int) {
        mRadioTextColor = ColorStateList.valueOf(color)
        mAdapter.setRadioTextColor(mRadioTextColor)
        mAdapter.notifyDataSetChanged()
    }

    /** 设置单选文字颜色[color] */
    fun setRadioTextColorRes(@ColorRes color: Int) {
        mRadioTextColor = ColorStateList.valueOf(getColorCompat(color))
        mAdapter.setRadioTextColor(mRadioTextColor)
        mAdapter.notifyDataSetChanged()
    }

    /** 设置单选文字大小[sp] */
    fun setRadioTextSize(sp: Float) {
        mRadioTextSizeSp = sp
        mAdapter.setRadioTextSize(mRadioTextSizeSp)
        mAdapter.notifyDataSetChanged()
    }

    /** 设置选择类型[type] */
    fun setRadioType(@RadioType type: Int) {
        mRadioType = type
    }

    /** 获取选择类型 */
    fun getRadioType(): Int = mRadioType

    /** 设置行数[count] */
    fun setSpanCount(count: Int) {
        mSpanCount = if (count > 0) count else 1
        mLayoutManager.spanCount = mSpanCount
        mAdapter.notifyDataSetChanged()
    }

    /** 获取行数 */
    fun getSpanCount(): Int = mSpanCount

    /** 设置文字距离图标的左侧间距[dp] */
    fun setRadioMarginStart(dp: Int) {
        mMarginStartDp = dp
        mAdapter.setRadioMarginStart(mMarginStartDp)
        mAdapter.notifyDataSetChanged()
    }

    /** 设置单选项布局方向[gravity] */
    fun setRadioGravity(gravity: Int) {
        mRadioGravity = gravity
        mAdapter.setRadioGravity(mRadioGravity)
        mAdapter.notifyDataSetChanged()
    }

    /** 设置单选项图片宽高[dp] */
    fun setRadioBtnSquare(dp: Int) {
        mRadioBtnSquareDp = dp
        mAdapter.setRadioBtnSquare(mRadioBtnSquareDp)
        mAdapter.notifyDataSetChanged()
    }

    /** 设置数据列表[data] */
    fun setDataList(data: MutableList<Radioable>) {
        mList = setWrapper(data)
        mAdapter.setData(mList)
    }

    /** 设置包装[data] */
    private fun setWrapper(data: MutableList<Radioable>): MutableList<RadioableWrapper> {
        val list: MutableList<RadioableWrapper> = ArrayList()
        data.forEach {
            val item = RadioableWrapper(it)
            item.isSelected = false
            list.add(item)
        }
        return list
    }

    /** 设置单选选中的[id] */
    fun setSelectedId(id: String) {
        for (i in mList.indices) {
            mList.get(i).isSelected = id.equals(mList.get(i).getIdTag())
        }
        mAdapter.setData(mList)
        mAdapter.notifyDataSetChanged()
    }

    /** 设置多选选中的[ids] */
    fun setSelectedId(ids: List<String>) {
        for (i in mList.indices) {
            mList.get(i).isSelected = false
            ids.forEachIndexed { index, id ->
                if (id.equals(mList.get(i).getIdTag())) {
                    mList.get(i).isSelected = true
                }
            }
        }
        mAdapter.setData(mList)
        mAdapter.notifyDataSetChanged()
    }

    /** 获取选中的Id*/
    fun getSelectedId(): List<String> {
        val list = ArrayList<String>()
        for (wrapper in mList) {
            if (wrapper.isSelected) {
                list.add(wrapper.getIdTag())
            }
        }
        return list
    }

    /** 是否已选中 */
    fun isSelectedId(): Boolean {
        mList.forEach {
            if (it.isSelected) {
                return true
            }
        }
        return false
    }

    /** 设置选中变化监听器[radioable]*/
    fun setOnCheckedChangeListener(listener: ((radioable: Radioable, isSelected: Boolean) -> Unit)?) {
        mOnCheckedChangeListener = listener
    }

}