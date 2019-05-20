package com.lodz.android.agiledevkt.modules.rv.head

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.rv.popup.LayoutManagerPopupWindow
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getScreenWidth
import com.lodz.android.pandora.widget.rv.recycler.BaseHeaderFooterRVAdapter

/**
 * 带头/底部的适配器
 * Created by zhouL on 2018/11/27.
 */
class HeadFooterAdapter(context: Context) : BaseHeaderFooterRVAdapter<String, String, String>(context) {

    /** 当前布局 */
    @LayoutManagerPopupWindow.LayoutManagerType
    private var mLayoutManagerType = LayoutManagerPopupWindow.TYPE_LINEAR
    /** 布局方向 */
    private var mOrientation = RecyclerView.VERTICAL

    /** 设置布局类型[type] */
    fun setLayoutManagerType(@LayoutManagerPopupWindow.LayoutManagerType type: Int) {
        mLayoutManagerType = type
    }

    /** 设置布局方向[orientation] */
    fun setOrientation(orientation: Int) {
        mOrientation = orientation
    }

    override fun getHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            HeaderViewHolder(getLayoutView(parent, R.layout.rv_item_head))

    override fun getItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            ItemViewHolder(getLayoutView(parent, R.layout.rv_item_head_footer))

    override fun getFooterViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            FooterViewHolder(getLayoutView(parent, R.layout.rv_item_footer))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            showHeaderItem(holder)
            return
        }
        if (holder is FooterViewHolder) {
            showFooterItem(holder)
            return
        }
        showItem(holder as ItemViewHolder, position)
    }

    private fun showHeaderItem(holder: HeaderViewHolder) {
        val data = getHeaderData()
        if (data.isNullOrEmpty()) {
            return
        }
        resizeHeaderAndFooter(holder.itemView)

        holder.titleTv.text = data
    }

    private fun showItem(holder: ItemViewHolder, position: Int) {
        val data = getItem(position)
        if (data.isNullOrEmpty()) {
            return
        }
        if (mLayoutManagerType == LayoutManagerPopupWindow.TYPE_LINEAR) {
            setItemViewWidth(holder.itemView, if (mOrientation == RecyclerView.VERTICAL) context.getScreenWidth() else ViewGroup.LayoutParams.WRAP_CONTENT)
            setItemViewHeight(holder.itemView, if (mOrientation == RecyclerView.VERTICAL) ViewGroup.LayoutParams.WRAP_CONTENT else context.dp2px(200))
        }
        if (mLayoutManagerType == LayoutManagerPopupWindow.TYPE_GRID) {
            setItemViewWidth(holder.itemView, if (mOrientation == RecyclerView.VERTICAL) context.getScreenWidth() / 3 else ViewGroup.LayoutParams.WRAP_CONTENT)
            setItemViewHeight(holder.itemView, if (mOrientation == RecyclerView.VERTICAL) ViewGroup.LayoutParams.WRAP_CONTENT else context.dp2px(200) / 3)
        }
        if (mLayoutManagerType == LayoutManagerPopupWindow.TYPE_STAGGERED) {
            if (mOrientation == RecyclerView.VERTICAL) {
                setItemViewWidth(holder.itemView, context.getScreenWidth() / 3)
                if (position % 3 == 0) {
                    setItemViewHeight(holder.itemView, context.dp2px(300))
                } else if (position % 3 == 1) {
                    setItemViewHeight(holder.itemView, context.dp2px(100))
                } else {
                    setItemViewHeight(holder.itemView, context.dp2px(200))
                }
            } else {
                setItemViewHeight(holder.itemView, context.dp2px(200) / 3)
                if (position % 3 == 0) {
                    setItemViewWidth(holder.itemView, context.dp2px(300))
                } else if (position % 3 == 1) {
                    setItemViewWidth(holder.itemView, context.dp2px(100))
                } else {
                    setItemViewWidth(holder.itemView, context.dp2px(200))
                }
            }

        }
        holder.dataTv.text = data
    }

    private fun showFooterItem(holder: FooterViewHolder) {
        val data = getFooterData()
        if (data.isNullOrEmpty()) {
            return
        }
        resizeHeaderAndFooter(holder.itemView)
        holder.titleTv.text = data
    }

    private fun resizeHeaderAndFooter(view: View) {
        setItemViewWidth(view, if (mOrientation == RecyclerView.VERTICAL) context.getScreenWidth() else context.dp2px(200))
        setItemViewHeight(view, if (mOrientation == RecyclerView.VERTICAL) context.dp2px(200) else context.dp2px(200))
    }

    private class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTv by bindView<TextView>(R.id.title_tv)
    }

    private class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dataTv by bindView<TextView>(R.id.data_tv)
    }

    private class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTv by bindView<TextView>(R.id.title_tv)
    }
}