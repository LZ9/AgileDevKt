package com.lodz.android.pandora.widget.rv.recycler.loadmore

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * RecyclerView带头部布局的加载更多适配器
 * @author zhouL
 * @date 2025/12/16
 */
abstract class AbsHeaderLoadMoreRvAdapter<T, H, VH : RecyclerView.ViewHolder>(context: Context) : AbsLoadMoreRvAdapter<T, VH>(context) {

    companion object {
        /** 顶部数据项 */
        internal const val VIEW_TYPE_HEADER = 11
    }

    /** 头信息数据 */
    private var mPdrHeaderData: H? = null

    override fun getItemViewType(position: Int): Int {
        if (mPdrHeaderData != null && position == 0){
            return VIEW_TYPE_HEADER
        }
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int =if (mPdrHeaderData != null) super.getItemCount() + 1 else super.getItemCount()

    /** 设置头部数据[headerData] */
    fun setHeaderData(headerData: H?) {
        mPdrHeaderData = headerData
    }

    /** 获取头部数据 */
    fun getHeaderData(): H? = mPdrHeaderData

    /** 适配GridLayoutManager */
    override fun adapterGridLayoutManager(layoutManager: GridLayoutManager) {
        if (layoutManager.orientation == RecyclerView.HORIZONTAL) {
            return
        }
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (mPdrHeaderData != null){
                    if (position == 0){
                        return layoutManager.spanCount
                    }
                }
//                // 默认减去头布局一个item数量，如果开启底部加载提示则需要再减去一个item数量
                val size = if (isShowBottomLayout()) layoutManager.itemCount - 2 else layoutManager.itemCount - 1
                if ((position - 1) == size) {// 滚到底部
                    return layoutManager.spanCount
                }
                return 1
            }
        }
    }

    /** 适配StaggeredGridLayoutManager */
    override fun adapterStaggeredGridLayoutManager(holder: VH) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            if (holder.itemViewType == VIEW_TYPE_LOADING_MORE || holder.itemViewType == VIEW_TYPE_LOAD_FINISH
                || holder.itemViewType == VIEW_TYPE_LOAD_FAIL || holder.itemViewType == VIEW_TYPE_HEADER
            ) {//item的类型是加载更多 || 加载完成 || 加载失败 || 头部布局时
                layoutParams.isFullSpan = true
            }
        }
    }

}