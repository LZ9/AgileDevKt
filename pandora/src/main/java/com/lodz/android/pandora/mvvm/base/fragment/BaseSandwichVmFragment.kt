package com.lodz.android.pandora.mvvm.base.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lodz.android.corekt.anko.toastLong
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.fragment.BaseSandwichFragment
import com.lodz.android.pandora.mvvm.vm.BaseSandwichViewModel

/**
 * ViewModel基类Fragment（带基础状态控件、中部刷新控件和顶部/底部扩展的）
 * @author zhouL
 * @date 2019/12/9
 */
abstract class BaseSandwichVmFragment<VM : BaseSandwichViewModel> : BaseSandwichFragment() {

    private val mPdrViewModel by lazy { ViewModelProviders.of(this).get(createViewModel()) }

    fun getViewModel(): VM = mPdrViewModel

    abstract fun createViewModel(): Class<VM>

    override fun setListeners(view: View) {
        super.setListeners(view)

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

        getViewModel().isPdrRefreshEnabled.observe(this, Observer { value ->
            setSwipeRefreshEnabled(value)
        })

        getViewModel().isPdrRefreshFinish.observe(this, Observer { value ->
            if (value){ setSwipeRefreshFinish() }
        })
    }
}