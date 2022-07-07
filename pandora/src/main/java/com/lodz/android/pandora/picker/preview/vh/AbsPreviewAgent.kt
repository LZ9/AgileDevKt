package com.lodz.android.pandora.picker.preview.vh

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * 预览器中介
 * Created by zhouL on 2019/1/30.
 */
abstract class AbsPreviewAgent<T, VH : RecyclerView.ViewHolder> {

    /** 创建图片控件，在RV的ViewHolder创建时调用 */
    abstract fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH

    /** 点击回调，[viewHolder]控件，[position]位置 */
    abstract fun onBind(context: Context, source: T, viewHolder: VH, position: Int)

    /** 当图片离开屏幕时回调 */
    open fun onViewDetachedFromWindow(viewHolder: VH) {}

    /** 当图片进入屏幕时回调 */
    open fun onViewAttachedToWindow(viewHolder: VH) {}

    /** 释放资源，在预览器关闭后回调 */
    open fun onRelease() {}
}