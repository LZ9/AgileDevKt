package com.lodz.android.componentkt.photopicker.picker.pick

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.componentkt.R
import com.lodz.android.componentkt.photopicker.contract.OnPhotoLoader
import com.lodz.android.componentkt.photopicker.picker.PickerItemBean
import com.lodz.android.componentkt.photopicker.picker.PickerUIConfig
import com.lodz.android.componentkt.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.getScreenWidth

/**
 * 照片选择适配器
 * Created by zhouL on 2018/12/20.
 */
internal class PhotoPickerAdapter(context: Context, photoLoader: OnPhotoLoader<String>?, isNeedCamera: Boolean, config: PickerUIConfig) : BaseRecyclerViewAdapter<PickerItemBean>(context) {

    /** 相机 */
    private val VIEW_TYPE_CAMERA = 0
    /** 图片 */
    private val VIEW_TYPE_ITEM = 1

    /** 图片加载接口 */
    private var mPhotoLoader: OnPhotoLoader<String>?
    /** 监听器 */
    private var mListener: Listener? = null

    /** 未选中图标 */
    private val mUnselectBitmap: Bitmap
    /** 已选中图标 */
    private val mSelectedBitmap: Bitmap

    /** 是否需要相机 */
    private val isNeedCamera: Boolean
    /** UI配置 */
    private val mConfig: PickerUIConfig

    init {
        this.mPhotoLoader = photoLoader
        this.isNeedCamera = isNeedCamera
        this.mConfig = config
        mUnselectBitmap = getUnselectBitmap(mConfig.getSelectedBtnUnselect())
        mSelectedBitmap = getSelectedBitmap(mConfig.getSelectedBtnSelected())
    }

    override fun getItemViewType(position: Int): Int = if (isNeedCamera && position == 0) VIEW_TYPE_CAMERA else VIEW_TYPE_ITEM

    override fun getItemCount(): Int = if (isNeedCamera) super.getItemCount() + 1 else super.getItemCount()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            if (viewType == VIEW_TYPE_CAMERA) {
                PickerCameraViewHolder(getLayoutView(parent, R.layout.componentkt_item_picker_camera))
            } else {
                PickerViewHolder(getLayoutView(parent, R.layout.componentkt_item_picker))
            }

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PickerCameraViewHolder) {
            showCameraItem(holder)
            return
        }

        val bean = getItem(if (isNeedCamera) position - 1 else position)
        if (bean == null || !(holder is PickerViewHolder)) {
            return
        }
        showItem(holder, bean, position)
    }

    /** 显示拍照item */
    private fun showCameraItem(holder: PickerCameraViewHolder) {
        setItemViewHeight(holder.itemView, context.getScreenWidth() / 3)
        holder.itemView.setBackgroundColor(context.getColorCompat(mConfig.getCameraBgColor()))
        if (mConfig.getCameraImg() != 0) {
            holder.cameraBtn.setImageResource(mConfig.getCameraImg())
        }
        holder.cameraBtn.setOnClickListener {
            mListener?.onClickCamera()
        }
    }

    /** 显示图片item */
    private fun showItem(holder: PickerViewHolder, bean: PickerItemBean, position: Int) {
        setItemViewHeight(holder.itemView, context.getScreenWidth() / 3)
        holder.itemView.setBackgroundColor(context.getColorCompat(mConfig.getItemBgColor()))
        mPhotoLoader?.displayImg(context, bean.path, holder.photoImg)
        holder.selectIconBtn.setImageBitmap(if (bean.isSelected) mSelectedBitmap else mUnselectBitmap)
        holder.selectIconBtn.setOnClickListener {
            mListener?.onSelected(bean, position)
        }
        if (mConfig.getMaskColor() != 0) {
            holder.maskView.setBackgroundColor(context.getColorCompat(mConfig.getMaskColor()))
        }
        holder.maskView.visibility = if (bean.isSelected) View.VISIBLE else View.GONE
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
        canvas.drawCircle(side / 2, side / 2, side / 2 - context.dp2px(4), paint)
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
        canvas.drawCircle(side / 2, side / 2, side / 2 - context.dp2px(4), paint)

        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        canvas.drawCircle(side / 2, side / 2, side / 2 - context.dp2px(10), paint)
        return bitmap
    }

    /** 释放资源 */
    fun release() {
        mPhotoLoader = null
    }

    override fun setItemClick(holder: RecyclerView.ViewHolder, position: Int) {
        super.setItemClick(holder, if (isNeedCamera) position - 1 else position)
    }

    override fun setItemLongClick(holder: RecyclerView.ViewHolder, position: Int) {
        super.setItemLongClick(holder, if (isNeedCamera) position - 1 else position)
    }

    /** 设置监听器[listener] */
    fun setListener(listener: Listener) {
        mListener = listener
    }

    interface Listener {
        fun onSelected(bean: PickerItemBean, position: Int)
        fun onClickCamera()
    }

    private inner class PickerCameraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** 拍照按钮 */
        val cameraBtn by bindView<ImageView>(R.id.camera_btn)
    }

    private inner class PickerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** 照片 */
        val photoImg by bindView<ImageView>(R.id.photo_img)
        /** 选中图标 */
        val selectIconBtn by bindView<ImageView>(R.id.select_icon_btn)
        /** 遮罩层 */
        val maskView by bindView<View>(R.id.mask_view)
    }
}