package com.lodz.android.agiledevkt.modules.fglifecycle.vp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.activity.BaseActivity

/**
 * ViewPager嵌套Fragment测试
 * @author zhouL
 * @date 2020/1/21
 */
class FgVpTestActivity :BaseActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, FgVpTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 顶部tab */
    private val TOP_TAB_NAMES = arrayListOf("A", "B", "C")

    private val mAcTabLayout by bindView<TabLayout>(R.id.ac_tab_layout)
    private val mAcViewPager by bindView<ViewPager>(R.id.ac_view_pager)

    override fun getLayoutId(): Int = R.layout.activity_fg_vp_test

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.fg_lifecycle_vp)
        initViewPager()
    }

    private fun initViewPager() {
        mAcViewPager.adapter = TabAdapter(supportFragmentManager, TOP_TAB_NAMES)
        mAcViewPager.offscreenPageLimit = TOP_TAB_NAMES.size
        mAcViewPager.setCurrentItem(0, true)
        mAcTabLayout.setupWithViewPager(mAcViewPager)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    private class TabAdapter(fm: FragmentManager, val tabs: ArrayList<String>) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment = VpTopTestFragment.newInstance(tabs[position])

        override fun getCount(): Int = tabs.size

        override fun getPageTitle(position: Int): CharSequence? = tabs[position]
    }

}