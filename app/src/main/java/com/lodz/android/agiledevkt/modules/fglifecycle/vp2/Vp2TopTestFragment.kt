package com.lodz.android.agiledevkt.modules.fglifecycle.vp2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lodz.android.agiledevkt.databinding.FragmentVp2TestBinding
import com.lodz.android.corekt.anko.setupViewPager
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.fragment.BaseFragment
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.vp2.SimpleVp2Adapter

/**
 * ViewPager嵌套Fragment测试
 * @author zhouL
 * @date 2020/1/21
 */
class Vp2TopTestFragment :BaseFragment(){

    companion object {
        /** 中部tab */
        val MIDDLE_TAB_NAMES = arrayListOf("左", "中", "右")

        private const val EXTRA_NAME = "extra_name"

        fun newInstance(name: String): Vp2TopTestFragment {
            val fragment = Vp2TopTestFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_NAME, name)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mBinding: FragmentVp2TestBinding by bindingLayout(FragmentVp2TestBinding::inflate)

    private var mName: String = ""

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(view: View, savedInstanceState: Bundle?) {
        super.findViews(view, savedInstanceState)
        mName = arguments?.getString(EXTRA_NAME) ?: ""
        initViewPager()
    }

    private fun initViewPager() {
        val list = ArrayList<Fragment>()
        for (i in 0 until MIDDLE_TAB_NAMES.size) {
            list.add(Vp2BottomTestFragment.newInstance(mName, MIDDLE_TAB_NAMES[i]))
        }

        mBinding.fgViewPager.adapter = SimpleVp2Adapter(this, list)
        mBinding.fgViewPager.offscreenPageLimit = MIDDLE_TAB_NAMES.size
        mBinding.fgViewPager.setCurrentItem(0, true)
        mBinding.fgTabLayout.setupViewPager(mBinding.fgViewPager, MIDDLE_TAB_NAMES)
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
}