package com.lodz.android.agiledevkt.modules.mvvm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.mvvm.abs.MvvmTestLazyFragment
import com.lodz.android.agiledevkt.modules.mvvm.base.MvvmTestBaseFragment
import com.lodz.android.agiledevkt.modules.mvvm.refresh.MvvmTestRefreshFragment
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.activity.BaseActivity

/**
 * Fragment生命周期测试类
 * @author zhouL
 * @date 2019/12/6
 */
class MvvmFragmentActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MvvmFragmentActivity::class.java)
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
        getTitleBarLayout().setTitleName(R.string.mvvm_demo_fragment_title)
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

    private inner class TabAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment = when (position) {
            0 -> MvvmTestLazyFragment.newInstance()
            1 -> MvvmTestBaseFragment.newInstance()
            2 -> MvvmTestRefreshFragment.newInstance()
            3 -> MvvmTestLazyFragment.newInstance()
            else -> MvvmTestLazyFragment.newInstance()
        }

        override fun getCount(): Int = TAB_NAMES.size

        override fun getPageTitle(position: Int): CharSequence? = TAB_NAMES[position]
    }
}