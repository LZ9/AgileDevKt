package com.lodz.android.agiledevkt.modules.mvvm.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityMvvmTestBinding
import com.lodz.android.pandora.mvvm.base.activity.BaseVmActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * MVVM带基础状态控件Activity
 * @author zhouL
 * @date 2019/12/4
 */
class MvvmTestBaseActivity : BaseVmActivity<MvvmTestBaseViewModel>() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, MvvmTestBaseActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createViewModel(): Class<MvvmTestBaseViewModel> = MvvmTestBaseViewModel::class.java

    private val mBinding: ActivityMvvmTestBinding by bindingLayout(ActivityMvvmTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.mvvm_demo_base_title)
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        getViewModel().getResult(true)
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
            getViewModel().getResult(true)
        }

        // 获取失败数据按钮
        mBinding.getFailReusltBtn.setOnClickListener {
            showStatusLoading()
            getViewModel().getResult(false)
        }

        getViewModel().mResultText.observe(this) { value ->
            mBinding.resultTv.text = value
        }
    }

    override fun initData() {
        super.initData()
        showStatusLoading()
        getViewModel().getResult(true)
    }
}