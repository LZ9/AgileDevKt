package com.lodz.android.agiledevkt.modules.rv.binder.second

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.bean.NationBean
import com.lodz.android.agiledevkt.databinding.RvItemBinderSecondBinding
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.pandora.widget.rv.recycler.DataVBViewHolder

/**
 * Created by zhouL on 2018/12/11.
 */
class SecondBinderAdapter(context: Context) : BaseRecyclerViewAdapter<NationBean>(context) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        DataVBViewHolder(getViewBindingLayout(RvItemBinderSecondBinding::inflate, parent))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val bean = getItem(position)
        if (bean == null || holder !is DataVBViewHolder) {
            return
        }
        showItem(holder, bean)
    }

    private fun showItem(holder: DataVBViewHolder, bean: NationBean) {
        holder.getVB<RvItemBinderSecondBinding>().apply {
            ImageLoader.create(context)
                .loadUrl(bean.imgUrl)
                .setCenterCrop()
                .into(nationImg)
            nationTv.text = StringBuilder("${bean.code}-${bean.name}")
        }
    }
}