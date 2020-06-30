package com.lodz.android.pandora.photopicker.picker.dialog

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
import com.lodz.android.corekt.album.PicInfo
import com.lodz.android.corekt.anko.dp2pxRF
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.pandora.R
import com.lodz.android.pandora.photopicker.contract.OnImgLoader
import com.lodz.android.pandora.photopicker.picker.PickerUIConfig
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.pandora.widget.rv.recycler.DataViewHolder

/**
 * 图片文件夹列表适配器
 * Created by zhouL on 2018/12/18.
 */
internal class ImageFolderAdapter(context: Context) : BaseRecyclerViewAdapter<ImageFolderItemBean>(context) {

    /** 图片加载器 */
    private var mPdrImgLoader: OnImgLoader<PicInfo>? = null
    /** 未选中图标 */
    private var mPdrUnselectBitmap: Bitmap? = null
    /** 已选中图标 */
    private var mPdrSelectedBitmap: Bitmap? = null

    /** 设置图片加载器[imgLoader] */
    fun setOnImgLoader(imgLoader: OnImgLoader<PicInfo>?) {
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

    private fun showItem(holder: DataViewHolder, bean: ImageFolderItemBean) {
        val imageFolder = bean.imageFolder ?: return

        // 封面图
        mPdrImgLoader?.displayImg(context, imageFolder.getCoverPicInfo(), holder.withView(R.id.pdr_cover_img))
        // 文件夹名称
        holder.withView<TextView>(R.id.pdr_floder_name_tv).text = imageFolder.name
        // 文件夹张数
        holder.withView<TextView>(R.id.pdr_count_tv).text = context.getString(R.string.pandora_picker_folder_num, imageFolder.getCount().toString())
        // 选中图标
        holder.withView<ImageView>(R.id.pdr_select_img).setImageBitmap(if (bean.isSelected) mPdrSelectedBitmap else mPdrUnselectBitmap)
        // 文件夹路径
        holder.withView<TextView>(R.id.pdr_dir_tv).text = if (imageFolder.isAllPicture()) imageFolder.name else imageFolder.dir
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