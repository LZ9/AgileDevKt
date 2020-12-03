package com.lodz.android.agiledevkt.modules.mvp.sandwich.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.mvp.sandwich.MvpTestSandwichPresenter
import com.lodz.android.agiledevkt.modules.mvp.sandwich.MvpTestSandwichViewContract
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.mvp.base.fragment.MvpBaseSandwichFragment
import com.lodz.android.pandora.widget.base.TitleBarLayout
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

    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)
    /** 获取成功数据按钮 */
    private val mGetSuccessResultBtn by bindView<Button>(R.id.get_success_reuslt_btn)
    /** 获取失败数据按钮 */
    private val mGetFailResultBtn by bindView<Button>(R.id.get_fail_reuslt_btn)
    /** 顶部标题栏 */
    private val mTitleBarLayout by bindView<TitleBarLayout>(R.id.title_bar_layout)

    override fun createMainPresenter(): MvpTestSandwichPresenter = MvpTestSandwichPresenter()

    override fun getLayoutId(): Int = R.layout.activity_mvp_test

    override fun getTopLayoutId(): Int = R.layout.view_mvp_test_top

    override fun getBottomLayoutId(): Int = R.layout.view_mvp_test_bottom

    override fun findViews(view: View, savedInstanceState: Bundle?) {
        super.findViews(view, savedInstanceState)
        mTitleBarLayout.setTitleName(R.string.mvp_demo_sandwich_title)
        mTitleBarLayout.visibility = View.GONE
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

        mTitleBarLayout.setOnBackBtnClickListener {
            finish()
        }

        mGetSuccessResultBtn.setOnClickListener {
            showStatusLoading()
            getPresenterContract()?.getResult(true)
        }

        mGetFailResultBtn.setOnClickListener {
            showStatusLoading()
            getPresenterContract()?.getResult(false)
        }
    }

    override fun initData(view: View) {
        super.initData(view)
        showStatusNoData()
    }

    override fun showResult() {
        mResultTv.visibility = View.VISIBLE
    }

    override fun setResult(result: String) {
        mResultTv.text = result
    }
}