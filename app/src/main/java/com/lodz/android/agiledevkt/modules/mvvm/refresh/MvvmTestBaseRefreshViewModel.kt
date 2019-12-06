package com.lodz.android.agiledevkt.modules.mvvm.refresh

import androidx.lifecycle.MutableLiveData
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.mvvm.ApiModuleSuspend
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.corekt.anko.runOnSuspendIOCatch
import com.lodz.android.pandora.mvvm.vm.BaseRefreshViewModel
import kotlinx.coroutines.GlobalScope

/**
 * MVVM带基础状态控件和下拉刷新控件ViewModel
 * @author zhouL
 * @date 2019/12/5
 */
class MvvmTestBaseRefreshViewModel :BaseRefreshViewModel(){

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