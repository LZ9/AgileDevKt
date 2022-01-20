package com.lodz.android.pandora.mvvm.base.activity

import com.lodz.android.corekt.anko.toastLong
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.mvvm.vm.BaseViewModel

/**
 * ViewModel基类Activity（带基础状态控件）
 * @author zhouL
 * @date 2019/12/4
 */
abstract class BaseVmActivity : BaseActivity() {

    abstract fun getViewModel(): BaseViewModel

    override fun setListeners() {
        super.setListeners()
        setViewModelObserves()
    }

    protected open fun setViewModelObserves() {
        getViewModel().isPdrFinish.observe(this) {
            if (it) {
                finish()
            }
        }

        getViewModel().mPdrShortToastMsg.observe(this) {
            if (it.isNullOrEmpty()) {
                return@observe
            }
            toastShort(it)
        }

        getViewModel().mPdrLongToastMsg.observe(this) {
            if (it.isNullOrEmpty()) {
                return@observe
            }
            toastLong(it)
        }

        getViewModel().isPdrShowNoData.observe(this) {
            if (it) {
                showStatusNoData()
            }
        }

        getViewModel().isPdrShowError.observe(this) {
            if (it.first) {
                showStatusError(it.second)
            }
        }

        getViewModel().isPdrShowLoading.observe(this) {
            if (it) {
                showStatusLoading()
            }
        }

        getViewModel().isPdrShowCompleted.observe(this) {
            if (it) {
                showStatusCompleted()
            }

        }

        getViewModel().isPdrShowTitleBar.observe(this) {
            if (it) {
                showTitleBar()
            } else {
                goneTitleBar()
            }
        }
    }

}