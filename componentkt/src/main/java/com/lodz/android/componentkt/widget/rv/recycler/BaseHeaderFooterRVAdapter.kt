package com.lodz.android.componentkt.widget.rv.recycler

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * 带Header和Footer的RecyclerView
 * Created by zhouL on 2018/11/21.
 */
abstract class BaseHeaderFooterRVAdapter<H, T, F>(context: Context) : BaseRecyclerViewAdapter<T>(context) {

    /** 头部  */
    protected val VIEW_TYPE_HEADER = 0
    /** 数据列表  */
    protected val VIEW_TYPE_ITEM = 1
    /** 底部  */
    protected val VIEW_TYPE_FOOTER = 2

    /** 头信息数据 */
    private var mHeaderData: H? = null

    /** 头信息数据 */
    private var mFooterData: F? = null

    /** 头部点击 */
    private var mOnHeaderClickListener: ((holder: RecyclerView.ViewHolder, data: H, position: Int) -> Unit)? = null
    /** 头部长按 */
    private var mOnHeaderLongClickListener: ((holder: RecyclerView.ViewHolder, data: H, position: Int) -> Unit)? = null
    /** 底部点击 */
    private var mOnFooterClickListener: ((holder: RecyclerView.ViewHolder, data: F, position: Int) -> Unit)? = null
    /** 底部长按 */
    private var mOnFooterLongClickListener: ((holder: RecyclerView.ViewHolder, data: F, position: Int) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        if (mHeaderData != null) {
            if (position == 0) {
                return VIEW_TYPE_HEADER
            }
        }
        if (mFooterData != null) {
            if (position == getItemCount() - 1) {
                return VIEW_TYPE_FOOTER
            }
        }
        return VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_HEADER) {
            return getHeaderViewHolder(parent)
        }
        if (viewType == VIEW_TYPE_FOOTER) {
            return getFooterViewHolder(parent)
        }
        return getItemViewHolder(parent, viewType)
    }

    /** 获取头布局的ViewHolder */
    abstract fun getHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    /** 获取列表布局的ViewHolder */
    abstract fun getItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /** 获取头布局的ViewHolder */
    abstract fun getFooterViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    /** 设置头部数据[headerData] */
    fun setHeaderData(headerData: H?) {
        mHeaderData = headerData
    }

    /** 设置底部数据[footerData] */
    fun setFooterData(footerData: F?) {
        mFooterData = footerData
    }

    override fun getItemCount(): Int {
        var count = super.getItemCount()//列表数据的数量
        if (mHeaderData != null) {
            count += 1
        }
        if (mFooterData != null) {
            count += 1
        }
        return count
    }

    /** 获取头部数据 */
    fun getHeaderData(): H? = mHeaderData

    /** 获取底部数据 */
    fun getFooterData(): F? = mFooterData

    override fun getItem(position: Int): T? {
        if (mHeaderData != null) {
            return super.getItem(position - 1)
        }
        return super.getItem(position)
    }

    override fun setItemClick(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener { view ->
            if (mHeaderData == null && mFooterData == null) {//没有头部 没有底部
                val item = getItem(position)
                if (item != null) {
                    mOnItemClickListener?.invoke(holder, item, position)
                }
                return@setOnClickListener
            }
            if (mHeaderData != null && mFooterData == null) {// 有头部 没有底部
                if (position == 0) {
                    mOnHeaderClickListener?.invoke(holder, mHeaderData!!, position)
                    return@setOnClickListener
                }
                val item = getItem(position)
                if (item != null) {
                    mOnItemClickListener?.invoke(holder, item, position - 1)
                }
                return@setOnClickListener
            }

            if (mHeaderData == null) {// 没有头部 有底部
                if (position == getItemCount() - 1) {
                    mOnFooterClickListener?.invoke(holder, mFooterData!!, position)
                    return@setOnClickListener
                }
                val item = getItem(position)
                if (item != null) {
                    mOnItemClickListener?.invoke(holder, item, position)
                }
                return@setOnClickListener
            }

            // 有头部 有底部
            if (position == 0) {
                mOnHeaderClickListener?.invoke(holder, mHeaderData!!, position)
                return@setOnClickListener
            }
            if (position == getItemCount() - 1) {
                mOnFooterClickListener?.invoke(holder, mFooterData!!, position)
                return@setOnClickListener
            }
            val item = getItem(position)
            if (item != null) {
                mOnItemClickListener?.invoke(holder, item, position - 1)
            }
        }
    }

    override fun setItemLongClick(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnLongClickListener { view ->
            if (mHeaderData == null && mFooterData == null) {//没有头部 没有底部
                val item = getItem(position)
                if (item != null) {
                    mOnItemLongClickListener?.invoke(holder, item, position)
                }
                return@setOnLongClickListener true
            }

            if (mHeaderData != null && mFooterData == null) {// 有头部 没有底部
                if (position == 0) {
                    mOnHeaderLongClickListener?.invoke(holder, mHeaderData!!, position)
                    return@setOnLongClickListener true
                }
                val item = getItem(position)
                if (item != null) {
                    mOnItemLongClickListener?.invoke(holder, item, position - 1)
                }
                return@setOnLongClickListener true
            }

            if (mHeaderData == null) {// 没有头部 有底部
                if (position == getItemCount() - 1) {
                    mOnFooterLongClickListener?.invoke(holder, mFooterData!!, position)
                    return@setOnLongClickListener true
                }
                val item = getItem(position)
                if (item != null) {
                    mOnItemLongClickListener?.invoke(holder, item, position)
                }
                return@setOnLongClickListener true
            }

            // 有头部 有底部
            if (position == 0) {
                mOnHeaderLongClickListener?.invoke(holder, mHeaderData!!, position)
            }
            if (position == getItemCount() - 1) {
                mOnFooterLongClickListener?.invoke(holder, mFooterData!!, position)
            }
            if (mOnItemLongClickListener != null) {
                val item = getItem(position)
                if (item != null) {
                    mOnItemLongClickListener?.invoke(holder, item, position - 1)
                }
            }
            return@setOnLongClickListener true
        }
    }

    /** 设置头部点击事件监听器 */
    fun setOnHeaderClickListener(listener: (holder: RecyclerView.ViewHolder, data: H, position: Int) -> Unit) {
        mOnHeaderClickListener = listener
    }

    /** 设置头部长按事件监听器 */
    fun setOnHeaderLongClickListener(listener: (holder: RecyclerView.ViewHolder, data: H, position: Int) -> Unit) {
        mOnHeaderLongClickListener = listener
    }

    /** 设置底部点击事件监听器 */
    fun setOnFooterClickListener(listener: (holder: RecyclerView.ViewHolder, data: F, position: Int) -> Unit) {
        mOnFooterClickListener = listener
    }

    /** 设置底部长按事件监听器 */
    fun setOnFooterLongClickListener(listener: (holder: RecyclerView.ViewHolder, data: F, position: Int) -> Unit) {
        mOnFooterLongClickListener = listener
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {// 网格布局时要优化加载排版
            adapterGridLayoutManager(manager)
        }
    }

    /** 适配GridLayoutManager */
    private fun adapterGridLayoutManager(layoutManager: GridLayoutManager) {
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (mHeaderData == null && mFooterData == null) {//没有头部 没有底部
                    return 1
                }
                if (mHeaderData != null && mFooterData == null) {// 有头部 没有底部
                    return if (position == 0) layoutManager.spanCount else 1
                }
                if (mHeaderData == null) {// 没有头部 有底部
                    return if (position == getItemCount() - 1) layoutManager.spanCount else 1
                }
                // 有头部 有底部
                if (position == 0 || position == getItemCount() - 1) {
                    return layoutManager.spanCount
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
            if (holder.itemViewType == VIEW_TYPE_HEADER || holder.itemViewType == VIEW_TYPE_FOOTER) {//item的类型是头部 || 底部
                layoutParams.isFullSpan = true
            }
        }
    }
}