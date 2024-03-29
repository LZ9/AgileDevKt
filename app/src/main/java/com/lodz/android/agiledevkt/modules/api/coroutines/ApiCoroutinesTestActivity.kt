package com.lodz.android.agiledevkt.modules.api.coroutines

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityApiCoroutinesTestBinding
import com.lodz.android.pandora.mvvm.base.activity.BaseVmActivity
import com.lodz.android.pandora.mvvm.vm.BaseViewModel
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.utils.viewmodel.bindViewModel

/**
 * 协程模式的接口请求
 * @author zhouL
 * @date 2021/12/14
 */
class ApiCoroutinesTestActivity : BaseVmActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ApiCoroutinesTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 请求类型 */
    private var mRequestType = ApiModuleSuspend.API_SUCCESS

    private val mViewModel by bindViewModel { ApiCoroutinesViewModel() }

    override fun getViewModel(): BaseViewModel = mViewModel

    private val mBinding: ActivityApiCoroutinesTestBinding by bindingLayout(ActivityApiCoroutinesTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.api_coroutines)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 网络异常
        mBinding.netSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mRequestType = when {
                isChecked -> ApiModuleSuspend.NETWORK_FAIL
                mBinding.dataSwitch.isChecked -> ApiModuleSuspend.API_FAIL
                else -> ApiModuleSuspend.API_SUCCESS
            }
        }

        // 接口失败
        mBinding.dataSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mRequestType = when {
                isChecked -> ApiModuleSuspend.API_FAIL
                mBinding.netSwitch.isChecked -> ApiModuleSuspend.NETWORK_FAIL
                else -> ApiModuleSuspend.API_SUCCESS
            }
        }

        mBinding.mockBtn.setOnClickListener {
            mViewModel.requestMock(getContext())
        }

        mBinding.postBtn.setOnClickListener {
            mViewModel.requestPostSpot(getContext(), mRequestType)
        }

        mBinding.getBtn.setOnClickListener {
            mViewModel.requestGetSpot(getContext(), mRequestType)
        }

        mBinding.customBtn.setOnClickListener {
            mViewModel.requestCustom(getContext())
        }
    }

    override fun setViewModelObserves() {
        super.setViewModelObserves()
        mViewModel.mResponseResult.observe(this) {
            mBinding.resultTv.text = it ?: ""
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}