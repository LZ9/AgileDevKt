package com.lodz.android.pandora.mvvm.base.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.lodz.android.corekt.anko.toastLong
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.fragment.LazyFragment
import com.lodz.android.pandora.mvvm.vm.AbsViewModel

/**
 * ViewModel基础Fragment
 * @author zhouL
 * @date 2019/12/6
 */
abstract class LazyVmFragment<VM : AbsViewModel> : LazyFragment() {

    private val mPdrViewModel by lazy { ViewModelProvider(this).get(createViewModel()) }

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
    }

}