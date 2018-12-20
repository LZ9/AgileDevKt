package com.lodz.android.componentkt.photopicker.preview

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.chrisbanes.photoview.PhotoView
import com.lodz.android.componentkt.photopicker.contract.OnPhotoLoader
import com.lodz.android.componentkt.widget.rv.recycler.BaseRecyclerViewAdapter

/**
 * 图片翻页适配器
 * Created by zhouL on 2018/12/17.
 */
internal class PicturePagerAdapter<T : Any>(context: Context, isScale: Boolean, photoLoader: OnPhotoLoader<T>) : BaseRecyclerViewAdapter<T>(context) {

    /** 是否缩放 */
    private val isScale: Boolean
    /** 图片加载器 */
    private var mPhotoLoader: OnPhotoLoader<T>?

    /** 点击 */
    private var mOnClickListener: ((RecyclerView.ViewHolder, T, Int) -> Unit)? = null
    /** 长按 */
    private var mOnLongClickListener: ((RecyclerView.ViewHolder, T, Int) -> Unit)? = null

    init {
        this.isScale = isScale
        this.mPhotoLoader = photoLoader
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val frameLayout = FrameLayout(parent.context)
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        frameLayout.layoutParams = layoutParams
        return DataViewHolder(frameLayout)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (isScale && holder is PicturePagerAdapter<*>.DataViewHolder) {
            if (holder.photoImg is PhotoView) {
                holder.photoImg.attacher.update()//离开屏幕后还原缩放
            }
        }
    }

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PicturePagerAdapter<*>.DataViewHolder) {
            showItem(holder, position)
        }
    }

    private fun showItem(holder: PicturePagerAdapter<*>.DataViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            mPhotoLoader?.displayImg(context, item, holder.photoImg)

            holder.photoImg.setOnClickListener {
                mOnClickListener?.invoke(holder, item, position)
            }

            holder.photoImg.setOnLongClickListener {
                mOnLongClickListener?.invoke(holder, item, position)
                return@setOnLongClickListener true
            }
        }
    }

    /** 释放资源 */
    fun release() {
        mPhotoLoader = null
    }

    /** 设置点击事件监听器 */
    fun setOnImgClickListener(listener: (viewHolder: RecyclerView.ViewHolder, item: T, position: Int) -> Unit) {
        mOnClickListener = listener
    }

    /** 设置长按事件监听器 */
    fun setOnImgLongClickListener(listener: (viewHolder: RecyclerView.ViewHolder, item: T, position: Int) -> Unit) {
        mOnLongClickListener = listener
    }

    private inner class DataViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        val photoImg: ImageView

        init {
            // 根据是否缩放设置图片控件
            photoImg = if (isScale) PhotoView(itemView.context) else ImageView(itemView.context)
            itemView.addView(photoImg, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            photoImg.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
    }

}