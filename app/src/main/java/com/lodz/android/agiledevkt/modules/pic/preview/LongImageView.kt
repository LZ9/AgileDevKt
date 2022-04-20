package com.lodz.android.agiledevkt.modules.pic.preview

import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.picker.contract.preview.PreviewController
import com.lodz.android.pandora.picker.preview.AbsImageView
import com.lodz.android.pandora.widget.custom.LongImageView
import java.io.File

/**
 * LongImageView实现
 * @author zhouL
 * @date 2022/4/20
 */
class LongImageView(private val context: Context, isScale: Boolean, private val isClickClose: Boolean) : AbsImageView<LongImageView, String>(isScale) {
    override fun onCreateView(context: Context, isScale: Boolean): LongImageView {
        val img = LongImageView(context)
        img.setPlaceholderRes(R.drawable.ic_launcher)
        img.setPlaceholderScaleType(ImageView.ScaleType.CENTER)
        return img
    }

    override fun onDisplayImg(context: Context, source: String, view: LongImageView) {
        ImageLoader.create(context).loadUrl(source).download(object : RequestListener<File> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                runOnMain {
                    if (resource != null){
                        view.setImageFile(resource)
                    }
                }
                return false
            }
        })
    }

    override fun onClickImpl(
        viewHolder: RecyclerView.ViewHolder,
        view: LongImageView,
        source: String,
        position: Int,
        controller: PreviewController
    ) {
        if (isClickClose) controller.close() else context.toastShort(context.getString(R.string.preview_click_tips, (position + 1).toString()))
    }

    override fun onLongClickImpl(
        viewHolder: RecyclerView.ViewHolder,
        view: LongImageView,
        source: String,
        position: Int,
        controller: PreviewController
    ): Boolean {
        context.toastShort(context.getString(R.string.preview_long_click_tips, (position + 1).toString()))
        return true
    }
}