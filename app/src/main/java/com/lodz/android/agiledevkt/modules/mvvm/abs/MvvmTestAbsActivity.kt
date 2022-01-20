package com.lodz.android.agiledevkt.modules.mvvm.abs

import android.content.Context
import android.content.Intent
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityMvvmTestBinding
import com.lodz.android.pandora.mvvm.base.activity.AbsVmActivity
import com.lodz.android.pandora.mvvm.vm.AbsViewModel
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.utils.viewmodel.bindViewModel

/**
 * MVVM基础Activity
 * @author zhouL
 * @date 2019/12/3
 */
class MvvmTestAbsActivity : AbsVmActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MvvmTestAbsActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mViewModel by bindViewModel { MvvmTestAbsViewModel() }

    override fun getViewModel(): AbsViewModel = mViewModel

    private val mBinding: ActivityMvvmTestBinding by bindingLayout(ActivityMvvmTestBinding::inflate)

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun setListeners() {
        super.setListeners()
        // 获取成功数据按钮
        mBinding.getSuccessReusltBtn.setOnClickListener {
            mViewModel.getResult(getContext(), true)
        }

        // 获取失败数据按钮
        mBinding.getFailReusltBtn.setOnClickListener {
            mViewModel.getResult(getContext(), false)
        }
    }

    override fun setViewModelObserves() {
        super.setViewModelObserves()
        mViewModel.mResultText.observe(this) {
            mBinding.resultTv.text = it
        }
    }

}