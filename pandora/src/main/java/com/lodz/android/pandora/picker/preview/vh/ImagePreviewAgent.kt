package com.lodz.android.pandora.picker.preview.vh

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.lodz.android.pandora.widget.rv.recycler.vh.DataViewHolder

/**
 * 用ImageView实现预览器中介
 * @author zhouL
 * @date 2022/6/21
 */
abstract class ImagePreviewAgent<T> : DataPreviewAgent<T>() {

    override fun getLayoutView(context: Context, viewType: Int): View {
        val imageView = ImageView(context)
        imageView.id = android.R.id.icon
        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        return imageView
    }

    override fun onBind(context: Context, source: T, viewHolder: DataViewHolder, position: Int) {
        displayImag(context, source, viewHolder.withView(android.R.id.icon), position)
    }

    abstract fun displayImag(context: Context, source: T, imageView: ImageView, position: Int)
}
