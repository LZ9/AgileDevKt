package com.lodz.android.pandora.widget.rv.swipe.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lodz.android.pandora.R
import com.lodz.android.pandora.widget.rv.recycler.vh.DataViewHolder
import com.lodz.android.pandora.widget.swipe.SwipeMenuLayout

/**
 * 侧滑ViewHolder
 * @author zhouL
 * @date 2022/10/8
 */
class SwipeDataViewHolder(
    parent: ViewGroup,
    attachToRoot: Boolean = false,
) : DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.pandora_item_swipe_menu, parent, attachToRoot)) {

    /** 侧滑布局  */
    var swipeMenuLayout: SwipeMenuLayout

    /** 内容布局  */
    var contentLayout: ViewGroup

    /** 右侧布局  */
    var rightLayout: ViewGroup

    /** 左侧布局  */
    var leftLayout: ViewGroup

    init {
        swipeMenuLayout = withView(R.id.pdr_swip_menu_layout)
        contentLayout = withView(R.id.pdr_content_view)
        rightLayout = withView(R.id.pdr_right_view)
        leftLayout = withView(R.id.pdr_left_view)
    }
}