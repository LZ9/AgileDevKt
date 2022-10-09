package com.lodz.android.agiledevkt.modules.rv.swipe

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.widget.rv.swipe.data.BaseSwipeDataRvAdapter
import com.lodz.android.pandora.widget.rv.swipe.data.SwipeDataViewHolder

/**
 * 侧滑菜单适配器
 * @author zhouL
 * @date 2022/10/9
 */
class SwipeRvAdapter(context: Context) : BaseSwipeDataRvAdapter<String>(context) {

    override fun getContentLayout(): Int = R.layout.rv_item_swipe_content

    override fun getLeftLayout(): Int = R.layout.rv_item_swipe_left

    override fun getRightLayout(): Int = R.layout.rv_item_swipe_right

    override fun configSwipeViewHolder(holder: SwipeDataViewHolder, parent: ViewGroup, viewType: Int) {
        super.configSwipeViewHolder(holder, parent, viewType)
        setItemViewHeight(holder.itemView, context.dp2px(50))
    }

    override fun onBind(holder: SwipeDataViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.apply {
            holder.withView<TextView>(R.id.title_tv).text = position.toString().append(". ").append(item)
            holder.withView<MaterialButton>(R.id.share_btn).setOnClickListener {
                context.toastShort(position.toString().append(context.getString(R.string.rv_swipe_share)))
            }
            holder.withView<MaterialButton>(R.id.like_btn).setOnClickListener {
                context.toastShort(position.toString().append(context.getString(R.string.rv_swipe_like)))
            }
        }
    }
}