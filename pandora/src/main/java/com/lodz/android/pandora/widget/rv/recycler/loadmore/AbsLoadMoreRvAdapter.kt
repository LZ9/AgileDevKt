package com.lodz.android.pandora.widget.rv.recycler.loadmore

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lodz.android.corekt.anko.getSize
import com.lodz.android.pandora.widget.rv.recycler.base.AbsRvAdapter

/**
 * RecyclerView加载更多基类适配器
 * Created by zhouL on 2018/11/20.
 */
abstract class AbsLoadMoreRvAdapter<T, VH : RecyclerView.ViewHolder>(context: Context) : AbsRvAdapter<T, VH>(context) {

    companion object {
        /** 列表内容 */
        internal const val VIEW_TYPE_ITEM = 0
        /** 正在加载更多 */
        internal const val VIEW_TYPE_LOADING_MORE = 1
        /** 已加载完全部数据 */
        internal const val VIEW_TYPE_LOAD_FINISH = 2
        /** 加载失败 */
        internal const val VIEW_TYPE_LOAD_FAIL = 3
        /** 隐藏数据 */
        internal const val VIEW_TYPE_HIDE_ITEM = 4
    }

    /** 总条数 */
    private var mPdrSumSize = 0
    /** 每页条数 */
    private var mPdrSize = 0
    /** 当前为第一页 */
    private var mPdrPage = 1
    /** 预加载偏移量，默认滑动到倒数第3个item时就回调加载接口 */
    private var mPdrLoadIndex = 3
    /** 是否启动加载更多 */
    private var isPdrLoadMore = false
    /** 是否显示底部提示界面 */
    private var isPdrShowBottomLayout = false
    /** 是否显示加载失败页面 */
    private var isPdrShowLoadFail = false
    /** 存放需要隐藏位置的position */
    private var mPdrHidePositionList: MutableList<Int>? = null

    /** 加载更多回调 */
    private var mPdrOnLoadMoreListener: ((currentPage: Int, nextPage: Int, size: Int, position: Int) -> Unit)? = null
    /** 加载失败回调 */
    private var mPdrOnLoadFailClickListener: ((reloadPage: Int, size: Int) -> Unit)? = null
    /** 所有item都隐藏回调 */
    private var mPdrOnAllItemHideListener: (() -> Unit)? = null

    /** 是否是GridLayoutManager */
    protected var isPdrGridLayoutManager = false

    override fun getItemViewType(position: Int): Int {
        if (isPdrShowBottomLayout && (position == itemCount - 1)) {// 启用底部提示页面 && 滑动到底部
            if (isPdrShowLoadFail) {
                return VIEW_TYPE_LOAD_FAIL
            }
            if (mPdrSumSize > getListItemCount()) {
                return VIEW_TYPE_LOADING_MORE
            }
            return VIEW_TYPE_LOAD_FINISH
        }
        if (isHidePosition(position)) {// 需要隐藏的数据
            return VIEW_TYPE_HIDE_ITEM
        }
        return VIEW_TYPE_ITEM
    }

    /** 当前的[position]是否是需要隐藏的 */
    private fun isHidePosition(position: Int): Boolean {
        val list = mPdrHidePositionList
        if (list.isNullOrEmpty()) {
            return false
        }
        for (p in list) {
            if (p == position) {
                return true
            }
        }
        return false
    }

    /** 显示正在加载界面 */
    abstract fun showLoadingMore(holder: VH)

    /** 显示加载完毕界面 */
    abstract fun showLoadFinish(holder: VH)

    /** 显示加载失败界面 */
    abstract fun showLoadFail(holder: VH)

    protected fun handleLoadFail(){
        mPdrOnLoadFailClickListener?.invoke(mPdrPage + 1, mPdrSize)
    }

    /** 处理加载更多，[position]当前滚动位置 */
    protected fun handleLoadMore(position: Int) {
        if (getListItemCount() >= mPdrSumSize) {// 已经全部加载完成
            return
        }
        if (getListItemCount() > mPdrSize * mPdrPage) {// 已加载的数据总数大于预测总数则把当前页+1
            mPdrPage++
        }
        // 计算预加载item的偏移量
        val loadIndex = if (mPdrSize - mPdrLoadIndex > 0) mPdrLoadIndex else 0

        if ((getListItemCount() - loadIndex) == (position + 1) && isPdrLoadMore && !isPdrShowLoadFail) {
            mPdrOnLoadMoreListener?.invoke(mPdrPage, (mPdrPage + 1), mPdrSize, position)
        }
    }

    /** 获取数据列表Item数量 */
    private fun getListItemCount(): Int = super.getItemCount()

    override fun getItemCount(): Int = if (isPdrShowBottomLayout) super.getItemCount() + 1 else super.getItemCount()

    /** 设置加载更多参数，[sumSize]总条数，[size]每页条数，[isShowBottomLayout]是否显示底部提示界面 */
    fun setLoadMoreParam(sumSize: Int, size: Int, isShowBottomLayout: Boolean) {
        this.mPdrSumSize = sumSize
        this.mPdrSize = size
        this.isPdrShowBottomLayout = isShowBottomLayout
        this.mPdrPage = 1
        mPdrHidePositionList?.clear()
        mPdrHidePositionList = null
        this.mPdrHidePositionList = ArrayList()
    }

    /** 预加载偏移量，滑动到倒数第[index]个item时就回调加载接口（默认值为3） */
    fun setLoadIndex(index: Int) {
        this.mPdrLoadIndex = index
    }

    /** 手动设置加载完成 */
    fun setLoadCompleted() {
        this.mPdrSumSize = getListItemCount()
    }

    /** 是否开启加载更多 */
    fun isLoadMore(): Boolean = isPdrLoadMore

    /** 设置加载更多[isLoadMore]（正在加载时可以把开关关掉，加载完毕后再打开） */
    fun setIsLoadMore(isLoadMore: Boolean) {
        this.isPdrLoadMore = isLoadMore
    }

    /** 是否处于加载失败状态 */
    fun isShowLoadFail(): Boolean = isPdrShowLoadFail

    /** 设置是否进入加载失败状态[isShowLoadFail] */
    fun setIsShowLoadFail(isShowLoadFail: Boolean) {
        this.isPdrShowLoadFail = isShowLoadFail
    }

    /** 隐藏某个位置[position]的Item */
    fun hideItem(position: Int) {
        mPdrHidePositionList?.add(position)
        if (mPdrHidePositionList.getSize() == mPdrSumSize) {// 隐藏的item数等于总数
            mPdrOnAllItemHideListener?.invoke()
        }
    }

    /** 设置加载更多监听器[listener]，回调：当前页码[currentPage] , 需要加载的页码[nextPage] , 每页大小[size] , 回调位置[position] */
    fun setOnLoadMoreListener(listener: (currentPage: Int, nextPage: Int, size: Int, position: Int) -> Unit) {
        this.mPdrOnLoadMoreListener = listener
    }

    /** 设置加载失败点击监听器[listener]，回调：需要重载的页码[reloadPage] , 每页大小[size] */
    fun setOnLoadFailClickListener(listener: (reloadPage: Int, size: Int) -> Unit) {
        this.mPdrOnLoadFailClickListener = listener
    }

    protected fun getPdrOnLoadFailClickListener() = mPdrOnLoadFailClickListener

    /** 设置所有item隐藏后的回调监听器[listener] */
    fun setOnAllItemHideListener(listener: () -> Unit) {
        this.mPdrOnAllItemHideListener = listener
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        isPdrGridLayoutManager = manager is GridLayoutManager// 网格布局时要优化加载排版
        if (isPdrGridLayoutManager) {
            adapterGridLayoutManager(manager as GridLayoutManager)
        }
    }

    /** 适配GridLayoutManager */
    private fun adapterGridLayoutManager(layoutManager: GridLayoutManager) {
        if (layoutManager.orientation == RecyclerView.HORIZONTAL) {
            return
        }
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // 如果开启底部加载提示则需要减去一个item数量
                val size = if (isPdrShowBottomLayout) layoutManager.itemCount - 1 else layoutManager.itemCount
                if ((position + 1) == size) {// 滚到底部
                    return layoutManager.spanCount - position % layoutManager.spanCount
                }
                return 1
            }
        }
    }

    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
        adapterStaggeredGridLayoutManager(holder)
    }

    /** 适配StaggeredGridLayoutManager */
    private fun adapterStaggeredGridLayoutManager(holder: VH) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            if (holder.itemViewType == VIEW_TYPE_LOADING_MORE || holder.itemViewType == VIEW_TYPE_LOAD_FINISH
                    || holder.itemViewType == VIEW_TYPE_LOAD_FAIL
            ) {//item的类型是加载更多 || 加载完成 || 加载失败时
                layoutParams.isFullSpan = true
            }
        }
    }
}