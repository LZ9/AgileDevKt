package com.lodz.android.pandora.widget.rv.recycler.loadmore.vh

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.pandora.widget.rv.recycler.loadmore.AbsLoadMoreRvAdapter

/**
 * RecyclerView加载更多基类适配器
 * Created by zhouL on 2018/11/20.
 */
abstract class BaseLoadMoreRvAdapter<T>(context: Context) : AbsLoadMoreRvAdapter<T, RecyclerView.ViewHolder>(context) {

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BaseLoadMoreRvAdapter<*>.LoadFinishViewHolder -> showLoadFinish(holder)
            is BaseLoadMoreRvAdapter<*>.LoadingMoreViewHolder -> showLoadingMore(holder)
            is BaseLoadMoreRvAdapter<*>.LoadFailViewHolder -> {
                holder.itemView.setOnClickListener {
                    handleLoadFail()
                }
                showLoadFail(holder)
            }
            is BaseLoadMoreRvAdapter<*>.BlankViewHolder -> {
                // 空白占位不需要操作
            }
            else -> super.onBindViewHolder(holder, position)
        }
        handleLoadMore(position)
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

    /** 加载完毕布局的ViewHolder */
    private inner class LoadFinishViewHolder(view: View) : RecyclerView.ViewHolder(view)

    /** 加载更多布局的ViewHolder */
    private inner class LoadingMoreViewHolder(view: View) : RecyclerView.ViewHolder(view)

    /** 加载失败布局的ViewHolder */
    private inner class LoadFailViewHolder(view: View) : RecyclerView.ViewHolder(view)

    /** 空白的ViewHolder */
    private inner class BlankViewHolder(view: View) : RecyclerView.ViewHolder(view)


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
}