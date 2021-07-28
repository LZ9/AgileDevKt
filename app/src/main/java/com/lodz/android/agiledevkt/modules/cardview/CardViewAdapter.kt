package com.lodz.android.agiledevkt.modules.cardview

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.databinding.RvItemCardViewBinding
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.pandora.widget.rv.recycler.DataVBViewHolder

/**
 * 卡片适配器
 * @author zhouL
 * @date 2019/6/5
 */
class CardViewAdapter(context:Context) :BaseRecyclerViewAdapter<String>(context){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  =
            DataVBViewHolder(getViewBindingLayout(RvItemCardViewBinding::inflate, parent))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val str = getItem(position)
        if (str.isNullOrEmpty()){
            return
        }
        if (holder !is DataVBViewHolder){
            return
        }
        showItem(holder, str)
    }

    private fun showItem(holder: DataVBViewHolder, str: String) {
        holder.getVB<RvItemCardViewBinding>().apply {
            titleTv.text = str
            var content = str
            for (i in str.indices) {
                content = content.append(str)
            }
            contentTv.text = content
            dateTv.text = DateUtils.getCurrentFormatString(DateUtils.TYPE_2)
        }
    }
}