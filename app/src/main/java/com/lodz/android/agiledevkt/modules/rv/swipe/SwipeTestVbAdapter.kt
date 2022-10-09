package com.lodz.android.agiledevkt.modules.rv.swipe

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.RvItemSwipeContentBinding
import com.lodz.android.agiledevkt.databinding.RvItemSwipeLeftBinding
import com.lodz.android.agiledevkt.databinding.RvItemSwipeRightBinding
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.widget.rv.swipe.vb.BaseSwipeVbRvAdapter
import com.lodz.android.pandora.widget.rv.swipe.vb.SwipeVbViewHolder

/**
 * 侧滑菜单适配器
 * @author zhouL
 * @date 2022/10/9
 */
class SwipeTestVbAdapter(context: Context) : BaseSwipeVbRvAdapter<String>(context) {

    override fun createContentVbInflate(): (LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> ViewBinding = RvItemSwipeContentBinding::inflate

    override fun createLeftVbInflate(): ((LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> ViewBinding) = RvItemSwipeLeftBinding::inflate

    override fun createRightVbInflate(): ((LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> ViewBinding) = RvItemSwipeRightBinding::inflate

    override fun configSwipeViewHolder(holder: SwipeVbViewHolder, parent: ViewGroup, viewType: Int) {
        super.configSwipeViewHolder(holder, parent, viewType)
        setItemViewHeight(holder.itemView, context.dp2px(50))
    }

    override fun onBind(holder: SwipeVbViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.getContentViewBinding<RvItemSwipeContentBinding>().apply {
            titleTv.text = position.toString().append(". ").append(item)
        }
        holder.getLeftViewBinding<RvItemSwipeLeftBinding>().apply {
            likeBtn.setOnClickListener {
                context.toastShort(position.toString().append(context.getString(R.string.rv_swipe_like)))
            }
        }
        holder.getRightViewBinding<RvItemSwipeRightBinding>().apply {
            shareBtn.setOnClickListener {
                context.toastShort(position.toString().append(context.getString(R.string.rv_swipe_share)))
            }
        }
    }
}