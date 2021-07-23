package com.lodz.android.agiledevkt.modules.viewbinding

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.databinding.RvItemViewBindingBinding
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.pandora.widget.rv.recycler.DataVBViewHolder

/**
 * ViewBinding测试类
 * @author zhouL
 * @date 2021/7/19
 */
class ViewBindingAdapter(context:Context) : BaseRecyclerViewAdapter<String>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        DataVBViewHolder(getLayoutView(RvItemViewBindingBinding::inflate, parent))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataVBViewHolder){
            holder.getVB<RvItemViewBindingBinding>().nameTv.text = getItem(position)
        }
    }

}