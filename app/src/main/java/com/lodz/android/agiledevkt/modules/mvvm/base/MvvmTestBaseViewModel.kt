package com.lodz.android.agiledevkt.modules.mvvm.base

import androidx.lifecycle.MutableLiveData
import com.lodz.android.agiledevkt.modules.api.ApiServiceImpl
import com.lodz.android.pandora.mvvm.vm.BaseViewModel
import com.lodz.android.pandora.utils.coroutines.runOnSuspendIOCatchRes

/**
 * MVVM带基础状态控件ViewModel
 * @author zhouL
 * @date 2019/12/4
 */
class MvvmTestBaseViewModel : BaseViewModel() {

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
}