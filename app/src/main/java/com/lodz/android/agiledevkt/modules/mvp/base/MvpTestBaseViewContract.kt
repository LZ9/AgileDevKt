package com.lodz.android.agiledevkt.modules.mvp.base

import com.lodz.android.pandora.mvp.contract.base.BaseViewContract

/**
 * 测试接口
 * @author zhouL
 * @date 2020/12/2
 */
interface MvpTestBaseViewContract : BaseViewContract {

    fun showResult()

    fun setResult(result: String)

}