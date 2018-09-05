package com.lodz.android.agiledevkt.modules.statusbar

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.componentkt.widget.rv.recycler.BaseRecyclerViewAdapter

/**
 * 简单数据适配器
 * Created by zhouL on 2018/9/5.
 */
class CoordinatorDataAdapter(context: Context) :BaseRecyclerViewAdapter<String>(context){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(getLayoutView(parent, R.layout.rv_item_coordinator))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        if (data.isNullOrEmpty()) {
            return
        }
        showItem(holder as DataViewHolder, data!!)
    }

    private fun showItem(viewHolder: DataViewHolder, data: String) {
        viewHolder.dataTv.text = data
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /** 数据 */
        @BindView(R.id.data_tv)
        lateinit var dataTv: TextView

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}