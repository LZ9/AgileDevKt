package com.lodz.android.agiledevkt.modules.fglifecycle.vp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.lodz.android.agiledevkt.databinding.FragmentVpTestBinding
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.fragment.BaseFragment
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * ViewPager嵌套Fragment测试
 * @author zhouL
 * @date 2020/1/21
 */
class VpBottomTestFragment :BaseFragment(){

    companion object {

        /** 中部tab */
        val MIDDLE_TAB_NAMES = arrayListOf("一", "二", "三")

        private const val EXTRA_NAME = "extra_name"
        private const val EXTRA_TOP = "extra_top"

        fun newInstance(name: String, top: String): VpBottomTestFragment {
            val fragment = VpBottomTestFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_NAME, name)
            bundle.putString(EXTRA_TOP, top)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mBinding: FragmentVpTestBinding by bindingLayout(FragmentVpTestBinding::inflate)

    private var mName: String = ""
    private var mTop: String = ""

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(view: View, savedInstanceState: Bundle?) {
        super.findViews(view, savedInstanceState)
        mName = arguments?.getString(EXTRA_NAME) ?: ""
        mTop = arguments?.getString(EXTRA_TOP) ?: ""
        initViewPager()
    }

    private fun initViewPager() {
        mBinding.fgViewPager.adapter = TabAdapter(mName, mTop, childFragmentManager)
        mBinding.fgViewPager.offscreenPageLimit = MIDDLE_TAB_NAMES.size
        mBinding.fgViewPager.setCurrentItem(0, true)
        mBinding.fgTabLayout.setupWithViewPager(mBinding.fgViewPager)
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

    private class TabAdapter(val name: String, val top: String, fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment = TestContentFragment.newInstance(name, top, MIDDLE_TAB_NAMES[position])

        override fun getCount(): Int = MIDDLE_TAB_NAMES.size

        override fun getPageTitle(position: Int): CharSequence? = MIDDLE_TAB_NAMES[position]

    }


}