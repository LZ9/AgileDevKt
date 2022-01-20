package com.lodz.android.pandora.mvvm.base.fragment

import android.view.View
import androidx.lifecycle.Observer
import com.lodz.android.corekt.anko.toastLong
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.fragment.LazyFragment
import com.lodz.android.pandora.mvvm.vm.AbsViewModel

/**
 * ViewModel基础Fragment
 * @author zhouL
 * @date 2019/12/6
 */
abstract class LazyVmFragment : LazyFragment() {

    abstract fun getViewModel(): AbsViewModel

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
    }
}