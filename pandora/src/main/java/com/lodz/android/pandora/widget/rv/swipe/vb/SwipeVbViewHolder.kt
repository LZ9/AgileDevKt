package com.lodz.android.pandora.widget.rv.swipe.vb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.pandora.databinding.PandoraItemSwipeMenuBinding

/**
 * 侧滑ViewHolder
 * @author zhouL
 * @date 2022/10/8
 */
open class SwipeVbViewHolder(
    parent: ViewGroup,
    attachToRoot: Boolean = false,
    binding: PandoraItemSwipeMenuBinding = PandoraItemSwipeMenuBinding.inflate(LayoutInflater.from(parent.context), parent, attachToRoot)
) : RecyclerView.ViewHolder(binding.root)