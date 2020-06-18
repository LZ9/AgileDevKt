package com.lodz.android.agiledevkt.modules.cardview

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.pandora.widget.rv.recycler.DataViewHolder

/**
 * 卡片适配器
 * @author zhouL
 * @date 2019/6/5
 */
class CardViewAdapter(context:Context) :BaseRecyclerViewAdapter<String>(context){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  =
            DataViewHolder(getLayoutView(parent, R.layout.rv_item_card_view))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val str = getItem(position)
        if (str.isNullOrEmpty()){
            return
        }
        if (holder !is DataViewHolder){
            return
        }
        showItem(holder, str)

    }

    private fun showItem(holder: DataViewHolder, str: String) {
        holder.withView<TextView>(R.id.title_tv).text = str
        holder.withView<TextView>(R.id.content_tv).apply {
            val content = StringBuilder(str)
            for (i in str.indices) {
                content.append(str)
            }
            text = content
        }
        holder.withView<TextView>(R.id.date_tv).text = DateUtils.getCurrentFormatString(DateUtils.TYPE_2)
    }
}