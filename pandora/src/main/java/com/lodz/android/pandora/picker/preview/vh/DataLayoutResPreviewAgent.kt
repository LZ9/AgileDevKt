package com.lodz.android.pandora.picker.preview.vh

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.lodz.android.pandora.widget.rv.recycler.vh.DataViewHolder

/**
 * 预览器中介（DataViewHolder）
 * Created by zhouL on 2019/1/30.
 */
abstract class DataLayoutResPreviewAgent<T> : DataPreviewAgent<T>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(LayoutInflater.from(context).inflate(getLayoutId(), parent, false))

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun getLayoutView(context: Context, viewType: Int): View? = null

}

