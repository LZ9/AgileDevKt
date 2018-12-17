package com.lodz.android.agiledevkt.modules.rv.snap

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.NationBean
import com.lodz.android.componentkt.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.imageloaderkt.ImageLoader

/**
 * Snap数据适配器
 * Created by zhouL on 2018/12/3.
 */
class SnapAdapter(context: Context) : BaseRecyclerViewAdapter<NationBean>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            DataViewHolder(getLayoutView(parent, R.layout.rv_item_snap))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val bean = getItem(position)
        if (bean == null || !(holder is DataViewHolder)) {
            return
        }
        showItem(holder, bean)
    }

    private fun showItem(holder: DataViewHolder, bean: NationBean) {
        showImg(bean.imgUrl, holder.nationImg)
        holder.titleTv.text = bean.getTitle()
    }

    private fun showImg(url: String, imageView: ImageView) {
        if (url.isEmpty()) {
            return
        }
        ImageLoader.create(context)
                .loadUrl(url)
                .useRoundCorner()
                .into(imageView)
    }

    private inner class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /** 国旗图片 */
        val nationImg by bindView<ImageView>(R.id.nation_img)
        /** 国家名称 */
        val titleTv by bindView<TextView>(R.id.title_tv)
    }
}