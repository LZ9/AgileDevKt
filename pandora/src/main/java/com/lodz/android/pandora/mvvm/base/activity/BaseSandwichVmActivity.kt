package com.lodz.android.pandora.mvvm.base.activity

import androidx.lifecycle.ViewModelProvider
import com.lodz.android.corekt.anko.toastLong
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseSandwichActivity
import com.lodz.android.pandora.mvvm.vm.BaseSandwichViewModel

/**
 * ViewModel基类Activity（带基础状态控件、中部刷新控件和顶部/底部扩展的）
 * @author zhouL
 * @date 2019/12/6
 */
abstract class BaseSandwichVmActivity<VM : BaseSandwichViewModel> : BaseSandwichActivity() {

    private val mPdrViewModel by lazy { ViewModelProvider(this)[createViewModel()] }

    fun getViewModel(): VM = mPdrViewModel

    abstract fun createViewModel(): Class<VM>

    override fun setListeners() {
        super.setListeners()

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

        getViewModel().isPdrRefreshEnabled.observe(this) {
            setSwipeRefreshEnabled(it)
        }

        getViewModel().isPdrRefreshFinish.observe(this) {
            if (it) {
                setSwipeRefreshFinish()
            }
        }

        getViewModel().setViewModelObserves()
    }

    protected open fun VM.setViewModelObserves(){}
}