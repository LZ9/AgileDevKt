package com.lodz.android.agiledevkt.modules.transition

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.bean.TransitionBean
import com.lodz.android.agiledevkt.databinding.RvItemTransitionBinding
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.pandora.widget.rv.recycler.DataVBViewHolder

/**
 * 共享动画列表适配器
 * Created by zhouL on 2018/11/20.
 */
class TransitionAdapter(context: Context) : BaseRecyclerViewAdapter<TransitionBean>(context) {

    private var mItemClickListener: ((img: ImageView, tv: TextView, item: TransitionBean, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        DataVBViewHolder(getViewBindingLayout(RvItemTransitionBinding::inflate, parent))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val bean = getItem(position)
        if (bean == null || holder !is DataVBViewHolder) {
            return
        }
        showItem(holder, bean, position)
    }

    private fun showItem(holder: DataVBViewHolder, bean: TransitionBean, position: Int) {
        holder.getVB<RvItemTransitionBinding>().apply {
            img.setImageResource(bean.imgRes)
            titleTv.text = bean.title
            holder.itemView.setOnClickListener {
                mItemClickListener?.invoke(img, titleTv, bean, position)
            }
        }
    }

    /** 设置点击事件监听器 */
    fun setOnClickListener(listener: (img: ImageView, tv: TextView, item: TransitionBean, position: Int) -> Unit) {
        mItemClickListener = listener
    }

}