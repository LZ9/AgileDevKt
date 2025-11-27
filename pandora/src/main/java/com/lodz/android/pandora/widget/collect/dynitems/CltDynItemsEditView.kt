package com.lodz.android.pandora.widget.collect.dynitems

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.postDelayed
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.getDrawableCompat
import com.lodz.android.corekt.anko.px2sp
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.pandora.R
import com.lodz.android.pandora.databinding.PandoraItemCltDynEditBinding
import com.lodz.android.pandora.widget.collect.CltEditView
import com.lodz.android.pandora.widget.collect.CltEditView.EditInputType
import com.lodz.android.pandora.widget.rv.anko.linear
import com.lodz.android.pandora.widget.rv.anko.setup
import com.lodz.android.pandora.widget.rv.recycler.base.BaseVbRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.vh.DataVBViewHolder
import kotlin.random.Random

/**
 * 动态条目输入框
 * @author zhouL
 * @date 2025/11/25
 */
class CltDynItemsEditView : FrameLayout {

    /** 必填图标 */
    private val mPdrRequiredImg by bindView<ImageView>(R.id.pdr_required_img)
    /** 标题控件 */
    private val mPdrTitleTv by bindView<TextView>(R.id.pdr_title_tv)
    /** 添加按钮 */
    private val mPdrAddBtn by bindView<ImageView>(R.id.pdr_add_btn)
    /** 跳转按钮 */
    private val mPdrJumpBtn by bindView<TextView>(R.id.pdr_jump_btn)
    /** 输入框条目列表 */
    private val mRecyclerView by bindView<RecyclerView>(R.id.pdr_edit_rv)

    /** 可动态新增的最大条数，-1表示不限制 */
    private var mMaxItems = -1

    /** 默认条目配置 */
    private val mPdrDefItemsEditBean = CltItemsEditBean()

    /** 条目列表适配器 */
    private lateinit var mAdapter: CltItemsEditAdapter

    /** 新增按钮点击监听器 */
    private var mPdrAddBtnClickListener: ((config: CltItemsEditBean) -> Unit)? = null

    /** 文字限制监听器 */
    private var mPdrItemLimitListener: ((s: CharSequence, max: Int, bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) -> Unit)? = null

    /** 文字变化监听器 */
    private var mPdrItemTextChangedListener: ((s: CharSequence, bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) -> Unit)? = null

    /** 编辑按钮点击监听器 */
    private var mPdrItemEditBtnClickListener: ((bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) -> Unit)? = null

    /** 删除按钮点击监听器 */
    private var mPdrItemDeleteBtnClickListener: ((bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) -> Unit)? = null

    private var mPdrDataList = ArrayList<CltItemsEditBean>()

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
        LayoutInflater.from(context).inflate(R.layout.pandora_view_clt_dynamic_edit, this)
    }

    private fun initRecyclerView() {
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.setHasFixedSize(false)
        mAdapter = CltItemsEditAdapter(context)
        mRecyclerView.linear().setup(mAdapter)
        mAdapter.setData(mPdrDataList)
    }

    // 配置布局
    private fun configLayout(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.CltDynItemsEditView)
        }

        // 设置必填图片是否显示
        val visibility = typedArray?.getInt(R.styleable.CltDynItemsEditView_requiredVisibility, 0) ?: 0
        when (visibility) {
            1 -> setRequiredVisibility(INVISIBLE)
            2 -> setRequiredVisibility(GONE)
            else -> setRequiredVisibility(VISIBLE)
        }

        // 设置必填图片
        val src: Drawable? = typedArray?.getDrawable(R.styleable.CltDynItemsEditView_requiredSrc)
        if (src != null) {
            setRequiredImg(src)
        }

        // 设置标题文字
        val titleText: String? = typedArray?.getString(R.styleable.CltDynItemsEditView_titleText)
        if (titleText != null) {
            setTitleText(titleText)
        }
        // 设置标题文字颜色
        val titleTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltDynItemsEditView_titleTextColor)
        if (titleTextColor != null) {
            setTitleTextColor(titleTextColor)
        }
        // 设置标题文字大小
        val titleTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.CltDynItemsEditView_titleTextSize, 0) ?: 0
        if (titleTextSize > 0) {
            setTitleTextSize(px2sp(titleTextSize.toFloat()))
        }
        // 设置标题控件宽度
        val titleWidth: Int = typedArray?.getDimensionPixelSize(R.styleable.CltDynItemsEditView_titleWidth, 0) ?: 0
        if (titleWidth > 0) {
            setTitleWidth(titleWidth)
        }
        // 设置标题背景
        val titleBackground: Drawable? = typedArray?.getDrawable(R.styleable.CltDynItemsEditView_titleBackground)
        if (titleBackground != null) {
            setTitleBackground(titleBackground)
        }

        // 设置是否只读
        setReadOnly(typedArray?.getBoolean(R.styleable.CltDynItemsEditView_isReadOnly, false) ?: false)

        // 设置新增图标
        val addBtnSrc: Drawable? =typedArray?.getDrawable(R.styleable.CltDynItemsEditView_addBtnSrc)
        if (addBtnSrc != null){
            setAddBtnDrawable(addBtnSrc)
        }
        // 设置新增图标控件宽高边长
        val addBtnSide: Int = typedArray?.getDimensionPixelSize(R.styleable.CltDynItemsEditView_addBtnSide, 0)?: 0
        if (addBtnSide > 0) {
            setAddBtnSide(addBtnSide)
        }
        // 设置新增图标的展示类型
        setAddBtnScaleType(getScaleTypeById(typedArray?.getInt(R.styleable.CltDynItemsEditView_addBtnScaleType, ScaleType.FIT_CENTER.ordinal)?:ScaleType.FIT_CENTER.ordinal))
        // 设置必填图片是否显示
        val addBtnVisibility = typedArray?.getInt(R.styleable.CltDynItemsEditView_addBtnVisibility, 0) ?: 0
        when (addBtnVisibility) {
            1 -> setAddBtnVisibility(INVISIBLE)
            2 -> setAddBtnVisibility(GONE)
            else -> setAddBtnVisibility(VISIBLE)
        }

        // 设置跳转按钮文字
        val jumpText: String? = typedArray?.getString(R.styleable.CltDynItemsEditView_jumpText)
        if (jumpText != null) {
            setNeedJumpBtn(true)
            setJumpBtnText(jumpText)
        } else {
            setNeedJumpBtn(false)
        }
        // 设置跳转按钮文字颜色
        val jumpTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltDynItemsEditView_jumpTextColor)
        if (jumpTextColor != null) {
            setJumpBtnTextColor(jumpTextColor)
        }
        // 设置跳转按钮文字大小
        val jumpTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.CltDynItemsEditView_jumpTextSize, 0) ?: 0
        if (jumpTextSize > 0) {
            setJumpBtnTextSize(px2sp(jumpTextSize.toFloat()))
        }
        // 设置跳转按钮背景
        val jumpBackground: Drawable? = typedArray?.getDrawable(R.styleable.CltDynItemsEditView_jumpBackground)
        if (jumpBackground != null) {
            setJumpBackground(jumpBackground)
        }

        // 设置是否开启嵌套内滚动
        mRecyclerView.isNestedScrollingEnabled = typedArray?.getBoolean(R.styleable.CltDynItemsEditView_nestedScrollingEnabled, false) ?: false

        // 设置最大条目数量
        val maxItems = typedArray?.getInt(R.styleable.CltDynItemsEditView_maxItems, 0) ?: 0
        if (maxItems > 0){
            setMaxItems(maxItems)
        }

        // 设置条目标题文字
        val itemTitleText: String? = typedArray?.getString(R.styleable.CltDynItemsEditView_itemTitleText)
        if (itemTitleText != null) {
            setItemTitleText(itemTitleText)
        }
        // 设置条目标题文字颜色
        val itemTitleTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltDynItemsEditView_itemTitleTextColor)
        if (itemTitleTextColor != null) {
            setItemTitleTextColor(itemTitleTextColor)
        }
        // 设置条目标题文字大小
        val itemTitleTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.CltDynItemsEditView_itemTitleTextSize, 0) ?: 0
        if (itemTitleTextSize > 0) {
            setItemTitleTextSize(px2sp(itemTitleTextSize.toFloat()))
        }
        // 设置条目标题控件宽度
        val itemTitleWidth: Int = typedArray?.getDimensionPixelSize(R.styleable.CltDynItemsEditView_itemTitleWidth, 0) ?: 0
        if (itemTitleWidth > 0) {
            setItemTitleWidth(itemTitleWidth)
        }
        // 设置条目标题背景
        val itemTitleBackground: Drawable? = typedArray?.getDrawable(R.styleable.CltDynItemsEditView_itemTitleBackground)
        if (itemTitleBackground != null) {
            setItemTitleBackground(itemTitleBackground)
        }

        // 设置条目输入框文字
        val itemEtText: String? = typedArray?.getString(R.styleable.CltDynItemsEditView_itemEtText)
        if (itemEtText != null) {
            setItemEtText(itemEtText)
        }
        // 设置条目输入框文字颜色
        val itemEtTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltDynItemsEditView_itemEtTextColor)
        if (itemEtTextColor != null) {
            setItemEtTextColor(itemEtTextColor)
        }
        // 设置条目输入框文字大小
        val itemEtTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.CltDynItemsEditView_itemEtTextSize, 0) ?: 0
        if (itemEtTextSize > 0) {
            setItemEtTextSize(px2sp(itemEtTextSize.toFloat()))
        }
        // 设置条目输入框提示语
        val itemEtHint: String? = typedArray?.getString(R.styleable.CltDynItemsEditView_itemEtHint)
        if (itemEtHint != null) {
            setItemEtHint(itemEtHint)
        }
        // 设置条目输入框提示语颜色
        val itemEtHintColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltDynItemsEditView_itemEtHintColor)
        if (itemEtHintColor != null) {
            setItemEtHintColor(itemEtHintColor)
        }
        // 设置条目输入框标记
        val itemEtTag: String? = typedArray?.getString(R.styleable.CltDynItemsEditView_itemEtTag)
        if (itemEtTag != null) {
            setItemEtTag(itemEtTag)
        }
        // 设置条目输入框背景
        val itemEtBackground: Drawable? = typedArray?.getDrawable(R.styleable.CltDynItemsEditView_itemEtBackground)
        if (itemEtBackground != null) {
            setItemEtBackground(itemEtBackground)
        }
        // 设置条目输入框文字位置
        val gravity = typedArray?.getInt(R.styleable.CltDynItemsEditView_itemEtGravity, 0) ?: 0
        when (gravity) {
            1 -> setItemEtGravity(Gravity.CENTER)
            2 -> setItemEtGravity(Gravity.START)
            3 -> setItemEtGravity(Gravity.END )
            4 -> setItemEtGravity(Gravity.START or Gravity.CENTER_VERTICAL)
            5 -> setItemEtGravity(Gravity.END or Gravity.CENTER_VERTICAL)
            else -> setItemEtGravity(Gravity.CENTER_VERTICAL)
        }
        // 设置条目输入框是否单行显示
        val isSingleLine = typedArray?.getBoolean(R.styleable.CltDynItemsEditView_itemEtSingleLine, false) ?: false
        if (isSingleLine){
            setSingleLine(true)
        }
        // 设置条目输入框文本最大长度
        val max: Int = typedArray?.getInt(R.styleable.CltDynItemsEditView_itemEtMaxLength, -1) ?: -1
        if (max >= 0) {
            setMaxLength(max)
        }
        // 设置输入类型
        setEditInputType(typedArray?.getInt(R.styleable.CltDynItemsEditView_itemEtInputType, CltEditView.TYPE_TEXT)?: CltEditView.TYPE_TEXT)
        // 最小行数
        val minLines: Int = typedArray?.getInt(R.styleable.CltDynItemsEditView_itemEtMinLines, 0) ?: 0
        if (minLines > 0) {
            setMinLines(minLines)
        }
        // 最大行数
        val maxLines: Int = typedArray?.getInt(R.styleable.CltDynItemsEditView_itemEtMaxLines, 0) ?: 0
        if (maxLines > 1) {
            setMaxLines(maxLines)
        }
        // 设置限制文字是否显示
        val limitVisibility = typedArray?.getInt(R.styleable.CltDynItemsEditView_itemEtLimitVisibility, 2) ?: 2
        when (limitVisibility) {
            1 -> setLimitVisibility(INVISIBLE)
            2 -> setLimitVisibility(GONE)
            else -> {
                if (max <= 0) {
                    setMaxLength(100)
                }
                setLimitVisibility(VISIBLE)
            }
        }

        // 设置条目删除图标控件宽高边长
        val itemHeight: Int = typedArray?.getDimensionPixelSize(R.styleable.CltDynItemsEditView_itemHeight, 0)?: 0
        if (itemHeight > 0) {
            setItemHeight(itemHeight)
        }
        // 设置条目单位文字
        val unitText: String? = typedArray?.getString(R.styleable.CltDynItemsEditView_unitText)
        if (unitText != null) {
            setNeedItemUnit(true)
            setItemUnitText(unitText)
        } else {
            setNeedItemUnit(false)
        }
        // 设置条目单位文字颜色
        val unitTextColor: ColorStateList? = typedArray?.getColorStateList(R.styleable.CltDynItemsEditView_unitTextColor)
        if (unitTextColor != null) {
            setItemUnitTextColor(unitTextColor)
        }
        // 设置条目单位文字大小
        val unitTextSize: Int = typedArray?.getDimensionPixelSize(R.styleable.CltDynItemsEditView_unitTextSize, 0) ?: 0
        if (unitTextSize > 0) {
            setItemUnitTextSize(px2sp(unitTextSize.toFloat()))
        }

        // 设置条目删除图标
        setItemDeleteBtnDrawable(typedArray?.getDrawable(R.styleable.CltDynItemsEditView_itemDeleteBtnSrc))
        // 设置条目删除图标控件宽高边长
        val itemDeleteBtnSide: Int = typedArray?.getDimensionPixelSize(R.styleable.CltDynItemsEditView_itemDeleteBtnSide, 0)?: 0
        if (itemDeleteBtnSide > 0) {
            setItemDeleteBtnSide(itemDeleteBtnSide)
        }
        // 设置条目删除图标的展示类型
        setItemDeleteBtnScaleType(getScaleTypeById(typedArray?.getInt(R.styleable.CltDynItemsEditView_itemDeleteBtnScaleType, ScaleType.FIT_CENTER.ordinal)?:ScaleType.FIT_CENTER.ordinal))
        // 设置条目删除图标是否显示
        val itemDeleteBtnVisibility = typedArray?.getInt(R.styleable.CltDynItemsEditView_itemDeleteBtnVisibility, 0) ?: 0
        when (itemDeleteBtnVisibility) {
            1 -> setItemDeleteBtnVisibility(INVISIBLE)
            2 -> setItemDeleteBtnVisibility(GONE)
            else -> setItemDeleteBtnVisibility(VISIBLE)
        }

        // 设置条目编辑图标
        setItemEditBtnDrawable(typedArray?.getDrawable(R.styleable.CltDynItemsEditView_itemEditBtnSrc))
        // 设置条目编辑图标控件宽高边长
        val itemEditBtnSide: Int = typedArray?.getDimensionPixelSize(R.styleable.CltDynItemsEditView_itemEditBtnSide, 0)?: 0
        if (itemEditBtnSide > 0) {
            setItemEditBtnSide(itemEditBtnSide)
        }
        // 设置条目编辑图标的展示类型
        setItemEditBtnScaleType(getScaleTypeById(typedArray?.getInt(R.styleable.CltDynItemsEditView_itemEditBtnScaleType, ScaleType.FIT_CENTER.ordinal)?:ScaleType.FIT_CENTER.ordinal))
        // 设置条目删除图标是否显示
        val itemEditBtnVisibility = typedArray?.getInt(R.styleable.CltDynItemsEditView_itemEditBtnVisibility, 0) ?: 0
        when (itemEditBtnVisibility) {
            1 -> setItemEditBtnVisibility(INVISIBLE)
            2 -> setItemEditBtnVisibility(GONE)
            else -> setItemEditBtnVisibility(VISIBLE)
        }

        typedArray?.recycle()
    }

    /** 设置监听器 */
    private fun setListeners() {
        mPdrAddBtn.setOnClickListener {
            if (mPdrAddBtnClickListener == null) {
                addItem()
                return@setOnClickListener
            }
            mPdrAddBtnClickListener?.invoke(mPdrDefItemsEditBean)
        }

        mAdapter.setOnItemEditBtnClickListener { bean, vb, holder, position ->
            mPdrItemEditBtnClickListener?.invoke(bean, vb, holder, position)
        }

        mAdapter.setOnItemDeleteBtnClickListener { bean, vb, holder, position ->
            if (mPdrItemDeleteBtnClickListener == null){
                mAdapter.notifyItemRemovedChanged(position)
                mRecyclerView.postDelayed(400) { // 删除后需要重绘RV，否则会有空白区域
                    mRecyclerView.requestLayout()
                }
                return@setOnItemDeleteBtnClickListener
            }
            mPdrItemDeleteBtnClickListener?.invoke(bean, vb, holder, position)
        }

        mAdapter.setOnItemLimitListener { s, max, bean, vb, holder, position ->
            mPdrItemLimitListener?.invoke(s, max, bean, vb, holder, position)
        }

        mAdapter.setOnItemTextChangedListener { s, bean, vb, holder, position ->
            for (i in 0 until mPdrDataList.size) {
                if (bean.id == mPdrDataList[i].id) {
                    mPdrDataList[i].itemEtText = s.toString()
                }
            }
            mPdrItemTextChangedListener?.invoke(s, bean, vb, holder, position)
        }

    }

    /** 根据编号[id]获取ScaleType */
    private fun getScaleTypeById(id: Int): ScaleType = when (id) {
        ScaleType.MATRIX.ordinal -> ScaleType.MATRIX
        ScaleType.FIT_XY.ordinal -> ScaleType.FIT_XY
        ScaleType.FIT_START.ordinal -> ScaleType.FIT_START
        ScaleType.FIT_CENTER.ordinal -> ScaleType.FIT_CENTER
        ScaleType.FIT_END.ordinal -> ScaleType.FIT_END
        ScaleType.CENTER.ordinal -> ScaleType.CENTER
        ScaleType.CENTER_CROP.ordinal -> ScaleType.CENTER_CROP
        ScaleType.CENTER_INSIDE.ordinal -> ScaleType.CENTER_INSIDE
        else -> ScaleType.FIT_CENTER
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
    fun isRequired(): Boolean = mPdrRequiredImg.visibility == VISIBLE

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


    /** 设置新增图标[drawable] */
    fun setAddBtnDrawable(drawable: Drawable?) {
        mPdrAddBtn.setImageDrawable(drawable)
    }

    /** 设置新增图标[resId] */
    fun setAddBtnResource(@DrawableRes resId: Int) {
        mPdrAddBtn.setImageResource(resId)
    }

    /** 设置新增图标的展示类型[scaleType] */
    fun setAddBtnScaleType(scaleType: ScaleType) {
        mPdrAddBtn.scaleType = scaleType
    }

    /** 设置新增图标控件宽高边长[px] */
    fun setAddBtnSide(px: Int) {
        mPdrAddBtn.layoutParams.width = px
        mPdrAddBtn.layoutParams.height = px
    }

    /** 设置新增图标控件显隐[visibility] */
    fun setAddBtnVisibility(visibility: Int) {
        mPdrAddBtn.visibility = visibility
    }

    /** 设置是否需要跳转按钮[isNeed] */
    fun setNeedJumpBtn(isNeed: Boolean) {
        mPdrJumpBtn.visibility = if (isNeed) VISIBLE else GONE
    }

    /** 是否显示跳转按钮 */
    fun isShowJumpBtn(): Boolean = mPdrJumpBtn.visibility == VISIBLE

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
    fun setOnJumpClickListener(listener: OnClickListener?) {
        mPdrJumpBtn.setOnClickListener(listener)
    }

    /** 设置是否只读[isReadOnly] */
    fun setReadOnly(isReadOnly: Boolean) {
        mPdrDefItemsEditBean.readOnly = isReadOnly
        mPdrAddBtn.isEnabled = !isReadOnly
        mPdrJumpBtn.isEnabled = !isReadOnly
    }

    /** 是否只读 */
    fun isReadOnly(): Boolean = mPdrDefItemsEditBean.readOnly

    /** 设置条目标题文字[title] */
    fun setItemTitleText(title: String) {
        mPdrDefItemsEditBean.itemTitleText = title
    }

    /** 设置条目标题文字资源[resId] */
    fun setItemTitleText(@StringRes resId: Int) {
        setItemTitleText(context.getString(resId))
    }

    /** 设置条目标题文字颜色[color] */
    fun setItemTitleTextColor(@ColorInt color: Int) {
        setItemTitleTextColor(ColorStateList.valueOf(color))
    }

    /** 设置条目标题文字颜色[color] */
    fun setItemTitleTextColorRes(@ColorRes color: Int) {
        setItemTitleTextColor(getColorCompat(color))
    }

    /** 设置条目标题文字颜色[color] */
    fun setItemTitleTextColor(color: ColorStateList) {
        mPdrDefItemsEditBean.itemTitleTextColor = color
    }

    /** 设置条目标题文字大小[sp] */
    fun setItemTitleTextSize(sp: Float) {
        mPdrDefItemsEditBean.itemTitleTextSizeSp = sp
    }

    /** 设置条目标题控件宽度 */
    fun setItemTitleWidth(px: Int) {
        mPdrDefItemsEditBean.itemTitleWidthPx = px
    }

    /** 设置条目标题背景[resId] */
    fun setItemTitleBackgroundRes(@DrawableRes resId: Int) {
        setItemTitleBackground(context.getDrawableCompat(resId))
    }

    /** 设置条目标题背景[color] */
    fun setItemTitleBackgroundColor(@ColorInt color: Int) {
        setItemTitleBackground(ColorDrawable(color))
    }

    /** 设置条目标题背景[color] */
    fun setItemTitleBackgroundColorRes(@ColorRes color: Int) {
        setItemTitleBackgroundColor(getColorCompat(color))
    }

    /** 设置条目标题背景[drawable] */
    fun setItemTitleBackground(drawable: Drawable?) {
        mPdrDefItemsEditBean.itemTitleBackgroundDrawable = drawable
    }

    /** 设置条目输入框文字[text] */
    fun setItemEtText(text: String) {
        mPdrDefItemsEditBean.itemEtText = text
    }

    /** 设置条目输入框文字资源[resId] */
    fun setItemEtText(@StringRes resId: Int) {
        setItemEtText(context.getString(resId))
    }

    /** 设置条目输入框标记[tag] */
    fun setItemEtTag(tag: String) {
        mPdrDefItemsEditBean.itemEtTag = tag
    }

    /** 设置条目输入框文字颜色[color] */
    fun setItemEtTextColor(@ColorInt color: Int) {
        setItemEtTextColor(ColorStateList.valueOf(color))
    }

    /** 设置条目输入框文字颜色[color] */
    fun setItemEtTextColorRes(@ColorRes color: Int) {
        setItemEtTextColor(getColorCompat(color))
    }

    /** 设置条目输入框文字颜色[color] */
    fun setItemEtTextColor(color: ColorStateList) {
        mPdrDefItemsEditBean.itemEtTextColor = color
    }

    /** 设置条目输入框文字大小[sp] */
    fun setItemEtTextSize(sp: Float) {
        mPdrDefItemsEditBean.itemEtTextSizeSp = sp
    }

    /** 设置条目输入框提示语资源[resId] */
    fun setItemEtHint(@StringRes resId: Int) {
        setItemEtHint(context.getString(resId))
    }

    /** 设置条目输入框提示语[hint] */
    fun setItemEtHint(hint: String) {
        mPdrDefItemsEditBean.itemEtHintText = hint
    }

    /** 设置输入框提示语颜色[color] */
    fun setItemEtHintColor(@ColorInt color: Int) {
        setItemEtHintColor(ColorStateList.valueOf(color))
    }

    /** 设置条目输入框提示语颜色[color] */
    fun setItemEtHintColorRes(@ColorRes color: Int) {
        setItemEtHintColor(getColorCompat(color))
    }

    /** 设置条目输入框提示语颜色[color] */
    fun setItemEtHintColor(color: ColorStateList) {
        mPdrDefItemsEditBean.itemEtHintTextColor = color
    }

    /** 设置条目输入框背景[drawable] */
    fun setItemEtBackground(drawable: Drawable?) {
        mPdrDefItemsEditBean.itemEtBackgroundDrawable = drawable
    }

    /** 设置条目输入框背景[color] */
    fun setItemEtBackground(@ColorInt color: Int) {
        setItemEtBackground(ColorDrawable(color))
    }

    /** 设置条目输入框背景[resId] */
    fun setItemEtBackgroundRes(@DrawableRes resId: Int) {
        setItemEtBackground(context.getDrawableCompat(resId))
    }

    /** 设置条目输入框文字位置 */
    fun setItemEtGravity(gravity: Int) {
        mPdrDefItemsEditBean.itemEtGravity = gravity
    }

    /** 设置条目输入框是否单行显示[isSingleLine] */
    fun setSingleLine(isSingleLine: Boolean) {
        mPdrDefItemsEditBean.itemEtSingleLine = isSingleLine
    }

    /** 设置条目输入框文本最大长度[max] */
    fun setMaxLength(max: Int) {
        mPdrDefItemsEditBean.itemEtMaxCount = max
    }

    /** 设置条目输入框最小行数[lines] */
    fun setMinLines(lines: Int) {
        mPdrDefItemsEditBean.itemEtMinLines = lines
    }

    /** 设置最大行数[lines] */
    fun setMaxLines(lines: Int) {
        mPdrDefItemsEditBean.itemEtMaxLines = lines
    }

    /** 设置条目输入框输入类型[type] */
    fun setEditInputType(@EditInputType type: Int) {
        mPdrDefItemsEditBean.itemEtInputType = type
    }

    /** 设置限制文字显隐[visibility] */
    fun setLimitVisibility(visibility: Int) {
        mPdrDefItemsEditBean.itemEtLimitVisibility = visibility
    }

    /** 设置条目高度[px] */
    fun setItemHeight(px: Int) {
        mPdrDefItemsEditBean.itemHeight = px
    }

    /** 设置条目是否需要单位[isNeed] */
    fun setNeedItemUnit(isNeed: Boolean) {
        mPdrDefItemsEditBean.needItemUnit = isNeed
    }

    /** 设置条目单位文字[unit] */
    fun setItemUnitText(unit: String) {
        mPdrDefItemsEditBean.itemUnitText = unit
    }

    /** 设置条目单位文字资源[resId] */
    fun setItemUnitText(@StringRes resId: Int) {
        setItemUnitText(context.getString(resId))
    }

    /** 设置条目单位文字颜色[color] */
    fun setItemUnitTextColor(@ColorInt color: Int) {
        setItemUnitTextColor(ColorStateList.valueOf(color))
    }

    /** 设置条目单位文字颜色[color] */
    fun setItemUnitTextColorRes(@ColorRes color: Int) {
        setItemUnitTextColor(getColorCompat(color))
    }

    /** 设置条目单位文字颜色[color] */
    fun setItemUnitTextColor(color: ColorStateList) {
        mPdrDefItemsEditBean.itemUnitTextColor = color
    }

    /** 设置条目单位文字大小[sp] */
    fun setItemUnitTextSize(sp: Float) {
        mPdrDefItemsEditBean.itemUnitTextSizeSp = sp
    }

    /** 设置条目删除图标[drawable] */
    fun setItemDeleteBtnDrawable(drawable: Drawable?) {
        mPdrDefItemsEditBean.itemDeleteBtnDrawable = drawable
    }

    /** 设置条目删除图标[resId] */
    fun setItemDeleteBtnResource(@DrawableRes resId: Int) {
        setItemDeleteBtnDrawable(context.getDrawableCompat(resId))
    }

    /** 设置条目删除图标控件宽高边长[px] */
    fun setItemDeleteBtnSide(px: Int) {
        mPdrDefItemsEditBean.itemDeleteBtnSidePx = px
    }

    /** 设置条目删除图标的展示类型[scaleType] */
    fun setItemDeleteBtnScaleType(scaleType: ScaleType) {
        mPdrDefItemsEditBean.itemDeleteBtnScaleType = scaleType
    }

    /** 设置条目删除图标控件显隐[visibility] */
    fun setItemDeleteBtnVisibility(visibility: Int) {
        mPdrDefItemsEditBean.itemDeleteBtnVisibility = visibility
    }

    /** 设置条目编辑图标[drawable] */
    fun setItemEditBtnDrawable(drawable: Drawable?) {
        mPdrDefItemsEditBean.itemEditBtnDrawable = drawable
    }

    /** 设置条目编辑图标[resId] */
    fun setItemEditBtnResource(@DrawableRes resId: Int) {
        setItemEditBtnDrawable(context.getDrawableCompat(resId))
    }

    /** 设置条目编辑图标控件宽高边长[px] */
    fun setItemEditBtnSide(px: Int) {
        mPdrDefItemsEditBean.itemEditBtnSidePx = px
    }

    /** 设置条目编辑图标的展示类型[scaleType] */
    fun setItemEditBtnScaleType(scaleType: ScaleType) {
        mPdrDefItemsEditBean.itemEditBtnScaleType = scaleType
    }

    /** 设置条目编辑图标控件显隐[visibility] */
    fun setItemEditBtnVisibility(visibility: Int) {
        mPdrDefItemsEditBean.itemEditBtnVisibility = visibility
    }

    /** 获取列表 */
    fun getRecyclerView(): RecyclerView = mRecyclerView

    /** 获取列表适配器 */
    fun getRvAdapter(): BaseVbRvAdapter<CltItemsEditBean> = mAdapter

    /** 获取条目配置 */
    fun getItemConfig(): CltItemsEditBean = copyConfig()

    /** 添加采集条目[itemBean] */
    fun addItem(itemBean: CltItemsEditBean? = null) {
        val count = mPdrDataList.size
        if (mMaxItems != -1 && count >= mMaxItems) { // 已经到达限制条数
            toastShort("您最多可以新增 $mMaxItems 条数据")
            return
        }
        val bean = copyConfig()
        val item = itemBean ?: bean
        if (item.id.isEmpty()){// 如果数据没有ID则补充上ID
            item.id = DateUtils.getCurrentFormatString(DateUtils.TYPE_3) + Random.nextInt(999).toString()
        }
        mPdrDataList.add(item)
        mAdapter.setData(mPdrDataList)
        mAdapter.notifyDataSetChanged()
    }

    /** 拷贝配置 */
    private fun copyConfig(): CltItemsEditBean {
        val bean = CltItemsEditBean()
        bean.readOnly = mPdrDefItemsEditBean.readOnly
        bean.itemHeight = mPdrDefItemsEditBean.itemHeight
        bean.itemTitleText = mPdrDefItemsEditBean.itemTitleText
        bean.itemTitleTextColor = mPdrDefItemsEditBean.itemTitleTextColor
        bean.itemTitleTextSizeSp = mPdrDefItemsEditBean.itemTitleTextSizeSp
        bean.itemTitleWidthPx = mPdrDefItemsEditBean.itemTitleWidthPx
        bean.itemTitleBackgroundDrawable = mPdrDefItemsEditBean.itemTitleBackgroundDrawable
        bean.itemEtTag = mPdrDefItemsEditBean.itemEtTag
        bean.itemEtText = mPdrDefItemsEditBean.itemEtText
        bean.itemEtTextColor = mPdrDefItemsEditBean.itemEtTextColor
        bean.itemEtTextSizeSp = mPdrDefItemsEditBean.itemEtTextSizeSp
        bean.itemEtHintText = mPdrDefItemsEditBean.itemEtHintText
        bean.itemEtHintTextColor = mPdrDefItemsEditBean.itemEtHintTextColor
        bean.itemEtBackgroundDrawable = mPdrDefItemsEditBean.itemEtBackgroundDrawable
        bean.itemEtGravity = mPdrDefItemsEditBean.itemEtGravity
        bean.itemEtSingleLine = mPdrDefItemsEditBean.itemEtSingleLine
        bean.itemEtMaxCount = mPdrDefItemsEditBean.itemEtMaxCount
        bean.itemEtMinLines = mPdrDefItemsEditBean.itemEtMinLines
        bean.itemEtMaxLines = mPdrDefItemsEditBean.itemEtMaxLines
        bean.itemEtLimitVisibility = mPdrDefItemsEditBean.itemEtLimitVisibility
        bean.itemEtInputType = mPdrDefItemsEditBean.itemEtInputType
        bean.needItemUnit = mPdrDefItemsEditBean.needItemUnit
        bean.itemUnitText = mPdrDefItemsEditBean.itemUnitText
        bean.itemUnitTextColor = mPdrDefItemsEditBean.itemUnitTextColor
        bean.itemUnitTextSizeSp = mPdrDefItemsEditBean.itemUnitTextSizeSp
        bean.itemDeleteBtnDrawable = mPdrDefItemsEditBean.itemDeleteBtnDrawable
        bean.itemDeleteBtnSidePx = mPdrDefItemsEditBean.itemDeleteBtnSidePx
        bean.itemDeleteBtnScaleType = mPdrDefItemsEditBean.itemDeleteBtnScaleType
        bean.itemDeleteBtnVisibility = mPdrDefItemsEditBean.itemDeleteBtnVisibility
        bean.itemEditBtnDrawable = mPdrDefItemsEditBean.itemEditBtnDrawable
        bean.itemEditBtnSidePx = mPdrDefItemsEditBean.itemEditBtnSidePx
        bean.itemEditBtnScaleType = mPdrDefItemsEditBean.itemEditBtnScaleType
        bean.itemEditBtnVisibility = mPdrDefItemsEditBean.itemEditBtnVisibility
        return bean
    }

    /** 设置最大条目数量[max] */
    fun setMaxItems(max: Int) {
        mMaxItems = max
    }

    /** 设置新增按钮点击监听器[listener] */
    fun setOnAddBtnClickListener(listener: (config: CltItemsEditBean) -> Unit) {
        mPdrAddBtnClickListener = listener
    }

    /** 设置文字限制监听器[listener] */
    fun setOnItemInputTextLimitListener(listener: (s: CharSequence, max: Int, bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) -> Unit) {
        mPdrItemLimitListener = listener
    }

    /** 设置文字变化监听器[listener] */
    fun setOnItemTextChangedListener(listener: (s: CharSequence, bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) -> Unit) {
        mPdrItemTextChangedListener = listener
    }

    /** 设置编辑按钮点击监听器[listener] */
    fun setOnItemEditBtnClickListener(listener: (bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) -> Unit) {
        mPdrItemEditBtnClickListener = listener
    }

    /** 设置删除按钮点击监听器[listener] */
    fun setOnItemDeleteBtnClickListener(listener: (bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) -> Unit) {
        mPdrItemDeleteBtnClickListener = listener
    }
}