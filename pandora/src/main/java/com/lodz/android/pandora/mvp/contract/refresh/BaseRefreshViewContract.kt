package com.lodz.android.pandora.mvp.contract.refresh

import com.lodz.android.pandora.mvp.contract.base.BaseViewContract

/**
 * 带基础状态控件和下拉刷新控件的View接口
 * @author zhouL
 * @date 2020/12/1
 */
interface BaseRefreshViewContract : BaseViewContract {

    /** 设置刷新结束（隐藏刷新进度条） */
    fun setSwipeRefreshFinish()

    /** 设置刷新状态[isRefreshing] */
    fun setSwipeRefreshStatus(isRefreshing: Boolean)

    /** 设置刷新控件是否启用[enabled] */
    fun setSwipeRefreshEnabled(enabled: Boolean)

}