package com.lodz.android.agiledevkt.modules.viewpager

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter

/**
 * ViewPager2适配器
 * @author zhouL
 * @date 2019/12/30
 */
class ViewPagerAdapter(context: Context) : BaseRecyclerViewAdapter<Pair<String, Int>>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            DataViewHolder(getLayoutView(parent, R.layout.rv_item_view_page))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val pair = getItem(position)
        if (pair == null || holder !is DataViewHolder) {
            return
        }
        showItem(holder, pair)
    }

    private fun showItem(holder: DataViewHolder, pair: Pair<String, Int>) {
        holder.strTv.text = pair.first
        holder.strTv.setBackgroundColor(context.getColorCompat(pair.second))
    }

    private inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val strTv by bindView<TextView>(R.id.str_tv)
    }
}