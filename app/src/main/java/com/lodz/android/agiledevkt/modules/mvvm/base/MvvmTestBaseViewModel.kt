package com.lodz.android.agiledevkt.modules.mvvm.base

import androidx.lifecycle.MutableLiveData
import com.lodz.android.agiledevkt.modules.mvvm.ApiModuleSuspend
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.corekt.anko.runOnSuspendIOCatch
import com.lodz.android.pandora.mvvm.vm.BaseViewModel
import kotlinx.coroutines.GlobalScope

/**
 * MVVM带基础状态控件ViewModel
 * @author zhouL
 * @date 2019/12/4
 */
class MvvmTestBaseViewModel : BaseViewModel() {

    var mResultText = MutableLiveData<String>()

    fun getResult(isSuccess: Boolean) {
        GlobalScope.runOnSuspendIOCatch({
            val result = ApiModuleSuspend.requestResult(isSuccess)
            GlobalScope.runOnMain {
                mResultText.value = result
                showStatusCompleted()
            }
        }, { e ->
            showStatusError(e)
        })
    }
}