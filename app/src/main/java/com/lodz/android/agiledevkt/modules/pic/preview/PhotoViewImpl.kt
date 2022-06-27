package com.lodz.android.agiledevkt.modules.pic.preview

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.chrisbanes.photoview.PhotoView
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.picker.preview.vh.AbsImageView
import com.lodz.android.pandora.picker.preview.vh.addViewInItem
import com.lodz.android.pandora.picker.preview.vh.createFrameLayout

/**
 * PhotoView适配器
 * @author zhouL
 * @date 2022/4/20
 */
class PhotoViewImpl(private val isScale: Boolean) : AbsImageView<String, PhotoViewImpl.PhotoViewHolder>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): PhotoViewHolder = PhotoViewHolder(context, isScale)

    override fun onBind(context: Context, source: String, viewHolder: PhotoViewHolder, position: Int) {

        viewHolder.photoView.setOnClickListener {
            context.toastShort(context.getString(R.string.preview_click_tips, (position + 1).toString()))
        }

        viewHolder.photoView.setOnLongClickListener {
            context.toastShort(context.getString(R.string.preview_long_click_tips, (position + 1).toString()))
            return@setOnLongClickListener true
        }

        ImageLoader.create(context).loadUrl(source).setFitCenter().into(viewHolder.photoView)
    }

    override fun onViewDetached(viewHolder: PhotoViewHolder) {
        super.onViewDetached(viewHolder)
        if (isScale){
            viewHolder.photoView.attacher.update()
        }
    }

    inner class PhotoViewHolder(context: Context, isScale: Boolean) : RecyclerView.ViewHolder(createFrameLayout(context)) {
        val photoView: PhotoView = PhotoView(context)
        init {
            photoView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            photoView.isZoomable = isScale
            addViewInItem(photoView)
        }
    }

}