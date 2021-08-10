package com.lodz.android.agiledevkt.modules.mvvm.abs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.lodz.android.agiledevkt.databinding.ActivityMvvmTestBinding
import com.lodz.android.pandora.mvvm.base.fragment.LazyVmFragment
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * MVVM基础Fragment
 * @author zhouL
 * @date 2019/12/6
 */
class MvvmTestLazyFragment :LazyVmFragment<MvvmTestAbsViewModel>(){

    companion object {
        fun newInstance(): MvvmTestLazyFragment = MvvmTestLazyFragment()
    }

    private val mBinding: ActivityMvvmTestBinding by bindingLayout(ActivityMvvmTestBinding::inflate)

    override fun getAbsViewBindingLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = mBinding.root

    override fun createViewModel(): Class<MvvmTestAbsViewModel> = MvvmTestAbsViewModel::class.java

    override fun setListeners(view: View) {
        super.setListeners(view)
        // 获取成功数据按钮
        mBinding.getSuccessReusltBtn.setOnClickListener {
            getViewModel().getResult(getContext(), true)
        }

        // 获取失败数据按钮
        mBinding.getFailReusltBtn.setOnClickListener {
            getViewModel().getResult(getContext(), false)
        }

        getViewModel().mResultText.observe(this, Observer { value ->
            mBinding.resultTv.text = value
        })
    }
}