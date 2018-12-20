package com.lodz.android.componentkt.photopicker.picker.dialog

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.componentkt.R
import com.lodz.android.componentkt.photopicker.contract.OnPhotoLoader
import com.lodz.android.componentkt.photopicker.picker.PickerUIConfig
import com.lodz.android.componentkt.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getColorCompat

/**
 * 图片文件夹列表适配器
 * Created by zhouL on 2018/12/18.
 */
internal class ImageFolderAdapter(context: Context) : BaseRecyclerViewAdapter<ImageFolderItemBean>(context) {

    /** 图片加载器 */
    private var mPhotoLoader: OnPhotoLoader<String>? = null
    /** 未选中图标 */
    private var mUnselectBitmap: Bitmap? = null
    /** 已选中图标 */
    private var mSelectedBitmap: Bitmap? = null

    /** 设置图片加载器[photoLoader] */
    fun setOnPhotoLoader(photoLoader: OnPhotoLoader<String>?) {
        mPhotoLoader = photoLoader
    }

    /** 设置UI配置[config] */
    fun setPickerUIConfig(config: PickerUIConfig) {
        mUnselectBitmap = getUnselectBitmap(config.getFolderSelectColor())
        mSelectedBitmap = getSelectedBitmap(config.getFolderSelectColor())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            ImageFolderViewHolder(getLayoutView(parent, R.layout.componentkt_item_img_folder))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val bean = getItem(position)
        if (bean == null || !(holder is ImageFolderViewHolder)) {
            return
        }
        showItem(holder, bean)
    }

    private fun showItem(holder: ImageFolderViewHolder, bean: ImageFolderItemBean) {
        val imageFolder = bean.imageFolder
        if (imageFolder == null) {
            return
        }

        mPhotoLoader?.displayImg(context, imageFolder.coverImgPath, holder.coverImg)
        holder.floderNameTv.text = imageFolder.name
        holder.countTv.text = context.getString(R.string.componentkt_picker_folder_num, imageFolder.count.toString())
        holder.selectImg.setImageBitmap(if (bean.isSelected) mSelectedBitmap else mUnselectBitmap)
        holder.dirTv.text = if (imageFolder.isAllPicture()) imageFolder.name else imageFolder.dir
    }

    private fun getUnselectBitmap(@ColorRes color: Int): Bitmap {
        val side = context.dp2px(30)
        val bitmap = Bitmap.createBitmap(side.toInt(), side.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.TRANSPARENT)
        val paint = Paint()
        paint.color = context.getColorCompat(color)
        paint.strokeWidth = 4f
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(side / 2, side / 2, side / 2 - 10, paint)
        return bitmap
    }

    private fun getSelectedBitmap(@ColorRes color: Int): Bitmap {
        val side = context.dp2px(30)
        val bitmap = Bitmap.createBitmap(side.toInt(), side.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.TRANSPARENT)
        val paint = Paint()
        paint.color = context.getColorCompat(color)
        paint.strokeWidth = 4f
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(side / 2, side / 2, side / 2 - 10, paint)

        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        canvas.drawCircle(side / 2, side / 2, side / 2 - 25, paint)
        return bitmap
    }

    private inner class ImageFolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** 封面图 */
        val coverImg by bindView<ImageView>(R.id.cover_img)
        /** 文件夹名称 */
        val floderNameTv by bindView<TextView>(R.id.floder_name_tv)
        /** 文件夹路径 */
        val dirTv by bindView<TextView>(R.id.dir_tv)
        /** 文件夹张数 */
        val countTv by bindView<TextView>(R.id.count_tv)
        /** 选中图标 */
        val selectImg by bindView<ImageView>(R.id.select_img)

    }
}