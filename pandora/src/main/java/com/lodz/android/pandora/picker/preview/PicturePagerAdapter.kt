package com.lodz.android.pandora.picker.preview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.pandora.picker.contract.preview.PreviewController
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter

/**
 * 图片翻页适配器
 * Created by zhouL on 2018/12/17.
 */
internal class PicturePagerAdapter<V : View, T : Any>(
    context: Context,
    private val mView: AbsImageView<V, T>, // 图片控件
    private val mController: PreviewController // 控制器
) : BaseRecyclerViewAdapter<T>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val frameLayout = FrameLayout(parent.context)
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        frameLayout.layoutParams = layoutParams
        return DataViewHolder(frameLayout)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is PicturePagerAdapter<*, *>.DataViewHolder) {
            mView.onViewDetached(holder.photoImg as V)
        }
    }

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PicturePagerAdapter<*, *>.DataViewHolder) {
            showItem(holder, position)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun showItem(holder: PicturePagerAdapter<*, *>.DataViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            // 图片加载
            mView.onDisplayImg(context, item, holder.photoImg as V)
            // 点击事件
            holder.photoImg.setOnClickListener {
                mView.onClickImpl(holder, holder.photoImg, item, position, mController)
            }
            // 长按事件
            holder.photoImg.setOnLongClickListener {
                mView.onLongClickImpl(holder, holder.photoImg, item, position, mController)
            }

        }
    }

    /** 释放资源 */
    fun release() {
        mView.onRelease()
    }

    private inner class DataViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        val photoImg: V = mView.onCreateView(itemView.context)

        init {
            itemView.addView(photoImg, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

}