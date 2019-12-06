package com.lodz.android.pandora.mvvm.vm

import androidx.lifecycle.MutableLiveData

/**
 * 带基础状态控件和下来刷新控件的ViewModel
 * @author zhouL
 * @date 2019/12/5
 */
open class BaseRefreshViewModel : BaseViewModel() {

    var baseIsRefreshFinish = MutableLiveData<Boolean>()
    var baseIsRefreshEnabled = MutableLiveData<Boolean>()

    /** 设置刷新结束（隐藏刷新进度条） */
    protected fun setSwipeRefreshFinish() { baseIsRefreshFinish.value = true }

    /** 设置刷新控件是否启用 */
    protected fun setSwipeRefreshEnabled(enabled: Boolean) { baseIsRefreshEnabled.value = enabled }
}