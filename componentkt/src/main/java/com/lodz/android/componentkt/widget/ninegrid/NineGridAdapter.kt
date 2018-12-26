package com.lodz.android.componentkt.widget.ninegrid

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.componentkt.R
import com.lodz.android.componentkt.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.corekt.anko.bindView

/**
 * 图片九宫格适配器
 * Created by zhouL on 2018/12/25.
 */
internal class NineGridAdapter(context: Context) : BaseRecyclerViewAdapter<String>(context) {

    /** 添加按钮 */
    private val VIEW_TYPE_ADD = 0
    /** 图片 */
    private val VIEW_TYPE_ITEM = 1

    /** 是否需要添加图标 */
    private var isNeedAddBtn = true
    /** 添加图标 */
    private var mAddBtnDrawable: Drawable? = null
    /** 是否需要删除按钮 */
    private var isShowDelete = true
    /** 删除图标 */
    private var mDeleteBtnDrawable: Drawable? = null
    /** 最大图片数 */
    private var maxPic = NineGridView.DEFAULT_MAX_PIC
    /** itme高度 */
    private var mItemHighPx = 0

    /** 监听器 */
    private var mListener: OnNineGridViewListener? = null

    /** 设置是否需要添加图标[isNeed] */
    fun setNeedAddBtn(isNeed: Boolean) {
        isNeedAddBtn = isNeed
    }

    /** 设置添加图标[drawable] */
    fun setAddBtnDrawable(drawable: Drawable?) {
        mAddBtnDrawable = drawable
    }

    /** 设置是否显示删除按钮[isShow] */
    fun setShowDelete(isShow: Boolean) {
        isShowDelete = isShow
    }

    /** 设置删除图标[drawable] */
    fun setDeleteBtnDrawable(drawable: Drawable?) {
        mDeleteBtnDrawable = drawable
    }

    /** 设置最大图片数[count] */
    fun setMaxPic(@IntRange(from = 1) count: Int) {
        maxPic = count
    }

    /** 获取最大图片数 */
    fun getMaxPic(): Int = maxPic

    /** 设置itme的高度[px] */
    fun setItemHigh(px: Int) {
        mItemHighPx = px
    }

    /** 设置监听器[listener] */
    fun setOnNineGridViewListener(listener: OnNineGridViewListener?) {
        mListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        if (!isNeedAddBtn) {//不需要添加按钮
            return VIEW_TYPE_ITEM
        }
        if (getDataSize() == maxPic) {//已经添加满数据了
            return VIEW_TYPE_ITEM
        }
        if (getDataSize() == 0) {//没有数据
            return VIEW_TYPE_ADD
        }
        if (position == getDataSize()) {//最后一位
            return VIEW_TYPE_ADD
        }
        return VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        if (!isNeedAddBtn) {
            return super.getItemCount()
        }
        if (super.getItemCount() == maxPic) {//照片数量和总数相等
            return super.getItemCount()
        }
        return super.getItemCount() + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            if (viewType == VIEW_TYPE_ADD) {
                NineGridAddViewHolder(getLayoutView(parent, R.layout.componentkt_item_nine_grid_add))
            } else {
                NineGridViewHolder(getLayoutView(parent, R.layout.componentkt_item_nine_grid))
            }

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        if (mItemHighPx > 0) {
            setItemViewHeight(holder.itemView, mItemHighPx)
        }
        if (holder is NineGridAddViewHolder) {
            showAddItem(holder)
            return
        }
        val data = getItem(position)
        if (data.isNullOrEmpty() || !(holder is NineGridViewHolder)) {
            return
        }
        showItem(holder, data)
    }

    /** 显示添加按钮Item */
    private fun showAddItem(holder: NineGridAddViewHolder) {
        if (mAddBtnDrawable != null) {
            holder.addBtn.setImageDrawable(mAddBtnDrawable)
        } else {
            holder.addBtn.setImageResource(R.drawable.componentkt_ic_nine_grid_add)
        }
        holder.addBtn.setOnClickListener {
            mListener?.onAddPic(maxPic - getDataSize())
        }
    }

    /** 显示图片Item */
    private fun showItem(holder: NineGridViewHolder, data: String) {
        mListener?.onDisplayImg(context, data, holder.img)
        holder.img.setOnClickListener {
            val str = getItem(holder.adapterPosition)
            if (!str.isNullOrEmpty()) {
                mListener?.onClickPic(str, holder.adapterPosition)
            }
        }

        if (mDeleteBtnDrawable != null) {
            holder.deleteBtn.setImageDrawable(mDeleteBtnDrawable)
        } else {
            holder.deleteBtn.setImageResource(R.drawable.componentkt_ic_nine_grid_delete)
        }
        holder.deleteBtn.visibility = if (isShowDelete) View.VISIBLE else View.GONE
        holder.deleteBtn.setOnClickListener {
            val str = getItem(holder.adapterPosition)
            if (!str.isNullOrEmpty()) {
                mListener?.onDeletePic(str, holder.adapterPosition)
            }
        }
    }

    internal inner class NineGridAddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** 添加按钮 */
        val addBtn by bindView<ImageView>(R.id.add_btn)
    }

    internal inner class NineGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** 图片 */
        val img by bindView<ImageView>(R.id.img)
        /** 删除按钮 */
        val deleteBtn by bindView<ImageView>(R.id.delete_btn)
    }

}