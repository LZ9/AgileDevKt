package com.lodz.android.agiledevkt.modules.mvc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityMvcDemoBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.mvc.abs.MvcTestAbsActivity
import com.lodz.android.agiledevkt.modules.mvc.base.MvcTestBaseActivity
import com.lodz.android.agiledevkt.modules.mvc.refresh.MvcTestRefreshActivity
import com.lodz.android.agiledevkt.modules.mvc.sandwich.MvcTestSandwichActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * MVC模式测试类
 * Created by zhouL on 2018/11/19.
 */
class MvcDemoActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MvcDemoActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityMvcDemoBinding by bindingLayout(ActivityMvcDemoBinding::inflate)

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
            MvcTestAbsActivity.start(getContext())
        }

        // 带基础状态控件Activity
        mBinding.baseBtn.setOnClickListener {
            MvcTestBaseActivity.start(getContext())
        }

        // 带基础状态控件和下来刷新控件Activity
        mBinding.refreshBtn.setOnClickListener {
            MvcTestRefreshActivity.start(getContext())
        }

        // 带基础状态控件、中部刷新控件和顶部/底部扩展Activity
        mBinding.sandwichBtn.setOnClickListener {
            MvcTestSandwichActivity.start(getContext())
        }

        // Fragment用例
        mBinding.fragmentBtn.setOnClickListener {
            MvcFragmentActivity.start(getContext())
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}