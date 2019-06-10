package com.lodz.android.agiledevkt.modules.rv.drag

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.rv.popup.LayoutManagerPopupWindow
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getScreenWidth
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter

/**
 * 拖拽适配器
 * Created by zhouL on 2018/11/30.
 */
class DragRvAdapter(context: Context) : BaseRecyclerViewAdapter<String>(context) {

    /** 设置拖拽帮助器 */
    private var mItemTouchHelper: ItemTouchHelper? = null
    /** 当前布局 */
    @LayoutManagerPopupWindow.LayoutManagerType
    private var mLayoutManagerType = LayoutManagerPopupWindow.TYPE_LINEAR
    /** 布局方向 */
    private var mOrientation = RecyclerView.VERTICAL

    /** 设置拖拽帮助器[helper] */
    fun setItemTouchHelper(helper: ItemTouchHelper?) {
        mItemTouchHelper = helper
    }

    /** 设置布局类型[type] */
    fun setLayoutManagerType(@LayoutManagerPopupWindow.LayoutManagerType type: Int) {
        mLayoutManagerType = type
    }

    /** 设置布局方向[orientation] */
    fun setOrientation(orientation: Int) {
        mOrientation = orientation
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            DragViewHolder(getLayoutView(parent, R.layout.rv_item_drag))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        if (data.isNullOrEmpty() || holder !is DragViewHolder) {
            return
        }
        showItem(holder, data)
    }

    private fun showItem(holder: DragViewHolder, data: String) {
        if (mLayoutManagerType == LayoutManagerPopupWindow.TYPE_LINEAR) {
            setItemViewWidth(holder.itemView, if (mOrientation == RecyclerView.VERTICAL) context.getScreenWidth() else ViewGroup.LayoutParams.WRAP_CONTENT)
            setItemViewHeight(holder.itemView, if (mOrientation == RecyclerView.VERTICAL) ViewGroup.LayoutParams.WRAP_CONTENT else context.dp2px(200))
        }
        if (mLayoutManagerType == LayoutManagerPopupWindow.TYPE_GRID) {
            setItemViewWidth(holder.itemView, if (mOrientation == RecyclerView.VERTICAL) context.getScreenWidth() / 3 else ViewGroup.LayoutParams.WRAP_CONTENT)
            setItemViewHeight(holder.itemView, if (mOrientation == RecyclerView.VERTICAL) ViewGroup.LayoutParams.WRAP_CONTENT else context.dp2px(200) / 3)
        }
        if (mLayoutManagerType == LayoutManagerPopupWindow.TYPE_STAGGERED) {
            val position = data.toInt()
            if (mOrientation == RecyclerView.VERTICAL) {
                setItemViewWidth(holder.itemView, context.getScreenWidth() / 2)
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
        holder.dataTv.text = data
        holder.enabledTouch(mItemTouchHelper != null)
    }

    private inner class DragViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnTouchListener {
        /** 数据 */
        val dataTv by bindView<TextView>(R.id.data_tv)
        /** 拖拽图片 */
        val dragImg by bindView<ImageView>(R.id.drag_img)

        @SuppressLint("ClickableViewAccessibility")
        internal fun enabledTouch(enabled: Boolean) {
            dragImg.setOnTouchListener(if (enabled) this else null)
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (event == null) {
                return false
            }
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (mItemTouchHelper != null) {
                    mItemTouchHelper!!.startDrag(this)
                }
            }
            return false
        }
    }
}