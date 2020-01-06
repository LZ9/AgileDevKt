package com.lodz.android.agiledevkt.modules.mvc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.mvc.abs.MvcTestLazyFragment
import com.lodz.android.agiledevkt.modules.mvc.base.MvcTestBaseFragment
import com.lodz.android.agiledevkt.modules.mvc.refresh.MvcTestRefreshFragment
import com.lodz.android.agiledevkt.modules.mvc.sandwich.MvcTestSandwichFragment
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.vp2.SimpleTabAdapter

/**
 * fragment生命周期测试类
 * Created by zhouL on 2018/11/19.
 */
class MvcFragmentActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MvcFragmentActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 主页tab名称 */
    private val TAB_NAMES = arrayOf("普通", "状态", "刷新", "三明治")

    /** TabLayout */
    private val mTabLayout by bindView<TabLayout>(R.id.tab_layout)
    /** ViewPager */
    private val mViewPager by bindView<ViewPager2>(R.id.view_pager)

    override fun getLayoutId(): Int = R.layout.activity_fragment_tab_viewpager

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(R.string.mvc_demo_fragment_title)
        initViewPager()
    }

    private fun initViewPager() {
        val list = arrayListOf(
            MvcTestLazyFragment.newInstance(),
            MvcTestBaseFragment.newInstance(),
            MvcTestRefreshFragment.newInstance(),
            MvcTestSandwichFragment.newInstance()
        )
        mViewPager.offscreenPageLimit = TAB_NAMES.size
        mViewPager.adapter = SimpleTabAdapter(this, list)
        TabLayoutMediator(mTabLayout, mViewPager) { tab, position ->
            tab.text = TAB_NAMES[position]
        }.attach()
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