package com.lodz.android.agiledevkt.modules.pic.preview

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.picker.preview.adapter.SimplePreviewAdapter
import java.io.File

/**
 * SubsamplingScaleImageView适配器
 * @author zhouL
 * @date 2022/4/20
 */
class SubsamplingScaleImageViewAdapter(context: Context, private val isScale: Boolean) : SimplePreviewAdapter<String, SubsamplingScaleImageViewAdapter.DataViewHolder>(context) {

    override fun getViewHolder(frameLayout: FrameLayout): DataViewHolder = DataViewHolder(context, frameLayout, isScale)

    override fun onDisplayImg(context: Context, source: String, holder: DataViewHolder) {
        ImageLoader.create(context).loadUrl(source).download(object :RequestListener<File>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean {
                runOnMain {
                    holder.imageView.setImage(ImageSource.resource(R.drawable.ic_launcher))
                }
                return false
            }
            override fun onResourceReady(resource: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                runOnMain {
                    if (resource != null){
                        holder.imageView.setImage(ImageSource.uri(Uri.fromFile(resource)))
                    }
                }
                return false
            }
        })
    }

    override fun onViewDetachedFromWindow(holder: DataViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (isScale){
            holder.imageView.resetScaleAndCenter()
        }
    }

    class DataViewHolder(context: Context, itemView: ViewGroup, isScale: Boolean) : RecyclerView.ViewHolder(itemView) {

        val imageView: SubsamplingScaleImageView = SubsamplingScaleImageView(context)
        init {
            imageView.setImage(ImageSource.resource(R.drawable.ic_launcher))
            imageView.isZoomEnabled = isScale
            itemView.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }
}