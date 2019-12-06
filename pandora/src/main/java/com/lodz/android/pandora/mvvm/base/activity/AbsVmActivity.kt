package com.lodz.android.pandora.mvvm.base.activity

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lodz.android.corekt.anko.toastLong
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.mvvm.vm.AbsViewModel

/**
 * ViewModel基础Activity
 * @author zhouL
 * @date 2019/11/29
 */
abstract class AbsVmActivity<VM : AbsViewModel> : AbsActivity() {

    private val mViewModel by lazy { ViewModelProviders.of(this).get(createViewModel()) }

    fun getViewModel(): VM = mViewModel

    abstract fun createViewModel(): Class<VM>

    override fun setListeners() {
        super.setListeners()

        getViewModel().absIsFinish.observe(this, Observer { value ->
            if (value) { finish() }
        })

        getViewModel().absShortToastMsg.observe(this, Observer { value ->
            if (value.isNullOrEmpty()) {
                return@Observer
            }
            toastShort(value)
        })

        getViewModel().absLongToastMsg.observe(this, Observer { value ->
            if (value.isNullOrEmpty()) {
                return@Observer
            }
            toastLong(value)
        })
    }
}

