package com.lodz.android.pandora.mvvm.vm.abs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 基础ViewModel
 * @author zhouL
 * @date 2019/11/29
 */
open class AbsViewModel : ViewModel() {
    var mAbsToastMsg = MutableLiveData<String>()

    fun showToast(msg: String) {
        mAbsToastMsg.value = msg
    }
}