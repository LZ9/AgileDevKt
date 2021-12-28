package com.lodz.android.agiledevkt.modules.mvvm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityMvvmDemoBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.mvvm.abs.MvvmTestAbsActivity
import com.lodz.android.agiledevkt.modules.mvvm.base.MvvmTestBaseActivity
import com.lodz.android.agiledevkt.modules.mvvm.loadmore.MvvmTestLoadMoreActivity
import com.lodz.android.agiledevkt.modules.mvvm.refresh.MvvmTestRefreshActivity
import com.lodz.android.agiledevkt.modules.mvvm.sandwich.MvvmTestSandwichActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * MVVM模式测试类
 * @author zhouL
 * @date 2019/11/29
 */
class MvvmDemoActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MvvmDemoActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityMvvmDemoBinding by bindingLayout(ActivityMvvmDemoBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 基础Activity
        mBinding.absBtn.setOnClickListener {
            MvvmTestAbsActivity.start(getContext())
        }

        // 带基础状态控件Activity
        mBinding.baseBtn.setOnClickListener {
            MvvmTestBaseActivity.start(getContext())
        }

        // 带基础状态控件和下来刷新控件Activity
        mBinding.refreshBtn.setOnClickListener {
            MvvmTestRefreshActivity.start(getContext())
        }

        // 带基础状态控件、中部刷新控件和顶部/底部扩展Activity
        mBinding.sandwichBtn.setOnClickListener {
            MvvmTestSandwichActivity.start(getContext())
        }

        // Fragment用例
        mBinding.fragmentBtn.setOnClickListener {
            MvvmFragmentActivity.start(getContext())
        }

        // 加载更多
        mBinding.loadmoreBtn.setOnClickListener {
            MvvmTestLoadMoreActivity.start(getContext())
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}