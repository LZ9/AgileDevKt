package com.lodz.android.componentkt.widget.rv.drag

import android.content.Context
import androidx.recyclerview.widget.*
import com.lodz.android.corekt.anko.createVibrator
import com.lodz.android.corekt.anko.getSize
import com.lodz.android.corekt.anko.hasVibrator
import java.util.*

/**
 * 拖拽帮助回调
 * Created by zhouL on 2018/12/3.
 */
open class DragHelperCallback<T> : ItemTouchHelper.Callback() {

    /** 允许拖拽  */
    private var mContext: Context? = null

    /** 允许拖拽  */
    private var mUseDrag = true
    /** 允许从右往左滑动  */
    private var mUseRightToLeftSwipe = true
    /** 允许从左往右滑动  */
    private var mUseLeftToRightSwipe = true
    /** 启用长按拖拽效果  */
    private var isLongPressDrag = true
    /** 启用滑动效果  */
    private var isSwipe = true
    /** 启用震动效果  */
    private var isVibrate = false

    /** 监听器  */
    private var mListener: ((list: List<T>) -> Unit)? = null// 列表数据
    /** 数据列表  */
    private var mList: MutableList<T>? = null
    /** 适配器  */
    private var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null

    /** 设置上下文[context] */
    fun setContext(context: Context) {
        this.mContext = context
    }

    /** 设置允许拖拽[useDrag] */
    fun setUseDrag(useDrag: Boolean) {
        this.mUseDrag = useDrag
    }

    /** 设置允许从右往左滑动[rightToLeftSwipe] */
    fun setUseRightToLeftSwipe(rightToLeftSwipe: Boolean) {
        this.mUseRightToLeftSwipe = rightToLeftSwipe
    }

    /** 设置允许从左往右滑动[leftToRightSwipe] */
    fun setUseLeftToRightSwipe(leftToRightSwipe: Boolean) {
        this.mUseLeftToRightSwipe = leftToRightSwipe
    }

    /** 设置启用长按拖拽效果[enabled] */
    fun setLongPressDrag(enabled: Boolean) {
        isLongPressDrag = enabled
    }

    /** 设置启用滑动效果[enabled] */
    fun setSwipe(enabled: Boolean) {
        isSwipe = enabled
    }

    /** 设置启用震动效果[enabled] */
    fun setVibrate(enabled: Boolean) {
        isVibrate = enabled
    }

    /** 设置适配器[adapter] */
    fun setAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        this.mAdapter = adapter
    }

    /** 设置监听器[listener] */
    fun setListener(listener: ((list: List<T>) -> Unit)?) {
        this.mListener = listener
    }

    /** 设置数据列表[list] */
    fun setList(list: MutableList<T>?) {
        this.mList = list
    }

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
        if (mAdapter != null) {
            mAdapter!!.notifyItemMoved(fromPosition, toPosition)
        }
        mListener?.invoke(mList!!)
        return true
    }

    // 滑动
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (mList == null) {
            return
        }
        val position = viewHolder.adapterPosition
        mList!!.removeAt(position)
        if (mAdapter != null) {
            mAdapter!!.notifyItemRemoved(position)
            if (position != mList.getSize()) { // 如果移除的是最后一个，忽略
                mAdapter!!.notifyItemRangeChanged(position, mList.getSize() - position)
            }
        }
        mListener?.invoke(mList!!)
    }

    // 当长按选中item时（拖拽开始时）调用
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (isVibrate && mContext != null) {
                if (mContext!!.hasVibrator()) {
                    mContext!!.createVibrator(100)//长按震动
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
        return isLongPressDrag
    }

    // 启用滑动效果
    override fun isItemViewSwipeEnabled(): Boolean {
        return isSwipe
    }
}