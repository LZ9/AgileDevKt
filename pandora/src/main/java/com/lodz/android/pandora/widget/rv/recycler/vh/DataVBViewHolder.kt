package com.lodz.android.pandora.widget.rv.recycler.vh

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * 支持ViewBinding的数据ViewHolder
 * @author zhouL
 * @date 2020/6/18
 */
 open class DataVBViewHolder(private val viewBinding: ViewBinding) : RecyclerView.ViewHolder(viewBinding.root){

    @Suppress("UNCHECKED_CAST")
    fun <VB : ViewBinding> getVB(): VB {
        return viewBinding as VB
    }
}