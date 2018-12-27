package com.lodz.android.pandora.widget.rv.binder

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerBinder的基类适配器
 * Created by zhouL on 2018/12/10.
 */
open class RvBinderAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /** 默认的Binder类型  */
    private val DEFAULT_BINDER_TYPE = -1
    /** RecyclerBinder列表 */
    private val mBinderList = ArrayList<RecyclerBinder<*>>()

    fun getContext(): Context = context

    override fun getItemViewType(position: Int): Int = getViewTypeByPosition(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binder = getBinderByViewType(viewType)
        if (binder == null) {
            return DefaultRecyclerBinder(getContext(), DEFAULT_BINDER_TYPE).onCreateViewHolder(parent)
        }
        return binder.onCreateViewHolder(parent)
    }

    override fun getItemCount(): Int {
        var sumCount = 0
        for (binder in mBinderList) {
            sumCount += binder.getCount()
        }
        return sumCount
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binder = getBinderByViewType(getItemViewType(position))
        if (binder == null) {
            return
        }
        binder.onBindViewHolder(holder, getBinderPosition(position))
    }

    /** 添加RecyclerBinder */
    fun addBinder(binder: RecyclerBinder<*>) {
        mBinderList.add(binder)
    }

    /** 删除RecyclerBinder */
    fun removeBinder(viewType: Int) {
        val binder = getBinderByViewType(viewType)
        if (binder != null) {
            mBinderList.remove(binder)
        }
    }

    /** 清除所有RecyclerBinder */
    fun clearBinder() {
        mBinderList.clear()
    }

    /** 根据界面类型[viewType]获取对于的Binder */
    private fun getBinderByViewType(viewType: Int): RecyclerBinder<*>? {
        for (binder in mBinderList) {
            if (binder.getViewType() == viewType) {
                return binder
            }
        }
        return null
    }

    /** 根据总位置[position]获取对应的ViewType */
    private fun getViewTypeByPosition(position: Int): Int {
        var size = 0
        for (binder in mBinderList) {
            size += binder.getCount()
            if (position < size) {
                return binder.getViewType()
            }
        }
        return DEFAULT_BINDER_TYPE
    }

    /** 根据总位置[position]换算出Binder内对应的位置 */
    private fun getBinderPosition(position: Int): Int {
        var currentSize = 0//当前总数
        var lastSize = 0//上一次总数
        for (binder in mBinderList) {
            currentSize += binder.getCount()
            if (position < currentSize) {
                return position - lastSize//当前Binder的相对position
            }
            lastSize = currentSize
        }
        return 0
    }

}