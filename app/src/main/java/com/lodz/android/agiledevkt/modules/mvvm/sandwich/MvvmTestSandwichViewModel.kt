package com.lodz.android.agiledevkt.modules.mvvm.sandwich

import androidx.lifecycle.MutableLiveData
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.api.coroutines.ApiModuleSuspend
import com.lodz.android.pandora.mvvm.vm.BaseSandwichViewModel
import com.lodz.android.pandora.utils.coroutines.runOnSuspendIORes

/**
 * MVVM带基础状态控件、中部刷新控件和顶部/底部扩展的ViewModel
 * @author zhouL
 * @date 2019/12/6
 */
class MvvmTestSandwichViewModel :BaseSandwichViewModel(){

    var mResultText = MutableLiveData<String>()

    fun getResult(isSuccess: Boolean) {
        runOnSuspendIORes(
            request = { ApiModuleSuspend.getResult(isSuccess) }) {
            onSuccess {
                mResultText.value = it.data ?: ""
                showStatusCompleted()
            }
            onError { e, isNetwork ->
                showStatusError(e)
            }
        }
    }

    fun getRefreshData(isSuccess: Boolean) {
        runOnSuspendIORes(
            request = { ApiModuleSuspend.getResult(isSuccess) }) {
            onSuccess {
                setSwipeRefreshFinish()
                mResultText.value = it.data ?: ""
                showStatusCompleted()
            }
            onError { e, isNetwork ->
                setSwipeRefreshFinish()
                toastShort(R.string.mvvm_demo_refresh_data_fail)
            }
        }
    }
}