package com.lodz.android.agiledevkt.modules.pic.preview.media

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.file.DocumentWrapper
import com.lodz.android.pandora.picker.preview.vh.AbsImageView
import com.lodz.android.pandora.picker.preview.vh.addViewInItem
import com.lodz.android.pandora.picker.preview.vh.createFrameLayout

/**
 * 多媒体控件实现
 * @author zhouL
 * @date 2022/6/20
 */
class MediaViewImpl(private val context: Context,private val isScale: Boolean) : AbsImageView<DocumentWrapper, MediaViewImpl.MediaViewHolder>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): MediaViewHolder =
        MediaViewHolder(context)

    override fun onBind(context: Context, source: DocumentWrapper, viewHolder: MediaViewHolder, position: Int) {
        viewHolder.mediaView.setData(source)
    }

    override fun onViewDetached(viewHolder: MediaViewHolder) {
        super.onViewDetached(viewHolder)
        viewHolder.mediaView.detached()
    }

    inner class MediaViewHolder(context: Context) : RecyclerView.ViewHolder(createFrameLayout(context)) {
        val mediaView: MediaView = MediaView(context, isScale)
        init {
            addViewInItem(mediaView)
        }
    }


}