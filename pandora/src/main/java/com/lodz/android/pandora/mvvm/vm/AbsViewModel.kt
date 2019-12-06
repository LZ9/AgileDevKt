package com.lodz.android.pandora.mvvm.vm

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lodz.android.pandora.base.application.BaseApplication

/**
 * 基础ViewModel
 * @author zhouL
 * @date 2019/11/29
 */
open class AbsViewModel : ViewModel() {
    var absLongToastMsg = MutableLiveData<String>()
    var absShortToastMsg = MutableLiveData<String>()
    var absIsFinish = MutableLiveData<Boolean>()

    /** 显示短提示，[msg]消息 */
    protected fun toastShort(msg: String) {
        absShortToastMsg.value = msg
    }

    /** 显示长提示，[msg]消息 */
    protected fun toastLong(msg: String) {
        absLongToastMsg.value = msg
    }

    /** 显示短提示，[id]文字资源id */
    protected fun toastShort(@StringRes id: Int) {
        absShortToastMsg.value = BaseApplication.get()?.getString(id) ?: ""
    }

    /** 显示长提示，[id]文字资源id */
    protected fun toastLong(@StringRes id: Int) {
        absLongToastMsg.value = BaseApplication.get()?.getString(id) ?: ""
    }

    /** 关闭Activity */
    protected fun finish() {
        absIsFinish.value = true
    }
}