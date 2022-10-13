package com.lodz.android.agiledevkt.modules.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityFragmentTabViewpagerBinding
import com.lodz.android.agiledevkt.modules.mvp.abs.fragment.MvpTestLazyFragment
import com.lodz.android.agiledevkt.modules.mvp.base.fragment.MvpTestBaseFragment
import com.lodz.android.agiledevkt.modules.mvp.refresh.fragment.MvpTestRefreshFragment
import com.lodz.android.agiledevkt.modules.mvp.sandwich.fragment.MvpTestSandwichFragment
import com.lodz.android.corekt.anko.setupViewPager
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.vp2.SimpleTabAdapter

/**
 * fragment生命周期测试类
 * Created by zhouL on 2018/11/19.
 */
class MvpFragmentActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MvpFragmentActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 主页tab名称 */
    private val TAB_NAMES = arrayOf("普通", "状态", "刷新", "三明治")

    private val mBinding: ActivityFragmentTabViewpagerBinding by bindingLayout(ActivityFragmentTabViewpagerBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(R.string.mvp_demo_fragment_title)
        initViewPager()
    }

    private fun initViewPager() {
        val list = arrayListOf(
            MvpTestLazyFragment.newInstance(),
            MvpTestBaseFragment.newInstance(),
            MvpTestRefreshFragment.newInstance(),
            MvpTestSandwichFragment.newInstance()
        )
        mBinding.viewPager.offscreenPageLimit = TAB_NAMES.size
        mBinding.viewPager.adapter = SimpleTabAdapter(this, list)
        mBinding.tabLayout.setupViewPager(mBinding.viewPager, TAB_NAMES)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}