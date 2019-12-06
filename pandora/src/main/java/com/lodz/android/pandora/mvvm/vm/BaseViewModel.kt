package com.lodz.android.pandora.mvvm.vm

import androidx.lifecycle.MutableLiveData

/**
 * 带基础状态控件的ViewModel
 * @author zhouL
 * @date 2019/12/4
 */
open class BaseViewModel : AbsViewModel() {

    var baseIsShowNoData = MutableLiveData<Boolean>()
    var baseIsShowError = MutableLiveData<Pair<Boolean, Throwable?>>()
    var baseIsShowLoading = MutableLiveData<Boolean>()
    var baseIsShowCompleted = MutableLiveData<Boolean>()
    var baseIsShowTitleBar = MutableLiveData<Boolean>()

    /** 显示无数据页面 */
    protected fun showStatusNoData() {
        baseIsShowNoData.value = true
    }

    /** 显示错误页面 */
    @JvmOverloads
    protected fun showStatusError(t: Throwable? = null) {
        baseIsShowError.value = Pair(true, t)
    }

    /** 显示加载页面 */
    protected fun showStatusLoading() {
        baseIsShowLoading.value = true
    }

    /** 显示内容页面 */
    protected fun showStatusCompleted() {
        baseIsShowCompleted.value = true
    }

    /** 显隐标题栏，[isShow]是否显示 */
    @JvmOverloads
    protected fun showTitleBar(isShow: Boolean = true) {
        baseIsShowTitleBar.value = isShow
    }
}