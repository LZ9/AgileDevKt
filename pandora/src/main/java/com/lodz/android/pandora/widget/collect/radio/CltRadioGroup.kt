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
    private val mPdrRequiredImg by bindView<ImageView>(R.id.pdr_required_img)
    /** 标题控件 */
    private val mPdrTitleTv by bindView<TextView>(R.id.pdr_title_tv)
    /** 单选列表 */
    private val mPdrRadioRv by bindView<RecyclerView>(R.id.pdr_radio_rv)
    /** 适配器 */
    private lateinit var mPdrAdapter: RadioGroupAdapter
    /** 布局管理器 */
    private lateinit var mPdrLayoutManager: GridLayoutManager

    /** 单选按钮图片 */
    private var mPdrBtnSrc: Drawable? = null
    /** 单选按钮图片资源id */
    private var mPdrBtnSrcResId: Int = 0
    /** 单选文字颜色 */
    private var mPdrRadioTextColor: ColorStateList? = null
    /** 单选文字大小 */
    private var mPdrRadioTextSizeSp: Float = 0f
    /** 选择类型 */
    private var mPdrRadioType: Int = TYPE_SINGLE
    /** 行数 */
    private var mPdrSpanCount: Int = 1
    /** 文字距离图标的左侧间距 */
    private var mPdrMarginStartDp = 0
    /** 布局方向 */
    private var mPdrRadioGravity = 0
    /** 单选项图片宽高 */
    private var mPdrRadioBtnSquareDp = 0
    /** 是否只读模式 */
    private var isPdrReadOnly = false

    /** 数据 */
    private var mPdrList: MutableList<RadioableWrapper> = ArrayList()
    /** item长按 */
    private var mPdrOnCheckedChangeListener: ((radioable: Radioable, isSelected: Boolean) -> Unit)? = null

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
        when (visibility) {
            1 -> setRequiredVisibility(View.INVISIBLE)
            2 -> setRequiredVisibility(View.GONE)
            else -> setRequiredVisibility(View.VISIBLE)
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

        typedArray?.recycle()
    }

    private fun initRecyclerView() {
        mPdrLayoutManager = GridLayoutManager(context, mPdrSpanCount)
        mPdrLayoutManager.orientation = RecyclerView.VERTICAL
        mPdrAdapter = RadioGroupAdapter(context)
        mPdrRadioRv.layoutManager = mPdrLayoutManager
        mPdrRadioRv.setHasFixedSize(true)
        mPdrRadioRv.isNestedScrollingEnabled = false
        mPdrRadioRv.adapter = mPdrAdapter
    }

    private fun setListeners() {
        mPdrAdapter.setOnItemClickListener { viewHolder, item, position ->
            if (isPdrReadOnly) {
                return@setOnItemClickListener
            }

            if (mPdrRadioType == TYPE_SINGLE) {// 单选
                for (i in mPdrList.indices) {
                    mPdrList[i].isSelected = if (item.radioable.getIdTag() == mPdrList[i].getIdTag()) {
                        if (!mPdrList[i].isSelected) {// 原来未选中 现在选中了则回调监听器
                            mPdrOnCheckedChangeListener?.invoke(item, true)
                        }
                        true
                    } else {
                        false
                    }
                }
            }
            if (mPdrRadioType == TYPE_MULTIPLE) {// 多选
                for (i in mPdrList.indices) {
                    if (item.radioable.getIdTag() == mPdrList[i].getIdTag()) {
                        mPdrList[i].isSelected = !mPdrList[i].isSelected
                        mPdrOnCheckedChangeListener?.invoke(item, mPdrList[i].isSelected)
                    }

                }
            }
            mPdrAdapter.setData(mPdrList)
            mPdrAdapter.notifyDataSetChanged()
        }
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

    /** 设置是否只读[isReadOnly] */
    fun setReadOnly(isReadOnly: Boolean) {
        this.isPdrReadOnly = isReadOnly
    }

    /** 是否只读 */
    fun isReadOnly(): Boolean = isPdrReadOnly

    /** 设置单选按钮图片[src] */
    fun setRadioBtnSrc(src: Drawable) {
        mPdrBtnSrc = src
        mPdrAdapter.setBtnSrc(mPdrBtnSrc)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 设置单选按钮图片[resId] */
    fun setRadioBtnSrc(@DrawableRes resId: Int) {
        mPdrBtnSrcResId = resId
        mPdrAdapter.setBtnSrcResId(mPdrBtnSrcResId)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 设置单选按钮图片[bitmap] */
    fun setRadioBtnSrc(bitmap: Bitmap) {
        mPdrBtnSrc = createBitmapDrawable(bitmap)
        mPdrAdapter.setBtnSrc(mPdrBtnSrc)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 设置单选文字颜色[textColor] */
    fun setRadioTextColor(textColor: ColorStateList) {
        mPdrRadioTextColor = textColor
        mPdrAdapter.setRadioTextColor(mPdrRadioTextColor)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 设置单选文字颜色[color] */
    fun setRadioTextColor(@ColorInt color: Int) {
        mPdrRadioTextColor = ColorStateList.valueOf(color)
        mPdrAdapter.setRadioTextColor(mPdrRadioTextColor)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 设置单选文字颜色[color] */
    fun setRadioTextColorRes(@ColorRes color: Int) {
        mPdrRadioTextColor = ColorStateList.valueOf(getColorCompat(color))
        mPdrAdapter.setRadioTextColor(mPdrRadioTextColor)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 设置单选文字大小[sp] */
    fun setRadioTextSize(sp: Float) {
        mPdrRadioTextSizeSp = sp
        mPdrAdapter.setRadioTextSize(mPdrRadioTextSizeSp)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 设置选择类型[type] */
    fun setRadioType(@RadioType type: Int) {
        mPdrRadioType = type
    }

    /** 获取选择类型 */
    fun getRadioType(): Int = mPdrRadioType

    /** 设置行数[count] */
    fun setSpanCount(count: Int) {
        mPdrSpanCount = if (count > 0) count else 1
        mPdrLayoutManager.spanCount = mPdrSpanCount
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 获取行数 */
    fun getSpanCount(): Int = mPdrSpanCount

    /** 设置文字距离图标的左侧间距[dp] */
    fun setRadioMarginStart(dp: Int) {
        mPdrMarginStartDp = dp
        mPdrAdapter.setRadioMarginStart(mPdrMarginStartDp)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 设置单选项布局方向[gravity] */
    fun setRadioGravity(gravity: Int) {
        mPdrRadioGravity = gravity
        mPdrAdapter.setRadioGravity(mPdrRadioGravity)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 设置单选项图片宽高[dp] */
    fun setRadioBtnSquare(dp: Int) {
        mPdrRadioBtnSquareDp = dp
        mPdrAdapter.setRadioBtnSquare(mPdrRadioBtnSquareDp)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 设置数据列表[data] */
    fun setDataList(data: MutableList<Radioable>) {
        mPdrList = setWrapper(data)
        mPdrAdapter.setData(mPdrList)
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
        for (i in mPdrList.indices) {
            mPdrList[i].isSelected = id == mPdrList[i].getIdTag()
        }
        mPdrAdapter.setData(mPdrList)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 设置多选选中的[ids] */
    fun setSelectedId(ids: List<String>) {
        for (i in mPdrList.indices) {
            mPdrList[i].isSelected = false
            ids.forEachIndexed { index, id ->
                if (id == mPdrList[i].getIdTag()) {
                    mPdrList[i].isSelected = true
                }
            }
        }
        mPdrAdapter.setData(mPdrList)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 获取选中的Id*/
    fun getSelectedId(): List<String> {
        val list = ArrayList<String>()
        for (wrapper in mPdrList) {
            if (wrapper.isSelected) {
                list.add(wrapper.getIdTag())
            }
        }
        return list
    }

    /** 是否已选中 */
    fun isSelectedId(): Boolean {
        mPdrList.forEach {
            if (it.isSelected) {
                return true
            }
        }
        return false
    }

    /** 设置选中变化监听器[listener]*/
    fun setOnCheckedChangeListener(listener: ((radioable: Radioable, isSelected: Boolean) -> Unit)?) {
        mPdrOnCheckedChangeListener = listener
    }

}