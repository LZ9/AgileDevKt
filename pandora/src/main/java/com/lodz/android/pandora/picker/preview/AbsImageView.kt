package com.lodz.android.pandora.picker.preview

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.pandora.picker.contract.preview.PreviewController

/**
 * 图片控件抽象类
 * Created by zhouL on 2019/1/30.
 */
abstract class AbsImageView<V : View, T>(private val isScale: Boolean) {

    /** 创建图片控件，在RV的ViewHolder创建时调用 */
    abstract fun onCreateView(context: Context, isScale: Boolean = isScale()): V

    /** 当图片离开屏幕时回调，可用于重置图片状态，[isScale]是否启用缩放 */
    open fun onViewDetached(view: V, isScale: Boolean = isScale()) {}

    /** 点击回调，[view]图片控件，[source]数据，[position]位置，[controller]控制器 */
    open fun onClickImpl(viewHolder: RecyclerView.ViewHolder, view: V, source: T, position: Int, controller: PreviewController) {}

    /** 长按回调，[view]图片控件，[source]数据，[position]位置，[controller]控制器 */
    open fun onLongClickImpl(viewHolder: RecyclerView.ViewHolder, view: V, source: T, position: Int, controller: PreviewController):Boolean = false

    /** 加载图片，[source]数据，[view]图片控件 */
    abstract fun onDisplayImg(context: Context, source: T, view: V)

    /** 释放资源，在预览器关闭后回调 */
    open fun onRelease() {}

    /** 是否可以缩放 */
    internal fun isScale(): Boolean = isScale
}