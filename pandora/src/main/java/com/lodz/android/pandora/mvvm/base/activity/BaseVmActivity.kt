package com.lodz.android.pandora.mvvm.base.activity

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lodz.android.corekt.anko.toastLong
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.mvvm.vm.BaseViewModel

/**
 * ViewModel基类Activity（带基础状态控件）
 * @author zhouL
 * @date 2019/12/4
 */
abstract class BaseVmActivity<VM : BaseViewModel> : BaseActivity() {

    private val mViewModel by lazy { ViewModelProviders.of(this).get(createViewModel()) }

    fun getViewModel(): VM = mViewModel

    abstract fun createViewModel(): Class<VM>

    override fun setListeners() {
        super.setListeners()

        getViewModel().isAbsFinish.observe(this, Observer { value ->
            if (value) {
                finish()
            }
        })

        getViewModel().mAbsShortToastMsg.observe(this, Observer { value ->
            if (value.isNullOrEmpty()) {
                return@Observer
            }
            toastShort(value)
        })

        getViewModel().mAbsLongToastMsg.observe(this, Observer { value ->
            if (value.isNullOrEmpty()) {
                return@Observer
            }
            toastLong(value)
        })

        getViewModel().isBaseShowNoData.observe(this, Observer { value ->
            if (value) {
                showStatusNoData()
            }
        })

        getViewModel().isBaseShowError.observe(this, Observer { value ->
            if (value.first) {
                showStatusError(value.second)
            }
        })

        getViewModel().isBaseShowLoading.observe(this, Observer { value ->
            if (value) {
                showStatusLoading()
            }
        })

        getViewModel().isBaseShowCompleted.observe(this, Observer { value ->
            if (value) {
                showStatusCompleted()
            }
        })

        getViewModel().isBaseShowTitleBar.observe(this, Observer { value ->
            if (value) {
                showTitleBar()
            } else {
                goneTitleBar()
            }
        })
    }
}