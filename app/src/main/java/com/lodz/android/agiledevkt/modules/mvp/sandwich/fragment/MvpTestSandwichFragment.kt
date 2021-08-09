package com.lodz.android.agiledevkt.modules.mvp.sandwich.fragment

import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityMvpTestBinding
import com.lodz.android.agiledevkt.databinding.ViewMvpTestBottomBinding
import com.lodz.android.agiledevkt.databinding.ViewMvpTestTopBinding
import com.lodz.android.agiledevkt.modules.mvp.sandwich.MvpTestSandwichPresenter
import com.lodz.android.agiledevkt.modules.mvp.sandwich.MvpTestSandwichViewContract
import com.lodz.android.pandora.mvp.base.fragment.MvpBaseSandwichFragment
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import kotlin.random.Random

/**
 * MVP带基础状态控件、中部刷新控件和顶部/底部扩展Fragment
 * @author zhouL
 * @date 2020/12/3
 */
class MvpTestSandwichFragment : MvpBaseSandwichFragment<MvpTestSandwichPresenter, MvpTestSandwichViewContract>(), MvpTestSandwichViewContract{

    companion object {
        fun newInstance(): MvpTestSandwichFragment = MvpTestSandwichFragment()
    }

    override fun createMainPresenter(): MvpTestSandwichPresenter = MvpTestSandwichPresenter()

    /** 内容布局 */
    private val mContentBinding: ActivityMvpTestBinding by bindingLayout(ActivityMvpTestBinding::inflate)
    /** 顶部布局 */
    private val mTopBinding: ViewMvpTestTopBinding by bindingLayout(ViewMvpTestTopBinding::inflate)
    /** 底部布局 */
    private val mBottomBinding: ViewMvpTestBottomBinding by bindingLayout(ViewMvpTestBottomBinding::inflate)

    override fun getViewBindingLayout(): View = mContentBinding.root

    override fun getTopViewBindingLayout(): View = mTopBinding.root

    override fun getBottomViewBindingLayout(): View = mBottomBinding.root

    override fun findViews(view: View, savedInstanceState: Bundle?) {
        super.findViews(view, savedInstanceState)
        mTopBinding.titleBarLayout.setTitleName(R.string.mvp_demo_sandwich_title)
        mTopBinding.titleBarLayout.visibility = View.GONE
        setSwipeRefreshEnabled(true)
    }

    override fun onDataRefresh() {
        super.onDataRefresh()
        getPresenterContract()?.getRefreshData(Random.nextInt(9) % 2 == 0)
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        getPresenterContract()?.getResult(true)
    }

    override fun setListeners(view: View) {
        super.setListeners(view)

        mTopBinding.titleBarLayout.setOnBackBtnClickListener {
            finish()
        }

        // 获取成功数据按钮
        mContentBinding.getSuccessReusltBtn.setOnClickListener {
            showStatusLoading()
            getPresenterContract()?.getResult(true)
        }

        // 获取失败数据按钮
        mContentBinding.getFailReusltBtn.setOnClickListener {
            showStatusLoading()
            getPresenterContract()?.getResult(false)
        }
    }

    override fun initData(view: View) {
        super.initData(view)
        showStatusNoData()
    }

    override fun showResult() {
        mContentBinding.resultTv.visibility = View.VISIBLE
    }

    override fun setResult(result: String) {
        mContentBinding.resultTv.text = result
    }
}