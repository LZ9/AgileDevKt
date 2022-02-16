package com.lodz.android.agiledevkt.modules.mvvm.abs

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.api.coroutines.ApiModuleSuspend
import com.lodz.android.pandora.mvvm.vm.AbsViewModel
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.coroutines.CoroutinesWrapper

/**
 * MVVM基础ViewModel
 * @author zhouL
 * @date 2019/12/3
 */
class MvvmTestAbsViewModel : AbsViewModel() {

    var mResultText = MutableLiveData<String>()

    fun getResult(context: Context, isSuccess: Boolean) {
        CoroutinesWrapper.create(this)
            .request { ApiModuleSuspend.getResult(isSuccess) }
            .actionPg(context, context.getString(R.string.mvvm_demo_loading), true){
                onSuccess {
                    mResultText.value = it.data ?: ""
                    toastShort(it.msg)
                }
                onError { e, isNetwork ->
                    val msg = RxUtils.getExceptionTips(e, isNetwork, "加载失败")
                    mResultText.value = msg
                    toastShort(msg)
                }
            }
    }
}