package com.lodz.android.pandora.widget.search

import androidx.recyclerview.widget.RecyclerView

/**
 * 搜索联想监听器
 * @author zhouL
 * @date 2019/10/25
 */
interface OnSearchRecomdListener {

    /** 输入文字回调 */
    fun onInputTextChange(text: String)

    /** 数据项点击 */
    fun onItemClick(viewHolder: RecyclerView.ViewHolder, data: RecomdData, position: Int)

}