package com.lodz.android.agiledevkt.modules.mvvm.refresh

import androidx.lifecycle.MutableLiveData
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.api.ApiServiceImpl
import com.lodz.android.pandora.mvvm.vm.BaseRefreshViewModel
import com.lodz.android.pandora.utils.coroutines.runOnSuspendIORes

/**
 * MVVM带基础状态控件和下拉刷新控件ViewModel
 * @author zhouL
 * @date 2019/12/5
 */
class MvvmTestRefreshViewModel :BaseRefreshViewModel(){

    var mResultText = MutableLiveData<String>()

    fun getResult(isSuccess: Boolean) {
        runOnSuspendIORes(
            request = { ApiServiceImpl.getResult(isSuccess) },
            action = {
                mResultText.value = it.data ?: ""
                showStatusCompleted()
            },
            error = { e, isNetwork ->
                showStatusError(e)
            })
    }

    fun getRefreshData(isSuccess: Boolean) {
        runOnSuspendIORes(
            request = { ApiServiceImpl.getResult(isSuccess) },
            action = {
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