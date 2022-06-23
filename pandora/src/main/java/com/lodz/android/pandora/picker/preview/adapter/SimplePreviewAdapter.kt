package com.lodz.android.pandora.picker.preview.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.widget.rv.recycler.base.AbsRvAdapter

/**
 * 图片翻页适配器
 * Created by zhouL on 2018/12/17.
 */
abstract class SimplePreviewAdapter<T, VH : RecyclerView.ViewHolder>(context: Context) : AbsRvAdapter<T, VH>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val frameLayout = FrameLayout(parent.context)
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        frameLayout.layoutParams = layoutParams
        return getViewHolder(frameLayout)
    }

    abstract fun getViewHolder(frameLayout: FrameLayout): VH

    override fun onBind(holder: VH, position: Int) {
        PrintLog.d("testtag", "onBind position : $position")
        val data = getItem(position)
        if (data != null){
            onDisplayImg(context, data, holder)
        }
    }

    /** 加载图片，[source]数据，[holder]图片控件 */
    abstract fun onDisplayImg(context: Context, source: T, holder: VH)
}