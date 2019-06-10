package com.lodz.android.agiledevkt.modules.coordinator

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter

/**
 * 简单数据适配器
 * Created by zhouL on 2018/9/5.
 */
class CoordinatorDataAdapter(context: Context) : BaseRecyclerViewAdapter<String>(context){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(getLayoutView(parent, R.layout.rv_item_coordinator))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        if (data.isNullOrEmpty()) {
            return
        }
        showItem(holder as DataViewHolder, data)
    }

    private fun showItem(viewHolder: DataViewHolder, data: String) {
        viewHolder.dataTv.text = data
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** 数据 */
        internal val dataTv by bindView<TextView>(R.id.data_tv)
    }
}