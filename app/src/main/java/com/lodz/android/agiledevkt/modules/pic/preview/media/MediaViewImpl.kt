package com.lodz.android.agiledevkt.modules.pic.preview.media

import android.content.Context
import android.view.View
import com.lodz.android.corekt.file.DocumentWrapper
import com.lodz.android.pandora.picker.preview.vh.DataPreviewAgent
import com.lodz.android.pandora.widget.rv.recycler.vh.DataViewHolder

/**
 * 多媒体控件实现
 * @author zhouL
 * @date 2022/6/20
 */
class MediaViewImpl(private val context: Context,private val isScale: Boolean) : DataPreviewAgent<DocumentWrapper>() {

    override fun getLayoutView(context: Context, viewType: Int): View? {
        val mediaView = MediaView(context, isScale)
        mediaView.id = android.R.id.icon
        return mediaView
    }

    override fun onBind(context: Context, source: DocumentWrapper, viewHolder: DataViewHolder, position: Int) {
        val mediaView = viewHolder.withView<MediaView>(android.R.id.icon)
        mediaView.setData(source)
    }

    override fun onViewDetachedFromWindow(viewHolder: DataViewHolder) {
        super.onViewDetachedFromWindow(viewHolder)
        viewHolder.withView<MediaView>(android.R.id.icon).detached()
    }

}