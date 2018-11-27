package com.lodz.android.componentkt.widget.rv.recycler

import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.recyclerview.widget.*
import com.lodz.android.corekt.anko.createVibrator
import com.lodz.android.corekt.anko.getSize
import com.lodz.android.corekt.anko.hasVibrator
import java.util.*

/**
 * RecyclerView拖拽帮助类
 * Created by zhouL on 2018/11/21.
 */
class RecyclerViewDragHelper<T>(val mContext: Context) {

    /** 允许拖拽  */
    private var mUseDrag = true
    /** 允许从右往左滑动  */
    private var mUseRightToLeftSwipe = true
    /** 允许从左往右滑动  */
    private var mUseLeftToRightSwipe = true
    /** 启用长按拖拽效果  */
    private var isLongPressDragEnabled = true
    /** 启用滑动效果  */
    private var isSwipeEnabled = true
    /** 启用震动效果  */
    private var isVibrateEnabled = false

    /** 监听器  */
    private var mListener: ((List<T>) -> Unit)? = null// 列表数据
    /** 数据列表  */
    private var mList: MutableList<T>? = null
    /** 适配器  */
    private lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private lateinit var mItemTouchHelper: ItemTouchHelper

    /** 设置是否允许拖拽[useDrag] */
    fun setUseDrag(useDrag: Boolean): RecyclerViewDragHelper<*> {
        this.mUseDrag = useDrag
        return this
    }

    /** 设置允许从右往左滑动[rightToLeftSwipe] */
    fun setUseRightToLeftSwipe(rightToLeftSwipe: Boolean): RecyclerViewDragHelper<*> {
        this.mUseRightToLeftSwipe = rightToLeftSwipe
        return this
    }

    /** 设置允许从左往右滑动[leftToRightSwipe] */
    fun setUseLeftToRightSwipe(leftToRightSwipe: Boolean): RecyclerViewDragHelper<*> {
        this.mUseLeftToRightSwipe = leftToRightSwipe
        return this
    }

    /** 设置是否启用[enabled]长按拖拽效果 */
    fun setLongPressDragEnabled(enabled: Boolean): RecyclerViewDragHelper<*> {
        isLongPressDragEnabled = enabled
        return this
    }

    /** 设置是否启用[enabled]滑动效果 */
    fun setSwipeEnabled(enabled: Boolean): RecyclerViewDragHelper<*> {
        isSwipeEnabled = enabled
        return this
    }

    /** 是否启用[enabled]震动效果 */
    @RequiresPermission(android.Manifest.permission.VIBRATE)
    fun setVibrateEnabled(enabled: Boolean): RecyclerViewDragHelper<*> {
        isVibrateEnabled = enabled
        return this
    }

    /** 完成构建，控件[recyclerView]，适配器[adapter] */
    fun build(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        mItemTouchHelper = ItemTouchHelper(mItemTouchHelperCallback)
        mItemTouchHelper.attachToRecyclerView(recyclerView)
        mAdapter = adapter
    }

    /** 设置数据列表[list] */
    fun setList(list: MutableList<T>) {
        this.mList = list
    }

    /** 设置监听器 */
    fun setListener(listener: (List<T>) -> Unit) {
        this.mListener = listener
    }

    /** 获取ItemTouchHelper */
    fun getItemTouchHelper(): ItemTouchHelper = mItemTouchHelper

    private val mItemTouchHelperCallback = object : ItemTouchHelper.Callback() {

        // 配置拖拽类型
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags: Int
            val swipeFlags: Int
            if (recyclerView.layoutManager is GridLayoutManager) {// 网格布局
                dragFlags = if (mUseDrag) ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT else 0
                swipeFlags = 0
            } else if (recyclerView.layoutManager is LinearLayoutManager) {// 线性布局
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (layoutManager.orientation == RecyclerView.VERTICAL) {//纵向
                    dragFlags = if (mUseDrag) ItemTouchHelper.UP or ItemTouchHelper.DOWN else 0
                    swipeFlags = (if (mUseRightToLeftSwipe) ItemTouchHelper.START else 0) or // START允许从右往左
                            (if (mUseLeftToRightSwipe) ItemTouchHelper.END else 0)// END允许从左往右
                } else {//横向
                    dragFlags = if (mUseDrag) ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT else 0
                    swipeFlags = 0//横向不允许侧滑
                }
            } else if (recyclerView.layoutManager is StaggeredGridLayoutManager) {// 瀑布流布局
                dragFlags = if (mUseDrag) ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT else 0
                swipeFlags = 0
            } else {// 其他布局
                dragFlags = if (mUseDrag) ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT else 0
                swipeFlags = (if (mUseRightToLeftSwipe) ItemTouchHelper.START else 0) or // START允许从右往左
                        (if (mUseLeftToRightSwipe) ItemTouchHelper.END else 0)// END允许从左往右
            }
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }

        // 拖拽
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            if (mList == null) {
                return false
            }
            // 得到拖动ViewHolder的Position
            val fromPosition = viewHolder.adapterPosition
            // 得到目标ViewHolder的Position
            val toPosition = target.adapterPosition

            if (fromPosition < toPosition) {//顺序小到大
                for (i in fromPosition until toPosition) {
                    Collections.swap(mList, i, i + 1)
                }
            } else {//顺序大到小
                for (i in fromPosition downTo (toPosition + 1)) {
                    Collections.swap(mList, i, i - 1)
                }
            }
            mAdapter.notifyItemMoved(fromPosition, toPosition)
            val listener = mListener
            if (listener != null) {
                listener.invoke(mList!!)
            }
            return true
        }

        // 滑动
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (mList == null) {
                return
            }
            val position = viewHolder.adapterPosition
            mList!!.removeAt(position)
            mAdapter.notifyItemRemoved(position)
            if (position != mList.getSize()) { // 如果移除的是最后一个，忽略
                mAdapter.notifyItemRangeChanged(position, mList.getSize() - position)
            }
            val listener = mListener
            if (listener != null) {
                listener.invoke(mList!!)
            }
        }

        // 当长按选中item时（拖拽开始时）调用
        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                if (isVibrateEnabled) {
                    if (mContext.hasVibrator()) {
                        mContext.createVibrator(100)//长按震动
                    }
                }
            }
            super.onSelectedChanged(viewHolder, actionState)
        }

        // 当手指松开时（拖拽完成时）调用
        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            // do something
        }

        // 是否启用长按拖拽效果
        override fun isLongPressDragEnabled(): Boolean {
            return this@RecyclerViewDragHelper.isLongPressDragEnabled
        }

        // 启用滑动效果
        override fun isItemViewSwipeEnabled(): Boolean {
            return this@RecyclerViewDragHelper.isSwipeEnabled
        }
    }

}