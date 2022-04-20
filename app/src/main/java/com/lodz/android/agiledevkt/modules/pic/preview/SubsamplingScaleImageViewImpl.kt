package com.lodz.android.agiledevkt.modules.pic.preview

import android.content.Context
import android.net.Uri
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
import com.lodz.android.pandora.picker.contract.preview.PreviewController
import com.lodz.android.pandora.picker.preview.AbsImageView
import java.io.File

/**
 * SubsamplingScaleImageView实现
 * @author zhouL
 * @date 2022/4/20
 */
class SubsamplingScaleImageViewImpl(private val context: Context, isScale: Boolean, private val isClickClose: Boolean) : AbsImageView<SubsamplingScaleImageView, String>(isScale) {
    override fun onCreateView(context: Context, isScale: Boolean): SubsamplingScaleImageView {
        val img = SubsamplingScaleImageView(context)
        img.setImage(ImageSource.resource(R.drawable.ic_launcher))
        img.isZoomEnabled = isScale
        return img
    }

    override fun onDisplayImg(context: Context, source: String, view: SubsamplingScaleImageView) {
        ImageLoader.create(context).loadUrl(source).download(object :RequestListener<File>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean {
                runOnMain {
                    view.setImage(ImageSource.resource(R.drawable.ic_launcher))
                }
                return false
            }

            override fun onResourceReady(resource: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                runOnMain {
                    if (resource != null){
                        view.setImage(ImageSource.uri(Uri.fromFile(resource)))
                    }
                }
                return false
            }
        })
    }

    override fun onClickImpl(
        viewHolder: RecyclerView.ViewHolder,
        view: SubsamplingScaleImageView,
        source: String,
        position: Int,
        controller: PreviewController
    ) {
        if (isClickClose) controller.close() else context.toastShort(context.getString(R.string.preview_click_tips, (position + 1).toString()))
    }

    override fun onLongClickImpl(
        viewHolder: RecyclerView.ViewHolder,
        view: SubsamplingScaleImageView,
        source: String,
        position: Int,
        controller: PreviewController
    ): Boolean {
        context.toastShort(context.getString(R.string.preview_long_click_tips, (position + 1).toString()))
        return true
    }

    override fun onViewDetached(view: SubsamplingScaleImageView, isScale: Boolean) {
        if (isScale){
            view.resetScaleAndCenter()
        }
    }
}