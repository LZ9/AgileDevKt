package com.lodz.android.pandora.mvvm.vm

import androidx.lifecycle.MutableLiveData

/**
 * 带基础状态控件、中部刷新控件和顶部/底部扩展的ViewModel
 * @author zhouL
 * @date 2019/12/6
 */
open class BaseSandwichViewModel : AbsViewModel() {

    var isPdrShowNoData = MutableLiveData<Boolean>()
    var isPdrShowError = MutableLiveData<Pair<Boolean, Throwable?>>()
    var isPdrShowLoading = MutableLiveData<Boolean>()
    var isPdrShowCompleted = MutableLiveData<Boolean>()
    var isPdrRefreshFinish = MutableLiveData<Boolean>()
    var isPdrRefreshEnabled = MutableLiveData<Boolean>()

    /** 显示无数据页面 */
    protected fun showStatusNoData() { isPdrShowNoData.postValue(true) }

    /** 显示错误页面 */
    protected fun showStatusError() {
        showStatusError(null)
    }
    /** 显示错误页面 */
    protected fun showStatusError(t: Throwable?) { isPdrShowError.postValue(Pair(true, t)) }

    /** 显示加载页面 */
    protected fun showStatusLoading() { isPdrShowLoading.postValue(true)}

    /** 显示内容页面 */
    protected fun showStatusCompleted() { isPdrShowCompleted.postValue(true) }

    /** 设置刷新结束（隐藏刷新进度条） */
    protected fun setSwipeRefreshFinish() { isPdrRefreshFinish.postValue(true) }

    /** 设置刷新控件是否启用 */
    protected fun setSwipeRefreshEnabled(enabled: Boolean) { isPdrRefreshEnabled.postValue(enabled) }

}