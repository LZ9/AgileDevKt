package com.lodz.android.pandora.photopicker.preview

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.pandora.photopicker.contract.preview.PreviewController

/**
 * 图片控件抽象类
 * Created by zhouL on 2019/1/30.
 */
abstract class AbsImageView<V : View, T> {

    /** 创建图片控件，在RV的ViewHolder创建时调用 */
    abstract fun onCreateView(context: Context, isScale: Boolean): V

    /** 当图片离开屏幕时回调，可用于重置图片状态，[isScale]是否启用缩放 */
    open fun onViewDetached(view: V, isScale: Boolean) {}

    /** 点击回调，返回true表示开发者自己实现，false使用默认实现 */
    open fun onClickImpl(viewHolder: RecyclerView.ViewHolder, view: V, item: T, position: Int, controller: PreviewController) {}

    /** 长按回调，返回true表示开发者自己实现，false使用默认实现 */
    open fun onLongClickImpl(viewHolder: RecyclerView.ViewHolder, view: V, item: T, position: Int, controller: PreviewController) {}

    /** 加载图片 */
    abstract fun onDisplayImg(context: Context, source: T, view: V)

    /** 释放资源，会在浏览器关闭后回调 */
    open fun onRelease() {}
}