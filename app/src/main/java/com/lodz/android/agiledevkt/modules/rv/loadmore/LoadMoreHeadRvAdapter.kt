package com.lodz.android.agiledevkt.modules.rv.loadmore

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.NationBean
import com.lodz.android.agiledevkt.databinding.RvItemHeadFooterBinding
import com.lodz.android.agiledevkt.modules.rv.popup.LayoutManagerPopupWindow
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getScreenWidth
import com.lodz.android.pandora.widget.rv.anko.grid
import com.lodz.android.pandora.widget.rv.anko.setupVB
import com.lodz.android.pandora.widget.rv.recycler.loadmore.vh.SimpleHeaderLoadMoreRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.vh.DataViewHolder

/**
 * 带头布局的加载更多适配器
 * @author zhouL
 * @date 2025/12/15
 */
class LoadMoreHeadRvAdapter(context: Context): SimpleHeaderLoadMoreRvAdapter<String, ArrayList<NationBean>>(context) {

    private var mOnClickDeleteListener: ((position: Int) -> Unit)? = null

    /** 当前布局 */
    @LayoutManagerPopupWindow.LayoutManagerType
    private var mManagerType = LayoutManagerPopupWindow.TYPE_LINEAR

    override fun getHeaderLayoutId(): Int = R.layout.rv_item_load_more_header

    override fun showHeader(holder: RecyclerView.ViewHolder) {
        val headerData = getHeaderData() ?: return
        val gridRv = holder.itemView.findViewById<RecyclerView>(R.id.grid_rv)
        gridRv.grid(3)
            .setupVB<NationBean, RvItemHeadFooterBinding>(RvItemHeadFooterBinding::inflate) { context, vb, holder, position ->
                setItemViewWidth(holder.itemView, context.getScreenWidth() / 3)
                val item = getItem(position)
                vb.dataTv.text = item?.name ?: ""
            }
            .setData(headerData)
        gridRv.isNestedScrollingEnabled = false
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

    /** 设置布局类型[type] */
    fun setLayoutManagerType(@LayoutManagerPopupWindow.LayoutManagerType type: Int) {
        mManagerType = type
    }

    /** 设置监听器[listener] */
    fun setOnClickDeleteListener(listener: (position: Int) -> Unit) {
        mOnClickDeleteListener = listener
    }
}
