package com.lodz.android.agiledevkt.modules.mvp.refresh

import com.lodz.android.pandora.mvp.contract.refresh.BaseRefreshViewContract

/**
 * 测试接口
 * @author zhouL
 * @date 2020/12/2
 */
interface MvpTestRefreshViewContract : BaseRefreshViewContract {

    fun showResult()

    fun setResult(result: String)

}