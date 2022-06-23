package com.lodz.android.agiledevkt.modules.rv.loadmore

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.rv.popup.LayoutManagerPopupWindow
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getScreenWidth
import com.lodz.android.pandora.widget.rv.recycler.vh.DataViewHolder
import com.lodz.android.pandora.widget.rv.recycler.loadmore.vh.SimpleLoadMoreRvAdapter

/**
 * 加载更多适配器
 * Created by zhouL on 2018/11/27.
 */
class LoadMoreRvAdapter(context: Context) : SimpleLoadMoreRvAdapter<String>(context) {

    private var mOnClickDeleteListener: ((position: Int) -> Unit)? = null

    /** 当前布局 */
    @LayoutManagerPopupWindow.LayoutManagerType
    private var mManagerType = LayoutManagerPopupWindow.TYPE_LINEAR

    init {
        setIndeterminateDrawable(R.drawable.anims_custom_progress)//自定义加载动画资源
    }

    /** 设置监听器[listener] */
    fun setOnClickDeleteListener(listener: (position: Int) -> Unit) {
        mOnClickDeleteListener = listener
    }

    /** 设置布局类型[type] */
    fun setLayoutManagerType(@LayoutManagerPopupWindow.LayoutManagerType type: Int) {
        mManagerType = type
    }

    override fun getItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            DataViewHolder(getLayoutView(parent, R.layout.rv_item_load_more))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        if (data.isNullOrEmpty() || holder !is DataViewHolder) {
            return
        }
        showItem(holder, data, position)
    }

    private fun showItem(holder: DataViewHolder, data: String, position: Int) {
        if (mManagerType == LayoutManagerPopupWindow.TYPE_LINEAR) {
            setItemViewWidth(holder.itemView, context.getScreenWidth())
            setItemViewHeight(holder.itemView, context.dp2px(100))
        }
        if (mManagerType == LayoutManagerPopupWindow.TYPE_GRID) {
            setItemViewWidth(holder.itemView, context.getScreenWidth() / 3)
            setItemViewHeight(holder.itemView, context.dp2px(100))
        }
        if (mManagerType == LayoutManagerPopupWindow.TYPE_STAGGERED) {
            setItemViewWidth(holder.itemView, context.getScreenWidth() / 3)
            when {
                position % 3 == 0 -> setItemViewHeight(holder.itemView, context.dp2px(300))
                position % 3 == 1 -> setItemViewHeight(holder.itemView, context.dp2px(100))
                else -> setItemViewHeight(holder.itemView, context.dp2px(200))
            }
        }
        holder.withView<TextView>(R.id.data_tv).text = data
        holder.withView<TextView>(R.id.delete_btn).setOnClickListener {
            mOnClickDeleteListener?.invoke(position)
        }
    }
}