package com.lodz.android.pandora.picker.preview.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

/**
 * 预览器简单的ViewHolder
 * @author zhouL
 * @date 2022/6/21
 */
class ImageViewHolder(context: Context, itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {

    val imageView: ImageView = ImageView(context)

    init {
        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        itemView.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

}
