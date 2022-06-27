package com.lodz.android.agiledevkt.modules.pic.preview

import android.content.Context
import android.view.ViewGroup
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
import com.lodz.android.pandora.picker.preview.vh.AbsImageView
import com.lodz.android.pandora.picker.preview.vh.addViewInItem
import com.lodz.android.pandora.picker.preview.vh.createFrameLayout
import com.lodz.android.pandora.widget.custom.LongImageView
import java.io.File

/**
 * LongImageView对象
 * @author zhouL
 * @date 2022/4/20
 */
class LongImageViewImpl : AbsImageView<String, LongImageViewImpl.LongImageViewHolder>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): LongImageViewHolder = LongImageViewHolder(context)

    override fun onBind(context: Context, source: String, viewHolder: LongImageViewHolder, position: Int) {
        viewHolder.itemView.setOnLongClickListener {
            context.toastShort(context.getString(R.string.preview_long_click_tips, (position + 1).toString()))
            return@setOnLongClickListener true
        }

        viewHolder.itemView.setOnClickListener {
            context.toastShort(context.getString(R.string.preview_click_tips, (position + 1).toString()))
        }

        ImageLoader.create(context).loadUrl(source).download(object :RequestListener<File>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean = false

            override fun onResourceReady(resource: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                runOnMain {
                    if (resource != null){
                        viewHolder.longImageView.setImageFile(resource)
                    }
                }
                return false
            }
        })
    }

    inner class LongImageViewHolder(context: Context) : RecyclerView.ViewHolder(createFrameLayout(context)) {
        val longImageView: LongImageView = LongImageView(context)
        init {
            longImageView.setPlaceholderRes(R.drawable.ic_launcher)
            longImageView.setPlaceholderScaleType(ImageView.ScaleType.CENTER)
            addViewInItem(longImageView)
        }
    }

}