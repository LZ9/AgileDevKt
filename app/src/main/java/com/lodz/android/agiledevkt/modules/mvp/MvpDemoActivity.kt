package com.lodz.android.agiledevkt.modules.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityMvpDemoBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.mvp.abs.activity.MvpTestAbsActivity
import com.lodz.android.agiledevkt.modules.mvp.base.activity.MvpTestBaseActivity
import com.lodz.android.agiledevkt.modules.mvp.refresh.activity.MvpTestRefreshActivity
import com.lodz.android.agiledevkt.modules.mvp.sandwich.activity.MvpTestSandwichActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * MVP模式测试类
 * Created by zhouL on 2018/11/19.
 */
class MvpDemoActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MvpDemoActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityMvpDemoBinding by bindingLayout(ActivityMvpDemoBinding::inflate)

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
            MvpTestAbsActivity.start(getContext())
        }

        // 带基础状态控件Activity
        mBinding.baseBtn.setOnClickListener {
            MvpTestBaseActivity.start(getContext())
        }

        // 带基础状态控件和下来刷新控件Activity
        mBinding.refreshBtn.setOnClickListener {
            MvpTestRefreshActivity.start(getContext())
        }

        // 带基础状态控件、中部刷新控件和顶部/底部扩展Activity
        mBinding.sandwichBtn.setOnClickListener {
            MvpTestSandwichActivity.start(getContext())
        }

        // Fragment用例
        mBinding.fragmentBtn.setOnClickListener {
            MvpFragmentActivity.start(getContext())
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}