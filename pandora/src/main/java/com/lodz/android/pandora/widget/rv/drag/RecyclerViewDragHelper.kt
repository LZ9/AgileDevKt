package com.lodz.android.pandora.widget.rv.drag

import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView拖拽帮助类
 * Created by zhouL on 2018/11/21.
 */
class RecyclerViewDragHelper<T>(private val mContext: Context) {

    /** 允许拖拽  */
    private var mUseDrag = true
    /** 允许从右往左滑动  */
    private var mUseRightToLeftSwipe = true
    /** 允许从左往右滑动  */
    private var mUseLeftToRightSwipe = true
    /** 启用长按拖拽效果  */
    private var isLongPressDragEnabled = true
    /** 启用滑动效果  */
    private var isSwipeEnabled = true
    /** 启用震动效果  */
    private var isVibrateEnabled = false

    /** 监听器  */
    private var mListener: ((list: List<T>) -> Unit)? = null// 列表数据
    /** 数据列表  */
    private var mList: MutableList<T>? = null

    private var mCallback: DragHelperCallback<T>? = null
    private var mItemTouchHelper: ItemTouchHelper? = null

    /** 设置是否允许拖拽[useDrag] */
    fun setUseDrag(useDrag: Boolean): RecyclerViewDragHelper<*> {
        this.mUseDrag = useDrag
        if (mCallback != null) {
            mCallback!!.setUseDrag(useDrag)
        }
        return this
    }

    /** 设置允许从右往左滑动[rightToLeftSwipe] */
    fun setUseRightToLeftSwipe(rightToLeftSwipe: Boolean): RecyclerViewDragHelper<*> {
        this.mUseRightToLeftSwipe = rightToLeftSwipe
        if (mCallback != null) {
            mCallback!!.setUseRightToLeftSwipe(rightToLeftSwipe)
        }
        return this
    }

    /** 设置允许从左往右滑动[leftToRightSwipe] */
    fun setUseLeftToRightSwipe(leftToRightSwipe: Boolean): RecyclerViewDragHelper<*> {
        this.mUseLeftToRightSwipe = leftToRightSwipe
        if (mCallback != null) {
            mCallback!!.setUseLeftToRightSwipe(leftToRightSwipe)
        }
        return this
    }

    /** 设置是否启用[enabled]长按拖拽效果 */
    fun setLongPressDragEnabled(enabled: Boolean): RecyclerViewDragHelper<*> {
        isLongPressDragEnabled = enabled
        if (mCallback != null) {
            mCallback!!.setLongPressDrag(enabled)
        }
        return this
    }

    /** 设置是否启用[enabled]滑动效果 */
    fun setSwipeEnabled(enabled: Boolean): RecyclerViewDragHelper<*> {
        isSwipeEnabled = enabled
        if (mCallback != null) {
            mCallback!!.setSwipe(enabled)
        }
        return this
    }

    /** 是否启用[enabled]震动效果 */
    @RequiresPermission(android.Manifest.permission.VIBRATE)
    fun setVibrateEnabled(enabled: Boolean): RecyclerViewDragHelper<*> {
        isVibrateEnabled = enabled
        if (mCallback != null) {
            mCallback!!.setVibrate(enabled)
        }
        return this
    }

    /** 设置数据列表[list] */
    fun setList(list: MutableList<T>): RecyclerViewDragHelper<*> {
        mList = list
        if (mCallback != null) {
            mCallback!!.setList(list)
        }
        return this
    }

    /** 设置监听器 */
    fun setListener(listener: (list: List<T>) -> Unit): RecyclerViewDragHelper<*> {
        mListener = listener
        mCallback?.setListener(listener)
        return this
    }

    /** 使用默认DragHelperCallback完成构建，控件[recyclerView]，适配器[adapter] */
    fun build(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        build(recyclerView, adapter, DragHelperCallback())
    }

    /** 使用扩展DragHelperCallback完成构建，控件[recyclerView]，适配器[adapter]，回调[callback] */
    fun build(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, callback: DragHelperCallback<T>) {
        callback.setContext(mContext)
        callback.setUseDrag(mUseDrag)
        callback.setUseRightToLeftSwipe(mUseRightToLeftSwipe)
        callback.setUseLeftToRightSwipe(mUseLeftToRightSwipe)
        callback.setLongPressDrag(isLongPressDragEnabled)
        callback.setSwipe(isSwipeEnabled)
        callback.setVibrate(isVibrateEnabled)
        callback.setListener(mListener)
        callback.setList(mList)
        callback.setAdapter(adapter)
        mCallback = callback
        build(recyclerView, callback)
    }

    /** 使用自定义ItemTouchHelper.Callback完成构建，控件[recyclerView]，回调[callback] */
    fun build(recyclerView: RecyclerView, callback: ItemTouchHelper.Callback) {
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper!!.attachToRecyclerView(recyclerView)
    }

    /** 获取ItemTouchHelper */
    fun getItemTouchHelper(): ItemTouchHelper {
        if (mItemTouchHelper == null) {
            throw RuntimeException("please call build() first")
        }
        return mItemTouchHelper!!
    }

}