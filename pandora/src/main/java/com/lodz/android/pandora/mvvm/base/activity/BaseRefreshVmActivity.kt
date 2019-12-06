package com.lodz.android.pandora.mvvm.base.activity

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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

    private val mViewModel by lazy { ViewModelProviders.of(this).get(createViewModel()) }

    fun getViewModel(): VM = mViewModel

    abstract fun createViewModel(): Class<VM>

    override fun setListeners() {
        super.setListeners()

        getViewModel().absIsFinish.observe(this, Observer { value ->
            if (value) {
                finish()
            }
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

        getViewModel().baseIsShowNoData.observe(this, Observer { value ->
            if (value) {
                showStatusNoData()
            }
        })

        getViewModel().baseIsShowError.observe(this, Observer { value ->
            if (value.first) {
                showStatusError(value.second)
            }
        })

        getViewModel().baseIsShowLoading.observe(this, Observer { value ->
            if (value) {
                showStatusLoading()
            }
        })

        getViewModel().baseIsShowCompleted.observe(this, Observer { value ->
            if (value) {
                showStatusCompleted()
            }
        })

        getViewModel().baseIsShowTitleBar.observe(this, Observer { value ->
            if (value) {
                showTitleBar()
            } else {
                goneTitleBar()
            }
        })

        getViewModel().baseIsRefreshEnabled.observe(this, Observer { value ->
            setSwipeRefreshEnabled(value)
        })

        getViewModel().baseIsRefreshFinish.observe(this, Observer { value ->
            if (value){
                setSwipeRefreshFinish()
            }
        })
    }
}