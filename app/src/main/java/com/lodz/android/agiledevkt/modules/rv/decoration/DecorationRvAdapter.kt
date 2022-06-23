package com.lodz.android.agiledevkt.modules.rv.decoration

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.rv.popup.LayoutManagerPopupWindow
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.pandora.widget.rv.recycler.base.BaseRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.vh.DataViewHolder

/**
 * 装饰器测试适配器
 * Created by zhouL on 2018/12/5.
 */
class DecorationRvAdapter(context: Context) : BaseRvAdapter<String>(context) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            DataViewHolder(getLayoutView(parent, R.layout.rv_item_decoration))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        if (data.isNullOrEmpty() || holder !is DataViewHolder) {
            return
        }
        showItem(holder, data)
    }

    private fun showItem(holder: DataViewHolder, data: String) {
        if (mLayoutManagerType == LayoutManagerPopupWindow.TYPE_LINEAR) {
            setItemViewWidth(holder.itemView, if (mOrientation == RecyclerView.VERTICAL) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT)
            setItemViewHeight(holder.itemView, if (mOrientation == RecyclerView.VERTICAL) ViewGroup.LayoutParams.WRAP_CONTENT else context.dp2px(200))
        }
        if (mLayoutManagerType == LayoutManagerPopupWindow.TYPE_GRID) {
            setItemViewWidth(holder.itemView, if (mOrientation == RecyclerView.VERTICAL) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT)
            setItemViewHeight(holder.itemView, if (mOrientation == RecyclerView.VERTICAL) ViewGroup.LayoutParams.WRAP_CONTENT else context.dp2px(200) / 3)
        }
        if (mLayoutManagerType == LayoutManagerPopupWindow.TYPE_STAGGERED) {
            val position = data.toInt()
            if (mOrientation == RecyclerView.VERTICAL) {
                setItemViewWidth(holder.itemView, ViewGroup.LayoutParams.MATCH_PARENT)
                if (position % 2 == 0) {
                    setItemViewHeight(holder.itemView, context.dp2px(300))
                } else {
                    setItemViewHeight(holder.itemView, context.dp2px(200))
                }
            } else {
                setItemViewHeight(holder.itemView, context.dp2px(200) / 2)
                if (position % 2 == 0) {
                    setItemViewWidth(holder.itemView, context.dp2px(300))
                } else {
                    setItemViewWidth(holder.itemView, context.dp2px(200))
                }
            }
        }
        holder.withView<TextView>(R.id.data_tv).text = data
    }
}