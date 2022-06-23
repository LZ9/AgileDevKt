package com.lodz.android.agiledevkt.modules.pic.preview

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.picker.preview.adapter.SimplePreviewAdapter
import com.lodz.android.pandora.widget.custom.LongImageView
import java.io.File

/**
 * LongImageView适配器
 * @author zhouL
 * @date 2022/4/20
 */
class LongImageViewAdapter(context: Context) : SimplePreviewAdapter<String, LongImageViewAdapter.DataViewHolder>(context) {

    override fun getViewHolder(frameLayout: FrameLayout): DataViewHolder = DataViewHolder(context, frameLayout)

    override fun onDisplayImg(context: Context, source: String, holder: DataViewHolder) {
        ImageLoader.create(context).loadUrl(source).download(object :RequestListener<File>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                runOnMain {
                    if (resource != null){
                        holder.longImageView.setImageFile(resource)
                    }
                }
                return false
            }
        })

    }

    class DataViewHolder(context: Context, itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {

        val longImageView: LongImageView = LongImageView(context)

        init {
            longImageView.setPlaceholderRes(R.drawable.ic_launcher)
            longImageView.setPlaceholderScaleType(ImageView.ScaleType.CENTER)
            itemView.addView(longImageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }
}