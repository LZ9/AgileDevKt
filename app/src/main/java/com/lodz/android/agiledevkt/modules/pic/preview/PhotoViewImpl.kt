package com.lodz.android.agiledevkt.modules.pic.preview

import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.chrisbanes.photoview.PhotoView
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.picker.contract.preview.PreviewController
import com.lodz.android.pandora.picker.preview.AbsImageView

/**
 * PhotoView实现
 * @author zhouL
 * @date 2022/4/20
 */
class PhotoViewImpl(private val context: Context, isScale: Boolean, private val isClickClose: Boolean) : AbsImageView<PhotoView, String>(isScale) {

    override fun onCreateView(context: Context, isScale: Boolean): PhotoView {
        val img = PhotoView(context)
        img.scaleType = ImageView.ScaleType.CENTER_INSIDE
        img.isZoomable = isScale
        return img
    }

    override fun onDisplayImg(context: Context, source: String, view: PhotoView) {
        ImageLoader.create(context).loadUrl(source).setFitCenter().into(view)
    }

    override fun onClickImpl(
        viewHolder: RecyclerView.ViewHolder,
        view: PhotoView,
        source: String,
        position: Int,
        controller: PreviewController
    ) {
        if (isClickClose) controller.close() else context.toastShort(context.getString(R.string.preview_click_tips, (position + 1).toString()))
    }

    override fun onLongClickImpl(
        viewHolder: RecyclerView.ViewHolder,
        view: PhotoView,
        source: String,
        position: Int,
        controller: PreviewController
    ): Boolean {
        context.toastShort(context.getString(R.string.preview_long_click_tips, (position + 1).toString()))
        return true
    }

    override fun onViewDetached(view: PhotoView, isScale: Boolean) {
        super.onViewDetached(view, isScale)
        if (isScale){
            view.attacher.update()
        }
    }
}