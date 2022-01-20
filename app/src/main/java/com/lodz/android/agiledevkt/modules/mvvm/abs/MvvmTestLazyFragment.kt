package com.lodz.android.agiledevkt.modules.mvvm.abs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lodz.android.agiledevkt.databinding.ActivityMvvmTestBinding
import com.lodz.android.pandora.mvvm.base.fragment.LazyVmFragment
import com.lodz.android.pandora.mvvm.vm.AbsViewModel
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.utils.viewmodel.bindViewModel

/**
 * MVVM基础Fragment
 * @author zhouL
 * @date 2019/12/6
 */
class MvvmTestLazyFragment :LazyVmFragment(){

    companion object {
        fun newInstance(): MvvmTestLazyFragment = MvvmTestLazyFragment()
    }

    private val mViewModel by bindViewModel { MvvmTestAbsViewModel() }

    override fun getViewModel(): AbsViewModel = mViewModel

    private val mBinding: ActivityMvvmTestBinding by bindingLayout(ActivityMvvmTestBinding::inflate)

    override fun getAbsViewBindingLayout( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View = mBinding.root

    override fun setListeners(view: View) {
        super.setListeners(view)
        // 获取成功数据按钮
        mBinding.getSuccessReusltBtn.setOnClickListener {
            mViewModel.getResult(context, true)
        }

        // 获取失败数据按钮
        mBinding.getFailReusltBtn.setOnClickListener {
            mViewModel.getResult(context, false)
        }
    }

    override fun setViewModelObserves() {
        super.setViewModelObserves()
        mViewModel.mResultText.observe(this) { value ->
            mBinding.resultTv.text = value
        }
    }
}