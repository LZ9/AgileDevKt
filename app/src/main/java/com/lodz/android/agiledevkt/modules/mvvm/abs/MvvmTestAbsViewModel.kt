package com.lodz.android.agiledevkt.modules.mvvm.abs

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.api.ApiServiceImpl
import com.lodz.android.pandora.mvvm.vm.AbsViewModel
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.coroutines.runOnSuspendIOPg

/**
 * MVVM基础ViewModel
 * @author zhouL
 * @date 2019/12/3
 */
class MvvmTestAbsViewModel : AbsViewModel() {

    var mResultText = MutableLiveData<String>()

    fun getResult(context: Context, isSuccess: Boolean) {
        runOnSuspendIOPg(
            context,
            context.getString(R.string.mvvm_demo_loading),
            cancelable = true,
            request = { ApiServiceImpl.getResult(isSuccess) },
            actionIO = {
                mResultText.value = it.data ?: ""
                toastShort(it.msg)
            }, error = { e, isNetwork ->
                val msg = RxUtils.getExceptionTips(e, isNetwork, "加载失败")
                mResultText.value = msg
                toastShort(msg)
            })
    }
}