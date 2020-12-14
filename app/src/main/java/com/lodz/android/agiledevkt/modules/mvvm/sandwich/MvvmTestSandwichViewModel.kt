package com.lodz.android.agiledevkt.modules.mvvm.sandwich

import androidx.lifecycle.MutableLiveData
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.api.ApiServiceImpl
import com.lodz.android.pandora.mvvm.vm.BaseSandwichViewModel
import com.lodz.android.pandora.utils.coroutines.runOnSuspendIOCatchRes

/**
 * MVVM带基础状态控件、中部刷新控件和顶部/底部扩展的ViewModel
 * @author zhouL
 * @date 2019/12/6
 */
class MvvmTestSandwichViewModel :BaseSandwichViewModel(){

    var mResultText = MutableLiveData<String>()

    fun getResult(isSuccess: Boolean) {
        runOnSuspendIOCatchRes(
            request = { ApiServiceImpl.getResult(isSuccess) },
            actionIO = {
                mResultText.value = it.data ?: ""
                showStatusCompleted()
            },
            error = { e, isNetwork ->
                showStatusError(e)
            })
    }

    fun getRefreshData(isSuccess: Boolean) {
        runOnSuspendIOCatchRes(
            request = { ApiServiceImpl.getResult(isSuccess) },
            actionIO = {
                setSwipeRefreshFinish()
                mResultText.value = it.data ?: ""
                showStatusCompleted()
            },
            error = { e, isNetwork ->
                setSwipeRefreshFinish()
                toastShort(R.string.mvvm_demo_refresh_data_fail)
            })
    }
}