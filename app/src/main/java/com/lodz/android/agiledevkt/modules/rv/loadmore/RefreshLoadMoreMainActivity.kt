package com.lodz.android.agiledevkt.modules.rv.loadmore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityRefreshLoadmoreMainBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import kotlin.getValue

/**
 * RV刷新/加载更多测试主页
 * @author zhouL
 * @date 2025/12/15
 */
class RefreshLoadMoreMainActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RefreshLoadMoreMainActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityRefreshLoadmoreMainBinding by bindingLayout(ActivityRefreshLoadmoreMainBinding::inflate)

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

        // 继承BaseRefreshActivity实现
        mBinding.extendsBtn.setOnClickListener {
            RefreshLoadMoreActivity.start(getContext())
        }

        // 嵌套滑动实现
        mBinding.nestedBtn.setOnClickListener {
            RefreshLoadMoreNestedActivity.start(getContext())
        }

        // 头布局实现嵌套
        mBinding.headerBtn.setOnClickListener {
            RefreshLoadMoreHeaderActivity.start(getContext())
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}