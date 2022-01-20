package com.lodz.android.pandora.mvvm.base.activity

import com.lodz.android.corekt.anko.toastLong
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseRefreshActivity
import com.lodz.android.pandora.mvvm.vm.BaseRefreshViewModel

/**
 * ViewModel基类Activity（带基础状态控件和下拉刷新控件）
 * @author zhouL
 * @date 2019/12/5
 */
abstract class BaseRefreshVmActivity : BaseRefreshActivity() {

    abstract fun getViewModel(): BaseRefreshViewModel

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

        getViewModel().isPdrRefreshEnabled.observe(this) {
            setSwipeRefreshEnabled(it)
        }

        getViewModel().isPdrRefreshFinish.observe(this) {
            if (it) {
                setSwipeRefreshFinish()
            }
        }
    }
}