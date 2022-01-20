package com.lodz.android.agiledevkt.modules.mvvm.sandwich

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityMvvmTestBinding
import com.lodz.android.agiledevkt.databinding.ViewMvvmTestBottomBinding
import com.lodz.android.agiledevkt.databinding.ViewMvvmTestTopBinding
import com.lodz.android.pandora.mvvm.base.activity.BaseSandwichVmActivity
import com.lodz.android.pandora.mvvm.vm.BaseSandwichViewModel
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.utils.viewmodel.bindViewModel
import kotlin.random.Random

/**
 * MVVM带基础状态控件、中部刷新控件和顶部/底部扩展的Activity
 * @author zhouL
 * @date 2019/12/6
 */
class MvvmTestSandwichActivity : BaseSandwichVmActivity() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, MvvmTestSandwichActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mViewModel by bindViewModel { MvvmTestSandwichViewModel() }

    override fun getViewModel(): BaseSandwichViewModel = mViewModel

    /** 内容布局 */
    private val mContentBinding: ActivityMvvmTestBinding by bindingLayout(ActivityMvvmTestBinding::inflate)
    /** 顶部布局 */
    private val mTopBinding: ViewMvvmTestTopBinding by bindingLayout(ViewMvvmTestTopBinding::inflate)
    /** 底部布局 */
    private val mBottomBinding: ViewMvvmTestBottomBinding by bindingLayout(ViewMvvmTestBottomBinding::inflate)

    override fun getViewBindingLayout(): View = mContentBinding.root

    override fun getTopViewBindingLayout(): View = mTopBinding.root

    override fun getBottomViewBindingLayout(): View = mBottomBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        mTopBinding.titleBarLayout.setTitleName(R.string.mvvm_demo_sandwich_title)
        setSwipeRefreshEnabled(true)
    }

    override fun onDataRefresh() {
        super.onDataRefresh()
        mViewModel.getRefreshData(Random.nextInt(9) % 2 == 0)
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        mViewModel.getResult(true)
    }


    override fun setListeners() {
        super.setListeners()

        mTopBinding.titleBarLayout.setOnBackBtnClickListener {
            finish()
        }

        // 获取成功数据按钮
        mContentBinding.getSuccessReusltBtn.setOnClickListener {
            showStatusLoading()
            mViewModel.getResult(true)
        }

        // 获取失败数据按钮
        mContentBinding.getFailReusltBtn.setOnClickListener {
            showStatusLoading()
            mViewModel.getResult(false)
        }
    }

    override fun setViewModelObserves() {
        super.setViewModelObserves()
        mViewModel.mResultText.observe(getLifecycleOwner()) {
            mContentBinding.resultTv.text = it
        }
    }

    override fun initData() {
        super.initData()
        showStatusNoData()
    }

}