package com.lodz.android.agiledevkt.modules.mvvm.sandwich

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityMvvmTestBinding
import com.lodz.android.agiledevkt.databinding.ViewMvvmTestBottomBinding
import com.lodz.android.agiledevkt.databinding.ViewMvvmTestTopBinding
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.mvvm.base.fragment.BaseSandwichVmFragment
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.base.TitleBarLayout
import kotlin.random.Random

/**
 * MVVM带基础状态控件、中部刷新控件和顶部/底部扩展的Fragment
 * @author zhouL
 * @date 2019/12/9
 */
class MvvmTestSandwichFragment : BaseSandwichVmFragment<MvvmTestSandwichViewModel>() {

    companion object {
        fun newInstance(): MvvmTestSandwichFragment = MvvmTestSandwichFragment()
    }

    override fun createViewModel(): Class<MvvmTestSandwichViewModel> = MvvmTestSandwichViewModel::class.java

    /** 内容布局 */
    private val mContentBinding: ActivityMvvmTestBinding by bindingLayout(ActivityMvvmTestBinding::inflate)
    /** 顶部布局 */
    private val mTopBinding: ViewMvvmTestTopBinding by bindingLayout(ViewMvvmTestTopBinding::inflate)
    /** 底部布局 */
    private val mBottomBinding: ViewMvvmTestBottomBinding by bindingLayout(ViewMvvmTestBottomBinding::inflate)

    override fun getViewBindingLayout(): View = mContentBinding.root

    override fun getTopViewBindingLayout(): View = mTopBinding.root

    override fun getBottomViewBindingLayout(): View = mBottomBinding.root

    override fun findViews(view: View, savedInstanceState: Bundle?) {
        super.findViews(view, savedInstanceState)
        mTopBinding.titleBarLayout.visibility = View.GONE
        setSwipeRefreshEnabled(true)
    }

    override fun onDataRefresh() {
        super.onDataRefresh()
        getViewModel().getRefreshData(Random.nextInt(9) % 2 == 0)
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        getViewModel().getResult(true)
    }

    override fun setListeners(view: View) {
        super.setListeners(view)

        // 获取成功数据按钮
        mContentBinding.getSuccessReusltBtn.setOnClickListener {
            showStatusLoading()
            getViewModel().getResult(true)
        }

        // 获取失败数据按钮
        mContentBinding.getFailReusltBtn.setOnClickListener {
            showStatusLoading()
            getViewModel().getResult(false)
        }

        getViewModel().mResultText.observe(this, Observer { value ->
            mContentBinding.resultTv.text = value
        })
    }

    override fun initData(view: View) {
        super.initData(view)
        showStatusNoData()
    }
}