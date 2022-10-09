package com.lodz.android.pandora.widget.rv.swipe.vb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.lodz.android.pandora.databinding.PandoraItemSwipeMenuBinding

/**
 * 侧滑ViewHolder
 * @author zhouL
 * @date 2022/10/8
 */
@Suppress("UNCHECKED_CAST")
open class SwipeVbViewHolder(
    parent: ViewGroup,
    attachToRoot: Boolean = false,
    val binding: PandoraItemSwipeMenuBinding = PandoraItemSwipeMenuBinding.inflate(LayoutInflater.from(parent.context), parent, attachToRoot)
) : RecyclerView.ViewHolder(binding.root) {

    internal var mContentViewBinding: ViewBinding? = null

    internal var mRightViewBinding: ViewBinding? = null

    internal var mLeftViewBinding: ViewBinding? = null

    /** 获取内容布局 */
    fun <VB : ViewBinding> getContentViewBinding(): VB = mContentViewBinding as VB

    /** 初始化右侧布局 */
    fun <VB : ViewBinding> getRightViewBinding(): VB = mRightViewBinding as VB

    /** 初始化左侧布局 */
    fun <VB : ViewBinding> getLeftViewBinding(): VB = mLeftViewBinding as VB

}