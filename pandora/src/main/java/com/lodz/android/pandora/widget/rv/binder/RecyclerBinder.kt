package com.lodz.android.pandora.widget.rv.binder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerBinder基类
 * Created by zhouL on 2018/12/10.
 */
abstract class RecyclerBinder<T>(private val context: Context, private val binderType: Int) {

    fun getContext(): Context = context

    abstract fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    fun getViewType(): Int = binderType

    abstract fun getData(position: Int): T?

    abstract fun getCount(): Int

    /** 在onCreateViewHolder方法中根据[layoutId]获取View */
    protected fun getLayoutView(parent: ViewGroup, @LayoutRes layoutId: Int, attachToRoot: Boolean = false): View =
            LayoutInflater.from(context).inflate(layoutId, parent, attachToRoot)
}