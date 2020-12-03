package com.lodz.android.agiledevkt.modules.mvp.abs

import com.lodz.android.pandora.mvp.contract.abs.ViewContract

/**
 * 测试接口
 * @author zhouL
 * @date 2020/12/2
 */
interface MvpTestAbsViewContract : ViewContract {

    fun showResult()

    fun setResult(result: String)

}