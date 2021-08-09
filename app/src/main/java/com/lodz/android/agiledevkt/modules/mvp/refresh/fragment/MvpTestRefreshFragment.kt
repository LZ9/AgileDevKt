package com.lodz.android.agiledevkt.modules.mvp.refresh.fragment

import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityMvpTestBinding
import com.lodz.android.agiledevkt.modules.mvp.refresh.MvpTestRefreshPresenter
import com.lodz.android.agiledevkt.modules.mvp.refresh.MvpTestRefreshViewContract
import com.lodz.android.pandora.mvp.base.fragment.MvpBaseRefreshFragment
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import kotlin.random.Random

/**
 * 刷新测试类
 * @author zhouL
 * @date 2020/12/3
 */
class MvpTestRefreshFragment : MvpBaseRefreshFragment<MvpTestRefreshPresenter, MvpTestRefreshViewContract>(), MvpTestRefreshViewContract{

    companion object {
        fun newInstance(): MvpTestRefreshFragment = MvpTestRefreshFragment()
    }

    override fun createMainPresenter(): MvpTestRefreshPresenter = MvpTestRefreshPresenter()

    private val mBinding: ActivityMvpTestBinding by bindingLayout(ActivityMvpTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(view: View, savedInstanceState: Bundle?) {
        super.findViews(view, savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.mvp_demo_refresh_title)
    }

    override fun onDataRefresh() {
        getPresenterContract()?.getRefreshData(Random.nextInt(9) % 2 == 0)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        getPresenterContract()?.getResult(true)
    }

    override fun setListeners(view: View) {
        super.setListeners(view)

        // 获取成功数据按钮
        mBinding.getSuccessReusltBtn.setOnClickListener {
            showStatusLoading()
            getPresenterContract()?.getResult(true)
        }

        // 获取失败数据按钮
        mBinding.getFailReusltBtn.setOnClickListener {
            showStatusLoading()
            getPresenterContract()?.getResult(false)
        }

    }

    override fun initData(view: View) {
        super.initData(view)
        showStatusLoading()
        getPresenterContract()?.getResult(true)
    }

    override fun showResult() {
        mBinding.resultTv.visibility = View.VISIBLE
    }

    override fun setResult(result: String) {
        mBinding.resultTv.text = result
    }
}