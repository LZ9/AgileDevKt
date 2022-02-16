package com.lodz.android.agiledevkt.modules.mvvm.base

import androidx.lifecycle.MutableLiveData
import com.lodz.android.agiledevkt.modules.api.coroutines.ApiModuleSuspend
import com.lodz.android.pandora.mvvm.vm.BaseViewModel
import com.lodz.android.pandora.utils.coroutines.CoroutinesWrapper

/**
 * MVVM带基础状态控件ViewModel
 * @author zhouL
 * @date 2019/12/4
 */
class MvvmTestBaseViewModel : BaseViewModel() {

    var mResultText = MutableLiveData<String>()

    fun getResult(isSuccess: Boolean) {
        CoroutinesWrapper.create(this)
            .request { ApiModuleSuspend.getResult(isSuccess) }
            .action {
                onSuccess {
                    mResultText.value = it.data ?: ""
                    showStatusCompleted()
                }

                onError { e, isNetwork ->
                    showStatusError(e)
                }
            }
    }
}