package com.lodz.android.pandora.widget.search

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.pandora.R
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter

/**
 * 搜索联想列表适配器
 * @author zhouL
 * @date 2019/10/25
 */
class RecomdListAdapter(context: Context) : BaseRecyclerViewAdapter<RecomdData>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        DataViewHolder(getLayoutView(parent, R.layout.pandora_item_search_recomd))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {

    }

    private inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        /** 数据 */
//        val dataTv by bindView<TextView>(R.id.data_tv)
//        /** 删除按钮 */
//        val deleteBtn by bindView<TextView>(R.id.delete_btn)
    }
}