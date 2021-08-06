package com.lodz.android.agiledevkt.modules.mvvm.refresh

import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityMvvmTestBinding
import com.lodz.android.pandora.mvvm.base.fragment.BaseRefreshVmFragment
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import kotlin.random.Random

/**
 * MVVM带基础状态控件和下拉刷新控件Fragment
 * @author zhouL
 * @date 2019/12/9
 */
class MvvmTestRefreshFragment : BaseRefreshVmFragment<MvvmTestRefreshViewModel>() {

    companion object {
        fun newInstance(): MvvmTestRefreshFragment = MvvmTestRefreshFragment()
    }

    override fun createViewModel(): Class<MvvmTestRefreshViewModel> = MvvmTestRefreshViewModel::class.java

    private val mBinding: ActivityMvvmTestBinding by bindingLayout(ActivityMvvmTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun onDataRefresh() {
        getViewModel().getRefreshData(Random.nextInt(9) % 2 == 0)
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        getViewModel().getResult(true)
    }

    override fun setListeners(view: View) {
        super.setListeners(view)
        //获取成功数据按钮
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

    override fun initData(view: View) {
        super.initData(view)
        showStatusLoading()
        getViewModel().getResult(true)
    }
}