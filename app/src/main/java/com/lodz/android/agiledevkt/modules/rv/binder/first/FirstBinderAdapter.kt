package com.lodz.android.agiledevkt.modules.rv.binder.first

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.NationBean
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.pandora.widget.rv.recycler.DataViewHolder

/**
 * Created by zhouL on 2018/12/10.
 */
class FirstBinderAdapter(context: Context) : BaseRecyclerViewAdapter<NationBean>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            DataViewHolder(getLayoutView(parent, R.layout.rv_item_binder_first))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val bean = getItem(position)
        if (bean == null || holder !is DataViewHolder) {
            return
        }
        showItem(holder, bean)
    }

    private fun showItem(holder: DataViewHolder, bean: NationBean) {
        ImageLoader.create(context)
                .loadUrl(bean.imgUrl)
                .setCenterCrop()
                .into(holder.withView<ImageView>(R.id.nation_img))
        holder.withView<TextView>(R.id.nation_tv).text = bean.name
    }
}