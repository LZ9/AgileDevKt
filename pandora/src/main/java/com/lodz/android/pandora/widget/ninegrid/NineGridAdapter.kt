package com.lodz.android.pandora.widget.ninegrid

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.R
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.pandora.widget.rv.recycler.DataViewHolder

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
    private var isPdrNeedAddBtn = true
    /** 添加图标 */
    private var mPdrAddBtnDrawable: Drawable? = null
    /** 是否需要删除按钮 */
    private var isPdrShowDelete = true
    /** 删除图标 */
    private var mPdrDeleteBtnDrawable: Drawable? = null
    /** 最大图片数 */
    private var mPdrMaxPic = NineGridView.DEFAULT_MAX_PIC
    /** itme高度 */
    private var mPdrItemHighPx = 0

    /** 监听器 */
    private var mPdrListener: OnNineGridViewListener? = null

    /** 设置是否需要添加图标[isNeed] */
    fun setNeedAddBtn(isNeed: Boolean) {
        isPdrNeedAddBtn = isNeed
    }

    /** 设置添加图标[drawable] */
    fun setAddBtnDrawable(drawable: Drawable?) {
        mPdrAddBtnDrawable = drawable
    }

    /** 设置是否显示删除按钮[isShow] */
    fun setShowDelete(isShow: Boolean) {
        isPdrShowDelete = isShow
    }

    /** 设置删除图标[drawable] */
    fun setDeleteBtnDrawable(drawable: Drawable?) {
        mPdrDeleteBtnDrawable = drawable
    }

    /** 设置最大图片数[count] */
    fun setMaxPic(@IntRange(from = 1) count: Int) {
        mPdrMaxPic = count
    }

    /** 获取最大图片数 */
    fun getMaxPic(): Int = mPdrMaxPic

    /** 设置itme的高度[px] */
    fun setItemHigh(px: Int) {
        mPdrItemHighPx = px
    }

    /** 设置监听器[listener] */
    fun setOnNineGridViewListener(listener: OnNineGridViewListener?) {
        mPdrListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        if (!isPdrNeedAddBtn) {//不需要添加按钮
            return VIEW_TYPE_ITEM
        }
        if (getDataSize() == mPdrMaxPic) {//已经添加满数据了
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
        if (!isPdrNeedAddBtn) {
            return super.getItemCount()
        }
        if (super.getItemCount() == mPdrMaxPic) {//照片数量和总数相等
            return super.getItemCount()
        }
        return super.getItemCount() + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            if (viewType == VIEW_TYPE_ADD) {
                NineGridAddViewHolder(getLayoutView(parent, R.layout.pandora_item_nine_grid_add))
            } else {
                NineGridViewHolder(getLayoutView(parent, R.layout.pandora_item_nine_grid))
            }

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        if (mPdrItemHighPx > 0) {
            setItemViewHeight(holder.itemView, mPdrItemHighPx)
        }
        if (holder is NineGridAddViewHolder) {
            showAddItem(holder)
            return
        }
        val data = getItem(position)
        if (data.isNullOrEmpty() || holder !is NineGridViewHolder) {
            return
        }
        showItem(holder, data)
    }

    /** 显示添加按钮Item */
    private fun showAddItem(holder: NineGridAddViewHolder) {
        // 添加按钮
        val addBtn = holder.withView<ImageView>(R.id.pdr_add_btn)
        if (mPdrAddBtnDrawable != null) {
            addBtn.setImageDrawable(mPdrAddBtnDrawable)
        } else {
            addBtn.setImageResource(R.drawable.pandora_ic_nine_grid_add)
        }
        addBtn.setOnClickListener {
            mPdrListener?.onAddPic(mPdrMaxPic - getDataSize())
        }
    }

    /** 显示图片Item */
    private fun showItem(holder: NineGridViewHolder, data: String) {
        // 图片
        val img = holder.withView<ImageView>(R.id.pdr_img)
        mPdrListener?.onDisplayImg(context, data, img)
        img.setOnClickListener {
            val str = getItem(holder.adapterPosition)
            if (!str.isNullOrEmpty()) {
                mPdrListener?.onClickPic(str, holder.adapterPosition)
            }
        }

        // 删除按钮
        val deleteBtn = holder.withView<ImageView>(R.id.pdr_delete_btn)
        if (mPdrDeleteBtnDrawable != null) {
            deleteBtn.setImageDrawable(mPdrDeleteBtnDrawable)
        } else {
            deleteBtn.setImageResource(R.drawable.pandora_ic_nine_grid_delete)
        }
        deleteBtn.visibility = if (isPdrShowDelete) View.VISIBLE else View.GONE
        deleteBtn.setOnClickListener {
            val str = getItem(holder.adapterPosition)
            if (!str.isNullOrEmpty()) {
                mPdrListener?.onDeletePic(str, holder.adapterPosition)
            }
        }
    }

    internal inner class NineGridAddViewHolder(itemView: View) : DataViewHolder(itemView)

    internal inner class NineGridViewHolder(itemView: View) : DataViewHolder(itemView)
}