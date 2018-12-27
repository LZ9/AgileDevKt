package com.lodz.android.agiledevkt.modules.rv.binder.second

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.NationBean
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter

/**
 * Created by zhouL on 2018/12/11.
 */
class SecondBinderAdapter(context: Context) : BaseRecyclerViewAdapter<NationBean>(context) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            DataViewHolder(getLayoutView(parent, R.layout.rv_item_binder_second))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val bean = getItem(position)
        if (bean == null || !(holder is DataViewHolder)) {
            return
        }
        showItem(holder, bean)
    }

    private fun showItem(holder: DataViewHolder, bean: NationBean) {
        ImageLoader.create(context)
                .loadUrl(bean.imgUrl)
                .setCenterCrop()
                .into(holder.nationImg)
        holder.nationTv.text = "${bean.code}-${bean.name}"
    }

    private inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** 图片 */
        val nationImg by bindView<ImageView>(R.id.nation_img)
        /** 文字 */
        val nationTv by bindView<TextView>(R.id.nation_tv)

    }
}