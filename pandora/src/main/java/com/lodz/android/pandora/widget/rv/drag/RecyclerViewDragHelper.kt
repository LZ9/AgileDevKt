package com.lodz.android.pandora.widget.rv.drag

import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView拖拽帮助类
 * Created by zhouL on 2018/11/21.
 */
class RecyclerViewDragHelper<T, VH : RecyclerView.ViewHolder>(private val mContext: Context) {

    /** 允许拖拽 */
    private var mPdrUseDrag = true
    /** 允许从右往左滑动 */
    private var mPdrUseRightToLeftSwipe = true
    /** 允许从左往右滑动 */
    private var mPdrUseLeftToRightSwipe = true
    /** 启用长按拖拽效果 */
    private var isPdrLongPressDragEnabled = true
    /** 启用滑动效果 */
    private var isPdrSwipeEnabled = true
    /** 启用震动效果 */
    private var isPdrVibrateEnabled = false

    /** 监听器 */
    private var mPdrListener: ((list: List<T>) -> Unit)? = null// 列表数据
    /** 数据列表 */
    private var mPdrList: MutableList<T>? = null

    private var mPdrCallback: DragHelperCallback<T, VH>? = null
    private var mPdrItemTouchHelper: ItemTouchHelper? = null

    /** 设置是否允许拖拽[useDrag] */
    fun setUseDrag(useDrag: Boolean): RecyclerViewDragHelper<T, VH> {
        this.mPdrUseDrag = useDrag
        mPdrCallback?.setUseDrag(useDrag)
        return this
    }

    /** 设置允许从右往左滑动[rightToLeftSwipe] */
    fun setUseRightToLeftSwipe(rightToLeftSwipe: Boolean): RecyclerViewDragHelper<T, VH> {
        this.mPdrUseRightToLeftSwipe = rightToLeftSwipe
        mPdrCallback?.setUseRightToLeftSwipe(rightToLeftSwipe)
        return this
    }

    /** 设置允许从左往右滑动[leftToRightSwipe] */
    fun setUseLeftToRightSwipe(leftToRightSwipe: Boolean): RecyclerViewDragHelper<T, VH> {
        this.mPdrUseLeftToRightSwipe = leftToRightSwipe
        mPdrCallback?.setUseLeftToRightSwipe(leftToRightSwipe)
        return this
    }

    /** 设置是否启用[enabled]长按拖拽效果 */
    fun setLongPressDragEnabled(enabled: Boolean): RecyclerViewDragHelper<T, VH> {
        isPdrLongPressDragEnabled = enabled
        mPdrCallback?.setLongPressDrag(enabled)
        return this
    }

    /** 设置是否启用[enabled]滑动效果 */
    fun setSwipeEnabled(enabled: Boolean): RecyclerViewDragHelper<T, VH> {
        isPdrSwipeEnabled = enabled
        mPdrCallback?.setSwipe(enabled)
        return this
    }

    /** 是否启用[enabled]震动效果 */
    @RequiresPermission(android.Manifest.permission.VIBRATE)
    fun setVibrateEnabled(enabled: Boolean): RecyclerViewDragHelper<T, VH> {
        isPdrVibrateEnabled = enabled
        mPdrCallback?.setVibrate(enabled)
        return this
    }

    /** 设置数据列表[list] */
    fun setList(list: MutableList<T>): RecyclerViewDragHelper<T, VH> {
        mPdrList = list
        mPdrCallback?.setList(list)
        return this
    }

    /** 设置监听器 */
    fun setListener(listener: (list: List<T>) -> Unit): RecyclerViewDragHelper<T, VH> {
        mPdrListener = listener
        mPdrCallback?.setListener(listener)
        return this
    }

    /** 使用默认DragHelperCallback完成构建，控件[recyclerView]，适配器[adapter] */
    fun build(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<VH>) {
        build(recyclerView, adapter, DragHelperCallback())
    }

    /** 使用扩展DragHelperCallback完成构建，控件[recyclerView]，适配器[adapter]，回调[callback] */
    fun build(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<VH>, callback: DragHelperCallback<T, VH>) {
        callback.setContext(mContext)
        callback.setUseDrag(mPdrUseDrag)
        callback.setUseRightToLeftSwipe(mPdrUseRightToLeftSwipe)
        callback.setUseLeftToRightSwipe(mPdrUseLeftToRightSwipe)
        callback.setLongPressDrag(isPdrLongPressDragEnabled)
        callback.setSwipe(isPdrSwipeEnabled)
        callback.setVibrate(isPdrVibrateEnabled)
        callback.setListener(mPdrListener)
        callback.setList(mPdrList)
        callback.setAdapter(adapter)
        mPdrCallback = callback
        build(recyclerView, callback)
    }

    /** 使用自定义ItemTouchHelper.Callback完成构建，控件[recyclerView]，回调[callback] */
    fun build(recyclerView: RecyclerView, callback: ItemTouchHelper.Callback) {
        mPdrItemTouchHelper = ItemTouchHelper(callback)
        mPdrItemTouchHelper?.attachToRecyclerView(recyclerView)
    }

    /** 获取ItemTouchHelper */
    fun getItemTouchHelper(): ItemTouchHelper {
        if (mPdrItemTouchHelper == null) {
            throw RuntimeException("please call build() first")
        }
        return mPdrItemTouchHelper!!
    }

}