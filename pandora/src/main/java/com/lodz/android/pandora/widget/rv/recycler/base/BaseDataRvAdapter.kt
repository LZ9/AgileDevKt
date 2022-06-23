package com.lodz.android.pandora.widget.rv.recycler.base

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.lodz.android.pandora.widget.rv.recycler.vh.DataViewHolder

/**
 * 使用DataViewHolder的基础适配器
 * @author zhouL
 * @date 2022/6/22
 */
abstract class BaseDataRvAdapter<T>(context: Context) : AbsRvAdapter<T, DataViewHolder>(context) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(getLayoutView(parent, getLayoutId()))

    @LayoutRes
    abstract fun getLayoutId(): Int
}