package com.lodz.android.pandora.mvp.contract.sandwich

import com.lodz.android.pandora.mvp.contract.abs.ViewContract

/**
 * 带基础状态控件、下拉刷新控件和顶部底部扩展的View接口
 * @author zhouL
 * @date 2020/12/1
 */
interface BaseSandwichViewContract : ViewContract {

    fun showStatusNoData()

    fun showStatusError()

    fun showStatusLoading()

    fun showStatusCompleted()

    /** 设置刷新结束（隐藏刷新进度条） */
    fun setSwipeRefreshFinish()

    /** 设置刷新控件是否启用[enabled] */
    fun setSwipeRefreshEnabled(enabled: Boolean)
}