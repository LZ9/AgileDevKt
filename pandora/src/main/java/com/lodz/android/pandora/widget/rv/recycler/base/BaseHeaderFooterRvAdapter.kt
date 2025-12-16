package com.lodz.android.pandora.widget.rv.recycler.base

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.lodz.android.pandora.databinding.PandoraItemBlankBinding

/**
 * 带Header和Footer的RecyclerView
 * Created by zhouL on 2018/11/21.
 */
abstract class BaseHeaderFooterRvAdapter<H, T, F>(context: Context) : BaseRvAdapter<T>(context) {

    companion object {
        /** 头部 */
        protected const val VIEW_TYPE_HEADER = 0
        /** 数据列表 */
        protected const val VIEW_TYPE_ITEM = 1
        /** 底部 */
        protected const val VIEW_TYPE_FOOTER = 2
    }

    /** 头信息数据 */
    private var mPdrHeaderData: H? = null

    /** 头信息数据 */
    private var mPdrFooterData: F? = null

    /** 头部点击 */
    private var mPdrOnHeaderClickListener: ((holder: RecyclerView.ViewHolder, data: H, position: Int) -> Unit)? = null
    /** 头部长按 */
    private var mPdrOnHeaderLongClickListener: ((holder: RecyclerView.ViewHolder, data: H, position: Int) -> Unit)? = null
    /** 底部点击 */
    private var mPdrOnFooterClickListener: ((holder: RecyclerView.ViewHolder, data: F, position: Int) -> Unit)? = null
    /** 底部长按 */
    private var mPdrOnFooterLongClickListener: ((holder: RecyclerView.ViewHolder, data: F, position: Int) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        if (mPdrHeaderData != null) {
            if (position == 0) {
                return VIEW_TYPE_HEADER
            }
        }
        if (mPdrFooterData != null) {
            if (position == itemCount - 1) {
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

    /** 获取底布局的ViewHolder */
    protected open fun getFooterViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        BlankViewHolder(getViewBindingLayout(PandoraItemBlankBinding::inflate, parent))

    /** 设置头部数据[headerData] */
    fun setHeaderData(headerData: H?) {
        mPdrHeaderData = headerData
    }

    /** 设置底部数据[footerData] */
    fun setFooterData(footerData: F?) {
        mPdrFooterData = footerData
    }

    override fun getItemCount(): Int {
        var count = super.getItemCount()//列表数据的数量
        if (mPdrHeaderData != null) {
            count += 1
        }
        if (mPdrFooterData != null) {
            count += 1
        }
        return count
    }

    /** 获取头部数据 */
    fun getHeaderData(): H? = mPdrHeaderData

    /** 获取底部数据 */
    fun getFooterData(): F? = mPdrFooterData

    override fun getItem(position: Int): T? {
        if (mPdrHeaderData != null) {
            return super.getItem(position - 1)
        }
        return super.getItem(position)
    }

    override fun setItemClick(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener { view ->
            if (mPdrHeaderData == null && mPdrFooterData == null) {//没有头部 没有底部
                val item = getItem(position)
                if (item != null) {
                    mPdrOnItemClickListener?.invoke(holder, item, position)
                }
                return@setOnClickListener
            }
            if (mPdrHeaderData != null && mPdrFooterData == null) {// 有头部 没有底部
                if (position == 0) {
                    mPdrOnHeaderClickListener?.invoke(holder, mPdrHeaderData!!, position)
                    return@setOnClickListener
                }
                val item = getItem(position)
                if (item != null) {
                    mPdrOnItemClickListener?.invoke(holder, item, position - 1)
                }
                return@setOnClickListener
            }

            if (mPdrHeaderData == null) {// 没有头部 有底部
                if (position == itemCount - 1) {
                    mPdrOnFooterClickListener?.invoke(holder, mPdrFooterData!!, position)
                    return@setOnClickListener
                }
                val item = getItem(position)
                if (item != null) {
                    mPdrOnItemClickListener?.invoke(holder, item, position)
                }
                return@setOnClickListener
            }

            // 有头部 有底部
            if (position == 0) {
                mPdrOnHeaderClickListener?.invoke(holder, mPdrHeaderData!!, position)
                return@setOnClickListener
            }
            if (position == itemCount - 1) {
                mPdrOnFooterClickListener?.invoke(holder, mPdrFooterData!!, position)
                return@setOnClickListener
            }
            val item = getItem(position)
            if (item != null) {
                mPdrOnItemClickListener?.invoke(holder, item, position - 1)
            }
        }
    }

    override fun setItemLongClick(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnLongClickListener { view ->
            if (mPdrHeaderData == null && mPdrFooterData == null) {//没有头部 没有底部
                val item = getItem(position)
                if (item != null) {
                    mPdrOnItemLongClickListener?.invoke(holder, item, position)
                }
                return@setOnLongClickListener true
            }

            if (mPdrHeaderData != null && mPdrFooterData == null) {// 有头部 没有底部
                if (position == 0) {
                    mPdrOnHeaderLongClickListener?.invoke(holder, mPdrHeaderData!!, position)
                    return@setOnLongClickListener true
                }
                val item = getItem(position)
                if (item != null) {
                    mPdrOnItemLongClickListener?.invoke(holder, item, position - 1)
                }
                return@setOnLongClickListener true
            }

            if (mPdrHeaderData == null) {// 没有头部 有底部
                if (position == itemCount - 1) {
                    mPdrOnFooterLongClickListener?.invoke(holder, mPdrFooterData!!, position)
                    return@setOnLongClickListener true
                }
                val item = getItem(position)
                if (item != null) {
                    mPdrOnItemLongClickListener?.invoke(holder, item, position)
                }
                return@setOnLongClickListener true
            }

            // 有头部 有底部
            if (position == 0) {
                mPdrOnHeaderLongClickListener?.invoke(holder, mPdrHeaderData!!, position)
            }
            if (position == itemCount - 1) {
                mPdrOnFooterLongClickListener?.invoke(holder, mPdrFooterData!!, position)
            }
            if (mPdrOnItemLongClickListener != null) {
                val item = getItem(position)
                if (item != null) {
                    mPdrOnItemLongClickListener?.invoke(holder, item, position - 1)
                }
            }
            return@setOnLongClickListener true
        }
    }

    /** 设置头部点击事件监听器 */
    fun setOnHeaderClickListener(listener: (holder: RecyclerView.ViewHolder, data: H, position: Int) -> Unit) {
        mPdrOnHeaderClickListener = listener
    }

    /** 设置头部长按事件监听器 */
    fun setOnHeaderLongClickListener(listener: (holder: RecyclerView.ViewHolder, data: H, position: Int) -> Unit) {
        mPdrOnHeaderLongClickListener = listener
    }

    /** 设置底部点击事件监听器 */
    fun setOnFooterClickListener(listener: (holder: RecyclerView.ViewHolder, data: F, position: Int) -> Unit) {
        mPdrOnFooterClickListener = listener
    }

    /** 设置底部长按事件监听器 */
    fun setOnFooterLongClickListener(listener: (holder: RecyclerView.ViewHolder, data: F, position: Int) -> Unit) {
        mPdrOnFooterLongClickListener = listener
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
                if (mPdrHeaderData == null && mPdrFooterData == null) {//没有头部 没有底部
                    return 1
                }
                if (mPdrHeaderData != null && mPdrFooterData == null) {// 有头部 没有底部
                    return if (position == 0) layoutManager.spanCount else 1
                }
                if (mPdrHeaderData == null) {// 没有头部 有底部
                    return if (position == itemCount - 1) layoutManager.spanCount else 1
                }
                // 有头部 有底部
                if (position == 0 || position == itemCount - 1) {
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

    /** 空白的ViewHolder */
    private class BlankViewHolder(view: ViewBinding) : RecyclerView.ViewHolder(view.root)
}