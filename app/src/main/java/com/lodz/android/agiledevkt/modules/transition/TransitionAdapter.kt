package com.lodz.android.agiledevkt.modules.transition

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.TransitionBean
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.pandora.widget.rv.recycler.DataViewHolder
import org.jetbrains.anko.imageResource

/**
 * 共享动画列表适配器
 * Created by zhouL on 2018/11/20.
 */
class TransitionAdapter(context: Context) : BaseRecyclerViewAdapter<TransitionBean>(context) {

    private var mItemClickListener: ((img: ImageView, tv: TextView, item: TransitionBean, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            DataViewHolder(getLayoutView(parent, R.layout.rv_item_transition))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val bean = getItem(position)
        if (bean == null || holder !is DataViewHolder) {
            return
        }
        showItem(holder, bean, position)
    }

    private fun showItem(holder: DataViewHolder, bean: TransitionBean, position: Int) {
        val img = holder.withView<ImageView>(R.id.img)
        img.imageResource = bean.imgRes
        val tv = holder.withView<TextView>(R.id.title)
        tv.text = bean.title
        holder.itemView.setOnClickListener {
            mItemClickListener?.invoke(img, tv, bean, position)
        }
    }

    /** 设置点击事件监听器 */
    fun setOnClickListener(listener: (img: ImageView, tv: TextView, item: TransitionBean, position: Int) -> Unit) {
        mItemClickListener = listener
    }

}