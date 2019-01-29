package com.lodz.android.pandora.photopicker.preview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.lodz.android.pandora.photopicker.contract.OnImgLoader
import com.lodz.android.pandora.photopicker.contract.preview.OnLargeImgLoader
import com.lodz.android.pandora.widget.photoview.PhotoView
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter

/**
 * 图片翻页适配器
 * Created by zhouL on 2018/12/17.
 */
internal class PicturePagerAdapter<T : Any>(context: Context, isScale: Boolean, imgLoader: OnImgLoader<T>?, largeImgLoader: OnLargeImgLoader<T>?) : BaseRecyclerViewAdapter<T>(context) {

    /** 是否缩放 */
    private val isScale: Boolean
    /** 图片加载器 */
    private var mImgLoader: OnImgLoader<T>?
    /** 大图加载器 */
    private var mLargeImgLoader: OnLargeImgLoader<T>?

    /** 点击 */
    private var mOnClickListener: ((viewHolder: RecyclerView.ViewHolder, item: T, position: Int) -> Unit)? = null
    /** 长按 */
    private var mOnLongClickListener: ((viewHolder: RecyclerView.ViewHolder, item: T, position: Int) -> Unit)? = null

    init {
        this.isScale = isScale
        this.mImgLoader = imgLoader
        this.mLargeImgLoader = largeImgLoader
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
                holder.photoImg.getAttacher().update()//离开屏幕后还原缩放
            }
            if (holder.photoImg is SubsamplingScaleImageView) {
                holder.photoImg.resetScaleAndCenter()
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
            if (holder.photoImg is ImageView){
                mImgLoader?.displayImg(context, item, holder.photoImg)
            }

            if (holder.photoImg is SubsamplingScaleImageView){
                mLargeImgLoader?.displayImg(context, item, holder.photoImg)
            }

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
        mImgLoader = null
        mLargeImgLoader = null
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
        val photoImg: View

        init {
            // 根据监听器情况判断要使用哪种控件
            if (mImgLoader != null) {
                // 根据是否缩放设置图片控件
                photoImg = if (isScale) PhotoView(itemView.context) else ImageView(itemView.context)
                (photoImg as ImageView).scaleType = ImageView.ScaleType.CENTER_INSIDE
            } else if (mLargeImgLoader != null) {
                photoImg = SubsamplingScaleImageView(itemView.context)
                photoImg.isZoomEnabled = isScale
            } else {
                photoImg = ImageView(itemView.context)
                photoImg.scaleType = ImageView.ScaleType.CENTER_INSIDE
            }
            itemView.addView(photoImg, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

}