package com.lodz.android.agiledevkt.modules.mvp.sandwich

import com.lodz.android.pandora.mvp.contract.sandwich.BaseSandwichViewContract

/**
 * 测试接口
 * @author zhouL
 * @date 2020/12/2
 */
interface MvpTestSandwichViewContract : BaseSandwichViewContract {

    fun showResult()

    fun setResult(result: String)

}