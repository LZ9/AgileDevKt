package com.lodz.android.agiledevkt.modules.mvvm.refresh

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityMvvmTestBinding
import com.lodz.android.pandora.mvvm.base.activity.BaseRefreshVmActivity
import com.lodz.android.pandora.mvvm.vm.BaseRefreshViewModel
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.utils.viewmodel.bindViewModel
import kotlin.random.Random

/**
 * MVVM带基础状态控件和下拉刷新控件Activity
 * @author zhouL
 * @date 2019/12/5
 */
class MvvmTestRefreshActivity : BaseRefreshVmActivity() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, MvvmTestRefreshActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mViewModel by bindViewModel { MvvmTestRefreshViewModel() }

    override fun getViewModel(): BaseRefreshViewModel = mViewModel

    private val mBinding: ActivityMvvmTestBinding by bindingLayout(ActivityMvvmTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.mvvm_demo_refresh_title)
    }

    override fun onDataRefresh() {
        mViewModel.getRefreshData(Random.nextInt(9) % 2 == 0)
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
        mViewModel.mResultText.observe(getLifecycleOwner()) { value ->
            mBinding.resultTv.text = value
        }
    }

    override fun initData() {
        super.initData()
        showStatusLoading()
        mViewModel.getResult(true)
    }
}