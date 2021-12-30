package com.lodz.android.agiledevkt.modules.mvvm.abs

import android.content.Context
import android.content.Intent
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityMvvmTestBinding
import com.lodz.android.pandora.mvvm.base.activity.AbsVmActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * MVVM基础Activity
 * @author zhouL
 * @date 2019/12/3
 */
class MvvmTestAbsActivity : AbsVmActivity<MvvmTestAbsViewModel>() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MvvmTestAbsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createViewModel(): Class<MvvmTestAbsViewModel> = MvvmTestAbsViewModel::class.java

    private val mBinding: ActivityMvvmTestBinding by bindingLayout(ActivityMvvmTestBinding::inflate)

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun setListeners() {
        super.setListeners()
        // 获取成功数据按钮
        mBinding.getSuccessReusltBtn.setOnClickListener {
            getViewModel().getResult(getContext(), true)
        }

        // 获取失败数据按钮
        mBinding.getFailReusltBtn.setOnClickListener {
            getViewModel().getResult(getContext(), false)
        }
    }

    override fun setViewModelObserves() {
        super.setViewModelObserves()
        getViewModel().mResultText.observe(this) { value ->
            mBinding.resultTv.text = value
        }
    }
}