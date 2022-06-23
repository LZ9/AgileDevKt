package com.lodz.android.pandora.picker.file.dialog

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.anko.dp2pxRF
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.file.DocumentWrapper
import com.lodz.android.pandora.R
import com.lodz.android.pandora.picker.contract.OnImgLoader
import com.lodz.android.pandora.picker.file.PickerUIConfig
import com.lodz.android.pandora.widget.rv.recycler.base.BaseRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.vh.DataViewHolder

/**
 * 图片文件夹列表适配器
 * Created by zhouL on 2018/12/18.
 */
internal class FolderAdapter(context: Context) : BaseRvAdapter<FolderItemBean>(context) {

    /** 图片加载器 */
    private var mPdrImgLoader: OnImgLoader<DocumentWrapper>? = null
    /** 未选中图标 */
    private var mPdrUnselectBitmap: Bitmap? = null
    /** 已选中图标 */
    private var mPdrSelectedBitmap: Bitmap? = null

    /** 设置图片加载器[imgLoader] */
    fun setOnImgLoader(imgLoader: OnImgLoader<DocumentWrapper>?) {
        mPdrImgLoader = imgLoader
    }

    /** 设置UI配置[config] */
    fun setPickerUIConfig(config: PickerUIConfig) {
        mPdrUnselectBitmap = getUnselectBitmap(config.getFolderSelectColor())
        mPdrSelectedBitmap = getSelectedBitmap(config.getFolderSelectColor())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            DataViewHolder(getLayoutView(parent, R.layout.pandora_item_img_folder))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val bean = getItem(position)
        if (bean == null || holder !is DataViewHolder) {
            return
        }
        showItem(holder, bean)
    }

    private fun showItem(holder: DataViewHolder, bean: FolderItemBean) {
        val imageFolder = bean.documentFolder ?: return
        val img = holder.withView<ImageView>(R.id.pdr_cover_img)
        val cover = imageFolder.coverDocument
        // 封面图
        if (cover == null) {
            img.setImageResource(R.drawable.pandora_ic_folder)
        } else {
            mPdrImgLoader?.displayImg(context, cover, img)
        }
        // 文件夹名称
        holder.withView<TextView>(R.id.pdr_floder_name_tv).text = imageFolder.dirName
        // 文件夹张数
        holder.withView<TextView>(R.id.pdr_count_tv).text = context.getString(R.string.pandora_picker_folder_num, imageFolder.getCount().toString())
        // 选中图标
        holder.withView<ImageView>(R.id.pdr_select_img).setImageBitmap(if (bean.isSelected) mPdrSelectedBitmap else mPdrUnselectBitmap)
        // 文件夹路径
        holder.withView<TextView>(R.id.pdr_dir_tv).text = imageFolder.dirName
    }

    private fun getUnselectBitmap(@ColorRes color: Int): Bitmap {
        val side = context.dp2pxRF(30)
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
        val side = context.dp2pxRF(30)
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
}