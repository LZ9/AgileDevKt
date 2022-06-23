package com.lodz.android.agiledevkt.modules.pic.preview

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.chrisbanes.photoview.PhotoView
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.picker.preview.adapter.SimplePreviewAdapter

/**
 * PhotoView适配器
 * @author zhouL
 * @date 2022/4/20
 */
class PhotoViewAdapter(context: Context, private val isScale: Boolean) : SimplePreviewAdapter<String, PhotoViewAdapter.DataViewHolder>(context) {

    override fun getViewHolder(frameLayout: FrameLayout): DataViewHolder = DataViewHolder(context, frameLayout, isScale)

    override fun onDisplayImg(context: Context, source: String, holder: DataViewHolder) {
        ImageLoader.create(context).loadUrl(source).setFitCenter().into(holder.photoView)
    }

    override fun onViewDetachedFromWindow(holder: DataViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (isScale){
            holder.photoView.attacher.update()
        }
    }

    class DataViewHolder(context: Context, itemView: ViewGroup, isScale: Boolean) : RecyclerView.ViewHolder(itemView) {

        val photoView: PhotoView = PhotoView(context)

        init {
            photoView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            photoView.isZoomable = isScale
            itemView.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }

    }

}