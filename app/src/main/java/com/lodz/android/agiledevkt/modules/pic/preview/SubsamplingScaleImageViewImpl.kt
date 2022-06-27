package com.lodz.android.agiledevkt.modules.pic.preview

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.picker.preview.vh.AbsImageView
import com.lodz.android.pandora.picker.preview.vh.addViewInItem
import com.lodz.android.pandora.picker.preview.vh.createFrameLayout
import java.io.File

/**
 * SubsamplingScaleImageView适配器
 * @author zhouL
 * @date 2022/4/20
 */
class SubsamplingScaleImageViewImpl(private val isScale: Boolean): AbsImageView<String, SubsamplingScaleImageViewImpl.SubsamplingScaleImageViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): SubsamplingScaleImageViewHolder = SubsamplingScaleImageViewHolder(context, isScale)

    override fun onBind(context: Context, source: String, viewHolder: SubsamplingScaleImageViewHolder, position: Int) {

        viewHolder.imageView.setOnClickListener {
            context.toastShort(context.getString(R.string.preview_click_tips, (position + 1).toString()))
        }

        viewHolder.imageView.setOnLongClickListener {
            context.toastShort(context.getString(R.string.preview_long_click_tips, (position + 1).toString()))
            return@setOnLongClickListener true
        }

        ImageLoader.create(context).loadUrl(source).download(object : RequestListener<File> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean {
                runOnMain {
                    viewHolder.imageView.setImage(ImageSource.resource(R.drawable.ic_launcher))
                }
                return false
            }

            override fun onResourceReady(resource: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                runOnMain {
                    if (resource != null){
                        viewHolder.imageView.setImage(ImageSource.uri(Uri.fromFile(resource)))
                    }
                }
                return false
            }
        })
    }

    override fun onViewDetached(viewHolder: SubsamplingScaleImageViewHolder) {
        super.onViewDetached(viewHolder)
        if (isScale){
            viewHolder.imageView.resetScaleAndCenter()
        }
    }

    inner class SubsamplingScaleImageViewHolder(context: Context, isScale: Boolean) : RecyclerView.ViewHolder(createFrameLayout(context)) {
        val imageView: SubsamplingScaleImageView = SubsamplingScaleImageView(context)

        init {
            imageView.setImage(ImageSource.resource(R.drawable.ic_launcher))
            imageView.isZoomEnabled = isScale
            addViewInItem(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

}
