package com.lodz.android.pandora.widget.rv.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 数据ViewHolder
 * @author zhouL
 * @date 2020/6/18
 */
open class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun <T : View> withView(id: Int): T = lazy {
        itemView.findViewById<T>(id)
    }.value
}