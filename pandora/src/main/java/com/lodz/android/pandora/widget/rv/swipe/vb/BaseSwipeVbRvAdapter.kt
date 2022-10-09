package com.lodz.android.pandora.widget.rv.swipe.vb

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lodz.android.pandora.widget.rv.recycler.base.AbsRvAdapter

/**
 * 带侧滑按钮的适配器基类
 * @author zhouL
 * @date 2022/10/8
 */
abstract class BaseSwipeVbRvAdapter<T>(
    context: Context
) : AbsRvAdapter<T, SwipeVbViewHolder>(context) {

    /** 获取内容布局 */
    abstract fun createContentVbInflate(): (LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> ViewBinding

    /** 初始化右侧布局 */
    protected open fun createRightVbInflate(): ((LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> ViewBinding)? = null

    /** 初始化左侧布局 */
    protected open fun createLeftVbInflate(): ((LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> ViewBinding)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeVbViewHolder {
        val holder = SwipeVbViewHolder(parent)
        configSwipeViewHolder(holder, parent, viewType)
        return holder
    }

    /** 配置ViewHolder */
    protected open fun configSwipeViewHolder(holder: SwipeVbViewHolder, parent: ViewGroup, viewType: Int) {
        holder.mContentViewBinding = getViewBindingLayout(createContentVbInflate(), parent)
        holder.binding.pdrContentView.addView(holder.mContentViewBinding?.root)
        val rightVbInflate = createRightVbInflate()
        if (rightVbInflate != null) {
            holder.mRightViewBinding = getViewBindingLayout(rightVbInflate, parent)
            holder.binding.pdrRightView.addView(holder.mRightViewBinding?.root)
        }
        val leftVbInflate = createLeftVbInflate()
        if (leftVbInflate != null) {
            holder.mLeftViewBinding = getViewBindingLayout(leftVbInflate, parent)
            holder.binding.pdrLeftView.addView(holder.mLeftViewBinding?.root)
        }
        holder.binding.pdrSwipMenuLayout.setSwipeEnable(rightVbInflate != null || leftVbInflate != null)//没有侧滑菜单禁止滑动
    }

    /** 关闭侧滑菜单 */
    protected fun smoothCloseMenu(holder: SwipeVbViewHolder) {
        holder.binding.pdrSwipMenuLayout.smoothCloseMenu()
    }

    override fun setItemClick(holder: SwipeVbViewHolder, position: Int) {
        holder.binding.pdrContentView.setOnClickListener {
            val item = getItem(position)
            if (position >= 0 && item != null) {
                mPdrOnItemClickListener?.invoke(holder, item, position)
            }
        }
    }

    override fun setItemLongClick(holder: SwipeVbViewHolder, position: Int) {
        holder.binding.pdrContentView.setOnLongClickListener {
            val item = getItem(position)
            if (position >= 0 && item != null) {
                mPdrOnItemLongClickListener?.invoke(holder, item, position)
            }
            return@setOnLongClickListener true
        }
    }
}