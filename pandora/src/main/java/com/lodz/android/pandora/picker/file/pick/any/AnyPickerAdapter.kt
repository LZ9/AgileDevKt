package com.lodz.android.pandora.picker.file.pick.any

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
import com.lodz.android.corekt.anko.dp2pxRF
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.getScreenWidth
import com.lodz.android.corekt.file.PickerInfo
import com.lodz.android.pandora.R
import com.lodz.android.pandora.picker.contract.OnImgLoader
import com.lodz.android.pandora.picker.file.PickerUIConfig
import com.lodz.android.pandora.picker.file.pick.DataWrapper
import com.lodz.android.pandora.widget.rv.recycler.base.BaseRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.vh.DataViewHolder

/**
 * 照片选择适配器
 * Created by zhouL on 2018/12/20.
 */
internal class AnyPickerAdapter<T>(
    context: Context,
    imgLoader: OnImgLoader<T>?,
    private val isPdrNeedCamera: Boolean,
    private val isPdrNeedBottomInfo: Boolean,
    private val mPdrConfig: PickerUIConfig
) : BaseRvAdapter<DataWrapper<T>>(context) {

    /** 相机 */
    private val VIEW_TYPE_CAMERA = 0
    /** 图片 */
    private val VIEW_TYPE_ITEM = 1

    /** 图片加载接口 */
    private var mPdrImgLoader: OnImgLoader<T>?
    /** 监听器 */
    private var mPdrListener: Listener<T>? = null

    /** 未选中图标 */
    private val mPdrUnselectBitmap: Bitmap
    /** 已选中图标 */
    private val mPdrSelectedBitmap: Bitmap

    init {
        this.mPdrImgLoader = imgLoader
        mPdrUnselectBitmap = getUnselectBitmap(mPdrConfig.getSelectedBtnUnselect())
        mPdrSelectedBitmap = getSelectedBitmap(mPdrConfig.getSelectedBtnSelected())
    }

    override fun getItemViewType(position: Int): Int = if (isPdrNeedCamera && position == 0) VIEW_TYPE_CAMERA else VIEW_TYPE_ITEM

    override fun getItemCount(): Int = if (isPdrNeedCamera) super.getItemCount() + 1 else super.getItemCount()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == VIEW_TYPE_CAMERA) {
            PickerCameraViewHolder(getLayoutView(parent, R.layout.pandora_item_picker_camera))
        } else {
            PickerViewHolder(getLayoutView(parent, R.layout.pandora_item_picker))
        }

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PickerCameraViewHolder) {
            showCameraItem(holder)
            return
        }

        val bean = getItem(if (isPdrNeedCamera) position - 1 else position)
        if (bean == null || holder !is PickerViewHolder) {
            return
        }
        showItem(holder, bean, position)
    }

    /** 显示拍照item */
    private fun showCameraItem(holder: PickerCameraViewHolder) {
        setItemViewHeight(holder.itemView, context.getScreenWidth() / 3)
        holder.itemView.setBackgroundColor(context.getColorCompat(mPdrConfig.getCameraBgColor()))
        // 拍照按钮
        val cameraBtn = holder.withView<ImageView>(R.id.pdr_camera_btn)
        if (mPdrConfig.getCameraImg() != 0) {
            cameraBtn.setImageResource(mPdrConfig.getCameraImg())
        }
        cameraBtn.setOnClickListener {
            mPdrListener?.onClickCamera()
        }
    }

    /** 显示图片item */
    private fun showItem(holder: PickerViewHolder, bean: DataWrapper<T>, position: Int) {
        setItemViewHeight(holder.itemView, context.getScreenWidth() / 3)
        holder.itemView.setBackgroundColor(context.getColorCompat(mPdrConfig.getItemBgColor()))
        // 选中图标
        holder.withView<ImageView>(R.id.pdr_select_icon_btn).apply {
            setImageBitmap(if (bean.isSelected) mPdrSelectedBitmap else mPdrUnselectBitmap)
            setOnClickListener {
                mPdrListener?.onSelected(bean, position)
            }
        }
        // 遮罩层
        val maskView = holder.withView<View>(R.id.pdr_mask_view)
        if (mPdrConfig.getMaskColor() != 0) {
            maskView.setBackgroundColor(context.getColorCompat(mPdrConfig.getMaskColor()))
        }
        maskView.visibility = if (bean.isSelected) View.VISIBLE else View.GONE

        val data = bean.data
        mPdrImgLoader?.displayImg(context, data, holder.withView(R.id.pdr_photo_img))

        val layout = holder.withView<ViewGroup>(R.id.pdr_bottom_info_layout)
        if (!isPdrNeedBottomInfo){
            layout.visibility = View.GONE
            return
        }

        if (data is PickerInfo) {
            val name = data.getName()
            val durationFormat = data.getDurationFormatStr()
            layout.visibility = if (name.isEmpty() && durationFormat.isEmpty()) View.GONE else View.VISIBLE
            holder.withView<TextView>(R.id.pdr_name_tv).text = name
            val durationTv = holder.withView<TextView>(R.id.pdr_duration_tv)
            if (durationFormat.isEmpty()) {
                durationTv.visibility = View.GONE
            } else {
                durationTv.visibility = View.VISIBLE
                durationTv.text = durationFormat
            }
        } else {
            layout.visibility = View.GONE
        }
    }

    /** 获取未选中的背景图，颜色[color] */
    private fun getUnselectBitmap(@ColorRes color: Int): Bitmap {
        val side = context.getScreenWidth() / 3f / 4f

        val bitmap = Bitmap.createBitmap(side.toInt(), side.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.TRANSPARENT)
        val paint = Paint()
        paint.color = context.getColorCompat(color)
        paint.strokeWidth = 4f
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(side / 2, side / 2, side / 2 - context.dp2pxRF(4), paint)
        return bitmap
    }

    /** 获取选中的背景图，颜色[color] */
    private fun getSelectedBitmap(@ColorRes color: Int): Bitmap {
        val side = context.getScreenWidth() / 3f / 4f

        val bitmap = Bitmap.createBitmap(side.toInt(), side.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.TRANSPARENT)
        val paint = Paint()
        paint.color = context.getColorCompat(color)
        paint.strokeWidth = 4f
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(side / 2, side / 2, side / 2 - context.dp2pxRF(4), paint)

        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        canvas.drawCircle(side / 2, side / 2, side / 2 - context.dp2pxRF(10), paint)
        return bitmap
    }

    /** 释放资源 */
    fun release() {
        mPdrImgLoader = null
        mPdrListener = null
    }

    override fun setItemClick(holder: RecyclerView.ViewHolder, position: Int) {
        super.setItemClick(holder, if (isPdrNeedCamera) position - 1 else position)
    }

    override fun setItemLongClick(holder: RecyclerView.ViewHolder, position: Int) {
        super.setItemLongClick(holder, if (isPdrNeedCamera) position - 1 else position)
    }

    /** 设置监听器[listener] */
    fun setListener(listener: Listener<T>) {
        mPdrListener = listener
    }

    interface Listener<T> {

        fun onSelected(bean: DataWrapper<T>, position: Int)

        fun onClickCamera()
    }

    private class PickerCameraViewHolder(itemView: View) : DataViewHolder(itemView)

    private class PickerViewHolder(itemView: View) : DataViewHolder(itemView)
}