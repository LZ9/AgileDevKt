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
class VpTopTestFragment :BaseFragment(){

    companion object {
        /** 中部tab */
        val MIDDLE_TAB_NAMES = arrayListOf("左", "中", "右")

        private const val EXTRA_NAME = "extra_name"

        fun newInstance(name: String): VpTopTestFragment {
            val fragment = VpTopTestFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_NAME, name)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mBinding: FragmentVpTestBinding by bindingLayout(FragmentVpTestBinding::inflate)

    private var mName: String = ""

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(view: View, savedInstanceState: Bundle?) {
        super.findViews(view, savedInstanceState)
        mName = arguments?.getString(EXTRA_NAME) ?: ""
        initViewPager()
    }

    private fun initViewPager() {
        mBinding.fgViewPager.adapter = TabAdapter(mName, childFragmentManager)
        mBinding.fgViewPager.offscreenPageLimit = MIDDLE_TAB_NAMES.size
        mBinding.fgViewPager.setCurrentItem(0, true)
        mBinding.fgTabLayout.setupWithViewPager(mBinding.fgViewPager)
    }

    override fun initData(view: View) {
        super.initData(view)
        showStatusCompleted()
        PrintLog.d("fgtag", "VpTestFragment top $mName -> showStatusCompleted")
    }

    override fun onFragmentResume() {
        super.onFragmentResume()
        PrintLog.d("fgtag", "VpTestFragment top $mName -> onFragmentResume")
    }

    override fun onFragmentPause() {
        super.onFragmentPause()
        PrintLog.d("fgtag", "VpTestFragment top $mName -> onFragmentPause")
    }

    private class TabAdapter(val name: String, fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment = VpBottomTestFragment.newInstance(name, MIDDLE_TAB_NAMES[position])

        override fun getCount(): Int = MIDDLE_TAB_NAMES.size

        override fun getPageTitle(position: Int): CharSequence = MIDDLE_TAB_NAMES[position]

    }


}