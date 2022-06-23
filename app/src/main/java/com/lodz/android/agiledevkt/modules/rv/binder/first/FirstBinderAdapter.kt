package com.lodz.android.agiledevkt.modules.rv.binder.first

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.bean.NationBean
import com.lodz.android.agiledevkt.databinding.RvItemBinderFirstBinding
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.widget.rv.recycler.base.BaseRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.vh.DataVBViewHolder

/**
 * Created by zhouL on 2018/12/10.
 */
class FirstBinderAdapter(context: Context) : BaseRvAdapter<NationBean>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        DataVBViewHolder(getViewBindingLayout(RvItemBinderFirstBinding::inflate, parent))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val bean = getItem(position)
        if (bean == null || holder !is DataVBViewHolder) {
            return
        }
        showItem(holder, bean)
    }

    private fun showItem(holder: DataVBViewHolder, bean: NationBean) {
        holder.getVB<RvItemBinderFirstBinding>().apply {
            ImageLoader.create(context)
                .loadUrl(bean.imgUrl)
                .setCenterCrop()
                .into(nationImg)
            nationTv.text = bean.name
        }
    }
}