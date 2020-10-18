package com.lodz.android.agiledevkt.modules.mvvm.abs

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.mvvm.ApiModuleSuspend
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.pandora.mvvm.vm.AbsViewModel
import com.lodz.android.pandora.rx.exception.DataException
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.coroutines.runOnSuspendIOCatchPg
import kotlinx.coroutines.GlobalScope

/**
 * MVVM基础ViewModel
 * @author zhouL
 * @date 2019/12/3
 */
class MvvmTestAbsViewModel : AbsViewModel() {

    var mResultText = MutableLiveData<String>()

    fun getResult(context: Context, isSuccess: Boolean) {
        GlobalScope.runOnSuspendIOCatchPg(
            context,
            context.getString(R.string.mvvm_demo_loading),
            cancelable = true,
            request = { ApiModuleSuspend.requestResult(isSuccess) },
            actionIO = {
                mResultText.value = it
                toastShort(it)
            }, error = { e, isNetwork ->
                val msg = RxUtils.getExceptionTips(e, isNetwork, "加载失败")
                mResultText.value = msg
                toastShort(msg)
            })
    }
}