package com.lodz.android.agiledevkt.modules.phonefile

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lodz.android.agiledevkt.databinding.RvItemPhoneFileBinding
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.file.*
import com.lodz.android.corekt.media.isImageMimeType
import com.lodz.android.corekt.media.isVideoMimeType
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.utils.media.MediaUtils
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.pandora.widget.rv.recycler.DataVBViewHolder

/**
 * 手机文件适配器
 * @author zhouL
 * @date 2022/4/22
 */
class PhoneFileAdapter(context: Context) : BaseRecyclerViewAdapter<DocumentWrapper>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        DataVBViewHolder(getViewBindingLayout(RvItemPhoneFileBinding::inflate, parent))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val file = getItem(position) ?: return
        if (holder !is DataVBViewHolder){
            return
        }
        showItem(holder, file)
    }

    private fun showItem(holder: DataVBViewHolder, document: DocumentWrapper) {
        holder.getVB<RvItemPhoneFileBinding>().apply {
            nameTv.text = "name : ".append(document.file.name)
            pathTv.text = "path : ".append(document.file.absolutePath)
            typeTv.text = "mimeType : ".append(document.documentFile.type)
            sizeTv.text = "size : ".append(document.file.length().formatFileSize())
            if (document.duration > 0) {
                sizeTv.text = sizeTv.text.append("        duration : ${document.duration.formatVideoDuration()}")
            }

            val resId = MediaUtils.getIconBySuffix(document.file.name.getFileSuffix())
            val mimeType = document.documentFile.type
            if (mimeType.isImageMimeType() || mimeType.isVideoMimeType()){
                ImageLoader.create(context).loadUri(document.documentFile.uri).setCenterCrop()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(fileImg)
                return
            }
            ImageLoader.create(context).loadResId(resId).setCenterCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(fileImg)

        }
    }
}