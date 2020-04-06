package com.lodz.android.agiledevkt.modules.fglifecycle.vp2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.fglifecycle.vp.TestContentFragment
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.fragment.BaseFragment
import com.lodz.android.pandora.widget.vp2.SimpleTabAdapter

/**
 * ViewPager嵌套Fragment测试
 * @author zhouL
 * @date 2020/1/21
 */
class Vp2BottomTestFragment :BaseFragment(){

    companion object {

        /** 中部tab */
        val MIDDLE_TAB_NAMES = arrayListOf("一", "二", "三")

        private const val EXTRA_NAME = "extra_name"
        private const val EXTRA_TOP = "extra_top"

        fun newInstance(name: String, top: String): Vp2BottomTestFragment {
            val fragment = Vp2BottomTestFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_NAME, name)
            bundle.putString(EXTRA_TOP, top)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mFgTabLayout by bindView<TabLayout>(R.id.fg_tab_layout)
    private val mFgViewPager by bindView<ViewPager2>(R.id.fg_view_pager)

    private var mName: String = ""
    private var mTop: String = ""

    override fun getLayoutId(): Int = R.layout.fragment_vp2_test

    override fun findViews(view: View, savedInstanceState: Bundle?) {
        super.findViews(view, savedInstanceState)
        mName = arguments?.getString(EXTRA_NAME) ?: ""
        mTop = arguments?.getString(EXTRA_TOP) ?: ""
        initViewPager()
    }

    private fun initViewPager() {
        val list = ArrayList<Fragment>()
        for (i in 0 until MIDDLE_TAB_NAMES.size) {
            list.add(TestContentFragment.newInstance(mName, mTop, MIDDLE_TAB_NAMES[i]))
        }
        mFgViewPager.adapter = SimpleTabAdapter(this, list)
        mFgViewPager.offscreenPageLimit = MIDDLE_TAB_NAMES.size
        mFgViewPager.setCurrentItem(0, true)
        TabLayoutMediator(mFgTabLayout, mFgViewPager) { tab, position ->
            tab.text = MIDDLE_TAB_NAMES[position]
        }.attach()
    }

    override fun initData(view: View) {
        super.initData(view)
        showStatusCompleted()
        PrintLog.e("fgtag", "VpTestFragment bottom $mName/$mTop -> showStatusCompleted")
    }

    override fun onFragmentResume() {
        super.onFragmentResume()
        PrintLog.e("fgtag", "VpTestFragment bottom $mName/$mTop -> onFragmentResume")
    }

    override fun onFragmentPause() {
        super.onFragmentPause()
        PrintLog.e("fgtag", "VpTestFragment bottom $mName/$mTop -> onFragmentPause")
    }

}