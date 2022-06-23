package com.lodz.android.agiledevkt.modules.pic.preview.media

import android.content.Context
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.file.DocumentWrapper
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.picker.preview.AbsImageView

/**
 * 多媒体控件实现
 * @author zhouL
 * @date 2022/6/20
 */
class MediaViewImpl(private val context: Context, isScale: Boolean, private val isClickClose: Boolean) : AbsImageView<MediaView, DocumentWrapper>(isScale) {

    override fun onCreateView(context: Context, isScale: Boolean): MediaView {
        val view = MediaView(context, isScale)
        return view
    }

    override fun onDisplayImg(context: Context, source: DocumentWrapper, view: MediaView) {
        view.setData(source)
        ImageLoader.create(context)
            .loadUri(source.documentFile.uri)
            .setPlaceholder(R.drawable.pandora_ic_file)
            .setError(R.drawable.pandora_ic_audio)
            .setFitCenter()
            .into(view.getImageView())
    }

    override fun onViewDetached(view: MediaView, isScale: Boolean) {
        super.onViewDetached(view, isScale)
        view.detached()
    }

}