package com.lodz.android.pandora.mvvm.base.activity

import androidx.lifecycle.ViewModelProvider
import com.lodz.android.corekt.anko.toastLong
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseRefreshActivity
import com.lodz.android.pandora.mvvm.vm.BaseRefreshViewModel

/**
 * ViewModel基类Activity（带基础状态控件和下拉刷新控件）
 * @author zhouL
 * @date 2019/12/5
 */
abstract class BaseRefreshVmActivity<VM : BaseRefreshViewModel> : BaseRefreshActivity() {

    private val mPdrViewModel by lazy { ViewModelProvider(this)[createViewModel()] }

    fun getViewModel(): VM = mPdrViewModel

    abstract fun createViewModel(): Class<VM>

    override fun setListeners() {
        super.setListeners()
        setViewModelObserves(getViewModel())
    }

    protected open fun setViewModelObserves(viewModel: VM) {
        viewModel.isPdrFinish.observe(this) {
            if (it) {
                finish()
            }
        }

        viewModel.mPdrShortToastMsg.observe(this) {
            if (it.isNullOrEmpty()) {
                return@observe
            }
            toastShort(it)
        }

        viewModel.mPdrLongToastMsg.observe(this) {
            if (it.isNullOrEmpty()) {
                return@observe
            }
            toastLong(it)
        }

        viewModel.isPdrShowNoData.observe(this) {
            if (it) {
                showStatusNoData()
            }
        }

        viewModel.isPdrShowError.observe(this) {
            if (it.first) {
                showStatusError(it.second)
            }
        }

        viewModel.isPdrShowLoading.observe(this) {
            if (it) {
                showStatusLoading()
            }
        }

        viewModel.isPdrShowCompleted.observe(this) {
            if (it) {
                showStatusCompleted()
            }
        }

        viewModel.isPdrShowTitleBar.observe(this) {
            if (it) {
                showTitleBar()
            } else {
                goneTitleBar()
            }
        }

        viewModel.isPdrRefreshEnabled.observe(this) {
            setSwipeRefreshEnabled(it)
        }

        viewModel.isPdrRefreshFinish.observe(this) {
            if (it) {
                setSwipeRefreshFinish()
            }
        }
    }
}