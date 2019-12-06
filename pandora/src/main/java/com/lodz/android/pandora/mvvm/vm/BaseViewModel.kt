package com.lodz.android.pandora.mvvm.vm

import androidx.lifecycle.MutableLiveData

/**
 * 带基础状态控件的ViewModel
 * @author zhouL
 * @date 2019/12/4
 */
open class BaseViewModel : AbsViewModel() {

    var isPdrShowNoData = MutableLiveData<Boolean>()
    var isPdrShowError = MutableLiveData<Pair<Boolean, Throwable?>>()
    var isPdrShowLoading = MutableLiveData<Boolean>()
    var isPdrShowCompleted = MutableLiveData<Boolean>()
    var isPdrShowTitleBar = MutableLiveData<Boolean>()

    /** 显示无数据页面 */
    protected fun showStatusNoData() { isPdrShowNoData.value = true }

    /** 显示错误页面 */
    @JvmOverloads
    protected fun showStatusError(t: Throwable? = null) { isPdrShowError.value = Pair(true, t) }

    /** 显示加载页面 */
    protected fun showStatusLoading() { isPdrShowLoading.value = true }

    /** 显示内容页面 */
    protected fun showStatusCompleted() { isPdrShowCompleted.value = true }

    /** 显隐标题栏，[isShow]是否显示 */
    @JvmOverloads
    protected fun showTitleBar(isShow: Boolean = true) { isPdrShowTitleBar.value = isShow }
}