package com.lodz.android.pandora.picker.preview.vh

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

/**
 * 预览器简单的ViewHolder
 * @author zhouL
 * @date 2022/6/21
 */
class SimpleImageViewHolder(context: Context) : RecyclerView.ViewHolder(createFrameLayout(context)) {

    val imageView: ImageView = ImageView(context)

    init {
        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        addViewInItem(imageView)

    }
}

fun RecyclerView.ViewHolder.addViewInItem(
    view: View,
    width: Int = ViewGroup.LayoutParams.MATCH_PARENT,
    height: Int = ViewGroup.LayoutParams.MATCH_PARENT
) {
    val layout = itemView
    if (layout is ViewGroup) {
        layout.addView(view, width, height)
    }
}

fun createFrameLayout(context: Context): ViewGroup {
    val frameLayout = FrameLayout(context)
    val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
    frameLayout.layoutParams = layoutParams
    return frameLayout
}


