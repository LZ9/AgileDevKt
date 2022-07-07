package com.lodz.android.pandora.picker.preview

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.pandora.picker.preview.vh.AbsPreviewAgent
import com.lodz.android.pandora.widget.rv.recycler.base.AbsRvAdapter

/**
 * 图片翻页适配器
 * Created by zhouL on 2018/12/17.
 */
internal class PicturePagerAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    context: Context,
    private val mView: AbsPreviewAgent<T, VH>, // 图片控件
) : AbsRvAdapter<T, VH>(context) {

    private var mOnPicturePagerListener: OnPicturePagerListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = mView.onCreateViewHolder(context, parent, viewType)

    override fun onViewDetachedFromWindow(holder: VH) {
        super.onViewDetachedFromWindow(holder)
        mView.onViewDetachedFromWindow(holder)
    }

    override fun onBind(holder: VH, position: Int) {
        val item = getItem(position) ?: return
        mView.onBind(context, item, holder, position)
    }

    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
        mView.onViewAttachedToWindow(holder)
        mOnPicturePagerListener?.onBindPosition(holder.bindingAdapterPosition)
    }

    /** 释放资源 */
    fun release() {
        mView.onRelease()
    }

    fun setOnPicturePagerListener(listener: OnPicturePagerListener) {
        mOnPicturePagerListener = listener
    }

    fun interface OnPicturePagerListener {
        fun onBindPosition(position: Int)
    }
}