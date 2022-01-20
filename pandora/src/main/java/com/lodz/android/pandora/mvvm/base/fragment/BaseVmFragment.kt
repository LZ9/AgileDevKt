package com.lodz.android.pandora.mvvm.base.fragment

import android.view.View
import androidx.lifecycle.Observer
import com.lodz.android.corekt.anko.toastLong
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.fragment.BaseFragment
import com.lodz.android.pandora.mvvm.vm.BaseViewModel

/**
 * ViewModel基类Fragment（带基础状态控件）
 * @author zhouL
 * @date 2019/12/6
 */
abstract class BaseVmFragment : BaseFragment() {

    abstract fun getViewModel(): BaseViewModel

    override fun setListeners(view: View) {
        super.setListeners(view)
        setViewModelObserves()
    }

    protected open fun setViewModelObserves() {
        getViewModel().mPdrShortToastMsg.observe(this, Observer { value ->
            if (value.isNullOrEmpty()) {
                return@Observer
            }
            toastShort(value)
        })

        getViewModel().mPdrLongToastMsg.observe(this, Observer { value ->
            if (value.isNullOrEmpty()) {
                return@Observer
            }
            toastLong(value)
        })

        getViewModel().isPdrShowNoData.observe(this, Observer { value ->
            if (value) { showStatusNoData() }
        })

        getViewModel().isPdrShowError.observe(this, Observer { value ->
            if (value.first) { showStatusError(value.second) }
        })

        getViewModel().isPdrShowLoading.observe(this, Observer { value ->
            if (value) { showStatusLoading() }
        })

        getViewModel().isPdrShowCompleted.observe(this, Observer { value ->
            if (value) { showStatusCompleted() }
        })

        getViewModel().isPdrShowTitleBar.observe(this, Observer { value ->
            if (value) { showTitleBar() } else { goneTitleBar() }
        })
    }
}