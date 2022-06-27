package com.lodz.android.pandora.picker.preview

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.pandora.picker.preview.vh.AbsImageView
import com.lodz.android.pandora.widget.rv.recycler.base.AbsRvAdapter

/**
 * 图片翻页适配器
 * Created by zhouL on 2018/12/17.
 */
internal class PicturePagerAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    context: Context,
    private val mView: AbsImageView<T, VH>, // 图片控件
) : AbsRvAdapter<T, VH>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = mView.onCreateViewHolder(context, parent, viewType)

    override fun onViewDetachedFromWindow(holder: VH) {
        super.onViewDetachedFromWindow(holder)
        mView.onViewDetached(holder)
    }

    override fun onBind(holder: VH, position: Int) {
        val item = getItem(position) ?: return
        mView.onBind(context, item, holder, position)
    }

    /** 释放资源 */
    fun release() {
        mView.onRelease()
    }
}