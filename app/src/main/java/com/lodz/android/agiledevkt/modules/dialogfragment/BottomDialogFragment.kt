package com.lodz.android.agiledevkt.modules.dialogfragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.widget.dialogfragment.BaseBottomDialogFragment

/**
 * 底部DialogFragment测试类
 * Created by zhouL on 2018/12/13.
 */
class BottomDialogFragment : BaseBottomDialogFragment() {

    private val TABS = arrayListOf(
            R.string.bottom_sheets_pikachu_pinarello,
            R.string.bottom_sheets_pikachu_colnago,
            R.string.bottom_sheets_pikachu_specialized
    )

    /** TabLayout */
    private val mTabLayout by bindView<TabLayout>(R.id.tab_layout)
    /** ViewPager */
    private val mViewPager by bindView<ViewPager>(R.id.view_pager)

    override fun getLayoutId(): Int = R.layout.dialog_fragment_bottom

    override fun findViews(view: View, savedInstanceState: Bundle?) {
        super.findViews(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        mViewPager.adapter = TabAdapter(requireContext(), childFragmentManager, TABS)
        mViewPager.offscreenPageLimit = TABS.size
        mViewPager.setCurrentItem(0, true)
        mTabLayout.setupWithViewPager(mViewPager)
    }

    private class TabAdapter(val context: Context, fm: FragmentManager, val tabs: ArrayList<Int>) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment = TestFragment.newInstance(context.getString(tabs.get(position)))

        override fun getCount(): Int = tabs.size

        override fun getPageTitle(position: Int): CharSequence? = context.getString(tabs.get(position))

    }

}