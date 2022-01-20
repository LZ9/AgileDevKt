package com.lodz.android.agiledevkt.modules.mvvm.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityMvvmTestBinding
import com.lodz.android.pandora.mvvm.base.activity.BaseVmActivity
import com.lodz.android.pandora.mvvm.vm.BaseViewModel
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.utils.viewmodel.bindViewModel

/**
 * MVVM带基础状态控件Activity
 * @author zhouL
 * @date 2019/12/4
 */
class MvvmTestBaseActivity : BaseVmActivity() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, MvvmTestBaseActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mViewModel by bindViewModel { MvvmTestBaseViewModel() }

    override fun getViewModel(): BaseViewModel = mViewModel

    private val mBinding: ActivityMvvmTestBinding by bindingLayout(ActivityMvvmTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.mvvm_demo_base_title)
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        mViewModel.getResult(true)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

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
        mViewModel.mResultText.observe(this) {
            mBinding.resultTv.text = it
        }
    }

    override fun initData() {
        super.initData()
        showStatusLoading()
        mViewModel.getResult(true)
    }
}