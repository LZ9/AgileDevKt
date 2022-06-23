package com.lodz.android.pandora.widget.rv.recycler.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lodz.android.pandora.widget.rv.recycler.vh.DataVBViewHolder

/**
 * 使用DataVBViewHolder的基础适配器
 * @author zhouL
 * @date 2022/6/22
 */
abstract class BaseVbRvAdapter<T>(context: Context) : AbsRvAdapter<T, DataVBViewHolder>(context) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataVBViewHolder =
        DataVBViewHolder(getViewBindingLayout(getVbInflate(), parent))
    abstract fun getVbInflate(): (LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> ViewBinding
}

