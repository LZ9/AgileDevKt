package com.lodz.android.pandora.picker.preview.vh

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.lodz.android.pandora.widget.rv.recycler.vh.DataViewHolder

/**
 * 预览器中介（DataViewHolder）
 * Created by zhouL on 2019/1/30.
 */
abstract class DataPreviewAgent<T> : AbsPreviewAgent<T, DataViewHolder>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): DataViewHolder {
        val root = createFrameLayout(context)
        root.addView(getLayoutView(context, viewType), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return DataViewHolder(root)
    }

    protected fun createFrameLayout(context: Context): ViewGroup {
        val frameLayout = FrameLayout(context)
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        frameLayout.layoutParams = layoutParams
        return frameLayout
    }

    abstract fun getLayoutView(context: Context, viewType: Int): View?
}