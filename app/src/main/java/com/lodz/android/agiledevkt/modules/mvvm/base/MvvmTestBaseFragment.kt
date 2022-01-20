package com.lodz.android.agiledevkt.modules.mvvm.base

import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityMvvmTestBinding
import com.lodz.android.pandora.mvvm.base.fragment.BaseVmFragment
import com.lodz.android.pandora.mvvm.vm.BaseViewModel
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.utils.viewmodel.bindViewModel

/**
 * MVVM带基础状态控件Fragment
 * @author zhouL
 * @date 2019/12/6
 */
class MvvmTestBaseFragment : BaseVmFragment() {

    companion object {
        fun newInstance(): MvvmTestBaseFragment = MvvmTestBaseFragment()
    }

    private val mViewModel by bindViewModel { MvvmTestBaseViewModel() }

    override fun getViewModel(): BaseViewModel = mViewModel

    private val mBinding: ActivityMvvmTestBinding by bindingLayout(ActivityMvvmTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        mViewModel.getResult(true)
    }

    override fun setListeners(view: View) {
        super.setListeners(view)

        // 获取成功数据按钮
        mBinding.getSuccessReusltBtn.setOnClickListener {
            showStatusLoading()
            mViewModel.getResult(true)
        }

        // 获取失败数据按钮
        mBinding.getFailReusltBtn.setOnClickListener {
            showStatusLoading()
            mViewModel.getResult(false)
        }
    }

    override fun setViewModelObserves() {
        super.setViewModelObserves()
        mViewModel.mResultText.observe(this) { value ->
            mBinding.resultTv.text = value
        }
    }

    override fun initData(view: View) {
        super.initData(view)
        showStatusLoading()
        mViewModel.getResult(true)
    }
}