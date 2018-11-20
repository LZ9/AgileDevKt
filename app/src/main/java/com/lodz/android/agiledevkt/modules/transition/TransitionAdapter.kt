package com.lodz.android.agiledevkt.modules.transition

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.TransitionBean
import com.lodz.android.componentkt.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.corekt.anko.bindView
import org.jetbrains.anko.imageResource

/**
 * 共享动画列表适配器
 * Created by zhouL on 2018/11/20.
 */
class TransitionAdapter(context: Context) : BaseRecyclerViewAdapter<TransitionBean>(context) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            DataViewHolder(getLayoutView(parent, R.layout.rv_item_transition))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val bean = getItem(position)
        if (bean == null || !(holder is DataViewHolder)) {
            return
        }
        showItem(holder, bean)
    }

    private fun showItem(holder: DataViewHolder, bean: TransitionBean) {
        holder.img.imageResource = bean.imgRes
        holder.titleTv.text = bean.title
    }

    inner class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img by bindView<ImageView>(R.id.img)
        val titleTv by bindView<TextView>(R.id.title)
    }
}