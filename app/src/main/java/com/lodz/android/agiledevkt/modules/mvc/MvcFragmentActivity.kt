package com.lodz.android.agiledevkt.modules.mvc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.mvc.abs.MvcTestLazyFragment
import com.lodz.android.agiledevkt.modules.mvc.base.MvcTestBaseFragment
import com.lodz.android.agiledevkt.modules.mvc.refresh.MvcTestRefreshFragment
import com.lodz.android.agiledevkt.modules.mvc.sandwich.MvcTestSandwichFragment
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.corekt.anko.bindView

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
    private val mViewPager by bindView<ViewPager>(R.id.view_pager)

    override fun getLayoutId(): Int = R.layout.activity_fragment_tab_viewpager

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(R.string.mvc_demo_fragment_title)
        initViewPager()
    }

    private fun initViewPager() {
        mViewPager.offscreenPageLimit = TAB_NAMES.size
        mViewPager.adapter = TabAdapter(supportFragmentManager)
        mTabLayout.setupWithViewPager(mViewPager)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    private inner class TabAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment = when (position) {
            0 -> MvcTestLazyFragment.newInstance()
            1 -> MvcTestBaseFragment.newInstance()
            2 -> MvcTestRefreshFragment.newInstance()
            3 -> MvcTestSandwichFragment.newInstance()
            else -> MvcTestLazyFragment.newInstance()
        }

        override fun getCount(): Int = TAB_NAMES.size

        override fun getPageTitle(position: Int): CharSequence? = TAB_NAMES[position]
    }
}