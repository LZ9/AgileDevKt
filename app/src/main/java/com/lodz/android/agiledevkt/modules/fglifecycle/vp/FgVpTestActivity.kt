package com.lodz.android.agiledevkt.modules.fglifecycle.vp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityFgVpTestBinding
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

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

    private val mBinding: ActivityFgVpTestBinding by bindingLayout(ActivityFgVpTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.fg_lifecycle_vp)
        initViewPager()
    }

    private fun initViewPager() {
        mBinding.acViewPager.adapter = TabAdapter(supportFragmentManager, TOP_TAB_NAMES)
        mBinding.acViewPager.offscreenPageLimit = TOP_TAB_NAMES.size
        mBinding.acViewPager.setCurrentItem(0, true)
        mBinding.acTabLayout.setupWithViewPager(mBinding.acViewPager)
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