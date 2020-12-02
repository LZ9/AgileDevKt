package com.lodz.android.pandora.mvp.contract.base

import com.lodz.android.pandora.mvp.contract.abs.ViewContract

/**
 * 带基础状态控件的View接口
 * @author zhouL
 * @date 2020/12/1
 */
interface BaseViewContract : ViewContract {

    fun showStatusNoData()

    fun showStatusError(t: Throwable?)

    fun showStatusError()

    fun showStatusLoading()

    fun showStatusCompleted()

    fun goneTitleBar()

    fun showTitleBar()
}