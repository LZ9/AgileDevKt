package com.lodz.android.agiledevkt.modules.mvvm.sandwich

import androidx.lifecycle.MutableLiveData
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.mvvm.ApiModuleSuspend
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.corekt.anko.runOnSuspendIOCatch
import com.lodz.android.pandora.mvvm.vm.BaseSandwichViewModel
import kotlinx.coroutines.GlobalScope

/**
 * MVVM带基础状态控件、中部刷新控件和顶部/底部扩展的ViewModel
 * @author zhouL
 * @date 2019/12/6
 */
class MvvmTestSandwichViewModel :BaseSandwichViewModel(){

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

    fun getRefreshData(isSuccess: Boolean) {
        GlobalScope.runOnSuspendIOCatch({
            val result = ApiModuleSuspend.requestResult(isSuccess)
            GlobalScope.runOnMain {
                setSwipeRefreshFinish()
                mResultText.value = result
                showStatusCompleted()
            }
        }, { e ->
            setSwipeRefreshFinish()
            toastShort(R.string.mvvm_demo_refresh_data_fail)
        })
    }
}