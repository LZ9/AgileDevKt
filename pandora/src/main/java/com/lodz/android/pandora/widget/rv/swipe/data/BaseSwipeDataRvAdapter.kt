package com.lodz.android.pandora.widget.rv.swipe.data

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.lodz.android.pandora.widget.rv.recycler.base.AbsRvAdapter

/**
 * 带侧滑按钮的适配器基类
 * @author zhouL
 * @date 2022/10/8
 */
abstract class BaseSwipeDataRvAdapter<T>(context: Context) : AbsRvAdapter<T, SwipeDataViewHolder>(context) {

    /** 获取内容布局 */
    @LayoutRes
    protected abstract fun getContentLayout(): Int

    /** 初始化右侧布局 */
    @LayoutRes
    protected open fun getRightLayout(): Int = 0

    /** 初始化左侧布局 */
    @LayoutRes
    protected open fun getLeftLayout(): Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeDataViewHolder {
        val holder = SwipeDataViewHolder(parent)
        configSwipeViewHolder(holder, parent, viewType)
        return holder
    }

    /** 配置ViewHolder */
    protected open fun configSwipeViewHolder(holder: SwipeDataViewHolder, parent: ViewGroup, viewType: Int) {
        if (getContentLayout() != 0) {
            holder.contentLayout.addView(getLayoutView(holder.contentLayout, getContentLayout()))
        }
        if (getRightLayout() != 0) {
            holder.rightLayout.addView(getLayoutView(holder.rightLayout, getRightLayout()))
        }
        if (getLeftLayout() != 0) {
            holder.leftLayout.addView(getLayoutView(holder.leftLayout, getLeftLayout()))
        }
        holder.swipeMenuLayout.setSwipeEnable(getRightLayout() != 0 || getLeftLayout() != 0)//没有侧滑菜单禁止滑动
    }

    /** 关闭侧滑菜单 */
    protected fun smoothCloseMenu(holder: SwipeDataViewHolder) {
        holder.swipeMenuLayout.smoothCloseMenu()
    }

    override fun setItemClick(holder: SwipeDataViewHolder, position: Int) {
        holder.contentLayout.setOnClickListener {
            val item = getItem(position)
            if (position >= 0 && item != null) {
                mPdrOnItemClickListener?.invoke(holder, item, position)
            }
        }
    }

    override fun setItemLongClick(holder: SwipeDataViewHolder, position: Int) {
        holder.contentLayout.setOnLongClickListener {
            val item = getItem(position)
            if (position >= 0 && item != null) {
                mPdrOnItemLongClickListener?.invoke(holder, item, position)
            }
            return@setOnLongClickListener true
        }
    }
}