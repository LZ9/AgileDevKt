package com.lodz.android.pandora.widget.rv.recycler.loadmore.vb

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lodz.android.pandora.databinding.PandoraItemBlankBinding
import com.lodz.android.pandora.widget.rv.recycler.loadmore.AbsLoadMoreRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.vh.DataVBViewHolder

/**
 * RecyclerView加载更多基类适配器
 * Created by zhouL on 2018/11/20.
 */
abstract class BaseLoadMoreVbRvAdapter<T>(context: Context) : AbsLoadMoreRvAdapter<T, DataVBViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataVBViewHolder {
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

    override fun onBindViewHolder(holder: DataVBViewHolder, position: Int) {
        when (holder) {
            is BaseLoadMoreVbRvAdapter<*>.LoadFinishViewHolder -> showLoadFinish(holder)
            is BaseLoadMoreVbRvAdapter<*>.LoadingMoreViewHolder -> showLoadingMore(holder)
            is BaseLoadMoreVbRvAdapter<*>.LoadFailViewHolder -> {
                holder.itemView.setOnClickListener {
                    handleLoadFail()
                }
                showLoadFail(holder)
            }
            is BaseLoadMoreVbRvAdapter<*>.BlankViewHolder -> {
                // 空白占位不需要操作
            }
            else -> super.onBindViewHolder(holder, position)
        }
        handleLoadMore(position)
    }

    /** 获取加载完毕的ViewHolder */
    private fun getLoadFinishViewHolder(parent: ViewGroup): DataVBViewHolder =
        LoadFinishViewHolder(getViewBindingLayout(getLoadFinishVbInflate(), parent))

    /** 获取正在加载更多的ViewHolder */
    private fun getLoadingMoreViewHolder(parent: ViewGroup): DataVBViewHolder =
        LoadingMoreViewHolder(getViewBindingLayout(getLoadingMoreVbInflate(), parent))

    /** 获取加载失败的ViewHolder */
    private fun getLoadFailViewHolder(parent: ViewGroup): DataVBViewHolder =
        LoadFailViewHolder(getViewBindingLayout(getLoadFailVbInflate(), parent))

    /** 获取空白占位的ViewHolder */
    private fun getBlankViewHolder(parent: ViewGroup): DataVBViewHolder =
        BlankViewHolder(getViewBindingLayout(PandoraItemBlankBinding::inflate, parent))

    /** 加载完毕布局的ViewHolder */
    private inner class LoadFinishViewHolder(view: ViewBinding) : DataVBViewHolder(view)

    /** 加载更多布局的ViewHolder */
    private inner class LoadingMoreViewHolder(view: ViewBinding) : DataVBViewHolder(view)

    /** 加载失败布局的ViewHolder */
    private inner class LoadFailViewHolder(view: ViewBinding) : DataVBViewHolder(view)

    /** 空白的ViewHolder */
    private inner class BlankViewHolder(view: ViewBinding) : DataVBViewHolder(view)

    /** 获取加载完毕的ViewBinding */
    abstract fun getLoadFinishVbInflate(): (LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> ViewBinding

    /** 获取加载完毕的ViewBinding */
    abstract fun getLoadingMoreVbInflate(): (LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> ViewBinding

    /** 获取加载失败的ViewBinding */
    abstract fun getLoadFailVbInflate(): (LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> ViewBinding

    abstract fun getItemViewHolder(parent: ViewGroup, viewType: Int): DataVBViewHolder
}