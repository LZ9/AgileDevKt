package com.lodz.android.agiledevkt.modules.rv.drag

import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.pandora.widget.rv.drag.DragHelperCallback

/**
 * 匀速拖拽回调
 * Created by zhouL on 2018/12/3.
 */
class DragSpeedCallback<T> : DragHelperCallback<T>() {
    /** 是否限速 */
    var isLimit = false

    override fun interpolateOutOfBoundsScroll(recyclerView: RecyclerView, viewSize: Int, viewSizeOutOfBounds: Int, totalSize: Int, msSinceStartScroll: Long): Int {
        // 重写加速度方法，自定义滚动加速度的值
        val value = super.interpolateOutOfBoundsScroll(recyclerView, viewSize, viewSizeOutOfBounds, totalSize, msSinceStartScroll)
        val speed = if (value >= 0) 10 else -10
        return if (isLimit) speed else value
    }
}