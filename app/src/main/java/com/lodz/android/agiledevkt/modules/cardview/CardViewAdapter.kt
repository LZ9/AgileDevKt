package com.lodz.android.agiledevkt.modules.cardview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter

/**
 *
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
        if (!(holder is DataViewHolder)){
            return
        }
        showItem(holder, str)

    }

    private fun showItem(holder: DataViewHolder, str: String) {
        holder.titleTv.text = str
        val content = StringBuilder(str)
        for(i in str.indices){
            content.append(str)
        }
        holder.contentTv.text = content
        holder.dateTv.text = DateUtils.getCurrentFormatString(DateUtils.TYPE_2)
    }

    private inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** 标题 */
        val titleTv by bindView<TextView>(R.id.title_tv)
        /** 内容 */
        val contentTv by bindView<TextView>(R.id.content_tv)
        /** 日期 */
        val dateTv by bindView<TextView>(R.id.date_tv)
    }
}