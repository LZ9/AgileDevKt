package com.lodz.android.agiledevkt.modules.mvvm.loadmore

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.databinding.RvItemMainBinding
import com.lodz.android.pandora.widget.rv.recycler.DataVBViewHolder
import com.lodz.android.pandora.widget.rv.recycler.SimpleLoadMoreRVAdapter

/**
 * 加载更多列表适配器
 * @author zhouL
 * @date 2021/12/27
 */
class LoadMoreListAdapter(context: Context) :SimpleLoadMoreRVAdapter<String>(context){

    override fun getItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        DataVBViewHolder(getViewBindingLayout(RvItemMainBinding::inflate, parent))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val str = getItem(position)
        if (str.isNullOrEmpty()) {
            return
        }
        if (holder !is DataVBViewHolder) {
            return
        }
        showItem(holder, str)
    }

    private fun showItem(holder: DataVBViewHolder, str: String) {
        holder.getVB<RvItemMainBinding>().apply {
            name.text = str
        }
    }

}