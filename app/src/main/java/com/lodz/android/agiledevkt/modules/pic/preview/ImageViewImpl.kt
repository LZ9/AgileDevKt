package com.lodz.android.agiledevkt.modules.pic.preview

import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.picker.contract.preview.PreviewController
import com.lodz.android.pandora.picker.preview.AbsImageView

/**
 * ImageView实现
 * @author zhouL
 * @date 2022/4/20
 */
class ImageViewImpl(private val context: Context, isScale: Boolean, private val isClickClose: Boolean) : AbsImageView<ImageView, String>(isScale) {
    override fun onCreateView(context: Context, isScale: Boolean): ImageView {
        val img = ImageView(context)
        img.scaleType = ImageView.ScaleType.CENTER_INSIDE
        return img
    }

    override fun onDisplayImg(context: Context, source: String, view: ImageView) {
        ImageLoader.create(context).loadUrl(source).setFitCenter().into(view)
    }

    override fun onClickImpl(
        viewHolder: RecyclerView.ViewHolder,
        view: ImageView,
        source: String,
        position: Int,
        controller: PreviewController
    ) {
        if (isClickClose) controller.close() else context.toastShort(context.getString(R.string.preview_click_tips, (position + 1).toString()))
    }

    override fun onLongClickImpl(
        viewHolder: RecyclerView.ViewHolder,
        view: ImageView,
        source: String,
        position: Int,
        controller: PreviewController
    ): Boolean {
        context.toastShort(context.getString(R.string.preview_long_click_tips, (position + 1).toString()))
        return true
    }

}