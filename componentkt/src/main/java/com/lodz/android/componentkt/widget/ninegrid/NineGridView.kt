package com.lodz.android.componentkt.widget.ninegrid

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
import com.lodz.android.componentkt.R
import com.lodz.android.componentkt.widget.rv.decoration.GridItemDecoration
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.createVibrator
import com.lodz.android.corekt.anko.getDrawableCompat
import com.lodz.android.corekt.anko.startScaleSelf
import java.util.*
import kotlin.collections.ArrayList

/**
 * 图片九宫格控件
 * Created by zhouL on 2018/12/25.
 */
open class NineGridView : FrameLayout {

    companion object {
        /** 默认图片最大数 */
        internal const val DEFAULT_MAX_PIC = 9
    }

    /** 默认列数 */
    private val DEFAULT_SPAN_COUNT = 3

    /** 列表 */
    private val mRecyclerView by bindView<RecyclerView>(R.id.recycler_view)
    /** 适配器 */
    private lateinit var mAdapter: NineGridAdapter
    /** 网格布局管理器 */
    private lateinit var mGridLayoutManager: GridLayoutManager

    /** 数据列表 */
    private var mDataList = ArrayList<String>()
    /** 是否需要拖拽 */
    private var isNeedDrag = false
    /** 是否需要拖拽震动提醒 */
    private var isNeedDragVibrate = true

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
        LayoutInflater.from(context).inflate(R.layout.componentkt_view_nine_grid, this)
        initRecyclerView()
        configLayout(attrs)
    }

    /** 配置属性 */
    private fun configLayout(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridView)
        }
        isNeedDrag = typedArray?.getBoolean(R.styleable.NineGridView_isNeedDrag, false) ?: false
        isNeedDragVibrate = typedArray?.getBoolean(R.styleable.NineGridView_isNeedDragVibrate, true) ?: true
        mAdapter.setNeedAddBtn(typedArray?.getBoolean(R.styleable.NineGridView_isNeedAddBtn, true) ?: true)
        mAdapter.setAddBtnDrawable(typedArray?.getDrawable(R.styleable.NineGridView_addBtnDrawable))
        mAdapter.setShowDelete(typedArray?.getBoolean(R.styleable.NineGridView_isShowDeleteBtn, true) ?: true)
        mAdapter.setDeleteBtnDrawable(typedArray?.getDrawable(R.styleable.NineGridView_deleteDrawable))
        mGridLayoutManager.spanCount = typedArray?.getInt(R.styleable.NineGridView_spanCount, DEFAULT_SPAN_COUNT) ?: DEFAULT_SPAN_COUNT
        mAdapter.setMaxPic(typedArray?.getInt(R.styleable.NineGridView_maxPic, DEFAULT_MAX_PIC) ?: DEFAULT_MAX_PIC)
        mAdapter.setItemHigh(typedArray?.getDimensionPixelSize(R.styleable.NineGridView_itemHigh, 0) ?: 0)
        if (typedArray != null) {
            typedArray.recycle()
        }
    }

    /** 拖拽回调  */
    private val mItemTouchHelperCallback = object : ItemTouchHelper.Callback() {
        // 配置拖拽类型
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = if (isNeedDrag && viewHolder is NineGridAdapter.NineGridViewHolder) {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            } else {
                0
            }
            val swipeFlags = 0
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }

        // 拖拽
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            if (mDataList.isEmpty()) {
                return false
            }
            if (!(viewHolder is NineGridAdapter.NineGridViewHolder) || !(target is NineGridAdapter.NineGridViewHolder)) {
                return false
            }

            // 得到拖动ViewHolder的Position
            val fromPosition = viewHolder.adapterPosition
            // 得到目标ViewHolder的Position
            val toPosition = target.adapterPosition

            if (fromPosition < toPosition) {//顺序小到大
                for (i in fromPosition until toPosition) {
                    Collections.swap(mDataList, i, i + 1)
                }
            } else {//顺序大到小
                for (i in fromPosition downTo (toPosition + 1)) {
                    Collections.swap(mDataList, i, i - 1)
                }
            }
            mAdapter.notifyItemMoved(fromPosition, toPosition)
            return true
        }

        // 滑动
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        // 当长按选中item时（拖拽开始时）调用
        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && isNeedDragVibrate) {
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
        mGridLayoutManager = GridLayoutManager(context, DEFAULT_SPAN_COUNT)
        mGridLayoutManager.orientation = RecyclerView.VERTICAL
        mAdapter = NineGridAdapter(context)
        mRecyclerView.layoutManager = mGridLayoutManager
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.addItemDecoration(GridItemDecoration.create(context).setDividerSpace(1).setDividerInt(Color.TRANSPARENT))
        mRecyclerView.adapter = mAdapter
        val itemTouchHelper = ItemTouchHelper(mItemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(mRecyclerView)
    }

    /** 设置是否需要添加图标[isNeed] */
    fun setNeedAddBtn(isNeed: Boolean) {
        mAdapter.setNeedAddBtn(isNeed)
        mAdapter.notifyDataSetChanged()
    }

    /** 设置添加图标[resId] */
    fun setAddBtnDrawable(@DrawableRes resId: Int) {
        mAdapter.setAddBtnDrawable(getDrawableCompat(resId))
        mAdapter.notifyDataSetChanged()
    }

    /** 设置是否显示删除按钮[isShow] */
    fun setShowDelete(isShow: Boolean) {
        mAdapter.setShowDelete(isShow)
        mAdapter.notifyDataSetChanged()
    }

    /** 设置删除图标[resId] */
    fun setDeleteBtnDrawable(@DrawableRes resId: Int) {
        mAdapter.setDeleteBtnDrawable(getDrawableCompat(resId))
        mAdapter.notifyDataSetChanged()
    }

    /** 设置最大图片数[count] */
    fun setMaxPic(@IntRange(from = 1) count: Int) {
        mAdapter.setMaxPic(count)
        clearData()
        mAdapter.notifyDataSetChanged()
    }

    /** 设置数据 */
    fun setData(data: ArrayList<String>) {
        mDataList = ArrayList()
        //如果数据大于最大图片数，则取前n位数据
        val length = if (data.size > mAdapter.getMaxPic()) mAdapter.getMaxPic() else data.size
        for (i in 0 until length) {
            mDataList.add(data.get(i))
        }
        mAdapter.setData(mDataList)
        mAdapter.notifyDataSetChanged()
    }

    /** 添加数据 */
    open fun addData(data: ArrayList<String>) {
        // 判断添加的数据长度和已有数据长度之和是否超过总长度
        val length = if ((data.size + mDataList.size) > mAdapter.getMaxPic()) mAdapter.getMaxPic() - mDataList.size else data.size
        for (i in 0 until length) {
            mDataList.add(data.get(i))
        }
        mAdapter.setData(mDataList)
        mAdapter.notifyDataSetChanged()
    }

    /** 删除数据 */
    open fun removeData(position: Int) {
        if (position < 0 || position >= mDataList.size) {
            return
        }
        mAdapter.notifyItemRemovedChanged(position)
        mRecyclerView.requestLayout()
    }

    /** 获取图片数据 */
    fun getPicData(): List<String> = mDataList

    /** 清空数据 */
    fun clearData() {
        setData(ArrayList())
    }

    /** 是否允许拖拽 */
    fun setNeedDrag(isNeed: Boolean) {
        isNeedDrag = isNeed
    }

    /** 是否允许拖拽震动提醒 */
    fun setNeedDragVibrate(isNeed: Boolean) {
        isNeedDragVibrate = isNeed
    }

    /** 设置监听器 */
    open fun setOnNineGridViewListener(listener: OnNineGridViewListener) {
        mAdapter.setOnNineGridViewListener(listener)
    }

}