package com.lodz.android.pandora.mvvm.vm

import androidx.lifecycle.MutableLiveData

/**
 * 带基础状态控件、中部刷新控件和顶部/底部扩展的ViewModel
 * @author zhouL
 * @date 2019/12/6
 */
open class BaseSandwichViewModel : AbsViewModel() {

    var baseIsShowNoData = MutableLiveData<Boolean>()
    var baseIsShowError = MutableLiveData<Pair<Boolean, Throwable?>>()
    var baseIsShowLoading = MutableLiveData<Boolean>()
    var baseIsShowCompleted = MutableLiveData<Boolean>()
    var baseIsRefreshFinish = MutableLiveData<Boolean>()
    var baseIsRefreshEnabled = MutableLiveData<Boolean>()

    /** 显示无数据页面 */
    protected fun showStatusNoData() { baseIsShowNoData.value = true }

    /** 显示错误页面 */
    @JvmOverloads
    protected fun showStatusError(t: Throwable? = null) { baseIsShowError.value = Pair(true, t) }

    /** 显示加载页面 */
    protected fun showStatusLoading() { baseIsShowLoading.value = true }

    /** 显示内容页面 */
    protected fun showStatusCompleted() { baseIsShowCompleted.value = true }

    /** 设置刷新结束（隐藏刷新进度条） */
    protected fun setSwipeRefreshFinish() { baseIsRefreshFinish.value = true }

    /** 设置刷新控件是否启用 */
    protected fun setSwipeRefreshEnabled(enabled: Boolean) { baseIsRefreshEnabled.value = enabled }

}