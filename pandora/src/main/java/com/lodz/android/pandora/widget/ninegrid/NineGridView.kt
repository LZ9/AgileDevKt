package com.lodz.android.pandora.widget.ninegrid

import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.anko.*
import com.lodz.android.pandora.R
import com.lodz.android.pandora.widget.rv.anko.layoutM
import com.lodz.android.pandora.widget.rv.anko.setup
import com.lodz.android.pandora.widget.rv.decoration.GridItemDecoration
import java.util.*
import kotlin.collections.ArrayList

/**
 * 图片九宫格控件
 * Created by zhouL on 2018/12/25.
 */
open class NineGridView<T> : FrameLayout {

    companion object {
        /** 默认图片最大数 */
        internal const val DEFAULT_MAX_PIC = 9
    }

    /** 默认列数 */
    private val DEFAULT_SPAN_COUNT = 3

    /** 列表 */
    private val mPdrRecyclerView by bindView<RecyclerView>(R.id.pdr_nine_grid_rv)
    /** 适配器 */
    private lateinit var mPdrAdapter: NineGridAdapter<T>
    /** 网格布局管理器 */
    private lateinit var mPdrGridLayoutManager: GridLayoutManager

    /** 数据列表 */
    private var mPdrDataList = ArrayList<T>()
    /** 是否需要拖拽 */
    private var isPdrNeedDrag = false
    /** 是否需要拖拽震动提醒 */
    private var isPdrNeedDragVibrate = true

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
        LayoutInflater.from(context).inflate(R.layout.pandora_view_nine_grid, this)
        initRecyclerView()
        configLayout(attrs)
    }

    /** 配置属性 */
    private fun configLayout(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridView)
        }
        isPdrNeedDrag = typedArray?.getBoolean(R.styleable.NineGridView_isNeedDrag, false) ?: false
        isPdrNeedDragVibrate = typedArray?.getBoolean(R.styleable.NineGridView_isNeedDragVibrate, true) ?: true
        mPdrAdapter.setNeedAddBtn(typedArray?.getBoolean(R.styleable.NineGridView_isNeedAddBtn, true)
                ?: true)
        mPdrAdapter.setAddBtnDrawable(typedArray?.getDrawable(R.styleable.NineGridView_addBtnDrawable))
        mPdrAdapter.setShowDelete(typedArray?.getBoolean(R.styleable.NineGridView_isShowDeleteBtn, true)
                ?: true)
        mPdrAdapter.setDeleteBtnDrawable(typedArray?.getDrawable(R.styleable.NineGridView_deleteDrawable))
        mPdrGridLayoutManager.spanCount = typedArray?.getInt(R.styleable.NineGridView_spanCount, DEFAULT_SPAN_COUNT) ?: DEFAULT_SPAN_COUNT
        mPdrAdapter.setMaxPic(typedArray?.getInt(R.styleable.NineGridView_maxPic, DEFAULT_MAX_PIC)
                ?: DEFAULT_MAX_PIC)
        mPdrAdapter.setItemHigh(typedArray?.getDimensionPixelSize(R.styleable.NineGridView_itemHigh, 0)
                ?: 0)
        val dividerSpacePx = typedArray?.getDimensionPixelSize(R.styleable.NineGridView_dividerSpace, 0) ?: 0
        if (dividerSpacePx > 0){
            mPdrRecyclerView.addItemDecoration(GridItemDecoration.create(context).setDividerSpace(context.px2dp(dividerSpacePx)).setDividerInt(Color.TRANSPARENT))
        }
        typedArray?.recycle()
    }

    /** 拖拽回调 */
    private val mPdrItemTouchHelperCallback = object : ItemTouchHelper.Callback() {
        // 配置拖拽类型
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = if (isPdrNeedDrag && viewHolder is NineGridAdapter<*>.NineGridViewHolder) {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            } else {
                0
            }
            val swipeFlags = 0
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        // 拖拽
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            if (mPdrDataList.isEmpty()) {
                return false
            }
            if (viewHolder !is NineGridAdapter<*>.NineGridViewHolder || target !is NineGridAdapter<*>.NineGridViewHolder) {
                return false
            }

            // 得到拖动ViewHolder的Position
            val fromPosition = viewHolder.bindingAdapterPosition
            // 得到目标ViewHolder的Position
            val toPosition = target.bindingAdapterPosition

            if (fromPosition < toPosition) {//顺序小到大
                for (i in fromPosition until toPosition) {
                    Collections.swap(mPdrDataList, i, i + 1)
                }
            } else {//顺序大到小
                for (i in fromPosition downTo (toPosition + 1)) {
                    Collections.swap(mPdrDataList, i, i - 1)
                }
            }
            mPdrAdapter.notifyItemMoved(fromPosition, toPosition)
            return true
        }

        // 滑动
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        // 当长按选中item时（拖拽开始时）调用
        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && isPdrNeedDragVibrate) {
                context.createVibrator(100)
            }
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null) {//开始拖拽
                viewHolder.itemView.startScaleSelf(1.0f, 1.05f, 1.0f, 1.05f, 50, true)
            }
            super.onSelectedChanged(viewHolder, actionState)
        }

        // 当手指松开时（拖拽完成时）调用
        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            viewHolder.itemView.startScaleSelf(1.05f, 1.0f, 1.05f, 1.0f, 50, true)
            super.clearView(recyclerView, viewHolder)
        }

        override fun isLongPressDragEnabled(): Boolean = true

        override fun isItemViewSwipeEnabled(): Boolean = false
    }

    private fun initRecyclerView() {
        mPdrGridLayoutManager = GridLayoutManager(context, DEFAULT_SPAN_COUNT, RecyclerView.VERTICAL, false)
        mPdrAdapter = mPdrRecyclerView.let {
            it.layoutM(mPdrGridLayoutManager)
            it.setup(NineGridAdapter<T>(context))
        }
        val itemTouchHelper = ItemTouchHelper(mPdrItemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(mPdrRecyclerView)
    }

    /** 设置是否需要添加图标[isNeed] */
    fun setNeedAddBtn(isNeed: Boolean) {
        mPdrAdapter.setNeedAddBtn(isNeed)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 设置添加图标[resId] */
    fun setAddBtnDrawable(@DrawableRes resId: Int) {
        mPdrAdapter.setAddBtnDrawable(getDrawableCompat(resId))
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 设置是否显示删除按钮[isShow] */
    fun setShowDelete(isShow: Boolean) {
        mPdrAdapter.setShowDelete(isShow)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 设置删除图标[resId] */
    fun setDeleteBtnDrawable(@DrawableRes resId: Int) {
        mPdrAdapter.setDeleteBtnDrawable(getDrawableCompat(resId))
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 设置最大图片数[count] */
    fun setMaxPic(@IntRange(from = 1) count: Int) {
        mPdrAdapter.setMaxPic(count)
        clearData()
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 设置数据 */
    fun setData(data: ArrayList<T>) {
        mPdrDataList = ArrayList()
        //如果数据大于最大图片数，则取前n位数据
        val length = if (data.size > mPdrAdapter.getMaxPic()) mPdrAdapter.getMaxPic() else data.size
        for (i in 0 until length) {
            mPdrDataList.add(data[i])
        }
        mPdrAdapter.setData(mPdrDataList)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 添加数据 */
    open fun addData(data: ArrayList<T>) {
        // 判断添加的数据长度和已有数据长度之和是否超过总长度
        val length = if ((data.size + mPdrDataList.size) > mPdrAdapter.getMaxPic()) mPdrAdapter.getMaxPic() - mPdrDataList.size else data.size
        for (i in 0 until length) {
            mPdrDataList.add(data[i])
        }
        mPdrAdapter.setData(mPdrDataList)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 删除数据 */
    open fun removeData(position: Int) {
        if (position < 0 || position >= mPdrDataList.size) {
            return
        }
        mPdrAdapter.notifyItemRemovedChanged(position)
        mPdrRecyclerView.requestLayout()
    }

    /** 获取图片数据 */
    fun getPicData(): List<T> = mPdrDataList

    /** 清空数据 */
    fun clearData() {
        setData(ArrayList())
    }

    /** 是否允许拖拽 */
    fun setNeedDrag(isNeed: Boolean) {
        isPdrNeedDrag = isNeed
    }

    /** 是否允许拖拽震动提醒 */
    fun setNeedDragVibrate(isNeed: Boolean) {
        isPdrNeedDragVibrate = isNeed
    }

    /** 添加装饰器[decor] */
    fun addItemDecoration(decor: RecyclerView.ItemDecoration) {
        mPdrRecyclerView.addItemDecoration(decor)
    }

    /** 删除装饰器[decor] */
    fun removeItemDecoration(decor: RecyclerView.ItemDecoration) {
        mPdrRecyclerView.removeItemDecoration(decor)
    }

    /** 设置监听器 */
    open fun setOnNineGridViewListener(listener: OnNineGridViewListener<T>) {
        mPdrAdapter.setOnNineGridViewListener(listener)
    }

}