package com.lodz.android.agiledevkt.modules.pic.preview

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.picker.preview.vh.DataPreviewAgent
import com.lodz.android.pandora.widget.custom.LongImageView
import com.lodz.android.pandora.widget.rv.recycler.vh.DataViewHolder
import java.io.File

/**
 * LongImageView预览器中介
 * @author zhouL
 * @date 2022/4/20
 */
class LongImageViewImpl : DataPreviewAgent<String>() {

    override fun getLayoutView(context: Context, viewType: Int): View {
        val longImageView = LongImageView(context)
        longImageView.id = android.R.id.icon
        longImageView.setPlaceholderRes(R.drawable.ic_launcher)
        longImageView.setPlaceholderScaleType(ImageView.ScaleType.CENTER)
        return longImageView
    }

    override fun onBind(context: Context, source: String, viewHolder: DataViewHolder, position: Int) {
        val imageView = viewHolder.withView<LongImageView>(android.R.id.icon)

        imageView.setOnClickListener {
            context.toastShort(context.getString(R.string.preview_click_tips, (position + 1).toString()))
        }

        imageView.setOnLongClickListener {
            context.toastShort(context.getString(R.string.preview_long_click_tips, (position + 1).toString()))
            true
        }

        ImageLoader.create(context).loadUrl(source).download(object :RequestListener<File>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean = false
            override fun onResourceReady(resource: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                runOnMain {
                    if (resource != null){
                        imageView.setImageFile(resource)
                    }
                }
                return false
            }
        })
    }

}