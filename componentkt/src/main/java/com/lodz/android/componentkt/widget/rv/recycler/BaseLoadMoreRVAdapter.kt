package com.lodz.android.componentkt.widget.rv.recycler

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lodz.android.corekt.anko.getSize

/**
 * RecyclerView加载更多基类适配器
 * Created by zhouL on 2018/11/20.
 */
abstract class BaseLoadMoreRVAdapter<T>(context: Context) : BaseRecyclerViewAdapter<T>(context) {

    /** 列表内容  */
    protected val VIEW_TYPE_ITEM = 0
    /** 正在加载更多  */
    protected val VIEW_TYPE_LOADING_MORE = 1
    /** 已加载完全部数据  */
    protected val VIEW_TYPE_LOAD_FINISH = 2
    /** 加载失败  */
    protected val VIEW_TYPE_LOAD_FAIL = 3
    /** 隐藏数据  */
    protected val VIEW_TYPE_HIDE_ITEM = 4

    /** 总条数  */
    private var mSumSize = 0
    /** 每页条数  */
    private var mSize = 0
    /** 当前为第一页  */
    private var mPage = 1
    /** 预加载偏移量，默认滑动到倒数第3个item时就回调加载接口  */
    private var mLoadIndex = 3
    /** 是否启动加载更多  */
    private var isLoadMore = false
    /** 是否显示底部提示界面  */
    private var isShowBottomLayout = false
    /** 是否显示加载失败页面  */
    private var isShowLoadFail = false
    /** 存放需要隐藏位置的position  */
    private var mHidePositionList: MutableList<Int>? = null

    /** 加载更多回调 */
    private var mOnLoadMoreListener: ((Int, Int, Int, Int) -> Unit)? = null// 当前页码 , 需要加载的页码 , 每页大小 , 回调位置
    /** 加载失败回调 */
    private var mOnLoadFailClickListener: ((Int, Int) -> Unit)? = null// 需要重载的页码 , 每页大小
    /** 所有item都隐藏回调 */
    private var mOnAllItemHideListener: (() -> Unit)? = null

    /** 是否是GridLayoutManager  */
    protected var isGridLayoutManager = false

    override fun getItemViewType(position: Int): Int {
        if (isShowBottomLayout && (position == getItemCount() - 1)) {// 启用底部提示页面 && 滑动到底部
            if (isShowLoadFail) {
                return VIEW_TYPE_LOAD_FAIL
            }
            if (mSumSize > getListItemCount()) {
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
        val list = mHidePositionList
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_LOAD_FAIL) {
            return getLoadFailViewHolder(parent)
        }
        if (viewType == VIEW_TYPE_LOADING_MORE) {
            return getLoadingMoreViewHolder(parent)
        }
        if (viewType == VIEW_TYPE_LOAD_FINISH) {
            return getLoadFinishViewHolder(parent)
        }
        if (viewType == VIEW_TYPE_HIDE_ITEM) {
            return getBlankViewHolder(parent)
        }
        return getItemViewHolder(parent, viewType)
    }

    /** 获取加载完毕的ViewHolder */
    private fun getLoadFinishViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            LoadFinishViewHolder(getLayoutView(parent, getLoadFinishLayoutId()))

    /** 获取正在加载更多的ViewHolder */
    private fun getLoadingMoreViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            LoadingMoreViewHolder(getLayoutView(parent, getLoadingMoreLayoutId()))

    /** 获取加载失败的ViewHolder */
    private fun getLoadFailViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            LoadFailViewHolder(getLayoutView(parent, getLoadFailLayoutId()))

    /** 获取空白占位的ViewHolder */
    private fun getBlankViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            BlankViewHolder(LinearLayout(parent.context))

    /** 获取加载完毕的LayoutId */
    @LayoutRes
    abstract fun getLoadFinishLayoutId(): Int

    /** 获取加载完毕的LayoutId */
    @LayoutRes
    abstract fun getLoadingMoreLayoutId(): Int

    /** 获取加载失败的LayoutId */
    @LayoutRes
    abstract fun getLoadFailLayoutId(): Int

    abstract fun getItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BaseLoadMoreRVAdapter<*>.LoadFinishViewHolder) {
            showLoadFinish(holder)
        } else if (holder is BaseLoadMoreRVAdapter<*>.LoadingMoreViewHolder) {
            showLoadingMore(holder)
        } else if (holder is BaseLoadMoreRVAdapter<*>.LoadFailViewHolder) {
            holder.itemView.setOnClickListener {
                val listener = mOnLoadFailClickListener
                if (listener != null) {
                    listener.invoke(mPage + 1, mSize)
                }
            }
            showLoadFail(holder)
        } else if (holder is BaseLoadMoreRVAdapter<*>.BlankViewHolder) {
            // 空白占位不需要操作
        } else {
            super.onBindViewHolder(holder, position)
        }
        handleLoadMore(position)
    }

    /** 显示正在加载界面 */
    abstract fun showLoadingMore(holder: RecyclerView.ViewHolder)

    /** 显示加载完毕界面 */
    abstract fun showLoadFinish(holder: RecyclerView.ViewHolder)

    /** 显示加载失败界面 */
    abstract fun showLoadFail(holder: RecyclerView.ViewHolder)

    /** 处理加载更多，[position]当前滚动位置 */
    private fun handleLoadMore(position: Int) {
        if (getListItemCount() >= mSumSize) {// 已经全部加载完成
            return
        }
        if (getListItemCount() > mSize * mPage) {// 已加载的数据总数大于预测总数则把当前页+1
            mPage++
        }
        // 计算预加载item的偏移量
        val loadIndex = if (mSize - mLoadIndex > 0) mLoadIndex else 0

        if ((getListItemCount() - loadIndex) == (position + 1) && isLoadMore && !isShowLoadFail) {
            val listener = mOnLoadMoreListener
            if (listener != null) {
                listener.invoke(mPage, (mPage + 1), mSize, position)
            }
        }
    }

    /** 获取数据列表Item数量 */
    private fun getListItemCount(): Int = super.getItemCount()

    override fun getItemCount(): Int = if (isShowBottomLayout) super.getItemCount() + 1 else super.getItemCount()

    /** 设置加载更多参数，[sumSize]总条数，[size]每页条数，[isShowBottomLayout]是否显示底部提示界面 */
    fun setLoadMoreParam(sumSize: Int, size: Int, isShowBottomLayout: Boolean) {
        this.mSumSize = sumSize
        this.mSize = size
        this.isShowBottomLayout = isShowBottomLayout
        this.mPage = 1
        if (mHidePositionList != null) {
            mHidePositionList!!.clear()
            mHidePositionList = null
        }
        this.mHidePositionList = ArrayList()
    }

    /** 预加载偏移量，滑动到倒数第[index]个item时就回调加载接口（默认值为3） */
    fun setLoadIndex(index: Int) {
        this.mLoadIndex = index
    }

    /** 手动设置加载完成 */
    fun setLoadCompleted() {
        this.mSumSize = getListItemCount()
    }

    /** 是否开启加载更多 */
    fun isLoadMore(): Boolean = isLoadMore

    /** 设置加载更多[isLoadMore]（正在加载时可以把开关关掉，加载完毕后再打开） */
    fun setIsLoadMore(isLoadMore: Boolean) {
        this.isLoadMore = isLoadMore
    }

    /** 是否处于加载失败状态 */
    fun isShowLoadFail(): Boolean = isShowLoadFail

    /** 设置是否进入加载失败状态[isShowLoadFail] */
    fun setIsShowLoadFail(isShowLoadFail: Boolean) {
        this.isShowLoadFail = isShowLoadFail
    }

    /** 隐藏某个位置[position]的Item */
    fun hideItem(position: Int) {
        if (mHidePositionList != null) {
            mHidePositionList!!.add(position)
            val listener = mOnAllItemHideListener
            if (mHidePositionList.getSize() == mSumSize && listener != null) {// 隐藏的item数等于总数
                listener.invoke()
            }
        }
    }

    /** 设置加载更多监听器[listener]，回调：当前页码[currentPage] , 需要加载的页码[nextPage] , 每页大小[size] , 回调位置[position] */
    fun setOnLoadMoreListener(listener: (Int, Int, Int, Int) -> Unit) {
        this.mOnLoadMoreListener = listener
    }

    /** 设置加载失败点击监听器[listener]，回调：需要重载的页码[reloadPage] , 每页大小[size] */
    fun setOnLoadFailClickListener(listener: (Int, Int) -> Unit) {
        this.mOnLoadFailClickListener = listener
    }

    /** 设置所有item隐藏后的回调监听器[listener] */
    fun setOnAllItemHideListener(listener: () -> Unit) {
        this.mOnAllItemHideListener = listener
    }

    /** 加载完毕布局的ViewHolder */
    private inner class LoadFinishViewHolder(view: View) : RecyclerView.ViewHolder(view)

    /** 加载更多布局的ViewHolder */
    private inner class LoadingMoreViewHolder(view: View) : RecyclerView.ViewHolder(view)

    /** 加载失败布局的ViewHolder */
    private inner class LoadFailViewHolder(view: View) : RecyclerView.ViewHolder(view)

    /** 空白的ViewHolder */
    private inner class BlankViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        isGridLayoutManager = manager is GridLayoutManager// 网格布局时要优化加载排版
        if (isGridLayoutManager) {
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
                val size = if (isShowBottomLayout) layoutManager.itemCount - 1 else layoutManager.itemCount
                if ((position + 1) == size) {// 滚到底部
                    return layoutManager.spanCount - position % layoutManager.spanCount
                }
                return 1
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        adapterStaggeredGridLayoutManager(holder)
    }

    /** 适配StaggeredGridLayoutManager */
    private fun adapterStaggeredGridLayoutManager(holder: RecyclerView.ViewHolder) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            if (holder.itemViewType == VIEW_TYPE_LOADING_MORE || holder.itemViewType == VIEW_TYPE_LOAD_FINISH
                    || holder.itemViewType == VIEW_TYPE_LOAD_FAIL) {//item的类型是加载更多 || 加载完成 || 加载失败时
                layoutParams.isFullSpan = true
            }
        }
    }
}