package com.lodz.android.agiledevkt.modules.pic.preview

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.github.chrisbanes.photoview.PhotoView
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.picker.preview.vh.DataPreviewAgent
import com.lodz.android.pandora.widget.rv.recycler.vh.DataViewHolder

/**
 * PhotoView预览器中介
 * @author zhouL
 * @date 2022/4/20
 */
abstract class PhotoViewImpl<T>(private val isScale: Boolean) : DataPreviewAgent<T>() {

    override fun getLayoutView(context: Context, viewType: Int): View? {
        val photoView = PhotoView(context)
        photoView.id = android.R.id.icon
        photoView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        photoView.isZoomable = isScale
        return photoView
    }

    override fun onBind(context: Context, source: T, viewHolder: DataViewHolder, position: Int) {
        val photoView = viewHolder.withView<PhotoView>(android.R.id.icon)

        photoView.setOnClickListener {
            context.toastShort(context.getString(R.string.preview_click_tips, (position + 1).toString()))
        }

        photoView.setOnLongClickListener {
            context.toastShort(context.getString(R.string.preview_long_click_tips, (position + 1).toString()))
            true
        }

        displayImag(context, source, photoView, position)
    }

    abstract fun displayImag(context: Context, source: T, photoView: PhotoView, position: Int)

    override fun onViewDetachedFromWindow(viewHolder: DataViewHolder) {
        super.onViewDetachedFromWindow(viewHolder)
        if (isScale){
            viewHolder.withView<PhotoView>(android.R.id.icon).attacher.update()
        }
    }
}